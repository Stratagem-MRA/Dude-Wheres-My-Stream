package com.example.dudewheresmystream.Jsoup

import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class Scraper {
    //val url = "https://www.themoviedb.org/tv/1402-the-walking-dead/watch?locale=GB"
    //TODO we can get the url above from the TMDb api and pass it to this function
    // possible locale shenanigans as well for viewing if a show might be available in a different country
    //TODO probably need to credit JustWatch where links are provided

    fun test(url: String) = runBlocking{
        coroutineScope {
            launch(context = coroutineContext + Dispatchers.IO) {
                extract(url)
            }
        }
    }

    suspend fun extract(url: String){
        delay(1000L)
        val doc = Jsoup.connect(url)
            .timeout(5000)
            .get()

        //This is hacked in directly from inspecting the html basically goes to the ott_provider class looks for children under the Stream h3 header
        // and then selects the non hidden children on the page
        //Note: some of the streams on TMDb appear not to link to exactly what you would expect from the web page but
        var elements = doc.getElementsByClass("ott_provider")
            elements = elements.select("h3:contains(Stream)").first().nextElementSiblings()
            elements = elements.select("li:not(li.hide*) a")

            Log.d("JSOUP",elements.size.toString())
            Log.d("JSOUP",elements.toString())

    }
}