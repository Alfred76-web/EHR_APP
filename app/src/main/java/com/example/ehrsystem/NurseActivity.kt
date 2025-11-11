package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class NurseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurse)

        // Find views
        val cardRecordVitals = findViewById<CardView>(R.id.cardRecordVitals)
        val cardPatientQueue = findViewById<CardView>(R.id.cardPatientQueue)
        val cardAdministerMed = findViewById<CardView>(R.id.cardAdministerMed)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Set click listeners
        cardRecordVitals.setOnClickListener {
            Toast.makeText(this, "Record Vital Signs clicked", Toast.LENGTH_SHORT).show()
        }

        cardPatientQueue.setOnClickListener {
            Toast.makeText(this, "View Patient Queue clicked", Toast.LENGTH_SHORT).show()
        }

        cardAdministerMed.setOnClickListener {
            Toast.makeText(this, "Administer Medication clicked", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}