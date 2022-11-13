package com.example.dudewheresmystream.api

import android.util.Log
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
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

interface TMDBApi {
    @GET("/3/discover/movie?language=en-US&sort_by=popularity.desc&include_video=false&with_watch_monetization_types=flatrate")
    suspend fun getTMDBTrendingMovieInfo(
        @Query("api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
        @Query("page") numPages: String,//TODO possible to use 1 page for home screen mini fragment and then XXX pages for the main fragment
        @Query("with_watch_providers") providerCodes: String,//Note: comma separated list is valid here
        @Query("watch_region") regionCode: String) : TMDBResponse

    @GET("/3/discover/tv?language=en-US&sort_by=popularity.desc&include_video=false&with_watch_monetization_types=flatrate")
    suspend fun getTMDBTrendingTVInfo(
        @Query("api_key") apiKey: String,
        @Query("page") numPages: String,//TODO possible to use 1 page for home screen mini fragment and then XXX pages for the main fragment?
        @Query("with_watch_providers") providerCodes: String,//Note: comma separated list is valid here
        @Query("watch_region") regionCode: String) : TMDBResponse


    @GET("/3/movie/{movie_id}?language=en-US")
    suspend fun getTMDBMovieDetails(
        @Path("movie_id") movieID: String,
        @Query("api_key") apiKey: String,
    ) : DetailsVideoData

    @GET("/3/tv/{tv_id}?language=en-US")
    suspend fun getTMDBTVDetails(
        @Path("tv_id") tvID: String,
        @Query("api_key") apiKey: String,
    ) : DetailsVideoData

    @GET("/3/movie/{movie_id}/credits?language=en-US")
    suspend fun getTMDBMovieCredits(
        @Path("movie_id") movieID: String,
        @Query("api_key") apiKey: String,
    ) : CreditsVideoData

    @GET("/3/tv/{tv_id}/credits?language=en-US")
    suspend fun getTMDBTVCredits(
        @Path("tv_id") tvID: String,
        @Query("api_key") apiKey: String,
    ) : CreditsVideoData

    @GET("/3/movie/{movie_id}/watch/providers?language=en-US")
    suspend fun getTMDBMovieProviders(//TODO need to grab "link" from this call and then alter the locale parameter in the url to match our selected region
        @Path("movie_id") movieID: String,
        @Query("api_key") apiKey: String,
    ) : ProviderResponse

    @GET("/3/tv/{tv_id}/watch/providers?language=en-US")
    suspend fun getTMDBTVProviders(//TODO need to grab "link" from this call and then alter the locale parameter in the url to match our selected region
        @Path("tv_id") tvID: String,
        @Query("api_key") apiKey: String,
    ) : ProviderResponse



    //TODO probably some @GET functions for searching

    class TMDBResponse(val results: List<DiscoverVideoData>)

    class ProviderResponse(val map: Map<String,String>)//TODO possibly add another response class ",val flatrate:NewResponse" if we want provider_name/logoURL
    class ProviderResponseDeserializer: JsonDeserializer<ProviderResponse>{
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ProviderResponse {
            return try{
                val result = HashMap<String,String>()
                json.asJsonObject.get("results").asJsonObject.entrySet().map {
                    result.put(it.key,it.value.asJsonObject.get("link").asString)
                }
                ProviderResponse(result)
            } catch(e: Throwable){
                Log.w("ProviderResponseDeserializer","${e.message}")
                ProviderResponse(emptyMap())
            }
        }
    }


    companion object {
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
                .registerTypeAdapter(ProviderResponse::class.java, ProviderResponseDeserializer())//TODO confirm this
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        // Keep the base URL simple
        private const val BASE_URL = "api.themoviedb.org"
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
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
