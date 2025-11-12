package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LabTestDao {
    @Insert
    suspend fun insert(labTest: LabTest)

    @Update
    suspend fun update(labTest: LabTest)

    @Query("SELECT * FROM lab_tests WHERE patientId = :patientId ORDER BY testId DESC")
    fun getLabTestsByPatient(patientId: Int): LiveData<List<LabTest>>

    @Query("SELECT * FROM lab_tests WHERE status = 'Pending' ORDER BY testId DESC")
    fun getPendingTests(): LiveData<List<LabTest>>

    @Query("SELECT * FROM lab_tests ORDER BY testId DESC")
    fun getAllLabTests(): LiveData<List<LabTest>>
}