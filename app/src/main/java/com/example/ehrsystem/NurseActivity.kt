package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class NurseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurse)

        val cardRecordVitals = findViewById<CardView>(R.id.cardRecordVitals)
        val cardPatientQueue = findViewById<CardView>(R.id.cardPatientQueue)
        val cardAdministerMed = findViewById<CardView>(R.id.cardAdministerMed)
        val btnBack = findViewById<Button>(R.id.btnBack)

        cardRecordVitals.setOnClickListener {
            startActivity(Intent(this, RecordVitalsActivity::class.java))
        }

        cardPatientQueue.setOnClickListener {
            startActivity(Intent(this, ViewPatientsActivity::class.java))
        }

        cardAdministerMed.setOnClickListener {
            // Coming soon
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}