package com.mazizs.cinemaze.di

import android.content.Context
import androidx.room.Room
import com.mazizs.cinemaze.data.source.local.room.Dao
import com.mazizs.cinemaze.data.source.local.room.Database
import com.mazizs.cinemaze.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    //Untuk menyediakan instansiasi objek Database Room
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context = context,
            klass = Database::class.java,
            name = DB_NAME
        ).build()
    }

    //Untuk menyediakan instansiasi objek Dao atau Data Access Object
    @Provides
    fun provideDao(database: Database): Dao = database.dao()
}