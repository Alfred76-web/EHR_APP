package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.Prescription
import com.example.ehrsystem.viewmodel.PrescriptionViewModel

class ViewPendingPrescriptionsActivity : AppCompatActivity() {

    private lateinit var prescriptionViewModel: PrescriptionViewModel
    private lateinit var rvPrescriptions: RecyclerView
    private lateinit var tvPrescriptionCount: TextView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnBack: Button
    private lateinit var prescriptionAdapter: PrescriptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pending_prescriptions)

        // Initialize views
        rvPrescriptions = findViewById(R.id.rvPrescriptions)
        tvPrescriptionCount = findViewById(R.id.tvPrescriptionCount)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        btnBack = findViewById(R.id.btnBack)

        // Initialize ViewModel
        prescriptionViewModel = ViewModelProvider(this)[PrescriptionViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Observe pending prescriptions
        observePendingPrescriptions()

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        prescriptionAdapter = PrescriptionAdapter(emptyList()) { prescription ->
            openDispenseActivity(prescription)
        }
        rvPrescriptions.apply {
            layoutManager = LinearLayoutManager(this@ViewPendingPrescriptionsActivity)
            adapter = prescriptionAdapter
        }
    }

    private fun observePendingPrescriptions() {
        prescriptionViewModel.pendingPrescriptions.observe(this) { prescriptions ->
            prescriptions?.let {
                prescriptionAdapter.updatePrescriptions(it)
                updateUI(it)
            }
        }
    }

    private fun updateUI(prescriptions: List<Prescription>) {
        if (prescriptions.isEmpty()) {
            rvPrescriptions.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            tvPrescriptionCount.text = "Pending Prescriptions: 0"
        } else {
            rvPrescriptions.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
            tvPrescriptionCount.text = "Pending Prescriptions: ${prescriptions.size}"
        }
    }

    private fun openDispenseActivity(prescription: Prescription) {
        val intent = Intent(this, DispenseMedicationActivity::class.java)
        intent.putExtra("PRESCRIPTION_ID", prescription.prescriptionId)
        intent.putExtra("PATIENT_ID", prescription.patientId)
        intent.putExtra("MEDICATION_NAME", prescription.medicationName)
        intent.putExtra("DOSAGE", prescription.dosage)
        intent.putExtra("FREQUENCY", prescription.frequency)
        intent.putExtra("DURATION", prescription.duration)
        intent.putExtra("INSTRUCTIONS", prescription.instructions)
        intent.putExtra("PRESCRIBED_BY", prescription.prescribedBy)
        intent.putExtra("PRESCRIBED_DATE", prescription.prescribedDate)
        startActivity(intent)
    }
}