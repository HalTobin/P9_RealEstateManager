package com.openclassrooms.realestatemanager

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import kotlin.Throws

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.openclassrooms.go4lunch", appContext.packageName)
    }
}