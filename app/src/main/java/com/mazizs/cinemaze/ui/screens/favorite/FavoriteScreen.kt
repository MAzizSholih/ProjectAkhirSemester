package com.mazizs.cinemaze.ui.screens.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mazizs.cinemaze.ui.screens.home.MovieCard
import com.mazizs.cinemaze.utils.State

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi FavoriteScreen
//yaitu untuk menampilkan halaman daftar film favorit
@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    //Utuk mengamati perubahan data pada favoriteMovies
    val movieState by remember { favoriteViewModel.favoriteMovies }.collectAsState()
    when (movieState) { //Untuk menampilkan konten berdasarkan status dari favoriteMovie
        is State.Loading -> {
            Box( //Saat masih memuat, maka akan menampilkan loading menggunakan tata letak Box
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
            }
        }

        is State.Success -> { //Tampilan ketika berhasil dalam memuat data
            //Maka akan menampilkaan daftar film favorit saat data berhasil dimuat
            val data = (movieState as State.Success).data
            if(data.isEmpty()){
                Box( //Untuk tampilan ketika tidak ada film favorit yang tersimpan menggunakan tata letak Box
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "No Favorite Movie", fontWeight = FontWeight.Bold)
                }
            }else{
                LazyColumn( //Untuk taampilan daftar film favorit menggunakan LazyColumn
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    items(data.size) { movie ->
                        MovieCard( //Untuk menampilkan detail film favorit menggunakan Card
                            data[movie],
                            navController = rememberNavController(),
                            clickable = false
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        is State.Error -> {} //Tampilan ketika terjadi kesalahan dalam memuat data
        else -> {}
    }
}