package com.example.mydearmovies.domain.repository

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaFilter
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.domain.model.WatchProviderModel
import com.example.mydearmovies.domain.result.DomainResult
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getGenres(mediaType: MediaType): Flow<List<GenreModel>>

    fun getMediaPaging(
        mediaType: MediaType,
        filter: MediaFilter,
        watchProviderIds: Set<Int> = emptySet(),
        sortBy: SortOption = SortOption.POPULARITY_DESC,
        advancedFilter: AdvancedFilterState? = null
    ): Flow<PagingData<ContentModel>>

    fun getMediaDetails(mediaType: MediaType, mediaId: Int): Flow<DomainResult<MediaDetailModel>>

    fun getWatchProviders(mediaType: MediaType): Flow<DomainResult<List<WatchProviderModel>>>
}
