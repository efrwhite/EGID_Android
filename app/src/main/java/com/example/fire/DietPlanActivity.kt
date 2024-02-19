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
    }
}