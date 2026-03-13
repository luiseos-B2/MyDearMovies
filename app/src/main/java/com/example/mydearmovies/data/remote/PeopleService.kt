package com.example.mydearmovies.data.remote

import com.example.mydearmovies.data.model.PagedPeopleResponseDto
import com.example.mydearmovies.data.model.PersonCombinedCreditsDto
import com.example.mydearmovies.data.model.PersonDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("page") page: Int
    ): PagedPeopleResponseDto

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int
    ): PersonDetailDto

    @GET("person/{person_id}/combined_credits")
    suspend fun getPersonCombinedCredits(
        @Path("person_id") personId: Int
    ): PersonCombinedCreditsDto
}
