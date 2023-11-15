package com.lahsuak.apps.wallpaperapp.ui.navigation

enum class Screen {
    HOME,
    VIEW_IMAGE,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object ViewImage : NavigationItem(Screen.VIEW_IMAGE.name)
}