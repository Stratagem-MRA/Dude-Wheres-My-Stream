package com.example.dudewheresmystream.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.dudewheresmystream.MainActivity
import com.example.dudewheresmystream.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class MainViewModel: ViewModel() {
    private var trending = MutableLiveData<List<VideoData>>()
    private var favorites: MutableLiveData<MutableList<VideoData>> = MutableLiveData(mutableListOf<VideoData>())


    private var tmdbData = MutableLiveData<List<TMDBData>>()
    val tmdbApi = TMDBApi.create()
    val tmdbRepo = TMDBRepo(tmdbApi)
    var tmdbFetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var streamData = MutableLiveData<List<StreamData>>()
    val streamApi = StreamApi.create() //TODO rename this after looking up actual site
    val streamRepo = StreamRepo(streamApi)
    var streamFetchDone: MutableLiveData<Boolean> = MutableLiveData(false)

    private var searchTerm = MutableLiveData<String>()

    init{
        //TODO refresh trending vods
        if(MainActivity.globalDebug){
            //MainActivity handles submitting debug lists this can remain empty for future testing purposes
        }
        else {
            tmdbRefresh()
            streamRefresh()
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

    fun streamRefresh(){
        streamFetchDone.postValue(false)

        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO){
            //TODO will want to come back to this to decide how to update API repositories
            streamData.postValue(streamRepo.getStreamInfo())
            streamFetchDone.postValue(true)
        }
    }

    fun combinedRefresh(){
        //TODO may need to use this once logic for combining api calls is fleshed out
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
    fun observeStreamData(): MutableLiveData<List<StreamData>> {
        return streamData
    }
    fun search(){
        //TODO
    }
    fun postSearchTerm(text: String){
        searchTerm.value = text
    }

}