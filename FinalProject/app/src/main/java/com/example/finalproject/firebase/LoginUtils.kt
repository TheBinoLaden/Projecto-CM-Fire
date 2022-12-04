package com.example.finalproject.firebase

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginUtils {


    companion object {
        public fun isUserInDB(username: String?, pwd: String?) {
            val dbConnection = Firebase.firestore
            val result = dbConnection.collection("users").whereEqualTo("name", username)
                .whereEqualTo("pwd", pwd).get()

            Log.d("LMAO", "The result is:  $result")

        }
    }


}