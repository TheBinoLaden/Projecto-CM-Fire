package com.example.finalproject.utils

import com.example.finalproject.dao.OccurrencesDao
import com.example.finalproject.misc.helperclasses.Address
import com.example.finalproject.misc.helperclasses.Occurrence
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class OccurrencesUtils {

    companion object {
        fun addNewOccurrence(coordinates: HashMap<String, Float>, title: String, description: String, type: String) {
            OccurrencesDao.addNewOccurrence(hashMapOf(
                    "coordinates" to coordinates,
                    "date" to System.currentTimeMillis(),
                    "description" to description,
                    "title" to title,
                    "type" to type
            ))
        }

        fun searchOccurrencesList(callback: (ArrayList<DocumentSnapshot>) -> Unit) {
            OccurrencesDao.searchOccurrences(callback)
        }

        fun removeOccurrence(documentSnapshot: DocumentSnapshot) {
            OccurrencesDao.removeOccurrence(documentSnapshot)
        }


        fun makeOccurrenceFromDatabase(document: QueryDocumentSnapshot): Occurrence {


            val newDate = document.get("date").toString()
            val coordinates = document.get("coordinates").toString()
            val description = document.get("description").toString()
            val title = document.get("title").toString()
            val type = document.get("type").toString()

            val lon = StringUtils.getLonDB(coordinates)
            val lat = StringUtils.getLatDB(coordinates)

            val newAddress = Address(coordinates, description, lat, lon)
            return Occurrence(newAddress, newDate, description, title, type)
        }
    }
}