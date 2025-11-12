package com.example.wastemanagementapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Admin_Dashboard : AppCompatActivity() {

    private lateinit var recyclerApplications: RecyclerView
    private lateinit var dbRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference
    private val applicationsList = mutableListOf<CollectorApplication>()
    private lateinit var adapter: ApplicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        recyclerApplications = findViewById(R.id.recyclerApplications)
        recyclerApplications.layoutManager = LinearLayoutManager(this)

        dbRef = FirebaseDatabase.getInstance().getReference("CollectorApplications")
        usersRef = FirebaseDatabase.getInstance().getReference("users")

        adapter = ApplicationAdapter(applicationsList) { userId ->
            approveApplication(userId)
        }

        recyclerApplications.adapter = adapter
        loadPendingApplications()
    }

    private fun loadPendingApplications() {
        dbRef.orderByChild("status").equalTo("pending").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                applicationsList.clear()
                for (data in snapshot.children) {
                    val app = data.getValue(CollectorApplication::class.java)
                    if (app != null) applicationsList.add(app)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Admin_Dashboard, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun approveApplication(userId: String) {
        // Update both application status and user role
        val updates = mapOf(
            "CollectorApplications/$userId/status" to "approved",
            "users/$userId/role" to "collector"
        )

        FirebaseDatabase.getInstance().reference.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Application Approved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
