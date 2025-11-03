package com.example.wastemanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApplicationAdapter(
    private val applications: List<CollectorApplication>,
    private val onApproveClick: (String) -> Unit
) : RecyclerView.Adapter<ApplicationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvNationalId: TextView = view.findViewById(R.id.tvNationalId)
        val tvWorkLocation: TextView = view.findViewById(R.id.tvWorkLocation)
        val tvEmployeeNumber: TextView = view.findViewById(R.id.tvEmployeeNumber)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = applications[position]
        holder.tvFullName.text = "Full Name: ${app.fullName}"
        holder.tvNationalId.text = "National ID: ${app.nationalId}"
        holder.tvWorkLocation.text = "Work Location: ${app.workLocation}"
        holder.tvEmployeeNumber.text = "Employee No: ${app.employeeNumber}"

        holder.btnApprove.setOnClickListener {
            app.userId?.let { id -> onApproveClick(id) }
        }
    }

    override fun getItemCount(): Int = applications.size
}
