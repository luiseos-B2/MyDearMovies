package com.example.mydearmovies.domain.model

sealed class MediaFilter {
    data object Popular : MediaFilter()
    data object NowPlaying : MediaFilter()
    data object Upcoming : MediaFilter()
    data object TopRated : MediaFilter()
    data object AiringToday : MediaFilter()
    data object OnTheAir : MediaFilter()
    data class Genre(val id: Int) : MediaFilter()
}
