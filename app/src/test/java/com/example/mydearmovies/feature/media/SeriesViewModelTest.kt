package com.example.mydearmovies.feature.media

import app.cash.turbine.test
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.WatchProviderModel
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.usecase.GetGenresUseCase
import com.example.mydearmovies.domain.usecase.GetMediaDiscoverUseCase
import com.example.mydearmovies.domain.usecase.GetWatchProvidersUseCase
import com.example.mydearmovies.testutil.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SeriesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getMediaDiscoverUseCase: GetMediaDiscoverUseCase = mockk()
    private val getWatchProvidersUseCase: GetWatchProvidersUseCase = mockk()

    @Test
    fun shouldExposeInitialState() = runTest {
        // Given
        every { getGenresUseCase(MediaType.TV) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // When
        Unit

        // Then
        viewModel.selectedTabIndex.test {
            assertEquals(0, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.watchProvidersLoading.test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.watchProviders.test {
            val state = awaitItem()
            assertTrue(state is DomainResult.Success)
            assertTrue((state as DomainResult.Success).data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitLoadingWhenLoadingWatchProvidersStarts() = runTest {
        // Given
        every { getGenresUseCase(MediaType.TV) } returns flowOf(emptyList())
        every { getWatchProvidersUseCase(MediaType.TV) } returns flow {}
        val viewModel = createViewModel()

        viewModel.watchProvidersLoading.test {
            // Then
            assertFalse(awaitItem())
            // When
            viewModel.loadWatchProviders()
            advanceUntilIdle()
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessWhenWatchProvidersUseCaseReturnsData() = runTest {
        // Given
        every { getGenresUseCase(MediaType.TV) } returns flowOf(emptyList())
        val providers = listOf(
            WatchProviderModel(
                id = 21,
                name = "Prime Video",
                logoUrl = "/prime.png"
            )
        )
        every { getWatchProvidersUseCase(MediaType.TV) } returns flowOf(DomainResult.Success(providers))
        val viewModel = createViewModel()

        viewModel.watchProviders.test {
            // Given
            val initial = awaitItem()

            // When
            viewModel.loadWatchProviders()
            advanceUntilIdle()

            // Then
            val success = awaitItem()
            assertTrue(initial is DomainResult.Success)
            assertTrue(success is DomainResult.Success)
            assertEquals(providers, (success as DomainResult.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorWhenWatchProvidersUseCaseReturnsFailure() = runTest {
        // Given
        every { getGenresUseCase(MediaType.TV) } returns flowOf(emptyList())
        every { getWatchProvidersUseCase(MediaType.TV) } returns flowOf(DomainResult.Failure(DomainError.Unknown))
        val viewModel = createViewModel()

        viewModel.watchProviders.test {
            // Given
            awaitItem()

            // When
            viewModel.loadWatchProviders()
            advanceUntilIdle()

            // Then
            val error = awaitItem()
            assertTrue(error is DomainResult.Failure)
            assertEquals(DomainError.Unknown, (error as DomainResult.Failure).error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(): SeriesViewModel =
        SeriesViewModel(
            getGenresUseCase = getGenresUseCase,
            getMediaDiscoverUseCase = getMediaDiscoverUseCase,
            getWatchProvidersUseCase = getWatchProvidersUseCase
        )
}
