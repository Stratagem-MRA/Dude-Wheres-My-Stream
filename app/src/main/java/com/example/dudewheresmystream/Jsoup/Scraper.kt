package com.example.dudewheresmystream.Jsoup

import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.Jsoup


private lateinit var streams: List<OneStreamData>


data class OneStreamData(
    val streamURL: String,
    val title: String,
    val titleClipped: String,
    val streamIconURL: String
)

class Scraper {
    //TODO probably need to credit JustWatch where links are provided



    fun extract(url: String?): List<OneStreamData> {
        if (url.isNullOrBlank()) {
            return emptyList()
        } else {
            val doc = Jsoup.connect(url)
                .timeout(5000)
                .get()

            //This is hacked in directly from inspecting the html. Goes to the ott_provider class looks for children under the 'Stream' h3 header
            // and then selects the non hidden children on the page
            //Note: some of the streams on TMDb appear not to link to exactly what you would expect from the web page
            try{
                var elements = doc.getElementsByClass("ott_provider")
                elements = elements.select("h3:contains(Stream)").first().nextElementSiblings()//can fail here if the show is available to buy or rent but not to stream
                elements = elements.select("li:not(li.hide*) a")//ignores links that are hidden on the website
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
            catch (e: Throwable){
                Log.w("Scraper","${e.message}")
                return emptyList()
            }
        }
    }
    private fun trimTitle(str: String): String{
        return str.substring(str.lastIndexOf(" on ")+4)
    }
}