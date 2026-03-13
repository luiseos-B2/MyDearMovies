package com.example.mydearmovies.feature.home.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.domain.usecase.GetInCinemasUseCase
import com.example.mydearmovies.domain.usecase.GetLatestTrailersUseCase
import com.example.mydearmovies.domain.usecase.GetPopularMoviesUseCase
import com.example.mydearmovies.domain.usecase.GetPopularTvShowsUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingTodayUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingWeekUseCase
import com.example.mydearmovies.domain.usecase.GetUpcomingMoviesUseCase
import com.example.mydearmovies.testutil.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase = mockk()
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase = mockk()
    private val getInCinemasUseCase: GetInCinemasUseCase = mockk()
    private val getTrendingTodayUseCase: GetTrendingTodayUseCase = mockk()
    private val getTrendingWeekUseCase: GetTrendingWeekUseCase = mockk()
    private val getLatestTrailersUseCase: GetLatestTrailersUseCase = mockk()

    @Test
    fun shouldExposeInitialTabState() = runTest {
        // Given
        stubDefaultUseCases()

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.selectedTabIndex.test {
            assertEquals(HomeTab.FILMES.index, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.selectedTrendTabIndex.test {
            assertEquals(TrendTab.HOJE.index, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.selectedTrailerTabIndex.test {
            assertEquals(TrailerTab.STREAMING.index, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldKeepPagingFlowInLoadingBeforeDelayedEmission() = runTest {
        // Given
        every { getPopularMoviesUseCase() } returns flow {}
        every { getPopularTvShowsUseCase() } returns flowOf(PagingData.empty())
        every { getUpcomingMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getInCinemasUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingTodayUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingWeekUseCase() } returns flowOf(PagingData.empty())
        every { getLatestTrailersUseCase(any()) } returns flowOf(PagingData.empty<TrailerModel>())
        val viewModel = createViewModel()

        // When
        viewModel.pagingData.test {
            // Then
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessWhenSelectedTabUseCaseReturnsData() = runTest {
        // Given
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getPopularTvShowsUseCase() } returns flowOf(PagingData.from(emptyList<ContentModel>()))
        every { getUpcomingMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getInCinemasUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingTodayUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingWeekUseCase() } returns flowOf(PagingData.empty())
        every { getLatestTrailersUseCase(any()) } returns flowOf(PagingData.empty<TrailerModel>())
        val viewModel = createViewModel()
        viewModel.pagingData.test {
            // When
            awaitItem()
            viewModel.selectTab(HomeTab.NA_TV.index)
            advanceUntilIdle()

            // Then
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        verify(exactly = 1) { getPopularTvShowsUseCase() }
    }

    @Test
    fun shouldEmitErrorWhenSelectedTabUseCaseThrows() = runTest {
        // Given
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getPopularTvShowsUseCase() } returns flowOf(PagingData.empty())
        every { getUpcomingMoviesUseCase() } returns flow {}
        every { getInCinemasUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingTodayUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingWeekUseCase() } returns flowOf(PagingData.empty())
        every { getLatestTrailersUseCase(any()) } returns flowOf(PagingData.empty<TrailerModel>())
        val viewModel = createViewModel()
        viewModel.pagingData.test {
            // When
            awaitItem()
            viewModel.selectTab(HomeTab.PROX_LANCAMENTOS.index)
            advanceUntilIdle()
            // Then
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
        verify(exactly = 1) { getUpcomingMoviesUseCase() }
    }

    private fun createViewModel(): HomeViewModel =
        HomeViewModel(
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            getInCinemasUseCase = getInCinemasUseCase,
            getTrendingTodayUseCase = getTrendingTodayUseCase,
            getTrendingWeekUseCase = getTrendingWeekUseCase,
            getLatestTrailersUseCase = getLatestTrailersUseCase
        )

    private fun stubDefaultUseCases() {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getPopularTvShowsUseCase() } returns flowOf(PagingData.empty())
        every { getUpcomingMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getInCinemasUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingTodayUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingWeekUseCase() } returns flowOf(PagingData.empty())
        every { getLatestTrailersUseCase(any()) } returns flowOf(PagingData.empty<TrailerModel>())
    }
}
