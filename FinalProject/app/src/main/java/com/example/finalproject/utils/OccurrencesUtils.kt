package com.example.finalproject.utils

import com.example.finalproject.firebase.dao.OccurrencesDao
import com.example.finalproject.weather.Model
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.HashMap

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
    }
}