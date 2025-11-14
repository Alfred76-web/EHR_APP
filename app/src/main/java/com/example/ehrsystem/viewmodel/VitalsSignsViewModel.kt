package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import com.example.ehrsystem.database.VitalSigns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VitalSignsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
    }

    fun insertVitalSigns(vitalSigns: VitalSigns) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertVitalSigns(vitalSigns)
        }
    }

    fun getVitalSignsByPatient(patientId: Int): LiveData<List<VitalSigns>> {
        return repository.getVitalSignsByPatient(patientId)
    }
}