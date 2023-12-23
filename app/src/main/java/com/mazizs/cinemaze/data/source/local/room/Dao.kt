package com.mazizs.cinemaze.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow


//Merupakan bagian dari arsitektur Room Persistence Library, yang memungkinkan akses ke data dalam database
//Untuk mengkonfigurasi data access object atau DAO untuk mengakses database
@Dao
interface Dao {
    //Umtuk menyimpan data seluruh movie yang didapat dari API ke database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    //Untuk mengambil seluruh movie dari database
    @Query("SELECT * FROM movie_entity ")
    fun getAllMovies(): Flow<List<MovieEntity>>

    //Untuk mengambil data movie sesuai id dari database
    @Query("SELECT * FROM movie_entity WHERE id=:movieId ORDER BY id")
    fun getMovieById(movieId: Int): Flow<MovieEntity>

    //Untuk engupdate nilai isFavorite pada movie sesuai id
    @Query("UPDATE movie_entity SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateMovie(movieId: Int, isFavorite: Boolean)

    //Untuk mengambil data movie yang memiliki nilai isFavorite = 1
    @Query("SELECT * FROM movie_entity WHERE isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>
}