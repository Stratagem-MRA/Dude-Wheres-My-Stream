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
import retrofit2.http.Path
import java.lang.reflect.Type

interface TMDBApi {
    @GET("/discover/movie?api_key={my_api_key}&language=en-US&sort_by=popularity.desc&include_video=false&page={num_pages}&with_watch_providers={watch_provider_code}&watch_region={watch_region}&with_watch_monetization_types=flatrate")
    suspend fun getTMDBTrendingMovieInfo(
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
        @Path("num_pages") numPages: String,//TODO possible to use 1 page for home screen mini fragment and then XXX pages for the main fragment
        @Path("watch_provider_code") providerCode: String,//TODO do we need to remove this portion of the url if no provider selected? Note: comma separated list is valid here
        @Path("watch_region") regionCode: String) : TMDBResponse

    @GET("/discover/tv?api_key={my_api_key}&language=en-US&sort_by=popularity.desc&include_video=false&page={num_pages}&with_watch_providers={watch_provider_code}&watch_region={watch_region}&with_watch_monetization_types=flatrate")
    suspend fun getTMDBTrendingTVInfo(
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
        @Path("num_pages") numPages: String,//TODO possible to use 1 page for home screen mini fragment and then XXX pages for the main fragment
        @Path("watch_provider_code") providerCode: String,//TODO do we need to remove this portion of the url if no provider selected? Note: comma separated list is valid here
        @Path("watch_region") regionCode: String) : TMDBResponse

    //TODO likely need to call trending tv/movie at the same time then combine into a single list by popularity

    @GET("/movie/{movie_id}?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBMovieDetails(
        @Path("movie_id") movieID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse

    @GET("/tv/{tv_id}?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBTVDetails(
        @Path("tv_id") tvID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse

    @GET("/movie/{movie_id}/credits?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBMovieCredits(
        @Path("movie_id") movieID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse

    @GET("/tv/{tv_id}/credits?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBTVCredits(
        @Path("tv_id") tvID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse

    @GET("/movie/{movie_id}/watch/providers?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBMovieProviders(//TODO need to grab "link" from this call and then alter the locale parameter in the url to match our selected region
        @Path("movie_id") movieID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse

    @GET("/tv/{tv_id}/watch/providers?api_key={my_api_key}&language=en-US")
    suspend fun getTMDBTVProviders(//TODO need to grab "link" from this call and then alter the locale parameter in the url to match our selected region
        @Path("tv_id") tvID: String,
        @Path("my_api_key") apiKey: String,//TODO this is fixed but is it best practice to leave it in the @GET statement or load it elsewhere???
    ) : TMDBResponse



    //TODO probably some @GET functions for searching

    class TMDBResponse(val data: TMDBResponseData)

    class TMDBResponseData(
        val children: List<TMDBChildrenResponse>,
        val after: String?,//TODO do we need before and after here?
        val before: String?
    )
    data class TMDBChildrenResponse(val data: VideoData)

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
        private const val BASE_URL = "www.api.themoviedb.org/3"
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
