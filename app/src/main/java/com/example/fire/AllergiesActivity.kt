package com.example.fire

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class AllergiesActivity : AppCompatActivity() {

    private lateinit var addAllergenButton: Button
    private lateinit var childId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergies)

        val allergenTableLayout: TableLayout = findViewById(R.id.allergenTableLayout)
        val pastAllergenTableLayout: TableLayout = findViewById(R.id.pastAllergenTableLayout)

        //Initialize button
        addAllergenButton = findViewById(R.id.addAllergenButton)

        //Set onlick
        addAllergenButton.setOnClickListener {
            intent = Intent(this, AddAllergiesActivity::class.java)
            startActivity(intent)
        }

        fetchAndDisplayAllergies(allergenTableLayout, pastAllergenTableLayout)

    }

    private fun getCurrentChildId(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CurrentChildId", null)
    }

    private fun fetchAndDisplayAllergies(allergyTableLayout: TableLayout, pastAllergyTableLayout: TableLayout) {
        val childId = getCurrentChildId()
        Firebase.firestore.collection("Allergens")
            .whereEqualTo("childId", childId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val allergenName = document.getString("allergenName") ?: "No Name"
                    val allergenId = document.id
                    val isDiscontinued = document.getBoolean("discontinue") ?: false
                    addRowToTable(allergyTableLayout, pastAllergyTableLayout, allergenName, allergenId, isDiscontinued)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to load current allergies: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    @SuppressLint("InflateParams")
    private fun addRowToTable(allergyTableLayout: TableLayout, pastAllergyTableLayout: TableLayout, name: String, allergenId: String, isDiscontinued: Boolean) {
        val row = layoutInflater.inflate(R.layout.table_row_item, null)
        val nameTextView = row.findViewById<TextView>(R.id.nameTextView)
        val editButton = row.findViewById<Button>(R.id.editButton)

        nameTextView.text = name

        nameTextView.text = name
        editButton.setOnClickListener {
            navigateToEditActivity(allergenId)
        }

        if (isDiscontinued) {
            // Add to past medications list
            pastAllergyTableLayout.addView(row)
        } else {
            // Add to current medications list
            allergyTableLayout.addView(row)
        }
    }
    private fun navigateToEditActivity(allergenId: String) {
        val intent = Intent(this, AddAllergiesActivity::class.java).apply {
            putExtra("allergenId", allergenId)
            putExtra("editMode", true) // Indicate that we are editing an existing allergen
        }
        startActivity(intent)
        finish()
    }

}


