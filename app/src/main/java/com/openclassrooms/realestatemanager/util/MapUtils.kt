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

    fun getMapStyle(context: Context): MapStyleOptions {
        var myMapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_day)
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) myMapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(
                context, R.raw.map_style_night
            )
        return myMapStyleOptions
    }

    // Move the map's camera to a location
    fun GoogleMap.navigateTo(coordinates: Coordinates) {
        navigateTo(coordinates, 12f)
    }

    // Move the map's camera to a location
    fun GoogleMap.navigateTo(coordinates: Coordinates, zoom: Float) {
        val myLatLng = LatLng(coordinates.xCoordinate, coordinates.yCoordinate)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng, zoom)
        this.moveCamera(cameraUpdate)
        this.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

}