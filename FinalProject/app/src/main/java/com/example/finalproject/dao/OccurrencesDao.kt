package com.example.finalproject.dao

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OccurrencesDao {

    companion object {

        fun searchOccurrences(callback: (ArrayList<DocumentSnapshot>) -> Unit) {
            val occurrences: ArrayList<DocumentSnapshot> = ArrayList()
            Firebase.firestore.collection("occurrences").get().addOnCompleteListener { result ->
                if (result.result.documents.isNotEmpty()) {
                    result.result.documents.forEach {
                        occurrences.add(it)
                    }
                }
                callback(occurrences)
            }
        }

        fun addNewOccurrence(dataset: HashMap<String, Any>) {
            val dbConnection = Firebase.firestore
            dbConnection.collection("occurrences").add(dataset)
                    .addOnSuccessListener {
                    }
        }

        fun removeOccurrence(documentSnapshot: DocumentSnapshot) {
            Firebase.firestore.collection("occurrences").document(documentSnapshot.id).delete()
        }
    }
}