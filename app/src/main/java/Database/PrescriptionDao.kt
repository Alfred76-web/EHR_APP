package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PrescriptionDao {
    @Insert
    suspend fun insert(prescription: Prescription)

    @Update
    suspend fun update(prescription: Prescription)

    @Query("SELECT * FROM prescriptions WHERE patientId = :patientId ORDER BY prescriptionId DESC")
    fun getPrescriptionsByPatient(patientId: Int): LiveData<List<Prescription>>

    @Query("SELECT * FROM prescriptions WHERE dispensedStatus = 'Pending' ORDER BY prescriptionId DESC")
    fun getPendingPrescriptions(): LiveData<List<Prescription>>

    @Query("SELECT * FROM prescriptions ORDER BY prescriptionId DESC")
    fun getAllPrescriptions(): LiveData<List<Prescription>>
}