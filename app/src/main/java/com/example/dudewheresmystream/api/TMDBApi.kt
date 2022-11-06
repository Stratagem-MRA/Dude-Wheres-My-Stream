package com.example.dudewheresmystream.api

import android.text.SpannableString
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type

interface TMDBApi {
    @GET("")
    suspend fun getTMDBInfo() : TMDBResponse
    //TODO figure out actual api calls needed

    class TMDBResponse(val data: TMDBResponseData)

    class TMDBResponseData(
        val children: List<TMDBChildrenResponse>,
        val after: String?,
        val before: String?
    )
    data class TMDBChildrenResponse(val data: TMDBData)
    //TODO do we want to import spannable string functionality from hw4 RedditApi.kt?
    class SpannableDeserializer : JsonDeserializer<SpannableString> {
        // @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): SpannableString {
            return SpannableString(json.asString)
        }
    }

    companion object {
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder().registerTypeAdapter(
                SpannableString::class.java, SpannableDeserializer()
            )
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        // Keep the base URL simple
        //private const val BASE_URL = "https://www.reddit.com/"TODO update this
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("www.reddit.com")//TODO update this
            .build()

        fun create(): TMDBApi = create(httpurl)
        private fun create(httpUrl: HttpUrl): TMDBApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    // Enable basic HTTP logging to help with debugging.
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(TMDBApi::class.java)
        }
    }
}
