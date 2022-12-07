package com.example.finalproject.firebase.utils

import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.finalproject.R
import com.example.finalproject.firebase.dao.OccurrencesDao
import com.example.finalproject.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.weather.Model
import java.util.*

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

        fun handleOccurrencesList(activity: ListNewOccurrenceActivity, listView: ListView) {

            val occurrenceList = OccurrencesDao.searchRecentOccurrences()
            val adapter: ArrayAdapter<*> = ArrayAdapter(
                activity,
                R.layout.activity_listoccurrences,
                occurrenceList
            )
            listView.adapter = adapter

        }
    }
}