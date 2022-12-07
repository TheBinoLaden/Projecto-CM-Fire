package com.example.finalproject.firebase.dao

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressDao {


    companion object {

        fun addNewAddress(username: String, newList: String) {

            val dbConnection = Firebase.firestore
            val dataToUpdate = hashMapOf(
                "favPlaces" to newList

            )
            dbConnection.collection("users").document(username)
                .set(dataToUpdate, SetOptions.merge())
        }
    }
}