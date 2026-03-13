package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaFilter
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaDiscoverUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(
        mediaType: MediaType,
        filter: MediaFilter,
        watchProviderIds: Set<Int> = emptySet(),
        sortBy: SortOption = SortOption.POPULARITY_DESC,
        advancedFilter: AdvancedFilterState? = null
    ): Flow<PagingData<ContentModel>> =
        repository.getMediaPaging(mediaType, filter, watchProviderIds, sortBy, advancedFilter)
}
