package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MedicationsActivity : AppCompatActivity() {

    private lateinit var addMedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medications)

        //Initialize button
        addMedButton = findViewById(R.id.addMedButton)

        //Set onClickListener for button
        addMedButton.setOnClickListener {
            val intent = Intent(this, AddMedicationActivity::class.java)
            startActivity(intent)
        }
    }
}