package com.example.finalproject.firebase

import android.util.Log
import com.example.finalproject.misc.Tags
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LoginUtils {

    companion object {
        private var pwdStatic = "pwd"

        /**
         * Checks if the user submitted exists or not in the Database
         */
        fun isUserInDB(username: String, pwd: String): Boolean {

            return if (username.isNotBlank() && pwd.isNotBlank()) {
                val dbConnection = Firebase.firestore
                val result = dbConnection.collection("users")
                    .document(username).get().addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(Tags.RESULT.name, "Document exists!")
                        } else {
                            Log.d(Tags.RESULT.name, "No such document")
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d(Tags.ERROR.name, "get failed with ", exception)
                    }
                val start = System.currentTimeMillis()
                // need this, since the call to the firebase is async
                while (!result.isComplete) {
                    Log.d(Tags.WAITING.name, "Waiting for task to finish.")
                }
                // for debugging purposes we added this Log to check the amount of time between callbacks
                val end = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start)
                Log.d(Tags.DONE.name, "Task Done! It took $end seconds.")
                if(!result.result.exists()){
                    return false
                }
                (result.result.get(pwdStatic) as String).compareTo(pwd) == 0
            } else false


        }

        fun createUserInBD(username: String, pwd: String) {

            if (username.isNotBlank() && pwd.isNotBlank()) {
                val dbConnection = Firebase.firestore
                val dataSet = hashMapOf(
                    "username" to username,
                    "pwd" to pwd
                )
                dbConnection.collection("users").document(username).set(dataSet)
                Log.d(Tags.QUERY.name, "Task Done. New User in the Database.")
            }
            Log.d(Tags.ERROR.name, "Task Error. Error Adding User to DB.")
        }

    }


}