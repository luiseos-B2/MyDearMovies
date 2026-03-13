package com.example.mydearmovies.data.repository

import com.example.mydearmovies.data.model.CreditsResponseDto
import com.example.mydearmovies.data.model.MovieDetailDto
import com.example.mydearmovies.data.model.PagedMovieResponseDto
import com.example.mydearmovies.data.model.PagedTvResponseDto
import com.example.mydearmovies.data.model.TvDetailDto
import com.example.mydearmovies.data.model.VideoResultDto
import com.example.mydearmovies.data.model.MovieVideosResponseDto
import com.example.mydearmovies.data.remote.MediaService
import com.example.mydearmovies.domain.model.MediaType
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
class MediaRepositoryImplTest {

    private val service: MediaService = mockk()
    private val repository = MediaRepositoryImpl(service)

    @Test
    fun `shouldReturnSuccessWhenGetMediaDetailsMovieSucceeds`() = runTest {
        // Given
        val detailDto = MovieDetailDto(
            id = 1,
            title = "Movie",
            overview = "Overview",
            backdropPath = "/back.jpg",
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            runtime = 120,
            voteAverage = 7.5f
        )
        val credits = CreditsResponseDto(cast = emptyList())
        val recommendations = PagedMovieResponseDto(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { service.getMovieDetails(1) } returns detailDto
        coEvery { service.getMovieCredits(1) } returns credits
        coEvery { service.getMovieRecommendations(1) } returns recommendations

        // When
        val result = repository.getMediaDetails(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Success)
        val data = (result as DomainResult.Success).data
        assertEquals(1, data.id)
        assertEquals("Movie", data.title)
    }

    @Test
    fun `shouldReturnFailureWithNetworkErrorWhenGetMediaDetailsThrowsIOException`() = runTest {
        // Given
        coEvery { service.getMovieDetails(1) } throws IOException()

        // When
        val result = repository.getMediaDetails(MediaType.MOVIE, 1).first()

        // Then
        assertTrue(result is DomainResult.Failure)
        assertEquals(DomainError.Network, (result as DomainResult.Failure).error)
    }
}

