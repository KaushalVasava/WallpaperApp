package com.lahsuak.apps.wallpaperapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lahsuak.apps.wallpaperapp.ui.screens.HomeScreen
import com.lahsuak.apps.wallpaperapp.ui.screens.WallpaperScreen
import com.lahsuak.apps.wallpaperapp.ui.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        }
    ) {
        composable(
            route = NavigationItem.Home.route,
        ) {
            HomeScreen(mainViewModel, navController)
        }
        composable(route = "${NavigationItem.ViewImage.route}/{url}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val imageUrl = backStackEntry.arguments?.getString("url")
            imageUrl?.let {
                WallpaperScreen(it, navController)
            }
        }
    }
}