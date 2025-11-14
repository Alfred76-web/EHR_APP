package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class LabAssistantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_assistant)

        val cardNewTest = findViewById<CardView>(R.id.cardNewTest)
        val cardEnterResults = findViewById<CardView>(R.id.cardEnterResults)
        val cardPendingTests = findViewById<CardView>(R.id.cardPendingTests)
        val btnBack = findViewById<Button>(R.id.btnBack)

        cardNewTest.setOnClickListener {
            startActivity(Intent(this, RequestLabTestActivity::class.java))
        }

        cardEnterResults.setOnClickListener {
            startActivity(Intent(this, ViewPendingTestsActivity::class.java))
        }

        cardPendingTests.setOnClickListener {
            startActivity(Intent(this, ViewPendingTestsActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}