package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Get references to the username, password fields, and sign-in button on the sign-in screen
        usernameEditText = findViewById(R.id.signInUsername)
        passwordEditText = findViewById(R.id.signInPassword)
        signInButton = findViewById(R.id.signInButton)

        signInButton.setOnClickListener {
            // Get the entered username and password
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if both username and password are provided
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Try to sign in using the provided username and password
                firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // If sign-in is successful, navigate to the main app screen
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign-in fails, show an error message
                            Toast.makeText(this, "Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Show a message if both username and password are not provided
                Toast.makeText(this, "Username and password are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
