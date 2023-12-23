package com.mazizs.cinemaze.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mazizs.cinemaze.ui.screens.detail.DetailScreen
import com.mazizs.cinemaze.ui.screens.favorite.FavoriteScreen
import com.mazizs.cinemaze.ui.screens.home.HomeScreen
import com.mazizs.cinemaze.ui.theme.CinemazeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

//Fungsi onCreate ini digunakan untuk mengedit tampilan aktivitas utama dengan menggunakan komponen UI yang telah didefinisikan dalam CinemazeApp
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isSplashScreenClosed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition{
            !isSplashScreenClosed
        }
        super.onCreate(savedInstanceState)
        setContent {
            CinemazeTheme {
                LaunchedEffect(key1 = Unit) {
                    delay(2000)
                    isSplashScreenClosed = true
                }
                CinemazeApp()
            }
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi CinemazeApp
//yaitu untuk merepresentasikan struktur utama dari aplikasi Cinemaze
@Composable
fun CinemazeApp(
    navController: NavHostController = rememberNavController()
) {
    //Untuk mendapatkan entri
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?.let { route ->
        when (route) {
            CinemazeScreen.HOME.route -> CinemazeScreen.HOME
            CinemazeScreen.DETAIL.route -> CinemazeScreen.DETAIL
            CinemazeScreen.FAVORITE.route -> CinemazeScreen.FAVORITE
            else -> null
        }
    } ?: CinemazeScreen.HOME

    Scaffold( //Untuk membuat top bar dan area konten
        topBar = {
            CinemazeAppBar( //Merupakan top app bar kustom dengan aksi navigasi dan favorit
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onFavoriteClicked = { navController.navigate(CinemazeScreen.FAVORITE.route) }
            )
        }
    ) { innerPadding ->
        NavHost( //Untuk menavigasi antar layar berbeda
            navController = navController,
            startDestination = CinemazeScreen.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {//Untuk menentukan konten composable untuk setiap layar
            composable(CinemazeScreen.HOME.route) {
                HomeScreen(
                    navController
                )
            }
            composable(
                route = CinemazeScreen.DETAIL.route + "/{movieId}",
                arguments = listOf(
                    navArgument(name = "movieId") {
                        type = NavType.IntType
                    }
                )
            ) {
                DetailScreen(backStackEntry?.arguments?.getInt("movieId"))
            }
            composable(CinemazeScreen.FAVORITE.route) {
                FavoriteScreen()
            }
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi CinemazeAppBar
//yaitu untuk menampilkan AppBar kustom untuk aplikasi Cinemaze
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemazeAppBar(
    currentScreen: CinemazeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        //Untuk menampilkan nama layar saat ini
        title = { Text(currentScreen.route) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier,
        navigationIcon = { //Ikon navigasi ditampilkan jika mendapatkan NavigateBack bernilai true
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (!canNavigateBack) {
                IconButton(onClick = onFavoriteClicked) {
                    Icon( //Untuk menampilkan iko favorite
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }
        }
    )
}

//Untk merepresentasikan berbagai layar dalam aplikasi Cinemaze
sealed class CinemazeScreen(val route: String) {
    //Objek HOME akan merepresentasikan layar utama
    object HOME : CinemazeScreen("Home")
    //Objek DETAIL akam merepresentasikan layar detail film
    object DETAIL : CinemazeScreen("Detail")
    //Objek FAVORITE akan merepresentasikan layar daftar film favorit
    object FAVORITE : CinemazeScreen("Favorite")
}