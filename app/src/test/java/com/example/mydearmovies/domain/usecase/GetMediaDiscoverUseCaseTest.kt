package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaFilter
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.domain.repository.MediaRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMediaDiscoverUseCaseTest {

    private val repository: MediaRepository = mockk()
    private val useCase = GetMediaDiscoverUseCase(repository)

    @Test
    fun `shouldDelegateToRepositoryWithCorrectParameters`() = runTest {
        // Given
        val pagingData: PagingData<ContentModel> = PagingData.empty()
        val filter: MediaFilter = MediaFilter.Popular
        val watchProviderIds = setOf(1, 2)
        val sortBy = SortOption.RATING_DESC
        val advancedFilter = AdvancedFilterState()
        every {
            repository.getMediaPaging(
                mediaType = MediaType.MOVIE,
                filter = filter,
                watchProviderIds = watchProviderIds,
                sortBy = sortBy,
                advancedFilter = advancedFilter
            )
        } returns flowOf(pagingData)

        // When
        val result = useCase(
            mediaType = MediaType.MOVIE,
            filter = filter,
            watchProviderIds = watchProviderIds,
            sortBy = sortBy,
            advancedFilter = advancedFilter
        ).first()

        // Then
        assertTrue(result is PagingData<ContentModel>)
    }
}

