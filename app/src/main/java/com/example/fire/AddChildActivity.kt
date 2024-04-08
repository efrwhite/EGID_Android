package com.example.fire

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddChildActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var birthDateEditText: EditText
    private lateinit var genderInput: AutoCompleteTextView
    private lateinit var dietInput: AutoCompleteTextView
    private lateinit var saveButton: Button
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var childId: String? = null // Used for edit mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_profile)

        firstNameEditText = findViewById(R.id.editText2)
        lastNameEditText = findViewById(R.id.editText)
        birthDateEditText = findViewById(R.id.editTextDate)
        genderInput = findViewById(R.id.GenderInputFields)
        dietInput = findViewById(R.id.DietInputFields)
        saveButton = findViewById(R.id.saveButton)

        // Check if in edit mode and if so, fetch and populate child data
        childId = intent.getStringExtra("childId")
        if (childId != null) {
            fetchAndPopulateChildData(childId!!)
        }

        saveButton.setOnClickListener {
            if (childId == null) {
                saveChildInfo()
            } else {
                updateChildInfo()
            }
        }

        setupDropdownMenus()

    }
    override fun onResume() {
        super.onResume()
        // Setup dropdown menus again to ensure they're always ready
        setupDropdownMenus()
    }

    private fun fetchAndPopulateChildData(childId: String) {
        Firebase.firestore.collection("Children").document(childId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    firstNameEditText.setText(document.getString("firstName"))
                    lastNameEditText.setText(document.getString("lastName"))
                    birthDateEditText.setText(document.getString("birthDate"))
                    genderInput.setText(document.getString("gender"))
                    dietInput.setText(document.getString("diet"))

                    setupDropdownMenus()
                    // Populate other fields as necessary
                } else {
                    Toast.makeText(this, "Child not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load child data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveChildInfo() {
        val userMap = hashMapOf(
            "firstName" to firstNameEditText.text.toString().trim(),
            "lastName" to lastNameEditText.text.toString().trim(),
            "birthDate" to birthDateEditText.text.toString().trim(),
            "gender" to genderInput.text.toString().trim(),
            "diet" to dietInput.text.toString().trim(),
            "parentUserId" to firebaseAuth.currentUser?.uid
        )

        Firebase.firestore.collection("Children").add(userMap)
            .addOnSuccessListener { documentReference ->
                // The new child's ID is obtained from the DocumentReference object
                val newChildId = documentReference.id

                // Here you would call saveCurrentChildId(newChildId) to save the ID to SharedPreferences
                saveCurrentChildId(newChildId)

                Toast.makeText(this, "Child added successfully", Toast.LENGTH_SHORT).show()
                goToNextActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add child: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateChildInfo() {
        val userMap = hashMapOf(
            "firstName" to firstNameEditText.text.toString().trim(),
            "lastName" to lastNameEditText.text.toString().trim(),
            "birthDate" to birthDateEditText.text.toString().trim(),
            "gender" to genderInput.text.toString().trim(),
            "diet" to dietInput.text.toString().trim(),
            "parentUserId" to firebaseAuth.currentUser?.uid
        )

        childId?.let {
            Firebase.firestore.collection("Children").document(it).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Child updated successfully", Toast.LENGTH_SHORT).show()
                    goToNextActivity()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update child: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveCurrentChildId(childId: String) {
        // Here, 'this' refers to the AddChildActivity instance, which is a Context object.
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("CurrentChildId", childId)
            apply()
        }
    }


    private fun setupDropdownMenus() {
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genderOptions)
        findViewById<AutoCompleteTextView>(R.id.GenderInputFields).apply {
            setAdapter(genderAdapter)
        }

        val dietOptions = resources.getStringArray(R.array.diet_options)
        val dietAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dietOptions)
        findViewById<AutoCompleteTextView>(R.id.DietInputFields).apply {
            setAdapter(dietAdapter)
        }

    }
    private fun goToNextActivity() {
        val isFirstTimeUser = intent.getBooleanExtra("isFirstTimeUser", false)
        val nextActivityIntent = if (isFirstTimeUser) {
            Intent(this, HomeActivity::class.java).apply {
                // Clear the activity stack to prevent backtracking
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        } else {
            Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        startActivity(nextActivityIntent)
    }
}