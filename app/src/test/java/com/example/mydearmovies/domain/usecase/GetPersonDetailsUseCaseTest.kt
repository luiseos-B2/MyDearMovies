package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.repository.PeopleRepository
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.DomainResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPersonDetailsUseCaseTest {

    private val repository: PeopleRepository = mockk()
    private val useCase = GetPersonDetailsUseCase(repository)

    @Test
    fun `shouldReturnSuccessWhenRepositoryReturnsPersonDetails`() = runTest {
        // Given
        val personDetail = PersonDetailModel(
            id = 1,
            name = "Name",
            biography = "Bio",
            birthday = "1990-01-01",
            placeOfBirth = "Place",
            knownForDepartment = "Acting",
            profileImageUrl = "/profile.jpg",
            knownForCredits = emptyList()
        )
        every { repository.getPersonDetails(1) } returns
            flowOf(DomainResult.Success(personDetail))

        // When
        val result = useCase(1).first()

        // Then
        assertTrue(result is DomainResult.Success)
        assertEquals(personDetail, (result as DomainResult.Success).data)
    }

    @Test
    fun `shouldReturnNetworkErrorWhenRepositoryReturnsNetworkFailure`() = runTest {
        // Given
        every { repository.getPersonDetails(1) } returns
            flowOf(DomainResult.Failure(DomainError.Network))

        // When
        val result = useCase(1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Network, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnNotFoundErrorWhenRepositoryReturnsNotFoundFailure`() = runTest {
        // Given
        every { repository.getPersonDetails(1) } returns
            flowOf(DomainResult.Failure(DomainError.NotFound))

        // When
        val result = useCase(1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.NotFound, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnInvalidParamsErrorWhenRepositoryReturnsInvalidParamsFailure`() = runTest {
        // Given
        every { repository.getPersonDetails(1) } returns
            flowOf(DomainResult.Failure(DomainError.InvalidParams))

        // When
        val result = useCase(1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.InvalidParams, (result as DomainResult.Failure).error)
    }

    @Test
    fun `shouldReturnUnknownErrorWhenRepositoryReturnsUnknownFailure`() = runTest {
        // Given
        every { repository.getPersonDetails(1) } returns
            flowOf(DomainResult.Failure(DomainError.Unknown))

        // When
        val result = useCase(1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Unknown, (result as DomainResult.Failure).error)
    }
}

