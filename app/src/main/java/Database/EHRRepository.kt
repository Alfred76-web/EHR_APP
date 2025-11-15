package com.example.ehrsystem.database

import androidx.lifecycle.LiveData

class EHRRepository(private val database: EHRDatabase) {

    // Patient operations
    val allPatients: LiveData<List<Patient>> = database.patientDao().getAllPatients()

    suspend fun insertPatient(patient: Patient): Long {
        return database.patientDao().insert(patient)
    }

    fun getPatientById(id: Int): LiveData<Patient> {
        return database.patientDao().getPatientById(id)
    }

    fun searchPatients(query: String): LiveData<List<Patient>> {
        return database.patientDao().searchPatients("%$query%")
    }

    // Vital Signs operations
    suspend fun insertVitalSigns(vitalSigns: VitalSigns) {
        database.vitalSignsDao().insert(vitalSigns)
    }

    fun getVitalSignsByPatient(patientId: Int): LiveData<List<VitalSigns>> {
        return database.vitalSignsDao().getVitalSignsByPatient(patientId)
    }

    // Lab Test operations
    suspend fun insertLabTest(labTest: LabTest) {
        database.labTestDao().insert(labTest)
    }

    suspend fun updateLabTest(labTest: LabTest) {
        database.labTestDao().update(labTest)
    }

    val pendingLabTests: LiveData<List<LabTest>> = database.labTestDao().getPendingTests()

    fun getLabTestsByPatient(patientId: Int): LiveData<List<LabTest>> {
        return database.labTestDao().getLabTestsByPatient(patientId)
    }

    // Diagnosis operations
    suspend fun insertDiagnosis(diagnosis: Diagnosis) {
        database.diagnosisDao().insert(diagnosis)
    }

    fun getDiagnosisByPatient(patientId: Int): LiveData<List<Diagnosis>> {
        return database.diagnosisDao().getDiagnosisByPatient(patientId)
    }

    // Prescription operations
    suspend fun insertPrescription(prescription: Prescription) {
        database.prescriptionDao().insert(prescription)
    }

    suspend fun updatePrescription(prescription: Prescription) {
        database.prescriptionDao().update(prescription)
    }

    val pendingPrescriptions: LiveData<List<Prescription>> =
        database.prescriptionDao().getPendingPrescriptions()

    fun getPrescriptionsByPatient(patientId: Int): LiveData<List<Prescription>> {
        return database.prescriptionDao().getPrescriptionsByPatient(patientId)
    }

    // Appointment operations
    suspend fun insertAppointment(appointment: Appointment) {
        database.appointmentDao().insert(appointment)
    }

    suspend fun updateAppointment(appointment: Appointment) {
        database.appointmentDao().update(appointment)
    }

    val scheduledAppointments: LiveData<List<Appointment>> =
        database.appointmentDao().getScheduledAppointments()

    fun getAppointmentsByPatient(patientId: Int): LiveData<List<Appointment>> {
        return database.appointmentDao().getAppointmentsByPatient(patientId)
    }

    // User operations - ADD THESE
    suspend fun insertUser(user: User): Long {
        return database.userDao().insert(user)
    }

    suspend fun login(username: String, password: String): User? {
        return database.userDao().login(username, password)
    }

    suspend fun getUserByUsername(username: String): User? {
        return database.userDao().getUserByUsername(username)
    }

    val allUsers: LiveData<List<User>> = database.userDao().getAllUsers()

    fun getUsersByRole(role: String): LiveData<List<User>> {
        return database.userDao().getUsersByRole(role)
    }
}