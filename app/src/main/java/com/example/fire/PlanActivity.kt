package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class PlanActivity : AppCompatActivity() {

    private lateinit var dietButton: Button
    private lateinit var medicationsButton: Button
    private lateinit var documentsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yourplan)

        //Initialize buttons
        dietButton = findViewById(R.id.dietButton)
        medicationsButton = findViewById(R.id.medicationsButton)
        documentsButton = findViewById(R.id.documentsButton)

        //set onClickListeners for Buttons
        dietButton.setOnClickListener {
            val intent = Intent(this, DietPlanActivity :: class.java)
            startActivity(intent)
        }

        medicationsButton.setOnClickListener {
            val intent = Intent(this, MedicationsActivity :: class.java)
            startActivity(intent)
        }

        documentsButton.setOnClickListener {
            val intent = Intent(this, DocumentsActivity :: class.java)
            startActivity(intent)
        }
    }
}