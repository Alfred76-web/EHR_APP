package com.example.ehrsystem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private var userName: String = ""
    private var userRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get user info from SharedPreferences
        val sharedPref = getSharedPreferences("EHRPrefs", Context.MODE_PRIVATE)
        userName = sharedPref.getString("fullName", "User") ?: "User"
        userRole = sharedPref.getString("role", "") ?: ""

        // Set action bar title
        supportActionBar?.title = "Welcome, $userName"
        supportActionBar?.subtitle = "Role: $userRole"

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutDialog()
                true
            }
            R.id.action_profile -> {
                showProfileDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun logout() {
        // Clear login state
        val sharedPref = getSharedPreferences("EHRPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        // Go to login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showProfileDialog() {
        val sharedPref = getSharedPreferences("EHRPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "N/A")
        val fullName = sharedPref.getString("fullName", "N/A")
        val role = sharedPref.getString("role", "N/A")

        val message = """
            Username: $username
            Full Name: $fullName
            Role: $role
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Profile Information")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}