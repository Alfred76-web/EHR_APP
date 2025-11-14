package com.example.ehrsystem

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegisterPatientActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var etFirstName: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etDateOfBirth: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_patient)

        // Initialize ViewModel
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        rgGender = findViewById(R.id.rgGender)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Setup Blood Group Spinner
        setupBloodGroupSpinner()

        // Setup Date Picker
        setupDatePicker()

        // Button listeners
        btnSave.setOnClickListener {
            savePatient()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupBloodGroupSpinner() {
        val bloodGroups = arrayOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = adapter
    }

    private fun setupDatePicker() {
        etDateOfBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        etDateOfBirth.setText(dateFormat.format(calendar.time))
    }

    private fun savePatient() {
        // Get values
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val dateOfBirth = etDateOfBirth.text.toString().trim()
        val gender = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> "Male"
            R.id.rbFemale -> "Female"
            R.id.rbOther -> "Other"
            else -> "Male"
        }
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val bloodGroup = spinnerBloodGroup.selectedItem.toString()

        // Validation
        if (firstName.isEmpty()) {
            etFirstName.error = "First name is required"
            etFirstName.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            etLastName.error = "Last name is required"
            etLastName.requestFocus()
            return
        }

        if (dateOfBirth.isEmpty()) {
            etDateOfBirth.error = "Date of birth is required"
            etDateOfBirth.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.error = "Phone number is required"
            etPhoneNumber.requestFocus()
            return
        }

        if (bloodGroup == "Select Blood Group") {
            Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show()
            return
        }

        // Get current date for registration
        val registrationDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
            .format(Date())

        // Create patient object
        val patient = Patient(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            gender = gender,
            phoneNumber = phoneNumber,
            email = email,
            address = address,
            bloodGroup = bloodGroup,
            registrationDate = registrationDate
        )

        // Save to database
        patientViewModel.insertPatient(patient)

        Toast.makeText(this, "Patient registered successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}