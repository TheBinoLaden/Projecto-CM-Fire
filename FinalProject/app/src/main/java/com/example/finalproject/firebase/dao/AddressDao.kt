package com.example.finalproject.firebase.dao

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressDao {
    companion object {
        fun addNewAddress(username: String, address: String, description: String) {
            Firebase.firestore.collection("users").document(username)
                .update("favAddress", FieldValue.arrayUnion(mapOf("Address" to address, "Description" to description)))
        }

        fun getFavAddress(username: String, callback: (address: ArrayList<Map<String, String>>) -> Unit) {
            var favAddress: ArrayList<Map<String, String>> = ArrayList()
            Firebase.firestore.collection("users").document(username).get().addOnCompleteListener { result ->
                if (result.result.get("favAddress") != null)
                    favAddress = result.result.get("favAddress") as ArrayList<Map<String, String>>
                callback(favAddress)
            }
        }

        fun removeAddress(username: String, addressToDel: Map<String, String>) {
            Firebase.firestore.collection("users").document(username).update("favAddress", FieldValue.arrayRemove(addressToDel))
        }
    }
}