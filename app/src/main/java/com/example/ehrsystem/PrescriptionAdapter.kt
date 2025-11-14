package com.example.ehrsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.Prescription

class PrescriptionAdapter(
    private var prescriptions: List<Prescription>,
    private val onDispenseClick: (Prescription) -> Unit
) : RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>() {

    class PrescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMedicationName: TextView = itemView.findViewById(R.id.tvMedicationName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDosageInfo: TextView = itemView.findViewById(R.id.tvDosageInfo)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvPrescribedBy: TextView = itemView.findViewById(R.id.tvPrescribedBy)
        val tvInstructions: TextView = itemView.findViewById(R.id.tvInstructions)
        val btnDispense: Button = itemView.findViewById(R.id.btnDispense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prescription, parent, false)
        return PrescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        val prescription = prescriptions[position]

        holder.tvMedicationName.text = prescription.medicationName
        holder.tvStatus.text = prescription.dispensedStatus
        holder.tvDosageInfo.text = "Dosage: ${prescription.dosage} | Frequency: ${prescription.frequency}"
        holder.tvDuration.text = "Duration: ${prescription.duration}"
        holder.tvPatientName.text = "Patient ID: ${prescription.patientId}"
        holder.tvPrescribedBy.text = "Prescribed by: ${prescription.prescribedBy}"
        holder.tvInstructions.text = "Instructions: ${prescription.instructions}"

        // Change status color
        when (prescription.dispensedStatus) {
            "Pending" -> holder.tvStatus.setBackgroundColor(0xFFFF9800.toInt())
            "Dispensed" -> holder.tvStatus.setBackgroundColor(0xFF4CAF50.toInt())
            else -> holder.tvStatus.setBackgroundColor(0xFF757575.toInt())
        }

        // Show/hide button based on status
        if (prescription.dispensedStatus == "Pending") {
            holder.btnDispense.visibility = View.VISIBLE
            holder.btnDispense.setOnClickListener {
                onDispenseClick(prescription)
            }
        } else {
            holder.btnDispense.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = prescriptions.size

    fun updatePrescriptions(newPrescriptions: List<Prescription>) {
        prescriptions = newPrescriptions
        notifyDataSetChanged()
    }
}