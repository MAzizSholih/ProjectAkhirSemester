package com.mazizs.cinemaze.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadiyarajesh.flower_core.Resource
import com.mazizs.cinemaze.data.repository.MovieRepository
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import com.mazizs.cinemaze.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//merupakan kelas ViewModel untuk layar utama yang bertanggung jawab mengelola data dan logika terkait daftar film
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    //Untuk menyimpan status dan data daftar film yang akan datang
    private val _upcomingMovies : MutableStateFlow<State<List<MovieEntity>>?> = MutableStateFlow(null)
    //Untuk mengamati perubahan pada daftar film yang akan datang
    val upcomingMovies: MutableStateFlow<State<List<MovieEntity>>?> = _upcomingMovies

    init {
        getUpcomingMovies()
    }

    //Umtuk mendapatkan data daftar film yang akan datang melalui repository
    private fun getUpcomingMovies() {
        viewModelScope.launch {
            repository.getUpcomingMovies().collect{ response ->
                //Memproses hasil status dari response
                when (response.status) {
                    //Set status ke State.Loading jika dalam proses memuat
                    is Resource.Status.Loading -> {
                        _upcomingMovies.value = State.Loading()
                    }

                    //set status ke State.Success dengan data yang diperoleh jika memuat data berhasil
                    is Resource.Status.Success -> {
                        val movies = (response.status as Resource.Status.Success).data
                        _upcomingMovies.value = State.Success(movies)
                    }

                    is Resource.Status.EmptySuccess -> {}

                    //Set status ke State.Error dengan pesan kesalahan jika terjadi kesalahan dalam memuat data
                    is Resource.Status.Error -> {
                        val errorMessage = (response.status as Resource.Status.Error).errorMessage
                        _upcomingMovies.value = State.Error(errorMessage)
                    }
                }
            }
        }
    }
}