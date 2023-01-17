package com.example.finalproject.activity.occurrence

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.finalproject.R
import com.example.finalproject.activity.MainActivity
import com.example.finalproject.utils.OccurrencesUtils
import com.example.finalproject.misc.weather.Model

class AddOccurrenceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var toolbarInfo: Toolbar
    lateinit var submitButton: Button
    lateinit var titleText: EditText
    lateinit var descriptionText: EditText
    lateinit var spinner: Spinner
    lateinit var selectedType: String

    val occurrenceTypes = listOf("Incendio", "Manutencao")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence)

        titleText = findViewById(R.id.txt_title)
        descriptionText = findViewById(R.id.txt_information)
        // Go back button
        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", this.intent.extras?.getString("username") ?: "")
            startActivity(intent)
        }

        spinner = findViewById(R.id.spinner)
        val staticAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, occurrenceTypes)
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = staticAdapter
        spinner.onItemSelectedListener = this;

        val lat = intent.extras!!.getDouble("latitude").toFloat()
        val lon = intent.extras!!.getDouble("longitude").toFloat()
        submitButton = findViewById(R.id.btn_submit)
        submitButton.setOnClickListener {
            OccurrencesUtils.addNewOccurrence(
                    hashMapOf("lat" to lat, "lon" to lon),
                    titleText.text.toString(),
                    descriptionText.text.toString(),
                    selectedType
            )

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", this.intent.extras?.getString("username") ?: "")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", this.intent.extras?.getString("username") ?: "")
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                selectedType = occurrenceTypes[position]
            }
            1 -> {
                selectedType = occurrenceTypes[position]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}