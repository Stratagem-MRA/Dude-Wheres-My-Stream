package com.example.dudewheresmystream.api

import android.text.SpannableString
import com.google.gson.annotations.SerializedName
//TODO this should combine the Stream API and TMDB api information together
data class VideoData(
    @SerializedName("name")
    val key: String,
    @SerializedName("title")
    val title: SpannableString?,
    @SerializedName("thumbnailURL")
    val thumbnailURL: String,
    @SerializedName("description")
    val description: SpannableString?
) {
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