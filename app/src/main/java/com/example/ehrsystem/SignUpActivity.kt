package com.example.ehrsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.User
import com.example.ehrsystem.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var etFullName: TextInputEditText
    private lateinit var etUsername: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var spinnerRole: Spinner
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSignUp: Button
    private lateinit var tvLogin: TextView
    private lateinit var progressBar: ProgressBar

    private var selectedRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize ViewModel
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Initialize views
        initializeViews()

        // Setup role spinner
        setupRoleSpinner()

        // Button listeners
        btnSignUp.setOnClickListener {
            signUp()
        }

        tvLogin.setOnClickListener {
            finish() // Go back to login
        }
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        spinnerRole = findViewById(R.id.spinnerRole)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLogin = findViewById(R.id.tvLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupRoleSpinner() {
        val roles = arrayOf(
            "Select Role",
            "Receptionist",
            "Nurse",
            "Lab Assistant",
            "Doctor",
            "Pharmacist"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRole = if (position > 0) roles[position] else ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedRole = ""
            }
        }
    }

    private fun signUp() {
        val fullName = etFullName.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validation
        if (!validateInputs(fullName, username, email, phoneNumber, password, confirmPassword)) {
            return
        }

        // Show progress
        showLoading(true)

        // Check if username already exists
        userViewModel.checkUsernameExists(username) { exists ->
            runOnUiThread {
                if (exists) {
                    showLoading(false)
                    Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_LONG).show()
                    etUsername.error = "Username already taken"
                    etUsername.requestFocus()
                } else {
                    // Create user
                    createUser(fullName, username, email, phoneNumber, password)
                }
            }
        }
    }

    private fun createUser(fullName: String, username: String, email: String, phoneNumber: String, password: String) {
        val registrationDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(Date())

        val user = User(
            fullName = fullName,
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            role = selectedRole,
            password = password, // Note: In production, you should hash the password
            registrationDate = registrationDate
        )

        userViewModel.insertUser(user) { success, message ->
            runOnUiThread {
                showLoading(false)

                if (success) {
                    Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                    finish() // Go back to login screen
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateInputs(
        fullName: String,
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        // Full Name validation
        if (fullName.isEmpty()) {
            etFullName.error = "Full name is required"
            etFullName.requestFocus()
            return false
        }

        if (fullName.length < 3) {
            etFullName.error = "Full name must be at least 3 characters"
            etFullName.requestFocus()
            return false
        }

        // Username validation
        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            etUsername.requestFocus()
            return false
        }

        if (username.length < 4) {
            etUsername.error = "Username must be at least 4 characters"
            etUsername.requestFocus()
            return false
        }

        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            etUsername.error = "Username can only contain letters, numbers, and underscores"
            etUsername.requestFocus()
            return false
        }

        // Email validation
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email"
            etEmail.requestFocus()
            return false
        }

        // Phone number validation
        if (phoneNumber.isEmpty()) {
            etPhoneNumber.error = "Phone number is required"
            etPhoneNumber.requestFocus()
            return false
        }

        if (phoneNumber.length < 10) {
            etPhoneNumber.error = "Please enter a valid phone number"
            etPhoneNumber.requestFocus()
            return false
        }

        // Role validation
        if (selectedRole.isEmpty()) {
            Toast.makeText(this, "Please select your role", Toast.LENGTH_SHORT).show()
            spinnerRole.requestFocus()
            return false
        }

        // Password validation
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return false
        }

        // Confirm password validation
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Please confirm your password"
            etConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            btnSignUp.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            btnSignUp.isEnabled = true
        }
    }
}