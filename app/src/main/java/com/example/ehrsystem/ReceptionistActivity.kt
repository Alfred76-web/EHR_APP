package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ReceptionistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receptionist)

        val cardRegisterPatient = findViewById<CardView>(R.id.cardRegisterPatient)
        val cardViewPatients = findViewById<CardView>(R.id.cardViewPatients)
        val cardScheduleAppt = findViewById<CardView>(R.id.cardScheduleAppt)
        val btnBack = findViewById<Button>(R.id.btnBack)

        cardRegisterPatient.setOnClickListener {
            startActivity(Intent(this, RegisterPatientActivity::class.java))
        }

        cardViewPatients.setOnClickListener {
            startActivity(Intent(this, ViewPatientsActivity::class.java))
        }

        cardScheduleAppt.setOnClickListener {
            // We'll implement this later
            // startActivity(Intent(this, ScheduleAppointmentActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}