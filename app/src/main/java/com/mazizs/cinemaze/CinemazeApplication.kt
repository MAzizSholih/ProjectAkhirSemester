package com.mazizs.cinemaze

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Untuk mendefinisikan kelas CinemazeApplication, yang mewakili kelas aplikasi untuk aplikasi Android Cinemaze
//Hilt merupakan depedency injection untuk android yang menyederhanakan dan mempermudah proses injeksi dependensi
//Anotasi @HiltAndroidApp digunakan untuk menghasilkan komponen Dagger/Hilt yang
//diperlukan dan menginisialisasi kerangka injeksi dependensi untuk aplikasi.
@HiltAndroidApp
class CinemazeApplication : Application()