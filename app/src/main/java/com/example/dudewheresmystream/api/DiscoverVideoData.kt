package com.example.dudewheresmystream.api

import com.google.gson.annotations.SerializedName

data class DiscoverVideoData(
    @SerializedName("name")//This only applies to TV
    val name: String?,
    @SerializedName("title")//This only applies to Movie
    val title: String?,
    @SerializedName("genre_ids")
    val genreIDs: List<Int>,//TODO v2 feature: create enum map for this
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
    @SerializedName("media_type")
    val mediaType: String,

    val nameOrTitle: String,
    val type: ShowType

): java.io.Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiscoverVideoData

        if (name != other.name) return false
        if (title != other.title) return false
        if (genreIDs != other.genreIDs) return false
        if (thumbnailURL != other.thumbnailURL) return false
        if (description != other.description) return false
        if (tmdbID != other.tmdbID) return false
        if (popularity != other.popularity) return false
        if (stars != other.stars) return false
        if (mediaType != other.mediaType) return false
        if (nameOrTitle != other.nameOrTitle) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + genreIDs.hashCode()
        result = 31 * result + thumbnailURL.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + tmdbID
        result = 31 * result + popularity.hashCode()
        result = 31 * result + stars.hashCode()
        result = 31 * result + mediaType.hashCode()
        result = 31 * result + nameOrTitle.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

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
    val tmdbURL: String?,
    val availableRegions: List<String>
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
): PersonInfo(PersonType.CAST)

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
): PersonInfo(PersonType.CREW)

open class PersonInfo(
    val personType: PersonType
)

data class SettingData(
    val name: String,
    val id: String
)


enum class ShowType{
    TV, MOVIE, EMPTY
}
enum class PersonType{
    CAST,CREW
}