package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Prescription
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.example.ehrsystem.viewmodel.PrescriptionViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class DispenseMedicationActivity : AppCompatActivity() {

    private lateinit var prescriptionViewModel: PrescriptionViewModel
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var tvPrescriptionInfo: TextView
    private lateinit var etPharmacistName: TextInputEditText
    private lateinit var cbConfirm: CheckBox
    private lateinit var btnDispense: Button
    private lateinit var btnCancel: Button

    private var prescriptionId: Int = -1
    private var currentPrescription: Prescription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispense_medication)

        // Initialize ViewModels
        prescriptionViewModel = ViewModelProvider(this)[PrescriptionViewModel::class.java]
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]

        // Initialize views
        tvPrescriptionInfo = findViewById(R.id.tvPrescriptionInfo)
        etPharmacistName = findViewById(R.id.etPharmacistName)
        cbConfirm = findViewById(R.id.cbConfirm)
        btnDispense = findViewById(R.id.btnDispense)
        btnCancel = findViewById(R.id.btnCancel)

        // Get prescription data from intent
        prescriptionId = intent.getIntExtra("PRESCRIPTION_ID", -1)

        if (prescriptionId != -1) {
            loadPrescriptionInfo()
        } else {
            Toast.makeText(this, "Error loading prescription", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnDispense.setOnClickListener {
            dispenseMedication()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadPrescriptionInfo() {
        val medicationName = intent.getStringExtra("MEDICATION_NAME") ?: ""
        val dosage = intent.getStringExtra("DOSAGE") ?: ""
        val frequency = intent.getStringExtra("FREQUENCY") ?: ""
        val duration = intent.getStringExtra("DURATION") ?: ""
        val instructions = intent.getStringExtra("INSTRUCTIONS") ?: ""
        val prescribedBy = intent.getStringExtra("PRESCRIBED_BY") ?: ""
        val prescribedDate = intent.getStringExtra("PRESCRIBED_DATE") ?: ""
        val patientId = intent.getIntExtra("PATIENT_ID", -1)

        // Load patient name
        if (patientId != -1) {
            patientViewModel.getPatientById(patientId).observe(this) { patient ->
                val patientName = if (patient != null) {
                    "${patient.firstName} ${patient.lastName}"
                } else {
                    "Unknown Patient"
                }

                tvPrescriptionInfo.text = """
                    Medication: $medicationName
                    Dosage: $dosage
                    Frequency: $frequency
                    Duration: $duration
                    Instructions: $instructions
                    
                    Patient: $patientName (ID: $patientId)
                    
                    Prescribed By: $prescribedBy
                    Date: $prescribedDate
                """.trimIndent()
            }
        }

        // Load full prescription object to update later
        prescriptionViewModel.pendingPrescriptions.observe(this) { prescriptions ->
            currentPrescription = prescriptions.find { it.prescriptionId == prescriptionId }
        }
    }

    private fun dispenseMedication() {
        val pharmacistName = etPharmacistName.text.toString().trim()

        // Validation
        if (pharmacistName.isEmpty()) {
            etPharmacistName.error = "Pharmacist name is required"
            etPharmacistName.requestFocus()
            return
        }

        if (pharmacistName.length < 3) {
            etPharmacistName.error = "Please enter a valid pharmacist name"
            etPharmacistName.requestFocus()
            return
        }

        if (!cbConfirm.isChecked) {
            Toast.makeText(this, "Please confirm that medication has been dispensed", Toast.LENGTH_SHORT).show()
            cbConfirm.requestFocus()
            return
        }

        if (currentPrescription == null) {
            Toast.makeText(this, "Error: Prescription data not loaded", Toast.LENGTH_SHORT).show()
            return
        }

        val dispensedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val updatedPrescription = currentPrescription!!.copy(
            dispensedStatus = "Dispensed",
            dispensedBy = pharmacistName,
            dispensedDate = dispensedDate
        )

        prescriptionViewModel.updatePrescription(updatedPrescription)

        Toast.makeText(this, "Medication dispensed successfully!", Toast.LENGTH_LONG).show()
        finish()
    }
}