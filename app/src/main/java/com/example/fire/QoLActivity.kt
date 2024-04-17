package com.example.fire

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class QoLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qol_score_checker)

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveScores()
        }
    }

    private fun saveScores() {
        val totalScore = calculateTotalScore()
        val responses = collectResponses()
        val dateInput: EditText = findViewById(R.id.visitDateInput)
        val dateString = dateInput.text.toString()

        if (dateString.isBlank()) {
            Toast.makeText(this, "Please enter a valid date.", Toast.LENGTH_SHORT).show()
            return
        }

        saveResultsToFirestore(totalScore, responses, dateString)
    }

    private fun calculateTotalScore(): Int {
        var score = 0
        val numberOfQuestions = 31  // Assuming you have 31 questions indexed from 1 to 31
        for (i in 1..numberOfQuestions) {
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            score += answer.text.toString().toIntOrNull() ?: 0
        }
        return score
    }

    private fun collectResponses(): List<Int> {
        val responses = mutableListOf<Int>()
        val numberOfQuestions = 31  // Adjust this based on your actual number of questions
        for (i in 1..numberOfQuestions) {
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            responses.add(answer.text.toString().toIntOrNull() ?: 0)
        }
        return responses
    }

    private fun saveResultsToFirestore(totalScore: Int, responses: List<Int>, date: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val childId = sharedPreferences.getString("CurrentChildId", null) ?: run {
            Toast.makeText(this, "No child selected", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimestamp = dateFormat.parse(date)?.time ?: run {
            Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "totalScore" to totalScore,
            "responses" to responses,
            "date" to dateTimestamp
        )

        Firebase.firestore.collection("Children").document(childId)
            .collection("Quality of Life Scores").add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Score saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
