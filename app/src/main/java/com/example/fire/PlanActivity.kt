package com.example.fire

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlanActivity : AppCompatActivity() {

    private lateinit var dietButton: Button
    private lateinit var medicationsButton: Button
    private lateinit var documentsButton: Button
    private lateinit var childDiet: String
    private lateinit var planName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yourplan)

        //Initialize buttons
        dietButton = findViewById(R.id.dietButton)
        medicationsButton = findViewById(R.id.medicationsButton)
        documentsButton = findViewById(R.id.documentsButton)
        planName = findViewById(R.id.planName)

        val childId = getCurrentChildId()
        childId?.let {
            fetchChildDiet(it)
        }
        //set onClickListeners for Buttons
        dietButton.setOnClickListener {
            val intent = when (childDiet) {
                "Diet 1" -> Intent(this, Diet1Activity::class.java)
                "Diet 2" -> Intent(this, Diet2Activity::class.java)
                "Diet 4" -> Intent(this, Diet4Activity::class.java)
                "Diet 6" -> Intent(this, Diet6Activity::class.java)
                else -> throw IllegalArgumentException("Invalid diet")
            }
            startActivity(intent)
        }

        medicationsButton.setOnClickListener {
            val intent = Intent(this, MedicationsActivity :: class.java)
            startActivity(intent)
        }

        documentsButton.setOnClickListener {
            val intent = Intent(this, DocumentsActivity :: class.java)
            startActivity(intent)
        }
    }

    private fun fetchChildDiet(childId: String) {
        val db = Firebase.firestore
        db.collection("Children").document(childId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    childDiet = document.getString("diet") ?: "No Diet Specified"
                    val childName = document.getString("firstName") ?: "No Name"

                    // Update UI with child data
                    planName.text = childName

                } else {
                    Toast.makeText(this, "Diet not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load child data: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getCurrentChildId(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CurrentChildId", null)
    }

}