package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PersonCombinedCreditsDto(
    @SerializedName("cast") val cast: List<PersonCastItemDto>?
)

data class PersonCastItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("media_type") val mediaType: String?
)
