package com.example.ehrsystem.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "prescriptions",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Prescription(
    @PrimaryKey(autoGenerate = true)
    val prescriptionId: Int = 0,
    val patientId: Int,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val instructions: String,
    val prescribedBy: String, // Doctor name
    val prescribedDate: String,
    val dispensedStatus: String, // Pending, Dispensed
    val dispensedBy: String?,
    val dispensedDate: String?
)