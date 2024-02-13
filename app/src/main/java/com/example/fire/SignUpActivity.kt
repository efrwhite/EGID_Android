package com.example.fire
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Get references to the email, password, confirm password, and sign-up button
        emailEditText = findViewById(R.id.signUpEmail)
        passwordEditText = findViewById(R.id.signUpPassword)
        confirmPasswordEditText = findViewById(R.id.signUpConfirm)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            // Retrieve the user's input from the email, password, and confirm password fields
            val email = emailEditText.text.toString()
            val pass = passwordEditText.text.toString()
            val confirmPass = confirmPasswordEditText.text.toString()

            // Checking if the user has entered valid information before attempting to sign up
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    // Attempt to create a new user account using Firebase authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // If account creation is successful, navigate to the sign-in page
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Show an error message if account creation fails
                                Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Show an error message if the password and confirm password do not match
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show an error message if any of the required fields are empty
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
