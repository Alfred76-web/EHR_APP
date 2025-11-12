package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(patient: Patient): Long

    @Update
    suspend fun update(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT * FROM patients ORDER BY patientId DESC")
    fun getAllPatients(): LiveData<List<Patient>>

    @Query("SELECT * FROM patients WHERE patientId = :id")
    fun getPatientById(id: Int): LiveData<Patient>

    @Query("SELECT * FROM patients WHERE firstName LIKE :search OR lastName LIKE :search")
    fun searchPatients(search: String): LiveData<List<Patient>>
}