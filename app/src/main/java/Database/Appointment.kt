package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "appointments",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val appointmentId: Int = 0,
    val patientId: Int,
    val doctorName: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val reason: String,
    val status: String, // Scheduled, Completed, Cancelled
    val createdBy: String // Receptionist name
)