package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vital_signs",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class VitalSigns(
    @PrimaryKey(autoGenerate = true)
    val vitalId: Int = 0,
    val patientId: Int,
    val bloodPressure: String,
    val temperature: String,
    val pulse: String,
    val respiratoryRate: String,
    val weight: String,
    val height: String,
    val recordedDate: String,
    val recordedBy: String // Nurse name
)
