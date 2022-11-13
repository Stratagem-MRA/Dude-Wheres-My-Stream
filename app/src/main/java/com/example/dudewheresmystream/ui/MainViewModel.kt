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


    val tmdbApi = TMDBApi.create()
    val tmdbRepo = TMDBRepo(tmdbApi)
    var tmdbFetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var streamData = MutableLiveData<List<OneStreamData>>()
    private var streamProviders = MutableLiveData<ProvidersVideoData>()

    private var oneShowDetails = MutableLiveData<DetailsVideoData>()
    private var oneShowCredits = MutableLiveData<CreditsVideoData>()

    private var searchData = MutableLiveData<List<DiscoverVideoData>>()
    private var searchTerm = MutableLiveData<String>()


    init{
        if(MainActivity.globalDebug){
            //MainActivity handles submitting debug lists this can remain empty for future testing purposes
        }
        else {

        }
    }

    fun tmdbTrendingRefresh(pageToLoad: String, providerCode:String = "", regionCode:String = ""){
        tmdbFetchDone.postValue(false)

        viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO){
            postTrending(tmdbRepo.getTMDBTrendingInfo(pageToLoad,providerCode,regionCode))
            tmdbFetchDone.postValue(true)
        }
    }

    fun tmdbDetailRefresh(vd: DiscoverVideoData){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO){
            postProviders(tmdbRepo.getTMDBProviders(vd))
            postDetails(tmdbRepo.getTMDBDetail(vd))
            postCredits(tmdbRepo.getTMDBCredits(vd))
        }
    }

    fun tmdbDetailClear(){
        postProviders(ProvidersVideoData(""))
        postStreamData(emptyList())
        postDetails(DetailsVideoData("","",ShowType.EMPTY))
        postCredits(CreditsVideoData(emptyList(), emptyList(),ShowType.EMPTY))
    }

    fun scrapeLinks(url: String?){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO) {
            postStreamData(Scraper().extract(url))
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
    fun observeFavorites(): MutableLiveData<MutableList<DiscoverVideoData>> {
        return favorites
    }


    private fun postTrending(list: List<DiscoverVideoData>){
        trending.postValue(list)
    }
    fun observeTrending(): MutableLiveData<List<DiscoverVideoData>> {
        return trending
    }


    private fun postStreamData(list: List<OneStreamData>){
        streamData.postValue(list)
    }
    fun observeStreamData(): MutableLiveData<List<OneStreamData>> {
        return streamData
    }



    private fun postProviders(data: ProvidersVideoData){
        streamProviders.postValue(data)
    }

    fun observeProviders(): MutableLiveData<ProvidersVideoData> {
        return streamProviders
    }


    private fun postDetails(data: DetailsVideoData){
        oneShowDetails.postValue(data)
    }
    fun observeDetails(): MutableLiveData<DetailsVideoData>{
        return oneShowDetails
    }


    private fun postCredits(data: CreditsVideoData){
        oneShowCredits.postValue(data)
    }
    fun observeCredits(): MutableLiveData<CreditsVideoData>{
        return oneShowCredits
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
}