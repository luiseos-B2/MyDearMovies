package com.example.mydearmovies.roboletric.screen.series

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import com.example.mydearmovies.R
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.usecase.GetGenresUseCase
import com.example.mydearmovies.domain.usecase.GetMediaDiscoverUseCase
import com.example.mydearmovies.domain.usecase.GetWatchProvidersUseCase
import com.example.mydearmovies.feature.media.SeriesScreen
import com.example.mydearmovies.feature.media.SeriesViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import com.example.mydearmovies.roboletric.base.BaseRobolectricTest

class SeriesScreenTest : BaseRobolectricTest() {

    @Test
    fun shouldDisplayScreenWhenLoaded() {
        val viewModel = createViewModel(
            discoverFlow = successPagingFlow(emptyList())
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                SeriesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_title_series)
        ).assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingState() {
        val viewModel = createViewModel(
            discoverFlow = loadingPagingFlow()
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                SeriesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_title_series)
        ).assertIsDisplayed()
        composeRule.onAllNodesWithText("Serie Popular").assertCountEquals(0)
    }

    @Test
    fun shouldShowContentWhenDataIsAvailable() {
        val viewModel = createViewModel(
            discoverFlow = successPagingFlow(listOf(sampleContent("Serie Popular")))
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                SeriesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText("Serie Popular").assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorStateWhenFailureOccurs() {
        val errorMessage = "falha-series"
        val viewModel = createViewModel(
            discoverFlow = errorPagingFlow(errorMessage)
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                SeriesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_error_load, errorMessage)
        ).assertIsDisplayed()
    }

    private fun createViewModel(
        discoverFlow: Flow<PagingData<ContentModel>>
    ): SeriesViewModel {
        val getGenresUseCase: GetGenresUseCase = mockk()
        val getMediaDiscoverUseCase: GetMediaDiscoverUseCase = mockk()
        val getWatchProvidersUseCase: GetWatchProvidersUseCase = mockk()

        every { getGenresUseCase(MediaType.TV) } returns flowOf(emptyList())
        every {
            getMediaDiscoverUseCase(
                MediaType.TV,
                any(),
                any(),
                any(),
                any()
            )
        } returns discoverFlow
        every { getWatchProvidersUseCase(MediaType.TV) } returns flowOf(DomainResult.Success(emptyList()))

        return SeriesViewModel(
            getGenresUseCase = getGenresUseCase,
            getMediaDiscoverUseCase = getMediaDiscoverUseCase,
            getWatchProvidersUseCase = getWatchProvidersUseCase
        )
    }

    private fun sampleContent(title: String) = ContentModel(
        id = 20,
        title = title,
        releaseDate = "2026-03-01",
        ratingPercentage = 82f,
        imageUrl = "/series.jpg",
        mediaType = MediaType.TV
    )
}
