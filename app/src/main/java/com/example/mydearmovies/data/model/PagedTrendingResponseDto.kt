package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PagedTrendingResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TrendingItemDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TrendingItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?
)
