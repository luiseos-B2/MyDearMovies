package com.example.mydearmovies.data.repository

import com.example.mydearmovies.data.model.PagedPeopleResponseDto
import com.example.mydearmovies.data.model.PersonCombinedCreditsDto
import com.example.mydearmovies.data.model.PersonDetailDto
import com.example.mydearmovies.data.model.PersonItemDto
import com.example.mydearmovies.data.model.PersonCastItemDto
import com.example.mydearmovies.data.remote.PeopleService
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PeopleRepositoryImplTest {

    private val service: PeopleService = mockk()
    private val repository = PeopleRepositoryImpl(service)

    @Test
    fun `shouldReturnSuccessWhenGetPersonDetailsSucceeds`() = runTest {
        // Given
        val detailDto = PersonDetailDto(
            id = 1,
            name = "Name",
            biography = "Bio",
            birthday = "1990-01-01",
            placeOfBirth = "Place",
            knownForDepartment = "Acting",
            profilePath = "/profile.jpg"
        )
        val creditsDto = PersonCombinedCreditsDto(
            cast = listOf(
                PersonCastItemDto(
                    id = 2,
                    title = "Movie",
                    name = null,
                    posterPath = "/poster.jpg",
                    releaseDate = "2020-01-01",
                    firstAirDate = null,
                    voteAverage = 7.0f,
                    mediaType = "movie"
                )
            )
        )
        coEvery { service.getPersonDetails(1) } returns detailDto
        coEvery { service.getPersonCombinedCredits(1) } returns creditsDto

        // When
        val result = repository.getPersonDetails(1).first()

        // Then
        assertTrue(result is DomainResult.Success)
        val data = (result as DomainResult.Success).data
        assertEquals(1, data.id)
        assertEquals("Name", data.name)
        assertEquals(1, data.knownForCredits.size)
    }

    @Test
    fun `shouldReturnFailureWithNetworkErrorWhenGetPersonDetailsThrowsIOException`() = runTest {
        // Given
        coEvery { service.getPersonDetails(1) } throws IOException()

        // When
        val result = repository.getPersonDetails(1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Network, (result as DomainResult.Failure).error)
    }
}

