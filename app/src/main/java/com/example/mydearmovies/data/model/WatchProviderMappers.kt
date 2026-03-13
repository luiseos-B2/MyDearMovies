package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.WatchProviderModel

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w92"

fun WatchProviderItemDto.toWatchProviderModel(): WatchProviderModel = WatchProviderModel(
    id = providerId,
    name = providerName.orEmpty(),
    logoUrl = logoPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty()
)
