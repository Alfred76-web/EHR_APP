package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import com.example.ehrsystem.database.Prescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrescriptionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository
    val pendingPrescriptions: LiveData<List<Prescription>>

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
        pendingPrescriptions = repository.pendingPrescriptions
    }

    fun insertPrescription(prescription: Prescription) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPrescription(prescription)
        }
    }

    fun updatePrescription(prescription: Prescription) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePrescription(prescription)
        }
    }

    fun getPrescriptionsByPatient(patientId: Int): LiveData<List<Prescription>> {
        return repository.getPrescriptionsByPatient(patientId)
    }
}