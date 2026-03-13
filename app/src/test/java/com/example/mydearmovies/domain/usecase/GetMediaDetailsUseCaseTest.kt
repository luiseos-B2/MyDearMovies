package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.repository.MediaRepository
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMediaDetailsUseCaseTest {

    private val repository: MediaRepository = mockk()
    private val useCase = GetMediaDetailsUseCase(repository)

    @Test
    fun `shouldReturnSuccessWhenRepositoryReturnsMediaDetails`() = runTest {
        // Given
        val detail = MediaDetailModel(
            id = 1,
            mediaType = MediaType.MOVIE,
            title = "Movie",
            year = "2024",
            overview = "Overview",
            backdropUrl = "/back.jpg",
            rating = 7.5f,
            runtimeOrSeasons = "120 min",
            certification = null,
            cast = emptyList(),
            recommendations = emptyList()
        )
        every { repository.getMediaDetails(MediaType.MOVIE, 1) } returns
            flowOf(DomainResult.Success(detail))

        // When
        val result = useCase(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Success)
        assertEquals(detail, (result as DomainResult.Success).data)
    }

    @Test
    fun `shouldReturnNetworkErrorWhenRepositoryReturnsNetworkFailure`() = runTest {
        // Given
        every { repository.getMediaDetails(MediaType.MOVIE, 1) } returns
            flowOf(DomainResult.Failure(DomainError.Network))

        // When
        val result = useCase(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Network, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnNotFoundErrorWhenRepositoryReturnsNotFoundFailure`() = runTest {
        // Given
        every { repository.getMediaDetails(MediaType.MOVIE, 1) } returns
            flowOf(DomainResult.Failure(DomainError.NotFound))

        // When
        val result = useCase(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.NotFound, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnInvalidParamsErrorWhenRepositoryReturnsInvalidParamsFailure`() = runTest {
        // Given
        every { repository.getMediaDetails(MediaType.MOVIE, 1) } returns
            flowOf(DomainResult.Failure(DomainError.InvalidParams))

        // When
        val result = useCase(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.InvalidParams, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnUnknownErrorWhenRepositoryReturnsUnknownFailure`() = runTest {
        // Given
        every { repository.getMediaDetails(MediaType.MOVIE, 1) } returns
            flowOf(DomainResult.Failure(DomainError.Unknown))

        // When
        val result = useCase(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Unknown, (result as DomainResult.Failure).error)
    }
}

