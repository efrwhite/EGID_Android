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
    private lateinit var signUpLink: Button
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
// Temporarily bypassing Firebase authentication for testing

            // Firebase authentication code (commented out for now)
            /*
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Username and password are required.", Toast.LENGTH_SHORT).show()
            }
            */

            // Directly navigate to HomeActivity for testing
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //navigate to sign up page from sign in page if dont have an acc
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
