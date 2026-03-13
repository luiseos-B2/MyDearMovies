package com.example.mydearmovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mydearmovies.data.model.toContentModel
import com.example.mydearmovies.data.model.toPersonDetailModel
import com.example.mydearmovies.data.model.toPersonModel
import com.example.mydearmovies.data.remote.PeopleService
import com.example.mydearmovies.data.util.toDomainError
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.repository.PeopleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class PeopleRepositoryImpl @Inject constructor(
    private val service: PeopleService
) : PeopleRepository {

    override fun getPersonDetails(personId: Int): Flow<DomainResult<PersonDetailModel>> = flow {
        emit(
            runCatching {
                val dto = service.getPersonDetails(personId)
                val creditsDto = service.getPersonCombinedCredits(personId)
                val credits = creditsDto.cast
                    ?.map { it.toContentModel() }
                    ?.filter { it.imageUrl.isNotBlank() }
                    ?.take(20)
                    ?: emptyList()
                toPersonDetailModel(dto, credits)
            }.fold(
                onSuccess = { DomainResult.Success(it) },
                onFailure = { DomainResult.Failure(it.toDomainError()) }
            )
        )
    }

    override fun getPopularPeople(): Flow<PagingData<PersonModel>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                PeoplePagingSource { page ->
                    val response = service.getPopularPeople(page)
                    val items = response.results.map { it.toPersonModel() }
                    val nextKey = if (response.page < response.totalPages) response.page + 1 else null
                    items to nextKey
                }
            }
        ).flow
}
