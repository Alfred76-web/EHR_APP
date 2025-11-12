package com.example.ehrsystem.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Patient::class,
        VitalSigns::class,
        LabTest::class,
        Diagnosis::class,
        Prescription::class,
        Appointment::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EHRDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun vitalSignsDao(): VitalSignsDao
    abstract fun labTestDao(): LabTestDao
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: EHRDatabase? = null

        fun getDatabase(context: Context): EHRDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EHRDatabase::class.java,
                    "ehr_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}