package com.example.finalproject.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.finalproject.dao.AddressDao
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


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

        fun getLocationFromAddress(context: Context, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<Address>?
            var p1: LatLng? = null
            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress!!, 5)
                if (address == null) {
                    return null
                }
                val location: Address = address[0]
                p1 = LatLng(location.latitude, location.longitude)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return p1
        }

        fun getAddressFromLocation(context: Context,coord: LatLng):String?{
            val coder = Geocoder(context)
            val address: List<Address>?
            var morada: String? = null
            try{
                address = coder.getFromLocation(coord.latitude,coord.longitude,5)
                if (address == null){
                    return null
                }
                morada = address[0].getAddressLine(0)
            }catch (ex: IOException){
                ex.printStackTrace()
            }
            return morada
        }
    }
}