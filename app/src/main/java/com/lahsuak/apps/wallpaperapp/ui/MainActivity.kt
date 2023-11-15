package com.lahsuak.apps.wallpaperapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lahsuak.apps.wallpaperapp.ui.navigation.AppNavHost
import com.lahsuak.apps.wallpaperapp.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            WallpaperAppTheme {
                Surface(Modifier.background(MaterialTheme.colorScheme.background)) {
                     AppNavHost(mainViewModel,navController)
                }
            }
        }
    }
}