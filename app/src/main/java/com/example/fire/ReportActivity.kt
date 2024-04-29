package com.example.fire

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    private lateinit var lineChart: LineChart
    private lateinit var descriptionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        lineChart = findViewById(R.id.lineChart)
        descriptionText = findViewById(R.id.descriptionText)

        val sourceActivity = intent.getStringExtra("sourceActivity") ?: "SymptomChecker"
        val childId = getChildId()

        loadChartData(sourceActivity, childId)
    }

    private fun getChildId(): String {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("CurrentChildId", "") ?: ""
    }

    private fun loadChartData(sourceActivity: String, childId: String) {
        val subCollection = when (sourceActivity) {
            "SymptomChecker" -> "Symptom Scores"
            "QoLActivity" -> "Quality of Life Scores"
            else -> "Symptom Scores"
        }

        Firebase.firestore.collection("Children").document(childId)
            .collection(subCollection)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(6)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents
                val entries = documents.mapIndexed { index, document ->
                    Entry(index.toFloat(), document.getLong("totalScore")?.toFloat() ?: 0f)
                }

                if (sourceActivity == "SymptomChecker") {
                    updateDescriptions(documents)
                }

                val dataSet = LineDataSet(entries, "Score Over Time")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextColor = Color.BLACK
                dataSet.valueTextSize = 12f

                lineChart.data = LineData(dataSet)
                lineChart.invalidate() // refresh graph
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateDescriptions(documents: List<DocumentSnapshot>) {
        val mostRecentWithY = documents.firstOrNull { doc ->
            (doc["responses"] as? List<*>)?.contains("y") == true
        }

        mostRecentWithY?.let { doc ->
            try {
                val timestamp = doc.getTimestamp("date")
                val date = timestamp?.toDate() ?: throw IllegalStateException("Date is not a valid timestamp")
                val descriptions = doc["symptomDescriptions"] as? List<*>
                val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date)
                descriptionText.text = if (descriptions.isNullOrEmpty()) {
                    "No descriptions for $formattedDate"
                } else {
                    "$formattedDate: ${descriptions.joinToString(", ")}"
                }
            } catch (e: Exception) {
                descriptionText.text = "Failed to parse date from database. ${e.message}"
            }
        } ?: run {
            descriptionText.text = "No recent entries with symptoms marked 'Y'."
        }
    }


}
