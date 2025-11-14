package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.Diagnosis
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiagnosisViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
    }

    fun insertDiagnosis(diagnosis: Diagnosis) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDiagnosis(diagnosis)
        }
    }

    fun getDiagnosisByPatient(patientId: Int): LiveData<List<Diagnosis>> {
        return repository.getDiagnosisByPatient(patientId)
    }
}