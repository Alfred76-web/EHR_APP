package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.Appointment
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository
    val scheduledAppointments: LiveData<List<Appointment>>

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
        scheduledAppointments = repository.scheduledAppointments
    }

    fun insertAppointment(appointment: Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAppointment(appointment)
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAppointment(appointment)
        }
    }

    private fun EHRRepository.updateAppointment(appointment: Appointment) {}

    fun getAppointmentsByPatient(patientId: Int): LiveData<List<Appointment>> {
        return repository.getAppointmentsByPatient(patientId)
    }
}