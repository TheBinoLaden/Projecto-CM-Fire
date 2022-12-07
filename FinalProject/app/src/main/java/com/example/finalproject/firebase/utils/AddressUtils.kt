package com.example.finalproject.firebase.utils

import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import com.example.finalproject.AddressActivity
import com.example.finalproject.R
import com.example.finalproject.firebase.dao.AddressDao


class AddressUtils {


    companion object {

        fun addNewAddress(
            username: String,
            favPlaces: String,
            address: String,
            description: String
        ) {

            val fullAddress = "$address:$description"
            val newList = favPlaces + fullAddress

            AddressDao.addNewAddress(username, newList)
        }

        fun handleAddressList(activity: AddressActivity, table: TableLayout) {

            for (b in table.children) {
                // Inflate your row "template" and fill out the fields.
                val row: TableRow = LayoutInflater.from(activity)
                    .inflate(R.layout.activity_adresses, null) as TableRow
                (row.findViewById(R.id.casa) as TextView).text = "test"
                (row.findViewById(R.id.morada) as TextView).text = "Testlol"
                table.addView(row)
            }
            table.requestLayout()

        }
    }
}