package com.example.finalproject.utils

import com.example.finalproject.firebase.dao.OccurrencesDao
import com.example.finalproject.weather.Model
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

class OccurrencesUtils {

    companion object {
        fun addNewOccurrence(coordinates: Model.Coord, title: String, description: String, type: String) {
            val dataset = hashMapOf(
                    "coordinates" to coordinates,
                    "date" to System.currentTimeMillis(),
                    "description" to description,
                    "title" to title,
                    "type" to type
            )
            OccurrencesDao.addNewOccurrence(dataset)
        }

        fun searchOccurrencesList(callback: (ArrayList<DocumentSnapshot>) -> Unit) {
            OccurrencesDao.searchOccurrences(callback)
        }

        fun removeOccurrence(documentSnapshot: DocumentSnapshot) {
            OccurrencesDao.removeOccurrence(documentSnapshot)
        }
    }
}