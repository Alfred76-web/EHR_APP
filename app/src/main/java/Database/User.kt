package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val password: String,
    val fullName: String,
    val role: String, // Receptionist, Nurse, Lab Assistant, Doctor, Pharmacist
    val email: String,
    val phoneNumber: String,
    val registrationDate: String
)