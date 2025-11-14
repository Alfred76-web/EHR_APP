package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import com.example.ehrsystem.database.LabTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LabTestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository
    val pendingLabTests: LiveData<List<LabTest>>

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
        pendingLabTests = repository.pendingLabTests
    }

    fun insertLabTest(labTest: LabTest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLabTest(labTest)
        }
    }

    fun updateLabTest(labTest: LabTest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLabTest(labTest)
        }
    }

    fun getLabTestsByPatient(patientId: Int): LiveData<List<LabTest>> {
        return repository.getLabTestsByPatient(patientId)
    }
}