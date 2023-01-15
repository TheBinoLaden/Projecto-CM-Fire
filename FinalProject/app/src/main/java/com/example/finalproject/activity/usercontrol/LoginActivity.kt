package com.example.finalproject.activity.usercontrol

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.finalproject.R
import com.example.finalproject.activity.MainActivity
import com.example.finalproject.enums.Tags
import com.example.finalproject.firebase.dao.LoginDao
import com.example.finalproject.utils.UserUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import android.view.inputmethod.InputMethodManager

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
        checkLocationPerms()

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
            logInButton.text = "Sign Up"

            signUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            isSignUp = true
        }

        logIn.setOnClickListener {
            logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            logIn.setTextColor(resources.getColor(R.color.textColor, null))

            signUp.background = null
            signUp.setTextColor(resources.getColor(R.color.RedColor, null))
            logInButton.text = "Log In"

            signUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            isSignUp = false
        }
        val view = this.currentFocus
        if(view != null){
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        logInButton.setOnClickListener {
            if (isSignUp) {
                val email = txtEmailSign.text.toString()
                val pwdSign = txtPwdSign.text.toString()
                val pwdCfm = txtPwdCfmSign.text.toString()
                if (email.isBlank())
                    Toast.makeText(this, "Please enter an email!", Toast.LENGTH_SHORT).show()
                else if (pwdSign.isBlank())
                    Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show()
                else if (pwdCfm.isBlank())
                    Toast.makeText(this, "Please re-enter the password!", Toast.LENGTH_SHORT).show()
                else if (pwdSign != pwdCfm)
                    Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                else {
                    if (!LoginDao.createUserInBD(email, pwdSign))
                        Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show()
                }
            } else if (LoginDao.isUserInDB(
                    txtEmail.text.toString(),
                    txtPwd.text.toString(),
                    contextArray
                )
            ) {
                Log.i(Tags.LOGIN.name, "Login Successful!!")
                val intent = Intent(this, MainActivity::class.java)
                val emailFromLogin = txtEmail.text.toString()
                val emailFromSignUp = txtEmailSign.text.toString()
                if (emailFromLogin.isNotBlank()) {
                    intent.putExtra("username", UserUtils.handlingEmailUsername(emailFromLogin))
                } else if (emailFromSignUp.isNotBlank()) {
                    intent.putExtra("username", UserUtils.handlingEmailUsername(emailFromSignUp))
                }
                startActivity(intent)
            } else
                Toast.makeText(
                    this,
                    "User doesn't exist or incorrect password!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun checkLocationPerms() {
        val enabled = false

        if (!enabled) {
            if (!locationPermissionAllowed()) {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        val provideRationale = locationPermissionAllowed()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.LoginCard),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@LoginActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        34
                    )
                }
                .show()
        } else {
            Log.d(Tags.ERROR.name, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                34
            )
        }
    }

    private fun locationPermissionAllowed(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        permissionsAllow: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, permissionsAllow)

        when (requestCode) {
            34 -> when {
                permissionsAllow.isEmpty() ->
                    Log.d(Tags.SUCCESS.name, "User cancelled the permission.")
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    permissionsAllow[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    Log.d(Tags.SUCCESS.name, "Permission granted.")
                else -> {
                    // User denied the location
                    Snackbar.make(
                        findViewById(R.id.LoginCard),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                               "com.example.finalproject",
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }
}
