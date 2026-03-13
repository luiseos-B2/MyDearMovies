package com.example.mydearmovies.feature.people.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.usecase.GetPopularPeopleUseCase
import com.example.mydearmovies.testutil.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

class PeopleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPopularPeopleUseCase: GetPopularPeopleUseCase = mockk()

    @Test
    fun shouldRequestPagingFlowOnInitialization() = runTest {
        // Given
        every { getPopularPeopleUseCase() } returns flowOf(PagingData.empty())

        // When
        createViewModel()

        // Then
        verify(exactly = 1) { getPopularPeopleUseCase() }
    }

    @Test
    fun shouldKeepPagingFlowLoadingBeforeDelayedEmission() = runTest {
        // Given
        every { getPopularPeopleUseCase() } returns flow {}
        val viewModel = createViewModel()

        // When
        viewModel.pagingData.test {
            // Then
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitSuccessWhenUseCaseReturnsPagingData() = runTest {
        // Given
        every { getPopularPeopleUseCase() } returns flowOf(PagingData.from(emptyList<PersonModel>()))
        val viewModel = createViewModel()

        // When
        viewModel.pagingData.test {
            // Then
            advanceUntilIdle()
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun shouldEmitErrorWhenUseCaseFlowFails() = runTest {
        // Given
        every { getPopularPeopleUseCase() } throws IllegalStateException("erro")

        // When
        val error = assertThrows(IllegalStateException::class.java) {
            createViewModel()
        }

        // Then
        assertEquals("erro", error.message)
    }

    private fun createViewModel(): PeopleViewModel =
        PeopleViewModel(getPopularPeopleUseCase = getPopularPeopleUseCase)
}
