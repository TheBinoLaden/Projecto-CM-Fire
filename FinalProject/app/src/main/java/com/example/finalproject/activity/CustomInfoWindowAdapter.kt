package com.example.finalproject.activity

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.example.finalproject.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(context: MainActivity):GoogleMap.InfoWindowAdapter {

    //var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window,null)

    private fun rendowWindowText (marker: Marker, view: View){
        val title = marker.title
        val tvTitle = view.findViewById<TextView>(R.id.title_info_window)

        val snippet = marker.snippet
        val tvSnippet = view.findViewById<TextView>(R.id.snippet_info_window)

        if(!title.equals("")){
            tvTitle.text = title
        }

        if(!snippet.equals("")){
            tvSnippet.text = snippet
        }
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoContents(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}