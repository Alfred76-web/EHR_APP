package com.example.ehrsystem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText

class ViewPatientsActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var rvPatients: RecyclerView
    private lateinit var etSearch: TextInputEditText
    private lateinit var tvPatientCount: TextView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnBack: Button
    private lateinit var patientAdapter: PatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_patients)

        // Initialize views
        rvPatients = findViewById(R.id.rvPatients)
        etSearch = findViewById(R.id.etSearch)
        tvPatientCount = findViewById(R.id.tvPatientCount)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        btnBack = findViewById(R.id.btnBack)

        // Initialize ViewModel
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Observe patients
        observePatients()

        // Setup search
        setupSearch()

        // Back button
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        patientAdapter = PatientAdapter(emptyList()) { patient ->
            onPatientClick(patient)
        }
        rvPatients.apply {
            layoutManager = LinearLayoutManager(this@ViewPatientsActivity)
            adapter = patientAdapter
        }
    }

    private fun observePatients() {
        patientViewModel.allPatients.observe(this) { patients ->
            patients?.let {
                patientAdapter.updatePatients(it)
                updateUI(it)
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    patientViewModel.allPatients.observe(this@ViewPatientsActivity) { patients ->
                        patientAdapter.updatePatients(patients)
                        updateUI(patients)
                    }
                } else {
                    patientViewModel.searchPatients(query).observe(this@ViewPatientsActivity) { patients ->
                        patientAdapter.updatePatients(patients)
                        updateUI(patients)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateUI(patients: List<Patient>) {
        if (patients.isEmpty()) {
            rvPatients.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            tvPatientCount.text = "Total Patients: 0"
        } else {
            rvPatients.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
            tvPatientCount.text = "Total Patients: ${patients.size}"
        }
    }

    private fun onPatientClick(patient: Patient) {
        Toast.makeText(
            this,
            "Patient: ${patient.firstName} ${patient.lastName}\nID: ${patient.patientId}",
            Toast.LENGTH_SHORT
        ).show()
        // Later we can open a detail screen here
    }
}