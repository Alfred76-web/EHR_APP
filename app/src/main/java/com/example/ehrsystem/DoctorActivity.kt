package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        // Find views
        val cardViewPatients = findViewById<CardView>(R.id.cardViewPatients)
        val cardDiagnose = findViewById<CardView>(R.id.cardDiagnose)
        val cardPrescribe = findViewById<CardView>(R.id.cardPrescribe)
        val cardViewLabResults = findViewById<CardView>(R.id.cardViewLabResults)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Set click listeners
        cardViewPatients.setOnClickListener {
            Toast.makeText(this, "View Patient Records clicked", Toast.LENGTH_SHORT).show()
        }

        cardDiagnose.setOnClickListener {
            Toast.makeText(this, "Diagnose Patient clicked", Toast.LENGTH_SHORT).show()
        }

        cardPrescribe.setOnClickListener {
            Toast.makeText(this, "Prescribe Medication clicked", Toast.LENGTH_SHORT).show()
        }

        cardViewLabResults.setOnClickListener {
            Toast.makeText(this, "View Lab Results clicked", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}