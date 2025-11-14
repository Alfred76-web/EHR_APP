package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.LabTest
import com.example.ehrsystem.viewmodel.LabTestViewModel
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class EnterTestResultsActivity : AppCompatActivity() {

    private lateinit var labTestViewModel: LabTestViewModel
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var tvTestInfo: TextView
    private lateinit var etResults: TextInputEditText
    private lateinit var etLabTechnician: TextInputEditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button

    private var currentLabTest: LabTest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_test_results)

        // Initialize ViewModels
        labTestViewModel = ViewModelProvider(this)[LabTestViewModel::class.java]
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]

        // Initialize views
        tvTestInfo = findViewById(R.id.tvTestInfo)
        etResults = findViewById(R.id.etResults)
        etLabTechnician = findViewById(R.id.etLabTechnician)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancel)

        // Get test ID from intent
        val testId = intent.getIntExtra("TEST_ID", -1)
        if (testId != -1) {
            loadTestInfo(testId)
        }

        btnSubmit.setOnClickListener {
            submitResults()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadTestInfo(testId: Int) {
        // In a real app, you'd fetch by ID. For simplicity, we'll observe pending tests
        labTestViewModel.pendingLabTests.observe(this) { tests ->
            currentLabTest = tests.find { it.testId == testId }
            currentLabTest?.let { test ->
                patientViewModel.getPatientById(test.patientId).observe(this) { patient ->
                    tvTestInfo.text = """
                        Test Name: ${test.testName}
                        Test Type: ${test.testType}
                        Patient: ${patient?.firstName} ${patient?.lastName}
                        Patient ID: ${test.patientId}
                        Requested By: ${test.requestedBy}
                        Request Date: ${test.requestedDate}
                    """.trimIndent()
                }
            }
        }
    }

    private fun submitResults() {
        if (currentLabTest == null) {
            Toast.makeText(this, "Error loading test information", Toast.LENGTH_SHORT).show()
            return
        }

        val results = etResults.text.toString().trim()
        val labTechnician = etLabTechnician.text.toString().trim()

        if (results.isEmpty()) {
            etResults.error = "Results are required"
            etResults.requestFocus()
            return
        }

        if (labTechnician.isEmpty()) {
            etLabTechnician.error = "Lab technician name is required"
            etLabTechnician.requestFocus()
            return
        }

        val completedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val updatedTest = currentLabTest!!.copy(
            results = results,
            completedDate = completedDate,
            labTechnician = labTechnician,
            status = "Completed"
        )

        labTestViewModel.updateLabTest(updatedTest)

        Toast.makeText(this, "Test results submitted successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}