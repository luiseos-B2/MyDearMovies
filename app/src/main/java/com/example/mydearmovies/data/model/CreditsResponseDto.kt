package com.example.mydearmovies.data.model

import com.google.gson.annotations.SerializedName

data class CreditsResponseDto(
    @SerializedName("cast") val cast: List<CastMemberDto>?
)

data class CastMemberDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("character") val character: String?
)
