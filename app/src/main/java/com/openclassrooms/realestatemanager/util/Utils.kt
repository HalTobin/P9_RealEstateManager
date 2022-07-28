package com.openclassrooms.realestatemanager.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.android.synthetic.main.overlay_imageview.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
    fun convertDollarToEuro(dollars: Int): Int = (dollars * 0.95238).roundToInt()

    fun Int.fromDollarToEuro(): Int = convertDollarToEuro(this)

    fun convertEuroToDollar(euros: Int): Int = (euros * 1.05).roundToInt()

    fun Int.fromEuroToDollar(): Int = convertEuroToDollar(this)

    // Check if the file name correspond to an image
    fun String.isAnImage(): Boolean =
        (this.getSuffix().lowercase() == ".jpg" || this.getSuffix().lowercase() == ".png")

    // Check if the file name correspond to a video
    fun String.isAVideo(): Boolean = this.getSuffix().lowercase() == ".mp4"

    // Return the file extension
    fun String.getSuffix(): String = this.substring(this.lastIndexOf("."))

    // Return a date as a String from a timestamp
    fun Long.toDateString(): String = SimpleDateFormat("dd/MM/yyyy").format(this)

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(date: Date): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(date)
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(connectivityManager: ConnectivityManager): Boolean {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    // Get the address of an estate as Following "Number Street, ZIPCODE, City, Country"
    fun fullAddress(address: String, zipCode: String?, city: String, country: String): String {
        return address.plus(", ").plus(zipCode).plus(" ").plus(city).plus(", ").plus(country)
    }

    // Check if the app is running tests
    fun isAnAndroidTest(): Boolean {
        return try {
            // If the app is running in Test Mode, then we set an agent and a location
            Class.forName("androidx.test.espresso.Espresso")
            Log.i("RUNNING", "TEST")
            true
        } catch (e: ClassNotFoundException) {
            Log.i("RUNNING", "DEBUG / RELEASE")
            false
        }
    }

}