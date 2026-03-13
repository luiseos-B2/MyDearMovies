package com.example.mydearmovies.domain.usecase

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.repository.HomeRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopularMoviesUseCaseTest {

    private val repository: HomeRepository = mockk()
    private val useCase = GetPopularMoviesUseCase(repository)

    @Test
    fun `shouldReturnPagingDataWhenRepositoryReturnsPopularMovies`() = runTest {
        // Given
        val pagingData: PagingData<ContentModel> = PagingData.empty()
        every { repository.getPopularMovies() } returns flowOf(pagingData)

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is PagingData<ContentModel>)
    }
}

