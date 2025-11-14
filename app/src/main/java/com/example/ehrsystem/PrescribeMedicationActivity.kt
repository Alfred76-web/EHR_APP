package com.example.ehrsystem

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.database.Prescription
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.example.ehrsystem.viewmodel.PrescriptionViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class PrescribeMedicationActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var prescriptionViewModel: PrescriptionViewModel
    private lateinit var spinnerPatient: Spinner
    private lateinit var etMedicationName: TextInputEditText
    private lateinit var etDosage: TextInputEditText
    private lateinit var etFrequency: TextInputEditText
    private lateinit var etDuration: TextInputEditText
    private lateinit var etInstructions: TextInputEditText
    private lateinit var etPrescribedBy: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var patientsList = listOf<Patient>()
    private var selectedPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescribe_medication)

        // Initialize ViewModels
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        prescriptionViewModel = ViewModelProvider(this)[PrescriptionViewModel::class.java]

        // Initialize views
        initializeViews()

        // Load patients
        loadPatients()

        // Button listeners
        setupButtonListeners()
    }

    private fun initializeViews() {
        spinnerPatient = findViewById(R.id.spinnerPatient)
        etMedicationName = findViewById(R.id.etMedicationName)
        etDosage = findViewById(R.id.etDosage)
        etFrequency = findViewById(R.id.etFrequency)
        etDuration = findViewById(R.id.etDuration)
        etInstructions = findViewById(R.id.etInstructions)
        etPrescribedBy = findViewById(R.id.etPrescribedBy)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupButtonListeners() {
        btnSave.setOnClickListener {
            savePrescription()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadPatients() {
        patientViewModel.allPatients.observe(this) { patients ->
            if (patients != null && patients.isNotEmpty()) {
                patientsList = patients
                setupPatientSpinner(patients)
            } else {
                Toast.makeText(
                    this,
                    "No patients registered. Please register patients first.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupPatientSpinner(patients: List<Patient>) {
        val patientNames = mutableListOf("Select Patient")
        patientNames.addAll(patients.map {
            "${it.firstName} ${it.lastName} (ID: ${String.format("%03d", it.patientId)})"
        })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            patientNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPatient.adapter = adapter

        spinnerPatient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedPatient = if (position > 0) {
                    patientsList[position - 1]
                } else {
                    null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPatient = null
            }
        }
    }

    private fun savePrescription() {
        if (selectedPatient == null) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            return
        }

        val medicationName = etMedicationName.text.toString().trim()
        val dosage = etDosage.text.toString().trim()
        val frequency = etFrequency.text.toString().trim()
        val duration = etDuration.text.toString().trim()
        val instructions = etInstructions.text.toString().trim()
        val prescribedBy = etPrescribedBy.text.toString().trim()

        // Validation
        if (!validateInputs(medicationName, dosage, frequency, duration, prescribedBy)) {
            return
        }

        val prescribedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val prescription = Prescription(
            patientId = selectedPatient!!.patientId,
            medicationName = medicationName,
            dosage = dosage,
            frequency = frequency,
            duration = duration,
            instructions = instructions,
            prescribedBy = prescribedBy,
            prescribedDate = prescribedDate,
            dispensedStatus = "Pending",
            dispensedBy = null,
            dispensedDate = null
        )

        prescriptionViewModel.insertPrescription(prescription)

        Toast.makeText(
            this,
            "Prescription created successfully for ${selectedPatient!!.firstName} ${selectedPatient!!.lastName}",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun validateInputs(
        medicationName: String,
        dosage: String,
        frequency: String,
        duration: String,
        prescribedBy: String
    ): Boolean {
        if (medicationName.isEmpty()) {
            etMedicationName.error = "Medication name is required"
            etMedicationName.requestFocus()
            return false
        }

        if (dosage.isEmpty()) {
            etDosage.error = "Dosage is required"
            etDosage.requestFocus()
            return false
        }

        if (frequency.isEmpty()) {
            etFrequency.error = "Frequency is required"
            etFrequency.requestFocus()
            return false
        }

        if (duration.isEmpty()) {
            etDuration.error = "Duration is required"
            etDuration.requestFocus()
            return false
        }

        if (prescribedBy.isEmpty()) {
            etPrescribedBy.error = "Doctor name is required"
            etPrescribedBy.requestFocus()
            return false
        }

        return true
    }
}