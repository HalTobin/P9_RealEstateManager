package com.openclassrooms.realestatemanager.util

import android.content.Context
import com.google.common.truth.Truth.assertThat
import android.content.res.Configuration
import com.openclassrooms.realestatemanager.util.MapUtils.isInDarkMode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After

import org.junit.Test

class MapUtilsTest {

    @After
    fun tearDown() = clearAllMocks()

    // TODO - check if this can run as a unit test
    @Test
    fun `check that false is returned when night mode is off`() {
        // Given
        val mockContext = mockk<Context>(relaxed = true)
        every { mockContext.resources.configuration } returns mockk {
            every { uiMode } returns Configuration.UI_MODE_NIGHT_NO
        }

        // When
        val darkMode = isInDarkMode(mockContext)

        // Then
        assertThat(darkMode).isFalse()
    }

    @Test
    fun navigateTo() {
    }

    @Test
    fun testNavigateTo() {
    }
}