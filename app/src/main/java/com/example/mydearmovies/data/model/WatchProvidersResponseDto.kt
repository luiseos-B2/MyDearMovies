package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class WatchProvidersResponseDto(
    @SerializedName("results") val results: List<WatchProviderItemDto>
)

data class WatchProviderItemDto(
    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("provider_name") val providerName: String?
)
