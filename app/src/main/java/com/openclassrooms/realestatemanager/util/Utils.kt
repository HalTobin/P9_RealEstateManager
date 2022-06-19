package com.openclassrooms.realestatemanager.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.nio.file.Path
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.95).toInt()
    }

    fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros * 1.05).toInt()
    }

    fun Int.fromEuroToDollar(): Int {
        return convertEuroToDollar(this)
    }

    fun Int.fromDollarToEuro(): Int {
        return convertDollarToEuro(this)
    }

    fun String.isValid(): Boolean {
        return@isValid this != ""
    }

    /*fun copyToInternal(context: Context, sourcePath: Uri): String {
        val mySource = File(sourcePath.path!!)
        val myDestination = File(context.filesDir.toPath().toString().plus("Images/IMG_").plus(System.currentTimeMillis()))
        createFileFromContentUri(context, sourcePath)
        //mySource.copyTo(myDestination)
        return myDestination.path
    }*/

    fun Uri.copyToInternal(context: Context) : File{
        var fileName = "IMG_".plus(System.currentTimeMillis())

        this.let { returnUri ->
            context.contentResolver.query(returnUri,null,null,null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }

        //  For extract file mimeType
        val fileType: String? = this.let { returnUri ->
            context.contentResolver.getType(returnUri)
        }

        val iStream : InputStream = context.contentResolver.openInputStream(this)!!
        val outputDir = File(context.filesDir.toPath().toString().plus("/Images/"))
        val outputFile = File(outputDir, fileName)
        copyStreamToFile(iStream, outputFile)
        iStream.close()
        return  outputFile
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

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting

        /*val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled*/
    }

    fun fullAddress(address: String, zipCode: String?, city: String, country: String): String {
        return address.plus(", ").plus(zipCode).plus(" ").plus(city).plus(", ").plus(country)
    }
}