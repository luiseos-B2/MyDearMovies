package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaType

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val VOTE_MAX = 10f

fun MovieItemDto.toContentModel(): ContentModel = ContentModel(
    id = id,
    title = title.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    ratingPercentage = (voteAverage ?: 0f) / VOTE_MAX,
    imageUrl = posterPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    mediaType = MediaType.MOVIE
)

fun TvItemDto.toContentModel(): ContentModel = ContentModel(
    id = id,
    title = name.orEmpty(),
    releaseDate = firstAirDate.orEmpty(),
    ratingPercentage = (voteAverage ?: 0f) / VOTE_MAX,
    imageUrl = posterPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    mediaType = MediaType.TV
)

fun TrendingItemDto.toContentModel(): ContentModel = ContentModel(
    id = id,
    title = title ?: name.orEmpty(),
    releaseDate = releaseDate ?: firstAirDate.orEmpty(),
    ratingPercentage = (voteAverage ?: 0f) / VOTE_MAX,
    imageUrl = posterPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    mediaType = mediaType.parseMediaType()
)

private fun String?.parseMediaType(): MediaType? = when (this?.lowercase()) {
    "movie" -> MediaType.MOVIE
    "tv" -> MediaType.TV
    else -> null
}

fun GenreItemDto.toGenreModel(): GenreModel = GenreModel(
    id = id,
    name = name.orEmpty()
)
