package com.example.ehrsystem

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.LabTest
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.viewmodel.LabTestViewModel
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RequestLabTestActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var labTestViewModel: LabTestViewModel
    private lateinit var spinnerPatient: Spinner
    private lateinit var etTestName: TextInputEditText
    private lateinit var spinnerTestType: Spinner
    private lateinit var etRequestedBy: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var patientsList = listOf<Patient>()
    private var selectedPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_lab_test)

        // Initialize ViewModels
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        labTestViewModel = ViewModelProvider(this)[LabTestViewModel::class.java]

        // Initialize views
        spinnerPatient = findViewById(R.id.spinnerPatient)
        etTestName = findViewById(R.id.etTestName)
        spinnerTestType = findViewById(R.id.spinnerTestType)
        etRequestedBy = findViewById(R.id.etRequestedBy)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Setup spinners
        loadPatients()
        setupTestTypeSpinner()

        // Button listeners
        btnSave.setOnClickListener {
            requestLabTest()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadPatients() {
        patientViewModel.allPatients.observe(this) { patients ->
            patientsList = patients
            val patientNames = mutableListOf("Select Patient")
            patientNames.addAll(patients.map { "${it.firstName} ${it.lastName} (ID: ${it.patientId})" })

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, patientNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPatient.adapter = adapter

            spinnerPatient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    selectedPatient = if (position > 0) patientsList[position - 1] else null
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedPatient = null
                }
            }
        }
    }

    private fun setupTestTypeSpinner() {
        val testTypes = arrayOf("Select Test Type", "Blood Test", "Urine Test", "X-Ray", "CT Scan", "MRI", "Ultrasound", "ECG", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, testTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTestType.adapter = adapter
    }

    private fun requestLabTest() {
        if (selectedPatient == null) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            return
        }

        val testName = etTestName.text.toString().trim()
        val testType = spinnerTestType.selectedItem.toString()
        val requestedBy = etRequestedBy.text.toString().trim()

        // Validation
        if (testName.isEmpty()) {
            etTestName.error = "Test name is required"
            etTestName.requestFocus()
            return
        }

        if (testType == "Select Test Type") {
            Toast.makeText(this, "Please select test type", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestedBy.isEmpty()) {
            etRequestedBy.error = "Doctor name is required"
            etRequestedBy.requestFocus()
            return
        }

        val requestedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val labTest = LabTest(
            patientId = selectedPatient!!.patientId,
            testName = testName,
            testType = testType,
            requestedBy = requestedBy,
            requestedDate = requestedDate,
            status = "Pending",
            results = null,
            completedDate = null,
            labTechnician = null
        )

        labTestViewModel.insertLabTest(labTest)

        Toast.makeText(this, "Lab test requested successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}