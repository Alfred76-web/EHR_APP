package com.example.ehrsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppointmentDao {
    @Insert
    suspend fun insert(appointment: Appointment)

    @Update
    suspend fun update(appointment: Appointment)

    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY appointmentId DESC")
    fun getAppointmentsByPatient(patientId: Int): LiveData<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE status = 'Scheduled' ORDER BY appointmentDate, appointmentTime")
    fun getScheduledAppointments(): LiveData<List<Appointment>>

    @Query("SELECT * FROM appointments ORDER BY appointmentId DESC")
    fun getAllAppointments(): LiveData<List<Appointment>>
}