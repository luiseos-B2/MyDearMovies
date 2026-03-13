package com.example.mydearmovies.domain.model

data class MediaDetailModel(
    val id: Int,
    val mediaType: MediaType,
    val title: String,
    val year: String,
    val overview: String,
    val backdropUrl: String,
    val rating: Float,
    val runtimeOrSeasons: String,
    val certification: String?,
    val cast: List<CastMemberModel>,
    val recommendations: List<ContentModel>
)
