package com.example.mydearmovies.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mydearmovies.R

sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Movies : BottomNavItem(
        route = "movies",
        titleResId = R.string.nav_movies,
        selectedIcon = Icons.Filled.Movie,
        unselectedIcon = Icons.Outlined.Movie
    )

    object Series : BottomNavItem(
        route = "series",
        titleResId = R.string.nav_series,
        selectedIcon = Icons.Filled.PlayCircle,
        unselectedIcon = Icons.Outlined.PlayCircle
    )

    object Home : BottomNavItem(
        route = "home",
        titleResId = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Actors : BottomNavItem(
        route = "actors",
        titleResId = R.string.nav_people,
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups
    )

    object Profile : BottomNavItem(
        route = "profile",
        titleResId = R.string.nav_profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Movies,
    BottomNavItem.Series,
    BottomNavItem.Home,
    BottomNavItem.Actors,
    BottomNavItem.Profile
)