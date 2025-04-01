package com.example.agilizaapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Productos : Screen("productos")
}

