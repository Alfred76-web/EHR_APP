package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find all card views
        val cardReceptionist = findViewById<CardView>(R.id.cardReceptionist)
        val cardNurse = findViewById<CardView>(R.id.cardNurse)
        val cardLabAssistant = findViewById<CardView>(R.id.cardLabAssistant)
        val cardDoctor = findViewById<CardView>(R.id.cardDoctor)
        val cardPharmacist = findViewById<CardView>(R.id.cardPharmacist)

        // Set click listeners for each card
        cardReceptionist.setOnClickListener {
            val intent = Intent(this, ReceptionistActivity::class.java)
            startActivity(intent)
        }

        cardNurse.setOnClickListener {
            val intent = Intent(this, NurseActivity::class.java)
            startActivity(intent)
        }

        cardLabAssistant.setOnClickListener {
            val intent = Intent(this, LabAssistantActivity::class.java)
            startActivity(intent)
        }

        cardDoctor.setOnClickListener {
            val intent = Intent(this, DoctorActivity::class.java)
            startActivity(intent)
        }

        cardPharmacist.setOnClickListener {
            val intent = Intent(this, PharmacistActivity::class.java)
            startActivity(intent)
        }
    }
}