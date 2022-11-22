package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class AddProblem : AppCompatActivity() {

    lateinit var toolbarInfo : Toolbar
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)

        submitButton = findViewById(R.id.btn_submit)
        submitButton.setOnClickListener {
            Toast.makeText(this,"Submissão Concluida",Toast.LENGTH_LONG).show()
        }

        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}