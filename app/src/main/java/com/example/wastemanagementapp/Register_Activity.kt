package com.example.wastemanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register_Activity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSignUp: Button
    private lateinit var tvSignIn: TextView
    private lateinit var roleSpinner: Spinner

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize views
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvSignIn = findViewById(R.id.tvSignIn)

        // Add role spinner dynamically below Confirm Password
        roleSpinner = Spinner(this)
        val layout = findViewById<LinearLayout>(R.id.main)
        val roleLabel = TextView(this)
        roleLabel.text = "Select Role"
        roleLabel.textSize = 16f
        roleLabel.setTextColor(getColor(android.R.color.darker_gray))
        layout.addView(roleLabel, layout.indexOfChild(findViewById(R.id.etConfirmPassword)) + 1)
        layout.addView(roleSpinner, layout.indexOfChild(roleLabel) + 1)

        // Spinner setup
        val roles = arrayOf("Resident", "Collector", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter

        // Sign up button click
        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val selectedRole = roleSpinner.selectedItem.toString()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create account in Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val userMap = mapOf(
                            "uid" to uid,
                            "name" to fullName,
                            "email" to email,
                            "role" to selectedRole
                        )

                        // Store user info in Realtime Database
                        if (uid != null) {
                            database.getReference("users").child(uid).setValue(userMap)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                                        // Navigate to dashboard based on role
                                        when (selectedRole) {
                                            "Admin" -> startActivity(Intent(this, Admin_Dashboard::class.java))
                                            "Collector" -> startActivity(Intent(this, Collector_Dashboard::class.java))
                                            else -> startActivity(Intent(this, Resident_Dashboard::class.java))
                                        }

                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Already have an account? → Go to login
        tvSignIn.setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }
    }
}
