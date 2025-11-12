package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "lab_tests",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LabTest(
    @PrimaryKey(autoGenerate = true)
    val testId: Int = 0,
    val patientId: Int,
    val testName: String,
    val testType: String, // Blood, Urine, X-Ray, etc.
    val requestedBy: String, // Doctor name
    val requestedDate: String,
    val status: String, // Pending, Completed
    val results: String?,
    val completedDate: String?,
    val labTechnician: String?
)