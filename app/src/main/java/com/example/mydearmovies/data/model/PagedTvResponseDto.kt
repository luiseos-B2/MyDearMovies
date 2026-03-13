package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PagedTvResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TvItemDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TvItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("first_air_date") val firstAirDate: String?
)
