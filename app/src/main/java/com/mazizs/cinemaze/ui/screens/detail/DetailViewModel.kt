package com.mazizs.cinemaze.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadiyarajesh.flower_core.Resource
import com.mazizs.cinemaze.data.repository.MovieRepository
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import com.mazizs.cinemaze.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Merupakan kelas ViewModel yang menggunakan Hilt untuk mengelola logika tampilan pada halaman detail film
@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {
    //Merupakanm mutableStateFlow yang menyimpan status state detail film
    private val _movieDetail = MutableStateFlow<State<MovieEntity>?>(null)
    //Merupakan stateFlow yang dapat diakses oleh tampilan untuk mengamati perubahan status state detail film
    val movieDetail: StateFlow<State<MovieEntity>?> get() = _movieDetail

    //Fungsi untuk mengambil detail film dari database berdasarkan ID film
    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieFromDb(movieId).collect { response ->
                when (response.status) {
                    is Resource.Status.Loading -> {
                        //Update state menjadi loading ketika status sedang memuat
                        _movieDetail.value = State.Loading()
                    }
                    is Resource.Status.Success -> {
                        //Update state menjadi Success dengan data yang diterima ketika data berhasil dimuat
                        val movies = (response.status as Resource.Status.Success).data
                        _movieDetail.value = State.Success(movies)
                    }
                    is Resource.Status.EmptySuccess -> {}
                    is Resource.Status.Error -> {
                        //Update state menjadi rrror dengan pesan kesalahan yang diterima ketika terjadi kesalahan
                        val errorMessage = (response.status as Resource.Status.Error).errorMessage
                        _movieDetail.value = State.Error(errorMessage)
                    }
                }
            }
        }
    }

    //Merupakan fungsi untuk mengubah status favorit film di database
    fun toggleFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            //Menggunakan coroutine untuk menjalankan proses pengubahan status favorit
            repository.updateMovieFavoriteStatus(movieId, isFavorite)
        }
    }
}