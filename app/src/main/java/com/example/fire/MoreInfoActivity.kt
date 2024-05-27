package com.example.fire

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MoreInfoActivity : AppCompatActivity() {

    private lateinit var naspgHanButton: Button
    private lateinit var aaaIButton: Button
    private lateinit var apfeDButton: Button
    private lateinit var chRichmondButton: Button
    private lateinit var cEGIRbutton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moreinfo)

        naspgHanButton = findViewById(R.id.NASPGHANbutton)
        aaaIButton = findViewById(R.id.AAAIbutton)
        apfeDButton = findViewById(R.id.APFEDbutton)
        chRichmondButton = findViewById(R.id.chrichmondButton)
        cEGIRbutton = findViewById(R.id.CEGIRbutton)

        naspgHanButton.setOnClickListener {
            openUrl("https://www.naspghan.org/")
        }

        aaaIButton.setOnClickListener {
            openUrl("https://www.aaaai.org/")
        }

        apfeDButton.setOnClickListener {
            openUrl("https://apfed.org/")
        }

        chRichmondButton.setOnClickListener {
            openUrl("https://www.chrichmond.org/")
        }

        cEGIRbutton.setOnClickListener {
            openUrl("https://cegir.rarediseasesnetwork.org/")
        }
    }
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
