package com.mazizs.cinemaze.ui.screens.favorite

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

//Merupakan kelas ViewModel untuk mengelola logika tampilan pada halaman daftar film favorit
@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MovieRepository): ViewModel(){
    //untuk menyimpan status dan data daftar film favorit
    private val _favoriteMovies : MutableStateFlow<State<List<MovieEntity>>?> = MutableStateFlow(null)
    val favoriteMovies: MutableStateFlow<State<List<MovieEntity>>?> = _favoriteMovies

    init {
        getFavoriteMovies()
    }
    //Untuk memuat daftar film favorit dari repositori,
    //menggunakan viewModelScope untuk menjalankan operasi secara asinkron
    private fun getFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovieFromDb().collect{ response ->
                when (response.status) {
                    //Set status ke loading jika masih dalam proses memuat
                    is Resource.Status.Loading -> {
                        _favoriteMovies.value = State.Loading()
                    }
                    //set status ke Success dengan data daftar film jika memuat data berhasil
                    is Resource.Status.Success -> {
                        val movies = (response.status as Resource.Status.Success).data
                        _favoriteMovies.value = State.Success(movies)
                    }
                    //Set status ke EmptySuccess jika daftar film favorit kosong
                    is Resource.Status.EmptySuccess -> {}
                    //Set status ke Error dengan pesan kesalahan jika terjadi kesalahan dalam memuat data
                    is Resource.Status.Error -> {
                        val errorMessage = (response.status as Resource.Status.Error).errorMessage
                        _favoriteMovies.value = State.Error(errorMessage)
                    }
                }
            }
        }
    }
}