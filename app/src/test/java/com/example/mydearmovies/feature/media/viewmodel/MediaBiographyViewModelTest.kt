package com.example.mydearmovies.feature.media.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.mydearmovies.core.error.ErrorMessageResProvider
import com.example.mydearmovies.domain.model.CastMemberModel
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.usecase.GetMediaDetailsUseCase
import com.example.mydearmovies.testutil.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MediaBiographyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMediaDetailsUseCase: GetMediaDetailsUseCase = mockk()
    private val errorMessageResProvider: ErrorMessageResProvider = mockk()

    @Test
    fun shouldStartWithLoadingState() = runTest {
        // Given
        every { getMediaDetailsUseCase(MediaType.MOVIE, 1) } returns flow {
            delay(5_000)
            emit(DomainResult.Success(sampleMediaDetail()))
        }
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "mediaType" to "movie",
                "mediaId" to "1"
            )
        )

        // When
        val viewModel = MediaBiographyViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = savedStateHandle
        )

        // Then
        viewModel.state.test {
            assertEquals(MediaBiographyState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitLoadingStateWhenLoadStarts() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 10
        every { getMediaDetailsUseCase(MediaType.TV, 77) } returns flow {
            delay(1_000)
            emit(DomainResult.Success(sampleMediaDetail()))
        }
        val viewModel = MediaBiographyViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Then
            assertTrue(awaitItem() is MediaBiographyState.Error)

            // When
            viewModel.load(MediaType.TV, 77)
            advanceUntilIdle()

            // Then
            assertEquals(MediaBiographyState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessStateWhenUseCaseReturnsData() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 10
        val detail = sampleMediaDetail()
        every { getMediaDetailsUseCase(MediaType.MOVIE, 1) } returns flowOf(DomainResult.Success(detail))
        val viewModel = MediaBiographyViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(MediaType.MOVIE, 1)
            advanceUntilIdle()

            // Then
            val success = awaitItem()
            assertTrue(success is MediaBiographyState.Success)
            assertEquals(detail.title, (success as MediaBiographyState.Success).title)
            assertEquals(detail.overview, success.overview)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorStateWhenUseCaseReturnsFailure() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 10
        every { errorMessageResProvider.getMessageRes(DomainError.Network) } returns 20
        every { getMediaDetailsUseCase(MediaType.TV, 99) } returns flowOf(DomainResult.Failure(DomainError.Network))
        val viewModel = MediaBiographyViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(MediaType.TV, 99)
            advanceUntilIdle()

            // Then
            val error = awaitItem()
            assertTrue(error is MediaBiographyState.Error)
            assertEquals(20, (error as MediaBiographyState.Error).errorMessageRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun sampleMediaDetail(): MediaDetailModel =
        MediaDetailModel(
            id = 1,
            mediaType = MediaType.MOVIE,
            title = "Titulo",
            year = "2026",
            overview = "Overview",
            backdropUrl = "/image.jpg",
            rating = 88.5f,
            runtimeOrSeasons = "120 min",
            certification = "16",
            cast = listOf(
                CastMemberModel(
                    id = 10,
                    name = "Actor",
                    profileImageUrl = "/actor.jpg",
                    character = "Heroi"
                )
            ),
            recommendations = listOf(
                ContentModel(
                    id = 2,
                    title = "Recomendado",
                    releaseDate = "2026-01-01",
                    ratingPercentage = 77f,
                    imageUrl = "/rec.jpg",
                    mediaType = MediaType.MOVIE
                )
            )
        )
}
