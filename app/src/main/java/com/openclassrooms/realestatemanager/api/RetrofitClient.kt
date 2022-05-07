package com.openclassrooms.realestatemanager.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Coordinates
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.positionstack.com/v1/"

    private var retrofit: Retrofit? = null

    // This allow the use of a custom deserializer (GetCoordinatesDeserializer)
    private fun createGsonConverter(): Converter.Factory? {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            object : TypeToken<Coordinates>() {}.type,
            GetCoordinatesDeserializer()
        )
        val gson: Gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    fun getClient(): Retrofit? {
        if(retrofit == null) {
            retrofit = createGsonConverter()?.let {
                    Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(it).build()
                }
        }
        return retrofit
    }

}