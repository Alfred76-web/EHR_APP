package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class PharmacistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacist)

        // Find views
        val cardViewPrescriptions = findViewById<CardView>(R.id.cardViewPrescriptions)
        val cardDispenseMed = findViewById<CardView>(R.id.cardDispenseMed)
        val cardInventory = findViewById<CardView>(R.id.cardInventory)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Set click listeners
        cardViewPrescriptions.setOnClickListener {
            Toast.makeText(this, "View Prescriptions clicked", Toast.LENGTH_SHORT).show()
        }

        cardDispenseMed.setOnClickListener {
            Toast.makeText(this, "Dispense Medication clicked", Toast.LENGTH_SHORT).show()
        }

        cardInventory.setOnClickListener {
            Toast.makeText(this, "Manage Inventory clicked", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}