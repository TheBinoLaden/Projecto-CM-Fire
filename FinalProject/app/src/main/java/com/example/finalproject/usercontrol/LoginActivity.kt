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
import com.example.finalproject.firebase.dao.LoginDao
import com.example.finalproject.firebase.utils.UserUtils
import com.example.finalproject.misc.Tags
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    lateinit var signUp: TextView
    lateinit var logIn: TextView
    lateinit var signUpLayout: LinearLayout
    lateinit var logInLayout: LinearLayout
    lateinit var logInButton: Button
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPwd: TextInputEditText
    lateinit var txtEmailSign: TextInputEditText
    lateinit var txtPwdSign: TextInputEditText
    lateinit var txtPwdCfmSign: TextInputEditText
    lateinit var contextArray: HashMap<String, Any> // context

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUp = findViewById(R.id.signUp)
        signUpLayout = findViewById(R.id.signUpLayout)

        logIn = findViewById(R.id.logIn)
        logInLayout = findViewById(R.id.logInLayout)


        logInButton = findViewById(R.id.btn_logIn)

        // Log-in
        txtEmail = findViewById(R.id.eMail)
        txtPwd = findViewById(R.id.passwords)

        //Signing up
        txtEmailSign = findViewById(R.id.emailSign)
        txtPwdSign = findViewById(R.id.pwdSign)
        txtPwdCfmSign = findViewById(R.id.pwdCfmSign)

        contextArray = HashMap(3)

        var isSignUp = false

        signUp.setOnClickListener {
            signUp.background = resources.getDrawable(R.drawable.switch_trcks, null)
            signUp.setTextColor(resources.getColor(R.color.textColor, null))

            logIn.background = null
            logIn.setTextColor(resources.getColor(R.color.RedColor, null))

            signUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            isSignUp = true
        }

        logIn.setOnClickListener {
            logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            logIn.setTextColor(resources.getColor(R.color.textColor, null))

            signUp.background = null
            signUp.setTextColor(resources.getColor(R.color.RedColor, null))

            signUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            isSignUp = false
        }

        logInButton.setOnClickListener {
            if (isSignUp) {
                val email = txtEmailSign.text.toString()
                val pwdSign = txtPwdSign.text.toString()
                val pwdCfm = txtPwdCfmSign.text.toString()
                if (pwdCfm.isNotBlank() && pwdSign.isNotBlank() && pwdSign == pwdCfm) {
                    LoginDao.createUserInBD(email, pwdSign)
                }
            } else if (LoginDao.isUserInDB(
                    txtEmail.text.toString(), txtPwd.text.toString(), contextArray
                )
            ) {
                Log.i(Tags.LOGIN.name, "Login Successful!!")
                val intent = Intent(this, MainActivity::class.java)
                val emailFromLogin = txtEmail.text.toString()
                val emailFromSignUp = txtEmailSign.text.toString()
                if (emailFromLogin.isNotBlank()) {
                    intent.putExtra(
                        "username",
                        UserUtils.handlingEmailUsername(emailFromLogin)
                    )
                    intent.putExtra("favPlaces", contextArray["favPlaces"].toString())
                } else if (emailFromSignUp.isNotBlank()) {
                    intent.putExtra(
                        "username",
                        UserUtils.handlingEmailUsername(emailFromSignUp)
                    )
                    intent.putExtra("favPlaces", contextArray["favPlaces"].toString())
                }
                startActivity(intent)

            }
        }
    }
}