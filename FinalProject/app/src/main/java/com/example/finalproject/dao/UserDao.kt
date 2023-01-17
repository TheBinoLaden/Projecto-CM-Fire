package com.example.finalproject.dao

import android.util.Log
import com.example.finalproject.enums.Tags
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDao {


    companion object {

        fun createNewNormalUser(username: String, pwd: String) {

            val dbConnection = Firebase.firestore
            val dataSet = hashMapOf(
                "username" to username,
                "pwd" to pwd,
                "typeOfUser" to "normal",
                "favPlaces" to arrayListOf<String>()
            )
            dbConnection.collection("users").document(username).set(dataSet)
            Log.d(Tags.QUERY.name, "Task Done. New User in the Database.")

        }

        fun createNewSuperUser(username: String, pwd: String) {

            val dbConnection = Firebase.firestore
            val dataSet = hashMapOf(
                "username" to username,
                "pwd" to pwd,
                "typeOfUser" to "Super",
                "placesToCheck" to arrayListOf<String>()

            )
            dbConnection.collection("users").document(username).set(dataSet)
            Log.d(Tags.QUERY.name, "Task Done. New User in the Database.")

        }


    }
}