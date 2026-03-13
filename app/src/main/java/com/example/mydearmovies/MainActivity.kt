package com.example.mydearmovies

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mydearmovies.core.navigation.BottomNavItem
import com.example.mydearmovies.core.navigation.CustomBottomNavigation
import com.example.mydearmovies.core.navigation.bottomNavItems
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.feature.home.ui.HomeScreen
import com.example.mydearmovies.feature.people.ui.PeopleScreen
import com.example.mydearmovies.feature.people.ui.PersonBiographyScreen
import com.example.mydearmovies.feature.people.ui.PersonDetailsScreen
import com.example.mydearmovies.feature.media.MoviesScreen
import com.example.mydearmovies.feature.media.SeriesScreen
import com.example.mydearmovies.feature.media.ui.MediaBiographyScreen
import com.example.mydearmovies.feature.media.ui.MediaDetailsScreen
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.core.navigation.AppRoutes
import com.example.mydearmovies.feature.profile.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDearMoviesTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val rootNavController = rememberNavController()
    val tabsNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = AppRoutes.MAIN,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(AppRoutes.MAIN) {
            Scaffold(
                bottomBar = {
                    CustomBottomNavigation(navController = tabsNavController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = tabsNavController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(BottomNavItem.Movies.route) {
                        MoviesScreen(
                            onItemClick = { item ->
                                rootNavController.navigate(AppRoutes.mediaDetails(MediaType.MOVIE.name, item.id))
                            }
                        )
                    }
                    composable(BottomNavItem.Series.route) {
                        SeriesScreen(
                            onItemClick = { item ->
                                rootNavController.navigate(AppRoutes.mediaDetails(MediaType.TV.name, item.id))
                            }
                        )
                    }
                    composable(BottomNavItem.Home.route) {
                        HomeScreen(
                            onMediaClick = { mediaId, mediaType ->
                                rootNavController.navigate(AppRoutes.mediaDetails(mediaType.name, mediaId)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable(BottomNavItem.Actors.route) {
                        PeopleScreen(
                            onPersonClick = { person ->
                                rootNavController.navigate(AppRoutes.personDetails(person.id))
                            }
                        )
                    }
                    composable(BottomNavItem.Profile.route) { ProfileScreen() }
                }
            }
        }
        composable(AppRoutes.PERSON_DETAILS) {
            PersonDetailsScreen(
                onBackClick = { rootNavController.popBackStack() },
                onVerMaisClick = { id, _, _ ->
                    rootNavController.navigate(AppRoutes.personBiography(id))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(AppRoutes.PERSON_BIOGRAPHY) {
            PersonBiographyScreen(
                onBackClick = { rootNavController.popBackStack() },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(AppRoutes.MEDIA_DETAILS) {
            MediaDetailsScreen(
                onBackClick = { rootNavController.popBackStack() },
                onVerMaisClick = { mediaType, mediaId ->
                    rootNavController.navigate(AppRoutes.mediaBiography(mediaType.name, mediaId))
                },
                onMediaClick = { mediaType, mediaId ->
                    rootNavController.navigate(AppRoutes.mediaDetails(mediaType.name, mediaId)) {
                        launchSingleTop = true
                    }
                },
                onPersonClick = { personId ->
                    rootNavController.navigate(AppRoutes.personDetails(personId))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(AppRoutes.MEDIA_BIOGRAPHY) {
            MediaBiographyScreen(
                onBackClick = { rootNavController.popBackStack() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun MainScreenPreview() {
    MyDearMoviesTheme {
        MainScreen()
    }
}
