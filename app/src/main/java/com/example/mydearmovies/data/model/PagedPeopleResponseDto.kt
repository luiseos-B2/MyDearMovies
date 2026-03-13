package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PagedPeopleResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<PersonItemDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class PersonItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for") val knownFor: List<KnownForItemDto>?
)

/** Item de known_for: filme tem "title", série tem "name". */
data class KnownForItemDto(
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?
)
