package com.example.fire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
class ProfilesActivity : AppCompatActivity() {

    private lateinit var addChildLink : Button
    private lateinit var addCaregiverLink : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profiles)

        addChildLink = findViewById(R.id.addChildlink)
        addCaregiverLink = findViewById(R.id.addCaregiverlink)

        addChildLink.setOnClickListener {
            val intent = Intent(this, AddChildActivity::class.java)
            startActivity(intent)
        }

        addCaregiverLink.setOnClickListener {
            val intent = Intent(this, AddCaregiverActivity::class.java)
            startActivity(intent)
        }
    }
}