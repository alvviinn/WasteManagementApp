package com.example.wastemanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class Login_Activity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
    private lateinit var signup: TextView
    private lateinit var forgotPassword: TextView

    private lateinit var lemail: EditText
    private lateinit var lpassword: EditText
    private lateinit var lbtnLogin: Button
    private lateinit var lsignup: TextView
    private lateinit var lforgot: TextView
    private lateinit var auth: FirebaseAuth

    private val adminEmail = "rio@gmail.com"
    private val adminPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        lemail = findViewById(R.id.email)
        lpassword = findViewById(R.id.password)
        lbtnLogin = findViewById(R.id.btnLogin)
        lsignup = findViewById(R.id.signup)
        lforgot = findViewById(R.id.forgotPassword)

        // Login button listener
        lbtnLogin.setOnClickListener {
            val email = lemail.text.toString().trim()
            val password = lpassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Admin login check
            if (email == adminEmail && password == adminPassword) {
                Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Admin_Dashboard::class.java))
                finish()
            } else {
                // Firebase login for residents
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Residents_Page1::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        // Go to sign-up screen
        lsignup.setOnClickListener {
            val intent = Intent(this, Register_Activity::class.java)
            startActivity(intent)
        }

        // Forgot password
        lforgot.setOnClickListener {
            val email = lemail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
