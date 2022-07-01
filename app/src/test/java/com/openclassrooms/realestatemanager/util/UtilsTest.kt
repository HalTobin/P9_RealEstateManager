package com.openclassrooms.realestatemanager.util

import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.util.Utils.isAnImage
import io.mockk.clearAllMocks

import org.junit.After
import org.junit.Test
import kotlin.test.assertTrue

class UtilsTest {

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun convertDollarToEuro() {
    }

    @Test
    fun convertEuroToDollar() {
    }

    @Test
    fun fromEuroToDollar() {
    }

    @Test
    fun fromDollarToEuro() {
    }

    @Test
    fun isValid() {
    }

    @Test
    fun copyToInternal() {
    }

    @Test
    fun `check the extension of a given path and return true if it corresponds to an image`() {
        // Given
        val path = "folder/thisisanimage.jpg"

        // When
        val isAnImage = path.isAnImage()

        // Then
        assertThat(isAnImage).isTrue()
    }

    @Test
    fun `check the extension of a given path and return true if it corresponds to a video`() {
    }

    @Test
    fun getSuffix() {
    }

    @Test
    fun toBitmap() {
    }

    @Test
    fun toDateString() {
    }

    @Test
    fun getTodayDate() {
    }

    @Test
    fun fullAddress() {
    }

    @Test
    fun openImageViewer() {
    }
}