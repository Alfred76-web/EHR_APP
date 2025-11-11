package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ReceptionistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receptionist)

        // Find views
        val cardRegisterPatient = findViewById<CardView>(R.id.cardRegisterPatient)
        val cardViewPatients = findViewById<CardView>(R.id.cardViewPatients)
        val cardScheduleAppt = findViewById<CardView>(R.id.cardScheduleAppt)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Set click listeners
        cardRegisterPatient.setOnClickListener {
            Toast.makeText(this, "Register New Patient clicked", Toast.LENGTH_SHORT).show()
        }

        cardViewPatients.setOnClickListener {
            Toast.makeText(this, "View All Patients clicked", Toast.LENGTH_SHORT).show()
        }

        cardScheduleAppt.setOnClickListener {
            Toast.makeText(this, "Schedule Appointment clicked", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish() // Return to previous screen
        }
    }
}