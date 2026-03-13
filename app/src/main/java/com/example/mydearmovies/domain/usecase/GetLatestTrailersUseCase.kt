package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestTrailersUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(category: Int): Flow<PagingData<TrailerModel>> =
        repository.getLatestTrailers(category)
}
