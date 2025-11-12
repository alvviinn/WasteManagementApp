package com.example.wastemanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CollectorApplicationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collector_application)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etNationalId = findViewById<EditText>(R.id.etNationalId)
        val etWorkLocation = findViewById<EditText>(R.id.etWorkLocation)
        val etEmployeeNumber = findViewById<EditText>(R.id.etEmployeeNumber)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitApplication)

        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val nationalId = etNationalId.text.toString().trim()
            val workLocation = etWorkLocation.text.toString().trim()
            val employeeNumber = etEmployeeNumber.text.toString().trim()

            if (fullName.isEmpty() || nationalId.isEmpty() || workLocation.isEmpty() || employeeNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid ?: return@setOnClickListener

            val application = CollectorApplication(
                userId = userId,
                fullName = fullName,
                nationalId = nationalId,
                workLocation = workLocation,
                employeeNumber = employeeNumber,
                status = "pending"
            )

            db.getReference("CollectorApplications").child(userId).setValue(application)
                .addOnSuccessListener {
                    Toast.makeText(this, "Application submitted. Await approval.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Residents_Page1::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit application: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
