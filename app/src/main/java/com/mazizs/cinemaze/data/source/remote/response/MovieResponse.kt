package com.mazizs.cinemaze.data.source.remote.response

import com.google.gson.annotations.SerializedName


//Informasi data yang diambil dari API
//Data yang diambil tidak semua atau Data yang dipakai bisa dilihat di MovieEntity
data class MovieResponse(

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val results: List<MovieItem>
)
//Data class yang merepresentasikan tentang detail informasi mengenai sebuah movie
data class MovieItem(

    @field:SerializedName("id") //ID unik movie
    val id: Int,

    @field:SerializedName("overview") //Deskripsi atau ringkasan dari movie
    val overview: String? = null,

    @field:SerializedName("backdrop_path") //Path gambar background untu movie
    val backdropPath: String? = null,

    @field:SerializedName("original_language") //Bahasa asli dari movie
    val originalLanguage: String,

    @field:SerializedName("release_date") //Tanggal rilis movie
    val releaseDate: String? = null,

    @field:SerializedName("popularity") //Populeritas movie
    val popularity: Double,

    @field:SerializedName("vote_average") //Nilai rata-rata vote untuk movie
    val voteAverage: Double,

    @field:SerializedName("title") //Judul dari movie
    val title: String,

    @field:SerializedName("genre_ids") //Daftar ID genre yang terkait dengan movie
    val genreIds: List<Int?>,

    @field:SerializedName("vote_count") //Jumlah total vote untuk movie
    val voteCount: Int,

    @field:SerializedName("poster_path") //Path gambar poster untuk movie
    val posterPath: String? = null
)
