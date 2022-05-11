package com.openclassrooms.realestatemanager.model

import android.content.Context
import android.media.Image
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

        fun saveImage(myImage: ImageWithDescription, fileName: String, context: Context) {
            val myPath = File(context.filesDir.toString() + "/playlists")
            val gson = Gson()
            val mySerializedPlaylist = gson.toJson(myImage)
            if (!myPath.exists()) myPath.mkdirs()
            val myFile = File("$myPath/$fileName.json")
            var myOutputStream: FileOutputStream? = null
            try {
                myOutputStream = FileOutputStream(myFile, false)
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
        }

    }
}