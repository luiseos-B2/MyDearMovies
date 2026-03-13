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

class PersonDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPersonDetailsUseCase: GetPersonDetailsUseCase = mockk()
    private val errorMessageResProvider: ErrorMessageResProvider = mockk()

    @Test
    fun shouldStartWithLoadingState() = runTest {
        // Given
        every { getPersonDetailsUseCase(4) } returns flow {
            delay(5_000)
            emit(DomainResult.Success(samplePersonDetail()))
        }
        val savedStateHandle = SavedStateHandle(mapOf("personId" to "4"))

        // When
        val viewModel = PersonDetailsViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = savedStateHandle
        )

        // Then
        viewModel.state.test {
            assertEquals(PersonDetailsState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitLoadingStateWhenLoadStarts() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 44
        every { getPersonDetailsUseCase(20) } returns flow {
            delay(1_000)
            emit(DomainResult.Success(samplePersonDetail()))
        }
        val viewModel = PersonDetailsViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Then
            assertTrue(awaitItem() is PersonDetailsState.Error)

            // When
            viewModel.load(20)
            advanceUntilIdle()

            // Then
            assertEquals(PersonDetailsState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessStateWhenUseCaseReturnsData() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 44
        val detail = samplePersonDetail()
        every { getPersonDetailsUseCase(7) } returns flowOf(DomainResult.Success(detail))
        val viewModel = PersonDetailsViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(7)
            advanceUntilIdle()

            // Then
            val success = awaitItem()
            assertTrue(success is PersonDetailsState.Success)
            assertEquals(detail, (success as PersonDetailsState.Success).detail)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorStateWhenUseCaseReturnsFailure() = runTest {
        // Given
        every { errorMessageResProvider.getMessageRes(DomainError.InvalidParams) } returns 44
        every { errorMessageResProvider.getMessageRes(DomainError.NotFound) } returns 404
        every { getPersonDetailsUseCase(99) } returns flowOf(DomainResult.Failure(DomainError.NotFound))
        val viewModel = PersonDetailsViewModel(
            getPersonDetailsUseCase = getPersonDetailsUseCase,
            errorMessageResProvider = errorMessageResProvider,
            savedStateHandle = SavedStateHandle()
        )

        viewModel.state.test {
            // Given
            awaitItem()

            // When
            viewModel.load(99)
            advanceUntilIdle()

            // Then
            val error = awaitItem()
            assertTrue(error is PersonDetailsState.Error)
            assertEquals(404, (error as PersonDetailsState.Error).errorMessageRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun samplePersonDetail(): PersonDetailModel =
        PersonDetailModel(
            id = 2,
            name = "Detalhes Pessoa",
            biography = "Uma biografia",
            birthday = "1985-09-10",
            placeOfBirth = "Rio de Janeiro",
            knownForDepartment = "Directing",
            profileImageUrl = "/person.jpg",
            knownForCredits = listOf(
                ContentModel(
                    id = 50,
                    title = "Serie Famosa",
                    releaseDate = "2020-02-02",
                    ratingPercentage = 82f,
                    imageUrl = "/serie.jpg",
                    mediaType = MediaType.TV
                )
            )
        )
}
