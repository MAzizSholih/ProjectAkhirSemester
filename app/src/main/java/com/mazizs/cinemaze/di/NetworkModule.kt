package com.mazizs.cinemaze.di

import com.hadiyarajesh.flower_retrofit.FlowerCallAdapterFactory
import com.mazizs.cinemaze.data.source.remote.network.ApiService
import com.mazizs.cinemaze.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //Untuk menyediakan instansiasi objek OkHttpClient
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    //Untuk menyediakan instansiasi objek ApiService melalui retrofit
    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()//Untuk membuat objek retrofit dengan konfigurasi dasar
            .baseUrl(BASE_URL) //Untuk mengatur URL dasar untuk API
            .addCallAdapterFactory(FlowerCallAdapterFactory.create()) //Menanbahkan adapter call untuk kustom handling response
            .addConverterFactory(GsonConverterFactory.create()) //Menambahkan converter factory untuk parsing JSON menggunakan Gson
            .client(client) //
            .build()
        return retrofit.create(ApiService::class.java) //mengembalikan objek ApiService yang telah dibangun oleh retrofit
    }
}