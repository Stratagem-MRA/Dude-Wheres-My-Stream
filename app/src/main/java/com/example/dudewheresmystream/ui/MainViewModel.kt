package com.example.dudewheresmystream.ui

import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.*
import com.example.dudewheresmystream.Jsoup.OneStreamData
import com.example.dudewheresmystream.Jsoup.Scraper
import com.example.dudewheresmystream.MainActivity
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class MainViewModel: ViewModel() {
    private var trending = MutableLiveData<List<VideoData>>()
    private var favorites: MutableLiveData<MutableList<VideoData>> = MutableLiveData(mutableListOf<VideoData>())


    private var tmdbData = MutableLiveData<List<TMDBData>>()
    val tmdbApi = TMDBApi.create()
    val tmdbRepo = TMDBRepo(tmdbApi)
    var tmdbFetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var streamData = MutableLiveData<List<OneStreamData>>()

    private var searchData = MutableLiveData<List<VideoData>>()
    private var searchTerm = MutableLiveData<String>()


    init{
        if(MainActivity.globalDebug){
            //MainActivity handles submitting debug lists this can remain empty for future testing purposes
        }
        else {
            tmdbRefresh()//TODO might want to make this a specific refresh trending call
        }
    }

    fun tmdbRefresh(){
        tmdbFetchDone.postValue(false)

        viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO){
            //TODO will want to come back to this to decide how to update API repositories
            tmdbData.postValue(tmdbRepo.getTMDBInfo())
            tmdbFetchDone.postValue(true)
        }
    }

    fun postFavorite(show: VideoData){
        favorites.value!!.add(show)
        favorites.value = favorites.value
    }
    fun removeFavorite(show: VideoData){
        val index = favorites.value!!.indexOf(show)
        favorites.value!!.removeAt(index)
        favorites.value = favorites.value
    }

    fun observeTrending(): MutableLiveData<List<VideoData>> {
        return trending
    }
    fun postTrending(list: List<VideoData>){
        trending.postValue(list)
    }
    fun observeFavorites(): MutableLiveData<MutableList<VideoData>> {
        return favorites
    }
    fun observeTMDBData(): MutableLiveData<List<TMDBData>> {
        return tmdbData
    }
    fun postStreamData(list: List<OneStreamData>){
        streamData.postValue(list)
    }
    fun observeStreamData(): MutableLiveData<List<OneStreamData>> {
        return streamData
    }
    fun search(){
        //TODO
    }
    fun postSearchTerm(text: String){
        searchTerm.value = text
    }

    fun observeSearch(): MutableLiveData<List<VideoData>> {
        return searchData
    }


    fun scrapeLinks(url: String){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO) {
                postStreamData(Scraper().extract(url))
            }
        }
    }