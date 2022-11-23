package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    lateinit var toolbarInfo : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        goBack()
    }

    //função para o botão de back na toolbar
    fun goBack(){
        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}