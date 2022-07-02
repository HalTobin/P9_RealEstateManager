package com.openclassrooms.realestatemanager.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.openclassrooms.realestatemanager.util.Utils.getSuffix
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    // Create a file into the internal storage of the app from an Uri object
    fun Uri.copyToInternal(context: Context) : File {
        var fileName = "IMG_".plus(System.currentTimeMillis())

        this.let { returnUri ->
            context.contentResolver.query(returnUri,null,null,null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = fileName.plus(cursor.getString(nameIndex).getSuffix())
        }

        val iStream : InputStream = context.contentResolver.openInputStream(this)!!
        val outputDir = File(context.filesDir.toPath().toString().plus("/Images/"))
        if(!outputDir.exists()) outputDir.mkdir()
        val outputFile = File(outputDir, fileName)
        copyStreamToFile(iStream, outputFile)
        iStream.close()
        return outputFile
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

}