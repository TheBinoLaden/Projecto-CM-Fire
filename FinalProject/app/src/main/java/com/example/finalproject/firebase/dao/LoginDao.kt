package com.example.finalproject.firebase.dao

import android.util.Log
import com.example.finalproject.misc.Tags
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LoginDao {

    companion object {
        private var pwdStatic = "pwd"

        /**
         * Checks if the user submitted exists or not in the Database
         */
        fun isUserInDB(username: String, pwd: String, contextArray: HashMap<String, Any>): Boolean {

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
                if (!result.result.exists()) {
                    return false
                }
                fillContextWithFields(result, contextArray)
                (result.result.get(pwdStatic) as String).compareTo(pwd) == 0
            } else false


        }

        /**
         * Creates a new Normal User into the DB
         */
        fun createUserInBD(username: String, pwd: String) {

            if (!isUserInDB(username, pwd)) {
                UserDao.createNewNormalUser(username, pwd)
            }
            Log.d(Tags.ERROR.name, "Task Error. Error Adding User to DB.")
        }

        /**
         * This fun will only be available to the system's admin
         */
        fun createUserInBDByAdmin(username: String, pwd: String) {

            if (!isUserInDB(username, pwd)) {
                UserDao.createNewSuperUser(username, pwd)
            }
            Log.d(Tags.ERROR.name, "Task Error. Error Adding User to DB.")
        }

        private fun fillContextWithFields(
            result: Task<DocumentSnapshot>,
            context: HashMap<String, Any>
        ) {
            val resultTask = result.result

            resultTask.get("username")?.let { context.put("username", it) }
            resultTask.get("typeOfUser")?.let { context.put("typeOfUser", it) }
            resultTask.get("favPlaces")?.let { context.put("favPlaces", it) }
        }

        /**
         * Checks if the user submitted exists or not in the Database
         */
        private fun isUserInDB(username: String, pwd: String): Boolean {

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
                if (!result.result.exists()) {
                    return false
                }
                (result.result.get(pwdStatic) as String).compareTo(pwd) == 0
            } else false


        }
    }


}