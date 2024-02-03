package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var planButton: Button
    private lateinit var foodTrackerButton: Button
    private lateinit var symptomCheckerButton: Button
    private lateinit var qoLButton: Button
    private lateinit var eOEedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize the buttons
        profileButton = findViewById(R.id.profileButton)
        planButton = findViewById(R.id.planButton)
        foodTrackerButton = findViewById(R.id.foodTrackerButton)
        symptomCheckerButton = findViewById(R.id.symptomCheckerButton)
        qoLButton = findViewById(R.id.QoLButton)
        eOEedButton = findViewById(R.id.EOEedButton)

        // Set onClickListeners for the buttons
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfilesActivity::class.java)
            startActivity(intent)
        }

        planButton.setOnClickListener {
            val intent = Intent(this, PlanActivity::class.java)
            startActivity(intent)
        }

        foodTrackerButton.setOnClickListener {
            val intent = Intent(this, FoodTrackerActivity::class.java)
            startActivity(intent)
        }

        symptomCheckerButton.setOnClickListener {
            val intent = Intent(this, SymptomCheckerActivity::class.java)
            startActivity(intent)
        }

        qoLButton.setOnClickListener {
            val intent = Intent(this, QoLActivity::class.java)
            startActivity(intent)
        }

        eOEedButton.setOnClickListener {
            val intent = Intent(this, EducationActivity::class.java)
            startActivity(intent)
        }
    }
}