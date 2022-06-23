package com.openclassrooms.realestatemanager.api

import com.google.gson.*
import com.openclassrooms.realestatemanager.model.Coordinates
import java.lang.reflect.Type

class GetCoordinatesDeserializer : JsonDeserializer<Coordinates?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Coordinates? {
        var temp: Coordinates? = null
        val jsonObject = json.asJsonObject
        try {
            val resultJson: JsonObject? = jsonObject.get("data").asJsonArray[0].asJsonObject
            if (resultJson != null) temp = Coordinates(
                resultJson.get("latitude").asDouble,
                resultJson.get("longitude").asDouble
            )
        } catch (e: Exception) {
            println("API : DETAILS ERROR : " + e.message + " " + json.toString())
        }
        return temp
    }
}