package com.example.finalproject.utils

import com.example.finalproject.firebase.dao.AddressDao

class AddressUtils {
    companion object {
        fun addNewAddress(username: String, address: String, description: String) {
            AddressDao.addNewAddress(username, address, description)
        }

        fun getFavAddress(username: String, callback: (address: ArrayList<Map<String, String>>) -> (Unit)) {
            AddressDao.getFavAddress(username, callback)
        }

        fun removeAddress(username: String, addressToDel: Map<String, String>) {
            AddressDao.removeAddress(username, addressToDel)
        }
    }
}