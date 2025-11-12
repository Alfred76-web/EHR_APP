package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VitalSignsDao {
    @Insert
    suspend fun insert(vitalSigns: VitalSigns)

    @Query("SELECT * FROM vital_signs WHERE patientId = :patientId ORDER BY vitalId DESC")
    fun getVitalSignsByPatient(patientId: Int): LiveData<List<VitalSigns>>

    @Query("SELECT * FROM vital_signs ORDER BY vitalId DESC")
    fun getAllVitalSigns(): LiveData<List<VitalSigns>>
}