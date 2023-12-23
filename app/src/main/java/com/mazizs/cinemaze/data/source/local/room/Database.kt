package com.mazizs.cinemaze.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mazizs.cinemaze.data.source.local.entity.MovieEntity
import com.mazizs.cinemaze.utils.Constants.DB_VERSION

//Untuk menkonfigurasi database
@Database(
    entities = [MovieEntity::class],
    version = DB_VERSION,
    exportSchema = false
)

//Untuk mendefinisikan dan mengatur database lokal serta memberikan akses ke
//objek Dao yang digunakan untuk berinteraksi dengan data di dalam database
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}