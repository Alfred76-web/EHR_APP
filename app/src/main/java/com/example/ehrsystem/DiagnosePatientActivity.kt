package com.example.ehrsystem

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Diagnosis
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.viewmodel.DiagnosisViewModel
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class DiagnosePatientActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var spinnerPatient: Spinner
    private lateinit var etSymptoms: TextInputEditText
    private lateinit var etDiagnosis: TextInputEditText
    private lateinit var etTreatmentPlan: TextInputEditText
    private lateinit var etDoctorName: TextInputEditText
    private lateinit var etNotes: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var patientsList = listOf<Patient>()
    private var selectedPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose_patient)

        // Initialize ViewModels
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        diagnosisViewModel = ViewModelProvider(this)[DiagnosisViewModel::class.java]

        // Initialize views
        initializeViews()

        // Load patients
        loadPatients()

        // Button listeners
        setupButtonListeners()
    }

    private fun initializeViews() {
        spinnerPatient = findViewById(R.id.spinnerPatient)
        etSymptoms = findViewById(R.id.etSymptoms)
        etDiagnosis = findViewById(R.id.etDiagnosis)
        etTreatmentPlan = findViewById(R.id.etTreatmentPlan)
        etDoctorName = findViewById(R.id.etDoctorName)
        etNotes = findViewById(R.id.etNotes)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupButtonListeners() {
        btnSave.setOnClickListener {
            saveDiagnosis()
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
                // No patients available
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

    private fun saveDiagnosis() {
        // Check if patient is selected
        if (selectedPatient == null) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            spinnerPatient.requestFocus()
            return
        }

        // Get values from input fields
        val symptoms = etSymptoms.text.toString().trim()
        val diagnosis = etDiagnosis.text.toString().trim()
        val treatmentPlan = etTreatmentPlan.text.toString().trim()
        val doctorName = etDoctorName.text.toString().trim()
        val notes = etNotes.text.toString().trim()

        // Validation
        if (!validateInputs(symptoms, diagnosis, treatmentPlan, doctorName)) {
            return
        }

        // Get current date and time
        val diagnosisDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        // Create diagnosis object
        val diagnosisRecord = Diagnosis(
            patientId = selectedPatient!!.patientId,
            symptoms = symptoms,
            diagnosis = diagnosis,
            treatmentPlan = treatmentPlan,
            doctorName = doctorName,
            diagnosisDate = diagnosisDate,
            notes = notes.ifEmpty { null }
        )

        // Save to database
        diagnosisViewModel.insertDiagnosis(diagnosisRecord)

        // Show success message
        Toast.makeText(
            this,
            "Diagnosis saved successfully for ${selectedPatient!!.firstName} ${selectedPatient!!.lastName}",
            Toast.LENGTH_LONG
        ).show()

        // Close activity
        finish()
    }

    private fun validateInputs(
        symptoms: String,
        diagnosis: String,
        treatmentPlan: String,
        doctorName: String
    ): Boolean {
        // Validate symptoms
        if (symptoms.isEmpty()) {
            etSymptoms.error = "Symptoms are required"
            etSymptoms.requestFocus()
            return false
        }

        if (symptoms.length < 10) {
            etSymptoms.error = "Please provide detailed symptoms (at least 10 characters)"
            etSymptoms.requestFocus()
            return false
        }

        // Validate diagnosis
        if (diagnosis.isEmpty()) {
            etDiagnosis.error = "Diagnosis is required"
            etDiagnosis.requestFocus()
            return false
        }

        if (diagnosis.length < 5) {
            etDiagnosis.error = "Please provide a valid diagnosis (at least 5 characters)"
            etDiagnosis.requestFocus()
            return false
        }

        // Validate treatment plan
        if (treatmentPlan.isEmpty()) {
            etTreatmentPlan.error = "Treatment plan is required"
            etTreatmentPlan.requestFocus()
            return false
        }

        if (treatmentPlan.length < 10) {
            etTreatmentPlan.error = "Please provide detailed treatment plan (at least 10 characters)"
            etTreatmentPlan.requestFocus()
            return false
        }

        // Validate doctor name
        if (doctorName.isEmpty()) {
            etDoctorName.error = "Doctor name is required"
            etDoctorName.requestFocus()
            return false
        }

        if (doctorName.length < 3) {
            etDoctorName.error = "Please enter a valid doctor name"
            etDoctorName.requestFocus()
            return false
        }

        return true
    }

    override fun onBackPressed() {
        // Confirm before closing if there's unsaved data
        if (hasUnsavedData()) {
            showUnsavedDataDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun hasUnsavedData(): Boolean {
        return etSymptoms.text?.isNotEmpty() == true ||
                etDiagnosis.text?.isNotEmpty() == true ||
                etTreatmentPlan.text?.isNotEmpty() == true ||
                etDoctorName.text?.isNotEmpty() == true ||
                etNotes.text?.isNotEmpty() == true
    }

    private fun showUnsavedDataDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("You have unsaved changes. Are you sure you want to leave?")
            .setPositiveButton("Leave") { _, _ ->
                finish()
            }
            .setNegativeButton("Stay", null)
            .show()
    }
}