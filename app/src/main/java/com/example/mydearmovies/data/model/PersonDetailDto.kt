package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class PersonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("profile_path") val profilePath: String?
)
