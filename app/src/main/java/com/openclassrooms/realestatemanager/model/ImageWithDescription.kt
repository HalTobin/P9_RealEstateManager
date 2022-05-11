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
    companion object {

        /*fun readSavedData(path: String?): ImageWithDescription? {
            val myStringBuffer = StringBuffer("")
            var myImage: ImageWithDescription? = null
            try {
                val myFile = path?.let { File(it) }
                val fIn = FileInputStream(myFile)
                val isr = InputStreamReader(fIn)
                val buffreader = BufferedReader(isr)
                var readString = buffreader.readLine()
                while (readString != null) {
                    myStringBuffer.append(readString)
                    readString = buffreader.readLine()
                }
                val g = Gson()
                myImage = g.fromJson(myStringBuffer.toString(), ImageWithDescription::class.java)
                isr.close()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
            return myImage
        }*/

        fun saveImage(myImage: Bitmap, fileName: Long, context: Context): String {
            val myPath = File(context.filesDir.toString() + "/images")
            val gson = Gson()
            val mySerializedPlaylist = gson.toJson(myImage)
            if (!myPath.exists()) myPath.mkdirs()
            val myFile = File("$myPath/$fileName.png")
            var myOutputStream: FileOutputStream? = null
            try {
                myOutputStream = FileOutputStream(myFile, false)
                myImage.compress(Bitmap.CompressFormat.JPEG, 100, myOutputStream)
                myOutputStream.write(mySerializedPlaylist.toByteArray(Charset.defaultCharset()))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    myOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return context.filesDir.toString() + "/images/" + fileName + "/jpg"
        }

    }
}