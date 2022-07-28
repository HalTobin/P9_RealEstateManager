package com.openclassrooms.realestatemanager.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.util.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.util.Utils.convertEuroToDollar
import com.openclassrooms.realestatemanager.util.Utils.getSuffix
import com.openclassrooms.realestatemanager.util.Utils.getTodayDate
import com.openclassrooms.realestatemanager.util.Utils.isAVideo
import com.openclassrooms.realestatemanager.util.Utils.isAnImage
import com.openclassrooms.realestatemanager.util.Utils.isInternetAvailable
import com.openclassrooms.realestatemanager.util.Utils.toDateString
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import java.util.*

class UtilsTest {

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `test the conversion from dollar to euro`() {
        // Given
        val dollar = 100000

        // When
        val euro = convertDollarToEuro(dollar)

        // Then
        assertThat(euro).isEqualTo(95238)
    }

    @Test
    fun `test the conversion from euro to dollar`() {
        // Given
        val euro = 100000

        // When
        val dollar = convertEuroToDollar(euro)

        // Then
        assertThat(dollar).isEqualTo(105000)
    }

    @Test
    fun `check the extension of a given path and return true if it corresponds to an image`() {
        // Given
        val path = "folder/this_is_an_image.jpg"

        // When
        val isAnImage = path.isAnImage()

        // Then
        assertThat(isAnImage).isTrue()
    }

    @Test
    fun `check the extension of a given path and return false if it doesn't correspond to an image`() {
        // Given
        val path = "folder/this_is_not_an_image.mp3"

        // When
        val isAnImage = path.isAnImage()

        // Then
        assertThat(isAnImage).isFalse()
    }

    @Test
    fun `check the extension of a given path and return true if it corresponds to a video`() {
        // Given
        val path = "folder/this_is_a_video.mp4"

        // When
        val isAVideo = path.isAVideo()

        // Then
        assertThat(isAVideo).isTrue()
    }

    @Test
    fun `check the extension of a given path and return false if it doesn't correspond to a video`() {
        // Given
        val path = "folder/this_is_not_a_video.mp3"

        // When
        val isAVideo = path.isAVideo()

        // Then
        assertThat(isAVideo).isFalse()
    }

    @Test
    fun `check that a file extension is correctly returned from its path as a String`() {
        // Given
        val path = "folder/this.is.a.file.path.mp3"

        // When
        val suffix = path.getSuffix()

        // Then
        assertThat(suffix).isEqualTo(".mp3")
    }

    @Test
    fun `check the conversion from a timestamp to a human readable date`() {
        // Given
        val timestamp = 1657843200000

        // When
        val date = timestamp.toDateString()

        // Then
        assertThat(date).isEqualTo("15/07/2022")
    }

    @Test
    fun `check the the conversion from a date objet to a human readable date`() {
        // Given
        val mockDate = mockk<Date>()
        every { mockDate.time } returns 1657843200000

        // When
        val date = getTodayDate(mockDate)

        // Then
        assertThat(date).isEqualTo("15/07/2022")
    }

    @Test
    fun `check if an internet connection over wifi available`() {
        // Given
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork) } returns mockk {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns false
        }

        // When
        val isInternetAvailable = isInternetAvailable(mockConnectivityManager)

        // Then
        assertThat(isInternetAvailable).isTrue()
    }

    @Test
    fun `check if an internet connection over cellular network available`() {
        // Given
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork) } returns mockk {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns false
        }

        // When
        val isInternetAvailable = isInternetAvailable(mockConnectivityManager)

        // Then
        assertThat(isInternetAvailable).isTrue()
    }

    @Test
    fun `check if an internet connection over ethernet available`() {
        // Given
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork) } returns mockk {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true
            every { hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns false
        }

        // When
        val isInternetAvailable = isInternetAvailable(mockConnectivityManager)

        // Then
        assertThat(isInternetAvailable).isTrue()
    }

    @Test
    fun `check if an internet connection over bluetooth available`() {
        // Given
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork) } returns mockk {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns true
        }

        // When
        val isInternetAvailable = isInternetAvailable(mockConnectivityManager)

        // Then
        assertThat(isInternetAvailable).isTrue()
    }

    @Test
    fun `check returns false when no connection is available`() {
        // Given
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork) } returns mockk {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns false
            every { hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns false
        }

        // When
        val isInternetAvailable = isInternetAvailable(mockConnectivityManager)

        // Then
        assertThat(isInternetAvailable).isFalse()
    }

    @Test
    fun `check the generation of a complete address from an address, a zipCode, a city and a country`() {
        // Given
        val address = "79 Avenue de la République"
        val zipCode = "75011"
        val city = "Paris"
        val country = "France"

        // When
        val fullAddress =
            Utils.fullAddress(address = address, zipCode = zipCode, city = city, country = country)

        // Then
        assertThat(fullAddress).isEqualTo("79 Avenue de la République, 75011 Paris, France")
    }

}