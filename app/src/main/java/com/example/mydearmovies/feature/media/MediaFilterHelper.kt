package com.example.mydearmovies.feature.media

import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaFilter
import com.example.mydearmovies.domain.model.MediaType

fun filterForMoviesTab(index: Int, genres: List<GenreModel>): MediaFilter = when (index) {
    0 -> MediaFilter.Popular
    1 -> MediaFilter.NowPlaying
    2 -> MediaFilter.Upcoming
    3 -> MediaFilter.TopRated
    else -> genres.getOrNull(index - 4)?.let { MediaFilter.Genre(it.id) } ?: MediaFilter.Popular
}

fun filterForSeriesTab(index: Int, genres: List<GenreModel>): MediaFilter = when (index) {
    0 -> MediaFilter.Popular
    1 -> MediaFilter.AiringToday
    2 -> MediaFilter.OnTheAir
    3 -> MediaFilter.TopRated
    else -> genres.getOrNull(index - 4)?.let { MediaFilter.Genre(it.id) } ?: MediaFilter.Popular
}

const val MOVIE_CATEGORY_COUNT = 4
const val SERIES_CATEGORY_COUNT = 4
