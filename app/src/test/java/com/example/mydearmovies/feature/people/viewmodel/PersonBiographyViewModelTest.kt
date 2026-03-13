package com.example.mydearmovies.feature.people.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.mydearmovies.core.error.ErrorMessageResProvider
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.usecase.GetPersonDetailsUseCase
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

class PersonBiographyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPersonDetailsUseCase: GetPersonDetailsUseCase = mockk()
    private val errorMessageResProvider: ErrorMessageResProvider = mockk()

    @Test
    fun shouldStartWithLoadingState() = runTest {
        // Given
        every { getPersonDetailsUseCase(9) } returns flow {
            delay(5_000)
            emit(DomainResult.Success(samplePersonDetail()))
        }
        val savedStateHandle = SavedStateHandle(mapOf("personId" to "9"))

        // When
        val viewModel = PersonBiographyViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = savedStateHandle
        )

        // Then
        viewModel.state.test {
            assertEquals(PersonBiographyState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitLoadingStateWhenLoadStarts() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 11
        every { getPersonDetailsUseCase(55) } returns flow {
            delay(1_000)
            emit(DomainResult.Success(samplePersonDetail()))
        }
        val viewModel = PersonBiographyViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Then
            assertTrue(awaitItem() is PersonBiographyState.Error)

            // When
            viewModel.load(55)
            advanceUntilIdle()

            // Then
            assertEquals(PersonBiographyState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessStateWhenUseCaseReturnsData() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 11
        val detail = samplePersonDetail()
        every { getPersonDetailsUseCase(1) } returns flowOf(DomainResult.Success(detail))
        val viewModel = PersonBiographyViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(1)
            advanceUntilIdle()

            // Then
            val success = awaitItem()
            assertTrue(success is PersonBiographyState.Success)
            assertEquals(detail.name, (success as PersonBiographyState.Success).name)
            assertEquals(detail.biography, success.biography)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorStateWhenUseCaseReturnsFailure() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 11
        every { errorMessageResProvider.getMessageRes(DomainError.Unknown) } returns 99
        every { getPersonDetailsUseCase(3) } returns flowOf(DomainResult.Failure(DomainError.Unknown))
        val viewModel = PersonBiographyViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(3)
            advanceUntilIdle()

            // Then
            val error = awaitItem()
            assertTrue(error is PersonBiographyState.Error)
            assertEquals(99, (error as PersonBiographyState.Error).errorMessageRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun samplePersonDetail(): PersonDetailModel =
        PersonDetailModel(
            id = 1,
            name = "Pessoa",
            biography = "Biografia completa",
            birthday = "1990-01-01",
            placeOfBirth = "Sao Paulo",
            knownForDepartment = "Acting",
            profileImageUrl = "/profile.jpg",
            knownForCredits = listOf(
                ContentModel(
                    id = 13,
                    title = "Filme",
                    releaseDate = "2022-05-01",
                    ratingPercentage = 70f,
                    imageUrl = "/filme.jpg",
                    mediaType = MediaType.MOVIE
                )
            )
        )
}
