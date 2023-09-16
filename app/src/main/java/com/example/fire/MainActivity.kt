package com.example.fire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Set click listeners for Sign Up and Log In buttons
        findViewById<View>(R.id.btnSignUp).setOnClickListener {
            // Navigate to the Sign Up activity
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<View>(R.id.btnLogin).setOnClickListener {
            // Navigate to the Log In activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
