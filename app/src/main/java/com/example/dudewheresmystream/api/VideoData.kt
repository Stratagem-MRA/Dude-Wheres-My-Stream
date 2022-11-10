package com.example.dudewheresmystream.api

import android.text.SpannableString
import com.google.gson.annotations.SerializedName
//TODO what happens when a query doesn't contain one of the below fields? I think it just gets returned as null

data class VideoData(
    @SerializedName("imdb_id")
    val key: String,
    @SerializedName("title")
    val title: SpannableString?,
    @SerializedName("poster_path")
    val thumbnailURL: String,
    @SerializedName("overview")
    val description: SpannableString?,
    @SerializedName("tmdbURL")
    val tmdbURL: String,
    @SerializedName("tvID")
    val tvID: Int,
    @SerializedName("movieID")
    val movieID: Int,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("cast")
    val cast: List<CastInfo>,
    @SerializedName("crew")
    val crew: List<CrewInfo>,

): java.io.Serializable {
    companion object{
        fun spannableStringsEqual(a: SpannableString?, b: SpannableString?): Boolean {
            if(a == null && b == null) return true
            if(a == null && b != null) return false
            if(a != null && b == null) return false
            val spA = a!!.getSpans(0, a.length, Any::class.java)
            val spB = b!!.getSpans(0, b.length, Any::class.java)
            return a.toString() == b.toString()
                    &&
                    spA.size == spB.size && spA.equals(spB)
        }
    }
}

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
    val popularity: Int,
    @SerializedName("profile_path")
    val profilePicURL: String,
    @SerializedName("cast_id")
    val castID: Int,
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
    val popularity: Int,
    @SerializedName("profile_path")
    val profilePicURL: String,
    @SerializedName("credit_id")
    val creditID: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("job")
    val job: String
)