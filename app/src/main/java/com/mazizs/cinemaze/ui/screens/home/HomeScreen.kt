package com.mazizs.cinemaze.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mazizs.cinemaze.R
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import com.mazizs.cinemaze.ui.CinemazeScreen
import com.mazizs.cinemaze.utils.Constants.BASE_URL_IMAGE
import com.mazizs.cinemaze.utils.State

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi HomeScreen
//yaitu untuk menampilkan tampilan daftar film pada layar utama
@Composable
fun HomeScreen(
    navController: NavController,
) {
    Box( //Merupakan kontainer utama dengan background sesuai warna latar belakang aplikasi
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        //Untuk mmanggil fungsi MovieList untuk menampilkan daftar film
        MovieList(navController)
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi MovieList
//yaitu untuk menampilkan daftar film dalam bentuk LazyColumn
@Composable
fun MovieList(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    //Untuk meengambil status dan data daftar film dari ViewModel
    val movieState by remember { homeViewModel.upcomingMovies }.collectAsState()
    //menggunakan when expression untuk menangani berbagai status yang mungkin
    when (movieState) {
        //Tampilan indikator loading jika masih dalam proses memuat
        is State.Loading -> {
            Box(
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
        //tampilan daftar film dalam LazyColumn jika berhasil memuat data
        is State.Success -> {
            val data = (movieState as State.Success).data
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                items(data.size) { movie ->
                    MovieCard(data[movie], navController)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        is State.Error -> {}
        else -> {}
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi MovieCard
//yaitu untuk menampilkan daftar film dalam bentuk card seperti kartu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(movie: MovieEntity, navController: NavController, clickable: Boolean = true) {
    Card( //Untuk menampilkan konten dalam bentuk kartu
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        onClick = {
            if(clickable){
                //Jika diklik film, maka akan berpindah ke layar detail film
                navController.navigate("${CinemazeScreen.DETAIL.route}/${movie.id}")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, Color.LightGray.copy(alpha = .2f), MaterialTheme.shapes.medium)
    ) {
        Row( //Untuk menyusun elemen secara horizontal di dalam card
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage( //Untuk menampilkan gambar film dengan URL yang diperoleh dari API
                modifier = Modifier
                    .height(150.dp)
                    .width(130.dp),
                model = BASE_URL_IMAGE + movie.posterPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_launcher_background)
            )
            Column( //Uuntuk menyusun teks dan informasi film secara vertikal
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Text( //Untuk menampilkan judul film
                    text = movie.title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text( //Untuk menampilkan ringkasan film
                    text = movie.overview.takeIf { it?.isNotBlank() == true } ?: "N/A",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text( //Untuk menampilkan tanggal rilis film
                    text = "Release date: ${movie.releaseDate}",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}