package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.repository.MediaRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetGenresUseCaseTest {

    private val repository: MediaRepository = mockk()
    private val useCase = GetGenresUseCase(repository)

    @Test
    fun `shouldReturnGenresWhenRepositoryReturnsList`() = runTest {
        // Given
        val genres = listOf(
            GenreModel(id = 1, name = "Action"),
            GenreModel(id = 2, name = "Drama")
        )
        every { repository.getGenres(MediaType.MOVIE) } returns flowOf(genres)

        // When
        val result = useCase(MediaType.MOVIE).first()

        // Then
        assertEquals(genres, result)
    }
}

