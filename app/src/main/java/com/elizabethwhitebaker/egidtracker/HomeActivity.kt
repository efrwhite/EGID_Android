package com.elizabethwhitebaker.egidtracker

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide

class HomeActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var planButton: Button
    private lateinit var foodTrackerButton: Button
    private lateinit var symptomCheckerButton: Button
    private lateinit var qoLButton: Button
    private lateinit var eOEedButton: Button
    private lateinit var childNameTextView: TextView
    private lateinit var childDietTextView: TextView
    private lateinit var childImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize the views
        initializeViews()

        val childId = getCurrentChildId()
        childId?.let {
            fetchAndDisplayChildData(it)
        }
    }

    private fun initializeViews() {
        profileButton = findViewById(R.id.profileButton)
        planButton = findViewById(R.id.planButton)
        foodTrackerButton = findViewById(R.id.foodTrackerButton)
        symptomCheckerButton = findViewById(R.id.symptomCheckerButton)
        qoLButton = findViewById(R.id.QoLButton)
        eOEedButton = findViewById(R.id.EOEedButton)
        childNameTextView = findViewById(R.id.childName)
        childDietTextView = findViewById(R.id.childDiet)
        childImageView = findViewById(R.id.homeImage)

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

    private fun getCurrentChildId(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("CurrentChildId", null)
    }

    private fun fetchAndDisplayChildData(childId: String) {
        Firebase.firestore.collection("Children").document(childId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val childName = document.getString("firstName") ?: "No Name"
                    val childDiet = document.getString("diet") ?: "No Diet Specified"
                    val imageUrl = document.getString("imageUrl") ?: ""

                    // Update UI with child data
                    childNameTextView.text = childName
                    childDietTextView.text = childDiet
                    Glide.with(this).load(imageUrl).into(childImageView)
                } else {
                    Toast.makeText(this, "Child not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load child data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
