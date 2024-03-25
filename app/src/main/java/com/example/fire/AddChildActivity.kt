package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AddChildActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_profile)

        // Assuming you have a button to proceed to AddCaregiverActivity
        val saveButton = findViewById<Button>(R.id.saveButton) // Replace with your actual button ID

        saveButton.setOnClickListener {
            goToNextActivity()
        }
    }

    private fun goToNextActivity() {
        // Check if this activity was opened as part of the initial setup for a new user
        val isFirstTimeUser = intent.getBooleanExtra("isFirstTimeUser", false)

        // Intent to start AddCaregiverActivity
        val intent = Intent(this, HomeActivity::class.java).apply {
            // Pass along the flag to indicate that the user is still in the initial setup process
            putExtra("isFirstTimeUser", isFirstTimeUser)
        }
        startActivity(intent)
    }
}