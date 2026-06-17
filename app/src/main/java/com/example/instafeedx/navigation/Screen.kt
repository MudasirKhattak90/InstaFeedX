package com.example.instafeedx.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import okhttp3.Route

sealed class Screen(val route: String) {

    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Feed : Screen("feed_screen")
    object Upload : Screen("upload_screen")
    object Profile : Screen("profile_screen")

}

data class BottomNavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector

)

val bottomNavigationItem = listOf(
    BottomNavigationItem(
        label = "Home",
        route = Screen.Feed.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Filled.Home
    ),

    BottomNavigationItem(
        label = "Upload",
        route = Screen.Upload.route,
        selectedIcon = Icons.Filled.AddBox,
        unselectedIcon = Icons.Filled.AddBox
    ),

    BottomNavigationItem(
        label = "Profile",
        route = Screen.Profile.route,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Filled.AccountCircle
    )
)