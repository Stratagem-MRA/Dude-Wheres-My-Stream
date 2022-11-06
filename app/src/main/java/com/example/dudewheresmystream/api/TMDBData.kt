package com.example.dudewheresmystream.api

import com.google.gson.annotations.SerializedName
//TODO this needs to be adjusted to fit api pull
data class TMDBData(
    @SerializedName("title")
    val key: String,
    @SerializedName("thumbnail")
    val thumbnailURL: String,
    @SerializedName("description")
    val description: String
) {
}