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

class MediaDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMediaDetailsUseCase: GetMediaDetailsUseCase = mockk()
    private val errorMessageResProvider: ErrorMessageResProvider = mockk()

    @Test
    fun shouldStartWithLoadingState() = runTest {
        // Given
        every { getMediaDetailsUseCase(MediaType.TV, 11) } returns flow {
            delay(5_000)
            emit(DomainResult.Success(sampleMediaDetail()))
        }
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "mediaType" to "tv",
                "mediaId" to "11"
            )
        )

        // When
        val viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = savedStateHandle
        )

        // Then
        viewModel.state.test {
            assertEquals(MediaDetailsState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitLoadingStateWhenLoadStarts() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 100
        every { getMediaDetailsUseCase(MediaType.MOVIE, 3) } returns flow {
            delay(1_000)
            emit(DomainResult.Success(sampleMediaDetail()))
        }
        val viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Then
            assertTrue(awaitItem() is MediaDetailsState.Error)

            // When
            viewModel.load(MediaType.MOVIE, 3)
            advanceUntilIdle()

            // Then
            assertEquals(MediaDetailsState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessStateWhenUseCaseReturnsData() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 100
        val detail = sampleMediaDetail()
        every { getMediaDetailsUseCase(MediaType.TV, 7) } returns flowOf(DomainResult.Success(detail))
        val viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(MediaType.TV, 7)
            advanceUntilIdle()

            // Then
            val success = awaitItem()
            assertTrue(success is MediaDetailsState.Success)
            assertEquals(detail, (success as MediaDetailsState.Success).detail)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorStateWhenUseCaseReturnsFailure() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 100
        every { errorMessageResProvider.getMessageRes(DomainError.NotFound) } returns 404
        every { getMediaDetailsUseCase(MediaType.MOVIE, 15) } returns flowOf(DomainResult.Failure(DomainError.NotFound))
        val viewModel = MediaDetailsViewModel(
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(MediaType.MOVIE, 15)
            advanceUntilIdle()

            // Then
            val error = awaitItem()
            assertTrue(error is MediaDetailsState.Error)
            assertEquals(404, (error as MediaDetailsState.Error).errorMessageRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun sampleMediaDetail(): MediaDetailModel =
        MediaDetailModel(
            id = 11,
            mediaType = MediaType.TV,
            title = "Serie X",
            year = "2025",
            overview = "Detalhes",
            backdropUrl = "/backdrop.png",
            rating = 91f,
            runtimeOrSeasons = "3 temporadas",
            certification = null,
            cast = listOf(
                CastMemberModel(
                    id = 1,
                    name = "Atriz",
                    profileImageUrl = "/atriz.png",
                    character = "Personagem"
                )
            ),
            recommendations = listOf(
                ContentModel(
                    id = 22,
                    title = "Outra Serie",
                    releaseDate = "2024-12-31",
                    ratingPercentage = 80f,
                    imageUrl = "/outra.png",
                    mediaType = MediaType.TV
                )
            )
        )
}
