package com.example.fire

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class SymptomCheckerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_score_checker)

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveScores()
        }
    }

    private fun saveScores() {
        val totalScore = calculateTotalScore()
        val responses = collectResponses()
        val symptomDescriptions = collectSymptomDescriptions()
        val dateInput: EditText = findViewById(R.id.visitDateInput)
        val dateString = dateInput.text.toString()

        saveResultsToFirestore(totalScore, responses, symptomDescriptions, dateString)
    }

    private fun calculateTotalScore(): Int {
        var score = 0
        for (i in 2..20) { // IDs for numerical score questions
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            score += answer.text.toString().toIntOrNull() ?: 0
        }
        for (i in 22..33) { // IDs for Y/N questions
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            score += if (answer.text.toString().equals("y", ignoreCase = true)) 1 else 0
        }
        return score
    }

    private fun collectResponses(): List<String> {
        val responses = mutableListOf<String>()
        for (i in 2..33) {
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            responses.add(answer.text.toString())
        }
        return responses
    }

    private fun collectSymptomDescriptions(): List<String> {
        val descriptions = mutableListOf<String>()
        for (i in 22..33) { // Assuming IDs 22 to 33 are for Y/N questions
            val answerId = resources.getIdentifier("answer$i", "id", packageName)
            val answer = findViewById<EditText>(answerId)
            if (answer.text.toString().equals("y", ignoreCase = true)) {
                val questionId = resources.getIdentifier("question$i", "id", packageName)
                val question = findViewById<TextView>(questionId)
                descriptions.add(question.text.toString())
            }
        }
        return descriptions
    }

    private fun saveResultsToFirestore(totalScore: Int, responses: List<String>, symptoms: List<String>, date: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val childId = sharedPreferences.getString("CurrentChildId", null) ?: run {
            Toast.makeText(this, "No child selected", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimestamp = dateFormat.parse(date)?.time ?: System.currentTimeMillis() // Default to current time if parsing fails

        val data = hashMapOf(
            "totalScore" to totalScore,
            "responses" to responses,
            "symptomDescriptions" to symptoms,
            "date" to dateTimestamp
        )

        Firebase.firestore.collection("Children").document(childId).collection("Symptom Scores")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Score saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
