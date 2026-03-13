package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.repository.PeopleRepository
import com.example.mydearmovies.domain.result.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke(personId: Int): Flow<DomainResult<PersonDetailModel>> =
        repository.getPersonDetails(personId)
}
