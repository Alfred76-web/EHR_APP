package com.example.ehrsystem

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.database.VitalSigns
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.example.ehrsystem.viewmodel.VitalSignsViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RecordVitalsActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var vitalSignsViewModel: VitalSignsViewModel
    private lateinit var spinnerPatient: Spinner
    private lateinit var etBloodPressure: TextInputEditText
    private lateinit var etTemperature: TextInputEditText
    private lateinit var etPulse: TextInputEditText
    private lateinit var etRespiratoryRate: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etHeight: TextInputEditText
    private lateinit var etNurseName: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var patientsList = listOf<Patient>()
    private var selectedPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_vitals)

        // Initialize ViewModels
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        vitalSignsViewModel = ViewModelProvider(this)[VitalSignsViewModel::class.java]

        // Initialize views
        spinnerPatient = findViewById(R.id.spinnerPatient)
        etBloodPressure = findViewById(R.id.etBloodPressure)
        etTemperature = findViewById(R.id.etTemperature)
        etPulse = findViewById(R.id.etPulse)
        etRespiratoryRate = findViewById(R.id.etRespiratoryRate)
        etWeight = findViewById(R.id.etWeight)
        etHeight = findViewById(R.id.etHeight)
        etNurseName = findViewById(R.id.etNurseName)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Load patients
        loadPatients()

        // Button listeners
        btnSave.setOnClickListener {
            saveVitalSigns()
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

    private fun saveVitalSigns() {
        if (selectedPatient == null) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            return
        }

        val bloodPressure = etBloodPressure.text.toString().trim()
        val temperature = etTemperature.text.toString().trim()
        val pulse = etPulse.text.toString().trim()
        val respiratoryRate = etRespiratoryRate.text.toString().trim()
        val weight = etWeight.text.toString().trim()
        val height = etHeight.text.toString().trim()
        val nurseName = etNurseName.text.toString().trim()

        // Validation
        if (bloodPressure.isEmpty()) {
            etBloodPressure.error = "Blood pressure is required"
            etBloodPressure.requestFocus()
            return
        }

        if (temperature.isEmpty()) {
            etTemperature.error = "Temperature is required"
            etTemperature.requestFocus()
            return
        }

        if (pulse.isEmpty()) {
            etPulse.error = "Pulse is required"
            etPulse.requestFocus()
            return
        }

        if (nurseName.isEmpty()) {
            etNurseName.error = "Nurse name is required"
            etNurseName.requestFocus()
            return
        }

        val recordedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val vitalSigns = VitalSigns(
            patientId = selectedPatient!!.patientId,
            bloodPressure = bloodPressure,
            temperature = temperature,
            pulse = pulse,
            respiratoryRate = respiratoryRate,
            weight = weight,
            height = height,
            recordedDate = recordedDate,
            recordedBy = nurseName
        )

        vitalSignsViewModel.insertVitalSigns(vitalSigns)

        Toast.makeText(this, "Vital signs recorded successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}