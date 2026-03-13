package com.example.mydearmovies.domain.model

data class PersonDetailModel(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String,
    val placeOfBirth: String,
    val knownForDepartment: String,
    val profileImageUrl: String,
    val knownForCredits: List<ContentModel>
)
