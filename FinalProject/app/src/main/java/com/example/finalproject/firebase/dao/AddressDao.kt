package com.example.finalproject.firebase.dao

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressDao {
    companion object {
        fun addNewAddress(username: String, address: String, description: String, lat: Float, lon: Float) {
            Firebase.firestore.collection("users").document(username).update("favAddress",
                    FieldValue.arrayUnion(hashMapOf(
                            "Address" to address,
                            "Description" to description,
                            "coordinates" to hashMapOf("lat" to lat.toDouble(), "lon" to lon.toDouble())
                    )))
        }

        fun getFavAddress(username: String, callback: (favAddress: ArrayList<HashMap<String, Any>>) -> Unit) {
            var favAddress: ArrayList<HashMap<String, Any>> = ArrayList()
            Firebase.firestore.collection("users").document(username).get().addOnCompleteListener { result ->
                if (result.result.get("favAddress") != null)
                    favAddress = result.result.get("favAddress") as ArrayList<HashMap<String, Any>>
                callback(favAddress)
            }
        }

        fun removeAddress(username: String, addressToDel: HashMap<String, Any>) {
            Firebase.firestore.collection("users").document(username).update("favAddress", FieldValue.arrayRemove(addressToDel))
        }
    }
}