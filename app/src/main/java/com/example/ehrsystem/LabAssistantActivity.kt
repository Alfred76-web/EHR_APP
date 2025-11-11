package com.example.ehrsystem

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class LabAssistantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_assistant)

        // Find views
        val cardNewTest = findViewById<CardView>(R.id.cardNewTest)
        val cardEnterResults = findViewById<CardView>(R.id.cardEnterResults)
        val cardPendingTests = findViewById<CardView>(R.id.cardPendingTests)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Set click listeners
        cardNewTest.setOnClickListener {
            Toast.makeText(this, "Conduct Lab Test clicked", Toast.LENGTH_SHORT).show()
        }

        cardEnterResults.setOnClickListener {
            Toast.makeText(this, "Enter Test Results clicked", Toast.LENGTH_SHORT).show()
        }

        cardPendingTests.setOnClickListener {
            Toast.makeText(this, "View Pending Tests clicked", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}