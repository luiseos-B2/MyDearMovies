package com.example.mydearmovies.roboletric.screen.people

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import com.example.mydearmovies.R
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.usecase.GetPopularPeopleUseCase
import com.example.mydearmovies.feature.people.ui.PeopleScreen
import com.example.mydearmovies.feature.people.viewmodel.PeopleViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import com.example.mydearmovies.roboletric.base.BaseRobolectricTest
import kotlinx.coroutines.flow.Flow

class PeopleScreenTest : BaseRobolectricTest() {

    @Test
    fun shouldDisplayScreenWhenLoaded() {
        val viewModel = createViewModel(
            peopleFlow = successPagingFlow(emptyList())
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                PeopleScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.people_title)
        ).assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingState() {
        val viewModel = createViewModel(
            peopleFlow = loadingPagingFlow()
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                PeopleScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.people_title)
        ).assertIsDisplayed()
        composeRule.onAllNodesWithText("Pessoa Famosa").assertCountEquals(0)
    }

    @Test
    fun shouldShowContentWhenDataIsAvailable() {
        val viewModel = createViewModel(
            peopleFlow = successPagingFlow(listOf(samplePerson("Pessoa Famosa")))
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                PeopleScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText("Pessoa Famosa").assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorStateWhenFailureOccurs() {
        val errorMessage = "falha-people"
        val viewModel = createViewModel(
            peopleFlow = errorPagingFlow(errorMessage)
        )

        composeRule.setContent {
            MyDearMoviesTheme {
                PeopleScreen(viewModel = viewModel)
            }
        }

        composeRule.onNodeWithText(
            RuntimeEnvironment.getApplication().getString(R.string.people_error_load, errorMessage)
        ).assertIsDisplayed()
    }

    private fun createViewModel(
        peopleFlow: Flow<PagingData<PersonModel>>
    ): PeopleViewModel {
        val getPopularPeopleUseCase: GetPopularPeopleUseCase = mockk()
        every { getPopularPeopleUseCase() } returns peopleFlow
        return PeopleViewModel(getPopularPeopleUseCase = getPopularPeopleUseCase)
    }

    private fun samplePerson(name: String) = PersonModel(
        id = 1,
        name = name,
        profileImageUrl = "/person.jpg",
        knownFor = "Atuacao"
    )
}
