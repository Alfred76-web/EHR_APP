package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import com.example.ehrsystem.database.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository
    val allPatients: LiveData<List<Patient>>

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
        allPatients = repository.allPatients
    }

    fun insertPatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPatient(patient)
        }
    }

    fun searchPatients(query: String): LiveData<List<Patient>> {
        return repository.searchPatients(query)
    }

    fun getPatientById(id: Int): LiveData<Patient> {
        return repository.getPatientById(id)
    }
}