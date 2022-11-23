package com.example.finalproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi

class LoginActivity : AppCompatActivity() {

    lateinit var signUp: TextView
    lateinit var logIn: TextView
    lateinit var signUpLayout: LinearLayout
    lateinit var logInLayout: LinearLayout
    lateinit var logInButton : Button

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUp = findViewById(R.id.singUp)
        signUpLayout = findViewById(R.id.singUpLayout)

        logIn = findViewById(R.id.logIn)
        logInLayout = findViewById(R.id.logInLayout)

        logInButton = findViewById(R.id.btn_logIn)

        signUp.setOnClickListener{
            signUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
            signUp.setTextColor(resources.getColor(R.color.textColor,null))

            logIn.background = null
            logIn.setTextColor(resources.getColor(R.color.RedColor,null))

            signUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
        }

        logIn.setOnClickListener{
            logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
            logIn.setTextColor(resources.getColor(R.color.textColor,null))

            signUp.background = null
            signUp.setTextColor(resources.getColor(R.color.RedColor,null))

            signUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
        }

        logInButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}