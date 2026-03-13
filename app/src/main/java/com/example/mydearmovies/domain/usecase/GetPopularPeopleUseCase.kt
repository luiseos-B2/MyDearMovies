package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.repository.PeopleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularPeopleUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke(): Flow<PagingData<PersonModel>> =
        repository.getPopularPeople()
}
