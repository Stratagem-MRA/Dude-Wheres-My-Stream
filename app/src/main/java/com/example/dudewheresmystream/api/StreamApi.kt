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
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

interface StreamApi {

    @GET("")
    suspend fun getStreamInfo() : StreamResponse
    //TODO figure out actual api calls needed

    class StreamResponse(val data: StreamResponseData)

    class StreamResponseData(
        val children: List<StreamChildrenResponse>,
        val after: String?,
        val before: String?
    )
    data class StreamChildrenResponse(val data: StreamData)

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

    companion object{
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
        fun create(): StreamApi = create(httpurl)
        private fun create(httpUrl: HttpUrl): StreamApi {
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
                .create(StreamApi::class.java)
        }
    }
}
