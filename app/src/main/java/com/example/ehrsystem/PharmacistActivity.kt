package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class PharmacistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacist)

        val cardViewPrescriptions = findViewById<CardView>(R.id.cardViewPrescriptions)
        val cardDispenseMed = findViewById<CardView>(R.id.cardDispenseMed)
        val cardInventory = findViewById<CardView>(R.id.cardInventory)
        val btnBack = findViewById<Button>(R.id.btnBack)

        cardViewPrescriptions.setOnClickListener {
            startActivity(Intent(this, ViewPendingPrescriptionsActivity::class.java))
        }

        cardDispenseMed.setOnClickListener {
            startActivity(Intent(this, ViewPendingPrescriptionsActivity::class.java))
        }

        cardInventory.setOnClickListener {
            Toast.makeText(this, "Inventory management - Coming soon", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}