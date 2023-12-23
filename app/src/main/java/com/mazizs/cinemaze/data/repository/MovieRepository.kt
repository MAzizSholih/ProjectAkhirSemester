package com.mazizs.cinemaze.data.repository

import com.hadiyarajesh.flower_core.Resource
import com.hadiyarajesh.flower_core.dbBoundResource
import com.hadiyarajesh.flower_core.dbResource
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import com.mazizs.cinemaze.data.source.local.room.Dao
import com.mazizs.cinemaze.data.source.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: Dao
){
    //Untuk mengambil data movie, selanjutnya data movie akan disimpan
    //ke dalam database bila data movie masih kosong di database
    suspend fun getUpcomingMovies(
        shouldMakeNetworkRequest: Boolean? = null
    ): Flow<Resource<List<MovieEntity>>> {
        return dbBoundResource(
            fetchFromLocal = { dao.getAllMovies() }, //Untuk mengambil data dari databse lokal
            shouldMakeNetworkRequest = { dbData ->
                shouldMakeNetworkRequest ?: dbData.isNullOrEmpty()
            },
            makeNetworkRequest = {
                apiService.getUpcomingMovies() //Membuat permintaan ke sumber eksternal (API) untuk mendapatkan data terbaru
            },
            processNetworkResponse = {},
            saveResponseData = { response ->
                val movies = response.results.map {
                    MovieEntity(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        posterPath = it.posterPath,
                        backdropPath = it.backdropPath,
                        releaseDate = it.releaseDate,
                        voteAverage = it.voteAverage,
                        originalLanguage = it.originalLanguage,
                        isFavorite = false
                    )
                }
                dao.insertAllMovies(movies)
            },
            onNetworkRequestFailed =     { _, _ -> } //Untuk menangani kegagalan permintaan ke sumber eksternal
        ).flowOn(Dispatchers.IO)
    }

    //Untuk mengambil data movie berdasarkan id
    fun getMovieFromDb(movieId: Int): Flow<Resource<MovieEntity>> {
        return dbResource { dao.getMovieById(movieId) }.flowOn(Dispatchers.IO)
    }

    //Untuk mengupdate status favorite dari movie
    suspend fun updateMovieFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        dao.updateMovie(movieId, isFavorite)
    }

    //Untuk mengambil data movie favorit
    fun getFavoriteMovieFromDb(): Flow<Resource<List<MovieEntity>>> {
        return dbResource { dao.getFavoriteMovies() }.flowOn(Dispatchers.IO)
    }
}