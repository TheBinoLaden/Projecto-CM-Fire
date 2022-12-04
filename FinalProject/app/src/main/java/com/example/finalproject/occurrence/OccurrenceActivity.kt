package com.example.finalproject.occurrence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.finalproject.MainActivity
import com.example.finalproject.R

class OccurrenceActivity : AppCompatActivity() {

    lateinit var toolbarInfo : Toolbar
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence)

        goBack()

        submitButton = findViewById(R.id.btn_submit)
        submitButton.setOnClickListener {
            Toast.makeText(this,"Submissão Concluida",Toast.LENGTH_LONG).show()
        }

    }

    //função para o botão de back na toolbar
    fun goBack(){
        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}