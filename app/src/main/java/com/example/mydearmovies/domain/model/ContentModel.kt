package com.example.mydearmovies.domain.model

data class ContentModel(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val ratingPercentage: Float,
    val imageUrl: String,
    val mediaType: MediaType? = null
)
