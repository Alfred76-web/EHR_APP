package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DiagnosisDao {
    @Insert
    suspend fun insert(diagnosis: Diagnosis)

    @Query("SELECT * FROM diagnosis WHERE patientId = :patientId ORDER BY diagnosisId DESC")
    fun getDiagnosisByPatient(patientId: Int): LiveData<List<Diagnosis>>

    @Query("SELECT * FROM diagnosis ORDER BY diagnosisId DESC")
    fun getAllDiagnosis(): LiveData<List<Diagnosis>>
}