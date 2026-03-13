package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.WatchProviderModel
import com.example.mydearmovies.domain.repository.MediaRepository
import com.example.mydearmovies.domain.result.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchProvidersUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(mediaType: MediaType): Flow<DomainResult<List<WatchProviderModel>>> =
        repository.getWatchProviders(mediaType)
}
