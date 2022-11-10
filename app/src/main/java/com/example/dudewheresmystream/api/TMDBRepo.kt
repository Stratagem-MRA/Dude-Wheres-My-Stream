package com.example.dudewheresmystream.api

import android.text.SpannableString
import com.google.gson.GsonBuilder
import com.example.dudewheresmystream.MainActivity

class TMDBRepo(private val tmdbApi: TMDBApi) {

    val gson = GsonBuilder().registerTypeAdapter(
        SpannableString::class.java, TMDBApi.SpannableDeserializer()
    ).create()

    private fun unpackData(response: TMDBApi.TMDBResponse): List<VideoData> {
        val dataListing = response.data.children.map {
            it.data
        }
        return dataListing
    }

    suspend fun getTMDBInfo(): List<VideoData> {//TODO add any params needed for query, same name as func in api
        //TODO possibly rename this to getTMDBTrendingInfo
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                "",
                TMDBApi.TMDBResponse::class.java
            )
            return unpackData(response)
        } else {
            val tmdbResponse = tmdbApi.getTMDBInfo()//TODO params go here
            val tmdbListing = tmdbResponse.data.children.map {
                it.data
            }
            return tmdbListing
        }
    }
}
