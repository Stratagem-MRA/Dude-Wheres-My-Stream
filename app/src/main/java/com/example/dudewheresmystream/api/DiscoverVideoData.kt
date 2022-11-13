package com.example.dudewheresmystream.api

import com.google.gson.annotations.SerializedName

data class DiscoverVideoData(//TODO there are differences in the field names for tv shows and movies that we need to account for
    @SerializedName("name")//TODO this only applies to TV
    val name: String?,
    @SerializedName("title")//TODO this only applies to Movie
    val title: String?,
    @SerializedName("genre_ids")
    val genreIDs: List<Int>,//TODO Make this an enum
    @SerializedName("poster_path")
    val thumbnailURL: String,
    @SerializedName("overview")
    val description: String?,
    @SerializedName("id")
    val tmdbID: Int,
    @SerializedName("popularity")
    val popularity: Float,
    @SerializedName("vote_average")
    val stars: Float,

    val nameOrTitle: String,
    val type: ShowType

): java.io.Serializable //TODO do we need this?

data class DetailsVideoData(
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,

    val type: ShowType
)

data class CreditsVideoData(
    @SerializedName("cast")
    val cast: List<CastInfo>,
    @SerializedName("crew")
    val crew: List<CrewInfo>,

    val type: ShowType
)

data class ProvidersVideoData(
    @SerializedName("tmdbURL")//TODO watch providers
    val tmdbURL: String?
)

data class CastInfo(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("known_for_department")
    val department: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("popularity")
    val popularity: Float,
    @SerializedName("profile_path")
    val profilePicURL: String,
    @SerializedName("cast_id")
    val castID: Int?,
    @SerializedName("character")
    val character: String,
    @SerializedName("credit_id")
    val creditID: String,
    @SerializedName("order")
    val order: Int
)

data class CrewInfo(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("popularity")
    val popularity: Float,
    @SerializedName("profile_path")
    val profilePicURL: String,
    @SerializedName("credit_id")
    val creditID: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("job")
    val job: String
)

enum class ShowType{//TODO is this used
TV, MOVIE, EMPTY
}