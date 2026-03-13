package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class TvDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("vote_average") val voteAverage: Float?
)
