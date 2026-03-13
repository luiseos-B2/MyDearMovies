package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.WatchProviderModel
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
class GetWatchProvidersUseCaseTest {

    private val repository: MediaRepository = mockk()
    private val useCase = GetWatchProvidersUseCase(repository)

    @Test
    fun `shouldReturnSuccessWhenRepositoryReturnsWatchProviders`() = runTest {
        // Given
        val providers = listOf(
            WatchProviderModel(
                id = 1,
                name = "Provider",
                logoUrl = "/logo.jpg"
            )
        )
        every { repository.getWatchProviders(MediaType.MOVIE) } returns
            flowOf(DomainResult.Success(providers))

        // When
        val result = useCase(MediaType.MOVIE).first()

        // Then
        assertTrue(result is DomainResult.Success)
        assertEquals(providers, (result as DomainResult.Success).data)
    }

    @Test
    fun `shouldReturnNetworkErrorWhenRepositoryReturnsNetworkFailure`() = runTest {
        // Given
        every { repository.getWatchProviders(MediaType.MOVIE) } returns
            flowOf(DomainResult.Failure(DomainError.Network))

        // When
        val result = useCase(MediaType.MOVIE).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Network, (result as DomainResult.Failure).error)
    }
}

