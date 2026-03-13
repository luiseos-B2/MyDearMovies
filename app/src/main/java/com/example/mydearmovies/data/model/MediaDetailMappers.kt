package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.CastMemberModel
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun CastMemberDto.toCastMemberModel(): CastMemberModel = CastMemberModel(
    id = id,
    name = name.orEmpty(),
    profileImageUrl = profilePath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    character = character.orEmpty()
)

fun MovieDetailDto.toMediaDetailModel(
    cast: List<CastMemberModel>,
    recommendations: List<ContentModel>
): MediaDetailModel = MediaDetailModel(
    id = id,
    mediaType = MediaType.MOVIE,
    title = title.orEmpty(),
    year = releaseDate?.take(4).orEmpty(),
    overview = overview.orEmpty(),
    backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    rating = voteAverage ?: 0f,
    runtimeOrSeasons = runtime?.let { "${it} min" }.orEmpty(),
    certification = null,
    cast = cast,
    recommendations = recommendations
)

fun TvDetailDto.toMediaDetailModel(
    cast: List<CastMemberModel>,
    recommendations: List<ContentModel>
): MediaDetailModel = MediaDetailModel(
    id = id,
    mediaType = MediaType.TV,
    title = name.orEmpty(),
    year = firstAirDate?.take(4).orEmpty(),
    overview = overview.orEmpty(),
    backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    rating = voteAverage ?: 0f,
    runtimeOrSeasons = numberOfSeasons?.let { "$it temporada(s)" }.orEmpty(),
    certification = null,
    cast = cast,
    recommendations = recommendations
)
