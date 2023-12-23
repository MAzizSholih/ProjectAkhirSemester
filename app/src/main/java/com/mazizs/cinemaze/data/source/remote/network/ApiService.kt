package com.mazizs.cinemaze.data.source.remote.network

import com.hadiyarajesh.flower_core.ApiResponse
import com.mazizs.cinemaze.data.source.remote.response.MovieResponse
import com.mazizs.cinemaze.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

//Untuk mendeklarasi metode-metode yang akan digunakan untuk membuat permintaan atau request
//ke sumber data eksternal atau untuk mendapatkan data upcoming movies dari API
interface ApiService {
    //Mengambil data upcoming movie
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        //Query untuk api key
        @Query("api_key") apiKey: String = API_KEY
    ): ApiResponse<MovieResponse>
}