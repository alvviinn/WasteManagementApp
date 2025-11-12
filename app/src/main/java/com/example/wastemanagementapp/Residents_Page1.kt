package com.example.wastemanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Residents_Page1 : AppCompatActivity() {
    private lateinit var action_Report : LinearLayout
    private lateinit var action_schedule : LinearLayout
    private lateinit var action_map : LinearLayout
    private lateinit var btn_apply : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_residents_page1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        action_Report = findViewById(R.id.Report_ID)
        btn_apply = findViewById(R.id.btnCollectorApplication)

        action_Report.setOnClickListener {
            val intent = Intent(this, Residents_Report ::class.java)
            startActivity(intent)
        }

        action_schedule = findViewById(R.id.Schedule_ID)

        action_schedule.setOnClickListener {
            val intent = Intent(this, Residents_Schedule ::class.java)
            startActivity(intent)
        }

        action_map = findViewById(R.id.Map_ID)

        action_map.setOnClickListener {
            val intent = Intent(this, Residents_map ::class.java)
            startActivity(intent)
        }
        btn_apply.setOnClickListener {
            val intent = Intent(this, CollectorApplicationActivity ::class.java)
            startActivity(intent)
        }

    }
}