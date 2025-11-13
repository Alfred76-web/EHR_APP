package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val patientId: Int = 0,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val gender: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val bloodGroup: String,
    val registrationDate: String
)
