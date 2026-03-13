package com.example.mydearmovies.domain.repository

import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.result.DomainResult
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData

interface PeopleRepository {

    fun getPopularPeople(): Flow<PagingData<PersonModel>>

    fun getPersonDetails(personId: Int): Flow<DomainResult<PersonDetailModel>>
}
