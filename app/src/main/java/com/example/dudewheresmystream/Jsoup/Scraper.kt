package com.example.dudewheresmystream.Jsoup

import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.Jsoup


private lateinit var streams: List<OneStreamData>


data class OneStreamData(
    val streamURL: String,
    val title: String,
    val titleClipped: String,//TODO do we need to keep track of the stream provider title?
    val streamIconURL: String
)

class Scraper {
    //val url = "https://www.themoviedb.org/tv/1402-the-walking-dead/watch?locale=GB"
    //TODO we can get the url above from the TMDb api and pass it to this function
    // possible locale shenanigans as well for viewing if a show might be available in a different country
    //TODO probably need to credit JustWatch where links are provided



    fun extract(url: String?): List<OneStreamData> {
        if (url.isNullOrBlank()) {
            return listOf()
        } else {
            val doc = Jsoup.connect(url)
                .timeout(5000)
                .get()

            //This is hacked in directly from inspecting the html. Goes to the ott_provider class looks for children under the 'Stream' h3 header
            // and then selects the non hidden children on the page
            //Note: some of the streams on TMDb appear not to link to exactly what you would expect from the web page
            var elements = doc.getElementsByClass("ott_provider")
            elements = elements.select("h3:contains(Stream)").first().nextElementSiblings()
            elements = elements.select("li:not(li.hide*) a")
            //val element = elements[0].attr("href")
            //val element = elements[0].attr("title")
            //val element = elements[0].select("img").attr("src")
            streams = elements.map {
                OneStreamData(
                    streamURL = it.attr("href"),
                    title = it.attr("title"),
                    titleClipped = trimTitle(it.attr("title")),
                    streamIconURL = "https://themoviedb.org" + it.select("img").attr("src")
                )
            }
            return streams
        }
    }
    private fun trimTitle(str: String): String{
        return str.substring(str.lastIndexOf(" on ")+4)
    }
}