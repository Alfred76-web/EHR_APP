package com.example.ehrsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehrsystem.database.LabTest

class LabTestAdapter(
    private var labTests: List<LabTest>,
    private val onEnterResultsClick: (LabTest) -> Unit
) : RecyclerView.Adapter<LabTestAdapter.LabTestViewHolder>() {

    class LabTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTestName: TextView = itemView.findViewById(R.id.tvTestName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTestType: TextView = itemView.findViewById(R.id.tvTestType)
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvRequestedBy: TextView = itemView.findViewById(R.id.tvRequestedBy)
        val tvRequestDate: TextView = itemView.findViewById(R.id.tvRequestDate)
        val btnEnterResults: Button = itemView.findViewById(R.id.btnEnterResults)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabTestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lab_test, parent, false)
        return LabTestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabTestViewHolder, position: Int) {
        val labTest = labTests[position]

        holder.tvTestName.text = labTest.testName
        holder.tvStatus.text = labTest.status
        holder.tvTestType.text = "Type: ${labTest.testType}"
        holder.tvPatientName.text = "Patient ID: ${labTest.patientId}"
        holder.tvRequestedBy.text = "Requested by: ${labTest.requestedBy}"
        holder.tvRequestDate.text = "Date: ${labTest.requestedDate}"

        // Change status color
        when (labTest.status) {
            "Pending" -> holder.tvStatus.setBackgroundColor(0xFFFF9800.toInt())
            "Completed" -> holder.tvStatus.setBackgroundColor(0xFF4CAF50.toInt())
            else -> holder.tvStatus.setBackgroundColor(0xFF757575.toInt())
        }

        // Show/hide button based on status
        if (labTest.status == "Pending") {
            holder.btnEnterResults.visibility = View.VISIBLE
            holder.btnEnterResults.setOnClickListener {
                onEnterResultsClick(labTest)
            }
        } else {
            holder.btnEnterResults.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = labTests.size

    fun updateLabTests(newLabTests: List<LabTest>) {
        labTests = newLabTests
        notifyDataSetChanged()
    }
}