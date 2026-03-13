package com.example.mydearmovies.core.navigation

object AppRoutes {
    const val MAIN = "main"
    const val PERSON_DETAILS = "person_details/{personId}"
    const val PERSON_BIOGRAPHY = "person_biography/{personId}"
    const val MEDIA_DETAILS = "media_details/{mediaType}/{mediaId}"
    const val MEDIA_BIOGRAPHY = "media_biography/{mediaType}/{mediaId}"

    fun mediaDetails(mediaType: String, mediaId: Int) = "media_details/$mediaType/$mediaId"
    fun mediaBiography(mediaType: String, mediaId: Int) = "media_biography/$mediaType/$mediaId"
    fun personDetails(personId: Int) = "person_details/$personId"
    fun personBiography(personId: Int) = "person_biography/$personId"
}
