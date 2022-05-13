package com.openclassrooms.realestatemanager.model

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.Gson
import java.io.*
import java.nio.charset.Charset

data class ImageWithDescription(
    val id: Long,
    val estateId: Long,
    val description: String = "",
    val imageUrl: String
) {

}