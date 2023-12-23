package com.mazizs.cinemaze.utils

//Untuk menangani state dari proses pengambilan data
sealed class State <T>{
    class Loading<T>: State<T>()
    data class Success<T>(val data: T): State<T>()
    data class Error<T>(val message: Any): State<T>()
}