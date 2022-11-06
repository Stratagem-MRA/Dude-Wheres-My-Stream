package com.example.dudewheresmystream.api

import android.text.SpannableString
import com.example.dudewheresmystream.MainActivity
import com.google.gson.GsonBuilder

class StreamRepo(private val streamApi: StreamApi) {

    val gson = GsonBuilder().registerTypeAdapter(
        SpannableString::class.java, StreamApi.SpannableDeserializer()
    ).create()

    private fun unpackData(response: StreamApi.StreamResponse): List<StreamData> {
        val dataListing = response.data.children.map {
            it.data
        }
        return dataListing
    }

    suspend fun getStreamInfo(): List<StreamData> {//TODO add any params needed for query, same name as func in api
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                "",
                StreamApi.StreamResponse::class.java
            )
            return unpackData(response)
        } else {
            val streamResponse = streamApi.getStreamInfo()//TODO params go here
            val streamListing = streamResponse.data.children.map {
                it.data
            }
            return streamListing
        }
    }
}
