package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class GenresResponseDto(
    @SerializedName("genres") val genres: List<GenreItemDto>
)

data class GenreItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?
)
