package com.example.dudewheresmystream.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.dudewheresmystream.Jsoup.OneStreamData
import com.example.dudewheresmystream.Jsoup.Scraper
import com.example.dudewheresmystream.MainActivity
import com.example.dudewheresmystream.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private var trending = MutableLiveData<List<DiscoverVideoData>>()
    private var favorites: MutableLiveData<MutableList<DiscoverVideoData>> = MutableLiveData(mutableListOf<DiscoverVideoData>())


    private var tmdbData = MutableLiveData<List<DiscoverVideoData>>()
    val tmdbApi = TMDBApi.create()
    val tmdbRepo = TMDBRepo(tmdbApi)
    var tmdbFetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var streamData = MutableLiveData<List<OneStreamData>>()
    private var streamProviders = MutableLiveData<ProvidersVideoData>()

    private var searchData = MutableLiveData<List<DiscoverVideoData>>()
    private var searchTerm = MutableLiveData<String>()


    init{
        if(MainActivity.globalDebug){
            //MainActivity handles submitting debug lists this can remain empty for future testing purposes
        }
        else {
            tmdbTrendingRefresh("1")//TODO might want to make this a specific refresh trending call
        }
    }

    fun tmdbTrendingRefresh(numPages: String, providerCode:String = "", regionCode:String = ""){
        tmdbFetchDone.postValue(false)

        viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO){
            postTrending(tmdbRepo.getTMDBTrendingInfo(numPages,providerCode,regionCode))
            tmdbFetchDone.postValue(true)
        }
    }

    fun tmdbDetailRefresh(vd: DiscoverVideoData){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO){
            postProviders(tmdbRepo.getTMDBProviders(vd))
            //TODO this needs everything added details,credits,etc...
        }

    }

    fun postFavorite(show: DiscoverVideoData){
        favorites.value!!.add(show)
        favorites.value = favorites.value
    }
    fun removeFavorite(show: DiscoverVideoData){
        val index = favorites.value!!.indexOf(show)
        favorites.value!!.removeAt(index)
        favorites.value = favorites.value
    }

    fun observeTrending(): MutableLiveData<List<DiscoverVideoData>> {
        return trending
    }
    fun postTrending(list: List<DiscoverVideoData>){
        trending.postValue(list)
    }
    fun observeFavorites(): MutableLiveData<MutableList<DiscoverVideoData>> {
        return favorites
    }
    fun observeTMDBData(): MutableLiveData<List<DiscoverVideoData>> {
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

    fun observeSearch(): MutableLiveData<List<DiscoverVideoData>> {
        return searchData
    }


    fun scrapeLinks(url: String?){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO) {
                postStreamData(Scraper().extract(url))
            }
        }

    fun postProviders(data: ProvidersVideoData){
        streamProviders.postValue(data)
    }

    fun observeProviders(): MutableLiveData<ProvidersVideoData> {
        return streamProviders
    }
}