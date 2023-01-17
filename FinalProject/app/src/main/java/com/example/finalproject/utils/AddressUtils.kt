package com.example.finalproject.utils

import com.example.finalproject.firebase.dao.AddressDao

class AddressUtils {
    companion object {
        fun addNewAddress(username: String, address: String, description: String, lat: Float, lon: Float) {
            AddressDao.addNewAddress(username, address, description, lat, lon)
        }

        fun getFavAddress(username: String, callback: (favAddress: ArrayList<HashMap<String, Any>>) -> (Unit)) {
            AddressDao.getFavAddress(username, callback)
        }

        fun removeAddress(username: String, addressToDel: HashMap<String, Any>) {
            AddressDao.removeAddress(username, addressToDel)
        }
    }
}