package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularTvShowsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<PagingData<ContentModel>> =
        repository.getPopularTvShows()
}
