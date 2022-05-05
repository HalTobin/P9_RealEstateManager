package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.util.Utils
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import kotlin.Throws

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun euroToDollar_isCorrect() {
        Assert.assertEquals(105, Utils.convertEuroToDollar(100))
    }

    @Test
    fun dollarToEuro_isCorrect() {
        Assert.assertEquals(95, Utils.convertDollarToEuro(100))
    }
}