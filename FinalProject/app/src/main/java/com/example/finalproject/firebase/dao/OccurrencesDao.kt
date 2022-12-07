package com.example.finalproject.firebase.dao

import android.util.Log
import com.example.finalproject.misc.Tags
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OccurrencesDao {


    companion object {

        fun searchRecentOccurrences(): ArrayList<String> {
            val dbConnection = Firebase.firestore
            val occurrences = dbConnection.collection("occurrences")

            val result = occurrences.orderBy("date").limit(3).get()

            val start = System.currentTimeMillis()
            // need this, since the call to the firebase is async
            while (!result.isComplete) {
                Log.d(Tags.WAITING.name, "Waiting for task to finish.")
            }
            // for debugging purposes we added this Log to check the amount of time between callbacks
            val end = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start)
            Log.d(Tags.DONE.name, "Task Done! It took $end seconds.")

            val arraylist = ArrayList<String>(3)
            for (i in result.result.documents.indices) {
                arraylist.add((result.result.documents[i].get("location") as String))
            }
            return arraylist
        }

        fun addNewOccurrence(dataset: HashMap<String, Any>) {
            val dbConnection = Firebase.firestore
            dbConnection.collection("occurrences").add(dataset)
                .addOnSuccessListener {
                    Log.d(Tags.QUERY.name, "Task Done. New Occurrence in the Database.")
                }
                .addOnFailureListener { e ->
                    Log.w(Tags.ERROR.name, "Error adding document", e)
                }
            Log.d(Tags.QUERY.name, "Task Done. New Occurrence in the Database.")
        }
    }
}