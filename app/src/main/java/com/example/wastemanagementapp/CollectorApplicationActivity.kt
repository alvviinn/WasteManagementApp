package com.example.wastemanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CollectorApplicationActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collector_application)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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

            val application = hashMapOf(
                "userId" to userId,
                "fullName" to fullName,
                "nationalId" to nationalId,
                "workLocation" to workLocation,
                "employeeNumber" to employeeNumber,
                "status" to "pending",
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("collector_applications")
                .document(userId)
                .set(application)
                .addOnSuccessListener {
                    Toast.makeText(this, "Application submitted. Await approval.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Residents_Page1::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit application.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
