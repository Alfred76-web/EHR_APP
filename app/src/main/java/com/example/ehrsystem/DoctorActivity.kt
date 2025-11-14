package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        val cardViewPatients = findViewById<CardView>(R.id.cardViewPatients)
        val cardDiagnose = findViewById<CardView>(R.id.cardDiagnose)
        val cardPrescribe = findViewById<CardView>(R.id.cardPrescribe)
        val cardViewLabResults = findViewById<CardView>(R.id.cardViewLabResults)
        val btnBack = findViewById<Button>(R.id.btnBack)

        cardViewPatients.setOnClickListener {
            startActivity(Intent(this, ViewPatientsActivity::class.java))
        }

        cardDiagnose.setOnClickListener {
            startActivity(Intent(this, DiagnosePatientActivity::class.java))
        }

        cardPrescribe.setOnClickListener {
            startActivity(Intent(this, PrescribeMedicationActivity::class.java))
        }

        cardViewLabResults.setOnClickListener {
            startActivity(Intent(this, ViewPendingTestsActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}