package com.example.mydearmovies.roboletric.screen.movies

import androidx.paging.PagingData
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.mydearmovies.R
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.usecase.GetGenresUseCase
import com.example.mydearmovies.domain.usecase.GetMediaDiscoverUseCase
import com.example.mydearmovies.domain.usecase.GetWatchProvidersUseCase
import com.example.mydearmovies.feature.media.MoviesScreen
import com.example.mydearmovies.feature.media.MoviesViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import com.example.mydearmovies.roboletric.base.BaseRobolectricTest

class MoviesScreenTest : BaseRobolectricTest() {

    @Test
    fun shouldDisplayScreenWhenLoaded() {
        val viewModel = createViewModel(
            discoverFlow = successPagingFlow(emptyList())
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                MoviesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_title_movies)
        ).assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingState() {
        val viewModel = createViewModel(
            discoverFlow = loadingPagingFlow()
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                MoviesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_title_movies)
        ).assertIsDisplayed()
        composeRule.onAllNodesWithText("Filme Popular").assertCountEquals(0)
    }

    @Test
    fun shouldShowContentWhenDataIsAvailable() {
        val viewModel = createViewModel(
            discoverFlow = successPagingFlow(listOf(sampleContent("Filme Popular")))
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                MoviesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText("Filme Popular").assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorStateWhenFailureOccurs() {
        val errorMessage = "falha-movies"
        val viewModel = createViewModel(
            discoverFlow = errorPagingFlow(errorMessage)
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                MoviesScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.media_error_load, errorMessage)
        ).assertIsDisplayed()
    }

    private fun createViewModel(
        discoverFlow: Flow<PagingData<ContentModel>>
    ): MoviesViewModel {
        val getGenresUseCase: GetGenresUseCase = mockk()
        val getMediaDiscoverUseCase: GetMediaDiscoverUseCase = mockk()
        val getWatchProvidersUseCase: GetWatchProvidersUseCase = mockk()

        every { getGenresUseCase(MediaType.MOVIE) } returns flowOf(emptyList())
        every {
            getMediaDiscoverUseCase(
                MediaType.MOVIE,
                any(),
                any(),
                any(),
                any()
            )
        } returns discoverFlow
        every { getWatchProvidersUseCase(MediaType.MOVIE) } returns flowOf(DomainResult.Success(emptyList()))

        return MoviesViewModel(
            getGenresUseCase = getGenresUseCase,
            getMediaDiscoverUseCase = getMediaDiscoverUseCase,
            getWatchProvidersUseCase = getWatchProvidersUseCase
        )
    }

    private fun sampleContent(title: String) = ContentModel(
        id = 10,
        title = title,
        releaseDate = "2026-05-01",
        ratingPercentage = 90f,
        imageUrl = "/movie.jpg",
        mediaType = MediaType.MOVIE
    )
}
