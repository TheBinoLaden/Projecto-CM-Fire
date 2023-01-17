package com.example.finalproject.utils

import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.finalproject.R
import com.example.finalproject.firebase.dao.OccurrencesDao
import com.example.finalproject.activity.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.weather.Model
import java.util.*
import kotlin.collections.ArrayList

class OccurrencesUtils {

    companion object {

        fun addNewOccurrence(coordinates: Model.Coord,title: String, description: String) {
            val rightNow = Calendar.getInstance().time
            val dataset = hashMapOf(
                "coordinates" to coordinates,
                "date" to rightNow,
                "description" to description,
                "title" to title
            )
            OccurrencesDao.addNewOccurrence(dataset)

        }

        fun handleOccurrencesList():
                ArrayList<String> {

            return OccurrencesDao.searchRecentOccurrences()


        }
    }
}