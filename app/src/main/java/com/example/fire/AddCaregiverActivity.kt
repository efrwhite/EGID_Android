package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddCaregiverActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var saveButton: Button
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var mode: String = "add" // Default mode
    private var caregiverId: String? = null // Used for edit mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_profile)

        // Initialize views
        firstNameEditText = findViewById(R.id.editText)
        lastNameEditText = findViewById(R.id.editText3)
        usernameEditText = findViewById(R.id.editText2) //
        saveButton = findViewById(R.id.saveButton)

        // Retrieve and display the username, and make it uneditable
        val username = intent.getStringExtra("username") ?: ""
        usernameEditText.setText(username)
        usernameEditText.isEnabled = false // Make the EditText uneditable

        // Determine the mode (add or edit) based on the intent extras
        intent.extras?.let {
            mode = it.getString("mode", "add")
            caregiverId = it.getString("caregiverId") // This will be null if in add mode
        }

        saveButton.setOnClickListener {
            if (mode == "add") {
                saveCaregiverInfo()
            } else {
                updateCaregiverInfo()
            }
        }
    }

    private fun saveCaregiverInfo() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val userMap = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "parentUserId" to firebaseAuth.currentUser?.uid
        )

        Firebase.firestore.collection("Caregivers").add(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Caregiver added successfully", Toast.LENGTH_SHORT).show()
                goToNextActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add caregiver: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateCaregiverInfo() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        caregiverId?.let { id ->
            val userMap = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "parentUserId" to firebaseAuth.currentUser?.uid
            )

            Firebase.firestore.collection("Caregivers").document(id).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Caregiver updated successfully", Toast.LENGTH_SHORT).show()
                    goToNextActivity()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update caregiver: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: Toast.makeText(this, "Error: Caregiver ID is missing.", Toast.LENGTH_SHORT).show()
    }

    private fun goToNextActivity() {
        val intent = if (intent.getBooleanExtra("isFirstTimeUser", false) && mode == "add") {
            Intent(this, AddChildActivity::class.java).apply {
                putExtra("isFirstTimeUser", true)
            }
        } else {
            Intent(this, ProfilesActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
