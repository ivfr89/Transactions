package com.fernandez.ivan.transactions.framework.server

import com.fernandez.ivan.data.models.ModelEntity
import com.fernandez.ivan.transactions.framework.fromJsonModelEntity
import com.google.gson.JsonParseException
import org.json.JSONArray

class ServerMapper
{
    inline fun <reified T: ModelEntity> parseObjectResponse(jsonString: String): T?
    {
        var objectResponse : T? = null
        try {

            objectResponse = jsonString.fromJsonModelEntity()

        } catch (e: JsonParseException) {
        }

        return objectResponse
    }

    inline fun <reified T: ModelEntity> parseArrayResponse(jsonString: String): List<T>
    {
        var listObjects: List<T> = emptyList()

        val objectArray = JSONArray(jsonString)

        for(i in 0 until objectArray.length())
        {
            parseObjectResponse<T>(objectArray.getString(i))?.let {
                listObjects += it
            }

        }

        return listObjects
    }
}
