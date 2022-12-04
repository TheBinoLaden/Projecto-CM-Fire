package com.example.finalproject.usercontrol

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.firebase.LoginUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var signUp: TextView
    lateinit var logIn: TextView
    lateinit var signUpLayout: LinearLayout
    lateinit var logInLayout: LinearLayout
    lateinit var logInButton: Button
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPwd: TextInputEditText

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUp = findViewById(R.id.singUp)
        signUpLayout = findViewById(R.id.singUpLayout)

        logIn = findViewById(R.id.logIn)
        logInLayout = findViewById(R.id.logInLayout)


        logInButton = findViewById(R.id.btn_logIn)

        txtEmail = findViewById(R.id.eMail)
        txtPwd = findViewById(R.id.passwords)

        signUp.setOnClickListener {
            signUp.background = resources.getDrawable(R.drawable.switch_trcks, null)
            signUp.setTextColor(resources.getColor(R.color.textColor, null))

            logIn.background = null
            logIn.setTextColor(resources.getColor(R.color.RedColor, null))

            signUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
        }

        logIn.setOnClickListener {
            logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            logIn.setTextColor(resources.getColor(R.color.textColor, null))

            signUp.background = null
            signUp.setTextColor(resources.getColor(R.color.RedColor, null))

            signUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
        }

        logInButton.setOnClickListener {
            if (txtEmail.text.toString() != "" && txtPwd.text.toString() != "") {
                LoginUtils.isUserInDB(txtEmail.text.toString(), txtPwd.text.toString())
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to "Ada",
            "born" to 1815,
            "pwd" to "123"
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("LMAO", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("LMAO", "Error adding document", e)
            }

    }
}