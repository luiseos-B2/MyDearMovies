package com.example.mydearmovies.roboletric.screen.home

import androidx.paging.PagingData
import com.example.mydearmovies.R
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.domain.usecase.GetInCinemasUseCase
import com.example.mydearmovies.domain.usecase.GetLatestTrailersUseCase
import com.example.mydearmovies.domain.usecase.GetPopularMoviesUseCase
import com.example.mydearmovies.domain.usecase.GetPopularTvShowsUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingTodayUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingWeekUseCase
import com.example.mydearmovies.domain.usecase.GetUpcomingMoviesUseCase
import com.example.mydearmovies.feature.home.ui.HomeScreen
import com.example.mydearmovies.feature.home.viewmodel.HomeViewModel
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import com.example.mydearmovies.roboletric.base.BaseRobolectricTest
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.flow.Flow

class HomeScreenTest : BaseRobolectricTest() {

    @Test
    fun shouldDisplayScreenWhenLoaded() {
        val viewModel = createViewModel(
            popularFlow = successPagingFlow(listOf(sampleContent(title = "Home Card"))),
            tvFlow = successPagingFlow(emptyList()),
            upcomingFlow = successPagingFlow(emptyList()),
            cinemasFlow = successPagingFlow(emptyList()),
            trendsTodayFlow = successPagingFlow(emptyList()),
            trendsWeekFlow = successPagingFlow(emptyList()),
            trailersFlow = successTrailerFlow(emptyList())
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                HomeScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.home_section_popular)
        ).assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingState() {
        val viewModel = createViewModel(
            popularFlow = loadingPagingFlow(),
            tvFlow = loadingPagingFlow(),
            upcomingFlow = loadingPagingFlow(),
            cinemasFlow = loadingPagingFlow(),
            trendsTodayFlow = loadingPagingFlow(),
            trendsWeekFlow = loadingPagingFlow(),
            trailersFlow = loadingTrailerFlow()
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                HomeScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.home_section_popular)
        ).assertIsDisplayed()
        composeRule.onAllNodesWithText("Home Card").assertCountEquals(0)
    }

    @Test
    fun shouldShowContentWhenDataIsAvailable() {
        val viewModel = createViewModel(
            popularFlow = successPagingFlow(listOf(sampleContent(title = "Filme em Destaque"))),
            tvFlow = successPagingFlow(emptyList()),
            upcomingFlow = successPagingFlow(emptyList()),
            cinemasFlow = successPagingFlow(emptyList()),
            trendsTodayFlow = successPagingFlow(emptyList()),
            trendsWeekFlow = successPagingFlow(emptyList()),
            trailersFlow = successTrailerFlow(listOf(sampleTrailer(title = "Trailer em Alta")))
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                HomeScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText("Filme em Destaque").assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorStateWhenFailureOccurs() {
        val errorMessage = "falha-home"
        val viewModel = createViewModel(
            popularFlow = errorPagingFlow(errorMessage),
            tvFlow = errorPagingFlow(errorMessage),
            upcomingFlow = errorPagingFlow(errorMessage),
            cinemasFlow = errorPagingFlow(errorMessage),
            trendsTodayFlow = errorPagingFlow(errorMessage),
            trendsWeekFlow = errorPagingFlow(errorMessage),
            trailersFlow = errorTrailerFlow(errorMessage)
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                HomeScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.home_error_load, errorMessage)
        ).assertIsDisplayed()
    }

    private fun createViewModel(
        popularFlow: Flow<PagingData<ContentModel>>,
        tvFlow: Flow<PagingData<ContentModel>>,
        upcomingFlow: Flow<PagingData<ContentModel>>,
        cinemasFlow: Flow<PagingData<ContentModel>>,
        trendsTodayFlow: Flow<PagingData<ContentModel>>,
        trendsWeekFlow: Flow<PagingData<ContentModel>>,
        trailersFlow: Flow<PagingData<TrailerModel>>
    ): HomeViewModel {
        val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
        val getPopularTvShowsUseCase: GetPopularTvShowsUseCase = mockk()
        val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase = mockk()
        val getInCinemasUseCase: GetInCinemasUseCase = mockk()
        val getTrendingTodayUseCase: GetTrendingTodayUseCase = mockk()
        val getTrendingWeekUseCase: GetTrendingWeekUseCase = mockk()
        val getLatestTrailersUseCase: GetLatestTrailersUseCase = mockk()

        every { getPopularMoviesUseCase() } returns popularFlow
        every { getPopularTvShowsUseCase() } returns tvFlow
        every { getUpcomingMoviesUseCase() } returns upcomingFlow
        every { getInCinemasUseCase() } returns cinemasFlow
        every { getTrendingTodayUseCase() } returns trendsTodayFlow
        every { getTrendingWeekUseCase() } returns trendsWeekFlow
        every { getLatestTrailersUseCase(any()) } returns trailersFlow

        return HomeViewModel(
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            getInCinemasUseCase = getInCinemasUseCase,
            getTrendingTodayUseCase = getTrendingTodayUseCase,
            getTrendingWeekUseCase = getTrendingWeekUseCase,
            getLatestTrailersUseCase = getLatestTrailersUseCase
        )
    }

    private fun successTrailerFlow(items: List<TrailerModel>) =
        flowOf(PagingData.from(items))

    private fun loadingTrailerFlow() = loadingPagingFlow<TrailerModel>()

    private fun errorTrailerFlow(message: String) = errorPagingFlow<TrailerModel>(message)

    private fun sampleContent(title: String) = ContentModel(
        id = 1,
        title = title,
        releaseDate = "2026-01-01",
        ratingPercentage = 85f,
        imageUrl = "/poster.jpg",
        mediaType = MediaType.MOVIE
    )

    private fun sampleTrailer(title: String) = TrailerModel(
        id = 1,
        title = title,
        subtitle = "Oficial",
        backdropUrl = "/backdrop.jpg",
        videoKey = "abc123"
    )
}
