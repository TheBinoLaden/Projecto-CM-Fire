package com.example.finalproject.utils

class StringUtils {

    companion object {
        private var DATE = 0
        private var COORDINATES = 1
        private var DESCRIPTION = 2
        private var TITLE = 3
        private var TYPE = 4
        fun getValueOfField(element: String, field: String): String {

            val arrayOfValues = element.split("|")
            var important: String? = null
            when (field) {
                "date" ->
                    important = arrayOfValues[DATE]
                "coordinates" ->
                    important = arrayOfValues[COORDINATES]
                "description" ->
                    important = arrayOfValues[DESCRIPTION]
                "title" ->
                    important = arrayOfValues[TITLE]
                "type" ->
                    important = arrayOfValues[TYPE]
            }
            if (important != null) {
                return important.substringAfter(":")
            }
            return ""
        }

        fun getSecondsFromDate(date : String) : String{
            return date.substringBefore(",").substringAfter("(")

        }

        fun getMilliSecondsFromDate(date : String) : String{
            return date.substringBefore(")").substringAfter(",")
        }

        fun getLon(coordinate : String): Double{
            return coordinate.substringBefore(",").substringAfter("(").toDouble()
        }
        fun getLat(coordinate : String): Double{
            return coordinate.substringBefore(")").substringAfter(",").toDouble()
        }

        fun getLonDB(coordinate : String): String{
            return coordinate.substringBefore(",").substringAfter("=")
        }
        fun getLatDB(coordinate : String): String{
            return coordinate.substringBefore("}").substringAfter(",").substringAfter("=")
        }
    }
}