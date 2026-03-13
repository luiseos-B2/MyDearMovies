package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PagedMovieResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieItemDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class MovieItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("release_date") val releaseDate: String?
)
