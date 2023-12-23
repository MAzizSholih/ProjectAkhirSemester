package com.mazizs.cinemaze.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mazizs.cinemaze.R
import com.mazizs.cinemaze.utils.Constants
import com.mazizs.cinemaze.utils.State

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi DetailScreen
//yaitu untuk menampilkan halaman detail film
@Composable
fun DetailScreen(
    imageId: Int?,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    //State untuk mengelola status kesukaan pengguna terhadap film
    var isFavorite by remember { mutableStateOf(false) }
    //Utuk mengamati perubahan data pada movieDetail
    val movieDetail by remember { detailViewModel.movieDetail }.collectAsState()

    LaunchedEffect(Unit) {//Untuk mengeksekusi permintaan mendapatkan detail film saat komposisi pertama kali diperoleh
        detailViewModel.getMovieDetail(imageId ?: 0)
    }

    Box( //Merupakan tampilan utama halaman detail film dengan Box
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            when (movieDetail) {
                is State.Loading -> {
                    Box( //Merupakan tampilan loading saat masih memuat data
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
                is State.Success -> {
                    //Merupakan tampilan konten berisi detail film ketika berhasil memuat data
                    val data = (movieDetail as State.Success).data
                    Box( //Header dengan gambar film dan tombol suka atau favorite
                        modifier = Modifier.height(285.dp)
                    ) {
                        AsyncImage( //Merupaan tampilan gambar film menggunakan AsyncImage
                            modifier = Modifier
                                .height(260.dp)
                                .fillMaxWidth(),
                            model = Constants.BASE_URL_IMAGE + data.backdropPath,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.ic_launcher_background)
                        )
                        ToggleFavoriteButton( //Merupakan tombol suka atau favorire untuk menandai sebagai film yang disukai
                            isFavorite = data.isFavorite,
                            onToggle = {
                                isFavorite = it
                                detailViewModel.toggleFavoriteStatus(data.id, isFavorite)
                               }, modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 10.dp)
                                .size(45.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                        )
                    }
                    Column( //Untuk menampilkan informasi detail film seperti judul, deskripsi, rating, tanggal rilis, dan bahasa
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    ) {
                        Text( //Untuk menampilkan judul film
                            text = data.title,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = data.overview.takeIf { it?.isNotBlank() == true } ?: "N/A",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row( //Untuk menampilkan informasi tambahan seperti rating, tanggal rilis, dan bahasa
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text( //Untuk menampilkan informasi rating film
                                    text = "Rating",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold

                                    )
                                )
                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(data.voteAverage.toString())
                                }
                            }

                            Column( //Untuk menampilkan informasi tanggal rilis film
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Release",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold

                                    )
                                )
                                Text(data.releaseDate.toString())
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text( //Untuk menampilkan bahasa
                                    text = "Language",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold

                                    )
                                )
                                Text(data.originalLanguage)
                            }
                        }
                    }
                }
                is State.Error -> {}
                else -> {}
            }
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi
//ToggleFavoriteButton yaitu untuk menampilkan tombol suka atau favorite
@Composable
fun ToggleFavoriteButton(
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit,
    isFavorite: Boolean = false
) {
    //Merupakan state untuk mengelola status tombol toggle suka
    var isChecked by remember { mutableStateOf(isFavorite) }

    IconToggleButton(//Merupakan tombol toggle suka dengan ikon berdasarkan status saat ini
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
            onToggle(it)
        }
    ) {
        //Merupakan ikon yang digunakan untuk menunjukkan status tombol toggle suka
        val icon = if (isChecked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        val iconTint = if (isChecked) Color.Red else Color.Red

        Icon( //Untuk menampilkan ikon berdasarkan wrna yang sesuai
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
        )
    }
}