package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "diagnosis",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Diagnosis(
    @PrimaryKey(autoGenerate = true)
    val diagnosisId: Int = 0,
    val patientId: Int,
    val symptoms: String,
    val diagnosis: String,
    val treatmentPlan: String,
    val doctorName: String,
    val diagnosisDate: String,
    val notes: String?
)