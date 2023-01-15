package com.example.finalproject.activity.occurrence

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.finalproject.activity.MainActivity
import com.example.finalproject.R
import com.example.finalproject.utils.OccurrencesUtils
import com.example.finalproject.weather.Model

class OccurrenceActivity : AppCompatActivity() {

    lateinit var toolbarInfo: Toolbar
    lateinit var submitButton: Button
    lateinit var titleText: EditText
    lateinit var descriptionText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence)

        titleText = findViewById(R.id.txt_title)
        descriptionText = findViewById(R.id.txt_information)
        goBack()

        submitButton = findViewById(R.id.btn_submit)
        submitButton.setOnClickListener {
            OccurrencesUtils.addNewOccurrence(
                Model.Coord(6F, 6F),
                titleText.text.toString(), descriptionText.text.toString()
            )
            Toast.makeText(this, "Submissão Concluída", Toast.LENGTH_LONG).show()
        }

    }

    //função para o botão de back na toolbar
    fun goBack() {
        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}