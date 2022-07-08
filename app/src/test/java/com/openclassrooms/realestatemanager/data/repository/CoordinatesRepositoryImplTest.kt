package com.openclassrooms.realestatemanager.data.repository

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.di.DataModule
import com.openclassrooms.realestatemanager.model.Coordinates
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.*
import java.util.concurrent.TimeUnit


class CoordinatesRepositoryImplTest {

    private val responseFile =
        "src/test/java/com/openclassrooms/realestatemanager/util/CoordinatesApiPlaceholder.json"

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(DataModule.createGsonConverter())
        .build()

    private val coordinatesRepository = CoordinatesRepositoryImpl(api)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        clearAllMocks()
        mockWebServer.shutdown()
    }

    @Test
    fun `check returned coordinates by CoordinatesRepository`() = runBlocking {
        // Given
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(getStringFromFile(responseFile))
        )

        // When
        coordinatesRepository.getCoordinates("95 Avenue de la République, 75011 Paris, France")
            .collect {
                val actual = it
                val expected = Coordinates(48.863845, 2.38283)

                // Then
                assertThat(actual).isEqualTo(expected)
            }

    }

    // Load a file as a String from local resources
    @Throws(Exception::class)
    fun getStringFromFile(filePath: String): String {
        val fl = File(filePath)
        val fin = FileInputStream(fl)
        val ret = convertStreamToString(fin)
        //Make sure you close all streams.
        fin.close()
        return ret
    }

    // Convert InputStream to String
    @Throws(Exception::class)
    private fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

}