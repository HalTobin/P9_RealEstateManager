package com.openclassrooms.realestatemanager.util

import android.content.Context
import android.content.res.Configuration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Coordinates

object MapUtils {

    // Check if the phone runs in dark mode or note
    private fun isInDarkMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    // Get the map style that matches the phone's theme
    fun getMapStyle(context: Context): MapStyleOptions {
        return if (isInDarkMode(context)) MapStyleOptions.loadRawResourceStyle(
            context,
            R.raw.map_style_night
        )
        else MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_day)
    }

    // Move the map's camera to a given location
    fun GoogleMap.navigateTo(coordinates: Coordinates) {
        navigateTo(coordinates, 12f)
    }

    // Move the map's camera to a given location with a given zoom
    fun GoogleMap.navigateTo(coordinates: Coordinates, zoom: Float) {
        val myLatLng = LatLng(coordinates.xCoordinate, coordinates.yCoordinate)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng, zoom)
        this.moveCamera(cameraUpdate)
        this.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

}