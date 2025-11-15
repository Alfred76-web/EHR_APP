package com.example.ehrsystem

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ehrsystem.database.Appointment
import com.example.ehrsystem.database.Patient
import com.example.ehrsystem.viewmodel.AppointmentViewModel
import com.example.ehrsystem.viewmodel.PatientViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAppointmentActivity : AppCompatActivity() {

    private lateinit var patientViewModel: PatientViewModel
    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var spinnerPatient: Spinner
    private lateinit var etDoctorName: TextInputEditText
    private lateinit var etAppointmentDate: TextInputEditText
    private lateinit var etAppointmentTime: TextInputEditText
    private lateinit var etReason: TextInputEditText
    private lateinit var etReceptionistName: TextInputEditText
    private lateinit var btnSchedule: Button
    private lateinit var btnCancel: Button

    private var patientsList = listOf<Patient>()
    private var selectedPatient: Patient? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_appointment)

        // Initialize ViewModels
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        appointmentViewModel = ViewModelProvider(this)[AppointmentViewModel::class.java]

        // Initialize views
        initializeViews()

        // Load patients
        loadPatients()

        // Setup date and time pickers
        setupDatePicker()
        setupTimePicker()

        // Button listeners
        setupButtonListeners()
    }

    private fun initializeViews() {
        spinnerPatient = findViewById(R.id.spinnerPatient)
        etDoctorName = findViewById(R.id.etDoctorName)
        etAppointmentDate = findViewById(R.id.etAppointmentDate)
        etAppointmentTime = findViewById(R.id.etAppointmentTime)
        etReason = findViewById(R.id.etReason)
        etReceptionistName = findViewById(R.id.etReceptionistName)
        btnSchedule = findViewById(R.id.btnSchedule)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupButtonListeners() {
        btnSchedule.setOnClickListener {
            scheduleAppointment()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadPatients() {
        patientViewModel.allPatients.observe(this) { patients ->
            if (patients != null && patients.isNotEmpty()) {
                patientsList = patients
                setupPatientSpinner(patients)
            } else {
                Toast.makeText(
                    this,
                    "No patients registered. Please register patients first.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupPatientSpinner(patients: List<Patient>) {
        val patientNames = mutableListOf("Select Patient")
        patientNames.addAll(patients.map {
            "${it.firstName} ${it.lastName} (ID: ${String.format("%03d", it.patientId)})"
        })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            patientNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPatient.adapter = adapter

        spinnerPatient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedPatient = if (position > 0) {
                    patientsList[position - 1]
                } else {
                    null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPatient = null
            }
        }
    }

    private fun setupDatePicker() {
        etAppointmentDate.setOnClickListener {
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
            // Set minimum date to today
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        etAppointmentDate.setText(dateFormat.format(calendar.time))
    }

    private fun setupTimePicker() {
        etAppointmentTime.setOnClickListener {
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    updateTimeInView()
                },
                currentHour,
                currentMinute,
                true // 24-hour format
            )
            timePickerDialog.show()
        }
    }

    private fun updateTimeInView() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
        etAppointmentTime.setText(timeFormat.format(calendar.time))
    }

    private fun scheduleAppointment() {
        // Check if patient is selected
        if (selectedPatient == null) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            spinnerPatient.requestFocus()
            return
        }

        // Get values
        val doctorName = etDoctorName.text.toString().trim()
        val appointmentDate = etAppointmentDate.text.toString().trim()
        val appointmentTime = etAppointmentTime.text.toString().trim()
        val reason = etReason.text.toString().trim()
        val receptionistName = etReceptionistName.text.toString().trim()

        // Validation
        if (!validateInputs(doctorName, appointmentDate, appointmentTime, reason, receptionistName)) {
            return
        }

        // Create appointment object
        val appointment = Appointment(
            patientId = selectedPatient!!.patientId,
            doctorName = doctorName,
            appointmentDate = appointmentDate,
            appointmentTime = appointmentTime,
            reason = reason,
            status = "Scheduled",
            createdBy = receptionistName
        )

        // Save to database
        appointmentViewModel.insertAppointment(appointment)

        Toast.makeText(
            this,
            "Appointment scheduled successfully for ${selectedPatient!!.firstName} ${selectedPatient!!.lastName}",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun validateInputs(
        doctorName: String,
        appointmentDate: String,
        appointmentTime: String,
        reason: String,
        receptionistName: String
    ): Boolean {
        if (doctorName.isEmpty()) {
            etDoctorName.error = "Doctor name is required"
            etDoctorName.requestFocus()
            return false
        }

        if (doctorName.length < 3) {
            etDoctorName.error = "Please enter a valid doctor name"
            etDoctorName.requestFocus()
            return false
        }

        if (appointmentDate.isEmpty()) {
            etAppointmentDate.error = "Appointment date is required"
            etAppointmentDate.requestFocus()
            Toast.makeText(this, "Please select appointment date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (appointmentTime.isEmpty()) {
            etAppointmentTime.error = "Appointment time is required"
            etAppointmentTime.requestFocus()
            Toast.makeText(this, "Please select appointment time", Toast.LENGTH_SHORT).show()
            return false
        }

        if (reason.isEmpty()) {
            etReason.error = "Reason for visit is required"
            etReason.requestFocus()
            return false
        }

        if (reason.length < 10) {
            etReason.error = "Please provide detailed reason (at least 10 characters)"
            etReason.requestFocus()
            return false
        }

        if (receptionistName.isEmpty()) {
            etReceptionistName.error = "Your name is required"
            etReceptionistName.requestFocus()
            return false
        }

        if (receptionistName.length < 3) {
            etReceptionistName.error = "Please enter a valid name"
            etReceptionistName.requestFocus()
            return false
        }

        return true
    }
}