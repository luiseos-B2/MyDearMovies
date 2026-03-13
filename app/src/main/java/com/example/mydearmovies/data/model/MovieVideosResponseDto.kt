package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName
data class MovieVideosResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoResultDto>
)

data class VideoResultDto(
    @SerializedName("key") val key: String?,
    @SerializedName("site") val site: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("name") val name: String?
)
