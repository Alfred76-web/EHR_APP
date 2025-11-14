package com.example.ehrsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.Patient
import java.text.SimpleDateFormat
import java.util.*

class PatientAdapter(
    private var patients: List<Patient>,
    private val onItemClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvPatientId: TextView = itemView.findViewById(R.id.tvPatientId)
        val tvPatientDetails: TextView = itemView.findViewById(R.id.tvPatientDetails)
        val tvPatientContact: TextView = itemView.findViewById(R.id.tvPatientContact)
        val tvRegistrationDate: TextView = itemView.findViewById(R.id.tvRegistrationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]

        holder.tvPatientName.text = "${patient.firstName} ${patient.lastName}"
        holder.tvPatientId.text = "ID: ${String.format("%03d", patient.patientId)}"

        // Calculate age from date of birth
        val age = calculateAge(patient.dateOfBirth)
        holder.tvPatientDetails.text =
            "Age: $age | Gender: ${patient.gender} | Blood: ${patient.bloodGroup}"

        holder.tvPatientContact.text = "📞 ${patient.phoneNumber}"
        holder.tvRegistrationDate.text = "Registered: ${patient.registrationDate}"

        holder.itemView.setOnClickListener {
            onItemClick(patient)
        }
    }

    override fun getItemCount(): Int = patients.size

    fun updatePatients(newPatients: List<Patient>) {
        patients = newPatients
        notifyDataSetChanged()
    }

    private fun calculateAge(dateOfBirth: String): Int {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val birthDate = sdf.parse(dateOfBirth)
            val today = Calendar.getInstance()
            val birth = Calendar.getInstance()
            birth.time = birthDate ?: Date()

            var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: Exception) {
            0
        }
    }
}