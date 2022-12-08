package com.example.finalproject.firebase.utils

import com.example.finalproject.firebase.dao.AddressDao


class AddressUtils {


    companion object {

        fun addNewAddress(
            username: String,
            favPlaces: String,
            address: String,
            description: String
        ) {
            var fullAddress = ""
            if (favPlaces.isBlank()) {
                fullAddress = "$address:$description"
            } else {
                fullAddress = "-$address:$description"
            }
            val newList = favPlaces + fullAddress

            AddressDao.addNewAddress(username, newList)
        }

        fun handleAddressList(listStringify: String): ArrayList<String> {

            val arraylist = ArrayList<String>(0)

            val array = listStringify.removeSurrounding("[", "]").split('-')

            for (item in array) {
                val location = item.split(":")[0]
                val description = item.split(":")[1]
                arraylist.add("$location $description")
            }
            return arraylist
        }
    }
}