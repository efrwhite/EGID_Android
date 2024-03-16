package com.example.fire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class DietPlanActivity : AppCompatActivity() {

    private lateinit var diet1Button: Button
    private lateinit var diet2Button: Button
    private lateinit var diet4Button: Button
    private lateinit var diet6Button: Button
    private lateinit var allergiesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        //initialize buttons
        diet1Button = findViewById(R.id.diet1Button)
        diet2Button = findViewById(R.id.diet2Button)
        diet4Button = findViewById(R.id.diet4Button)
        diet6Button = findViewById(R.id.diet6Button)
        allergiesButton = findViewById(R.id.allergiesButton)

        //set onClickListeners
        diet1Button.setOnClickListener {
            val intent = Intent(this, Diet1Activity::class.java)
            startActivity(intent)
        }
        diet2Button.setOnClickListener {
            val intent = Intent(this, Diet2Activity::class.java)
            startActivity(intent)
        }
        diet4Button.setOnClickListener {
            val intent = Intent(this, Diet4Activity::class.java)
            startActivity(intent)
        }
        diet6Button.setOnClickListener {
            val intent = Intent(this, Diet6Activity::class.java)
            startActivity(intent)
        }
        allergiesButton.setOnClickListener {
            val intent = Intent(this, AllergiesActivity::class.java)
            startActivity(intent)
        }
    }
}