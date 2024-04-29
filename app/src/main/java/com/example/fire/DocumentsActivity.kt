package com.example.fire

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class DocumentsActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var docsRecyclerView: RecyclerView
    private lateinit var documentUploadLauncher: ActivityResultLauncher<Intent>
    private var documentsList = mutableListOf<Document>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        addButton = findViewById(R.id.addButton)
        docsRecyclerView = findViewById(R.id.docsRecyclerView)

        docsRecyclerView.layoutManager = LinearLayoutManager(this)
        docsRecyclerView.adapter = DocumentAdapter(documentsList)

        documentUploadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val dataIntent = result.data  // Store the intent in a local variable
                if (dataIntent != null && dataIntent.data != null) {
                    processDocumentUri(dataIntent.data!!)
                } else {
                    Toast.makeText(this, "No file selected or an error occurred.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        addButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            documentUploadLauncher.launch(intent)
        }
    }

    private fun processDocumentUri(uri: Uri) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

                if (nameIndex != -1 && sizeIndex != -1) {
                    val name = it.getString(nameIndex)
                    val size = it.getLong(sizeIndex)
                    val date = Date() // Using the current date as a placeholder
                    val type = contentResolver.getType(uri) ?: "Unknown"

                    val document = Document(
                        name = name,
                        url = uri.toString(),
                        type = type,
                        size = size,
                        date = date,
                        thumbnail = null
                    )
                    documentsList.add(document)
                    docsRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Error: Document name or size not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    data class Document(
        val name: String,
        val url: String,
        val type: String,
        val size: Long,
        val date: Date,
        val thumbnail: String?
    )

    inner class DocumentAdapter(private val documents: List<Document>) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_document_item, parent, false)
            return DocumentViewHolder(view)
        }

        override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
            holder.bind(documents[position])
        }

        override fun getItemCount() = documents.size

        inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.documentName)
            private val dateTextView: TextView = itemView.findViewById(R.id.documentDate)
            private val sizeTextView: TextView = itemView.findViewById(R.id.documentSize)

            fun bind(document: Document) {
                nameTextView.text = document.name
                dateTextView.text = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(document.date)
                sizeTextView.text = "${document.size} bytes"
            }
        }
    }
}

