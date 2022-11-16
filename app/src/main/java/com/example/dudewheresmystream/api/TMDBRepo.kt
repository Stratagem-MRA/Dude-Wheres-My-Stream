package com.example.dudewheresmystream.api

import android.util.Log
import java.lang.Integer.min

class TMDBRepo(private val tmdbApi: TMDBApi) {

    private val tmdbAPIKey = "edfc0e18aaba20f6f68fbed52f5204ba"

    private fun sortPopular(tv:List<DiscoverVideoData>, movie:List<DiscoverVideoData>): List<DiscoverVideoData>{
        val minLength = min(tv.size,movie.size) //we want to take only the most popular elements from the combined lists
        val result = tv + movie
        return result.sortedByDescending { it.popularity }.subList(0,minLength)
    }

    suspend fun getTMDBTrendingInfo(numPages: String, providerCode:String = "", regionCode:String = ""): List<DiscoverVideoData> {

        val tmdbTrendingTVResponse = tmdbApi.getTMDBTrendingTVInfo(tmdbAPIKey,numPages,providerCode,regionCode)
        val tmdbTVListing = tmdbTrendingTVResponse.results.map {
            DiscoverVideoData(
                name = if(it.name.isNullOrBlank()) "" else it.name,
                title = if(it.title.isNullOrBlank()) "" else it.title,
                genreIDs = if(it.genreIDs.isNullOrEmpty()) listOf() else it.genreIDs,
                thumbnailURL = "https://image.tmdb.org/t/p/original${it.thumbnailURL}",
                description = if(it.description.isNullOrBlank()) "" else it.description,
                tmdbID = it.tmdbID,
                popularity = it.popularity,
                stars = it.stars,
                nameOrTitle = if(it.title.isNullOrBlank()) it.name!! else it.title,
                type = ShowType.TV)
        }

        val tmdbTrendingMovieResponse = tmdbApi.getTMDBTrendingMovieInfo(tmdbAPIKey,numPages,providerCode,regionCode)
        val tmdbMovieListing = tmdbTrendingMovieResponse.results.map{
            DiscoverVideoData(
                name = if(it.name.isNullOrBlank()) "" else it.name,
                title = if(it.title.isNullOrBlank()) "" else it.title,
                genreIDs = if(it.genreIDs.isNullOrEmpty()) listOf() else it.genreIDs,
                thumbnailURL = "https://image.tmdb.org/t/p/original${it.thumbnailURL}",
                description = if(it.description.isNullOrBlank()) "" else it.description,
                tmdbID = it.tmdbID,
                popularity = it.popularity,
                stars = it.stars,
                nameOrTitle = if(it.title.isNullOrBlank()) it.name!! else it.title,
                type = ShowType.MOVIE)
        }
        return sortPopular(tmdbTVListing,tmdbMovieListing)
    }

    suspend fun getTMDBDetail(vd: DiscoverVideoData): DetailsVideoData{
        return if(vd.type == ShowType.MOVIE){
            DetailsVideoData(
                firstAirDate = "",
                releaseDate = tmdbApi.getTMDBMovieDetails(vd.tmdbID.toString(),tmdbAPIKey).releaseDate,
                type = ShowType.MOVIE)
        } else{
            DetailsVideoData(
                firstAirDate = tmdbApi.getTMDBTVDetails(vd.tmdbID.toString(),tmdbAPIKey).firstAirDate,
                releaseDate = "",
                type = ShowType.TV)
        }
    }

    suspend fun getTMDBCredits(vd: DiscoverVideoData): CreditsVideoData{
        return if(vd.type == ShowType.MOVIE){
            tmdbApi.getTMDBMovieCredits(vd.tmdbID.toString(),tmdbAPIKey).let {
                CreditsVideoData(
                    cast = it.cast.map{cast -> CastInfo(cast.adult,cast.gender,cast.id,cast.department,cast.name,cast.originalName,cast.popularity,"https://image.tmdb.org/t/p/original${cast.profilePicURL}",cast.castID,cast.character,cast.creditID,cast.order)},
                    crew = it.crew.map{crew -> CrewInfo(crew.adult,crew.gender,crew.id,crew.knownForDepartment,crew.name,crew.originalName,crew.popularity,"https://image.tmdb.org/t/p/original${crew.profilePicURL}",crew.creditID,crew.department,crew.job)},
                    type = ShowType.MOVIE
                )
            }
        }
        else{
            tmdbApi.getTMDBTVCredits(vd.tmdbID.toString(),tmdbAPIKey).let {
                CreditsVideoData(
                    cast = it.cast.map{cast -> CastInfo(cast.adult,cast.gender,cast.id,cast.department,cast.name,cast.originalName,cast.popularity,"https://image.tmdb.org/t/p/original${cast.profilePicURL}",cast.castID,cast.character,cast.creditID,cast.order)},
                    crew = it.crew.map{crew -> CrewInfo(crew.adult,crew.gender,crew.id,crew.knownForDepartment,crew.name,crew.originalName,crew.popularity,"https://image.tmdb.org/t/p/original${crew.profilePicURL}",crew.creditID,crew.department,crew.job)},
                    type = ShowType.TV
                )
            }
        }
    }

    suspend fun getTMDBProviders(vd: DiscoverVideoData, regionCode: String): ProvidersVideoData{
        return if(vd.type == ShowType.MOVIE){
            val providers = tmdbApi.getTMDBMovieProviders(vd.tmdbID.toString(),tmdbAPIKey)
            Log.d("","available MOVIE regions: ${providers.map.keys}")
            ProvidersVideoData(providers.map[regionCode])
        }
        else{
            val providers = tmdbApi.getTMDBTVProviders(vd.tmdbID.toString(),tmdbAPIKey)
            Log.d("","available TV regions: ${providers.map.keys}")
            ProvidersVideoData(providers.map[regionCode])
        }
    }
}

