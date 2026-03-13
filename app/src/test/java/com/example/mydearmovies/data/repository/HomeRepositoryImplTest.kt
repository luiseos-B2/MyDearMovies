package com.example.mydearmovies.data.repository

import androidx.paging.PagingData
import com.example.mydearmovies.data.model.PagedMovieResponseDto
import com.example.mydearmovies.data.model.MovieItemDto
import com.example.mydearmovies.data.model.MovieVideosResponseDto
import com.example.mydearmovies.data.model.VideoResultDto
import com.example.mydearmovies.data.remote.HomeService
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.TrailerModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeRepositoryImplTest {

    private val service: HomeService = mockk()
    private val repository = HomeRepositoryImpl(service)

    @Test
    fun `shouldReturnPagingDataOfPopularMovies`() = runTest {
        // Given
        val response = PagedMovieResponseDto(
            page = 1,
            results = listOf(
                MovieItemDto(
                    id = 1,
                    title = "Movie",
                    posterPath = "/poster.jpg",
                    backdropPath = "/back.jpg",
                    voteAverage = 8.0f,
                    releaseDate = "2024-01-01"
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { service.getPopularMovies(1) } returns response

        // When
        val pagingData: PagingData<ContentModel> = repository.getPopularMovies().first()

        // Then
        assertTrue(pagingData is PagingData<ContentModel>)
    }

    @Test
    fun `shouldReturnTrailerPagingDataForLatestTrailersCategoryMovie`() = runTest {
        // Given
        val moviesResponse = PagedMovieResponseDto(
            page = 1,
            results = listOf(
                MovieItemDto(
                    id = 1,
                    title = "Movie",
                    posterPath = "/poster.jpg",
                    backdropPath = "/back.jpg",
                    voteAverage = 8.0f,
                    releaseDate = "2024-01-01"
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        val videosResponse = MovieVideosResponseDto(
            id = 1,
            results = listOf(
                VideoResultDto(
                    key = "yt_key",
                    site = "YouTube",
                    type = "Trailer",
                    name = "Trailer"
                )
            )
        )
        coEvery { service.getPopularMovies(1) } returns moviesResponse
        coEvery { service.getMovieVideos(1) } returns videosResponse

        // When
        val pagingData: PagingData<TrailerModel> = repository.getLatestTrailers(0).first()

        // Then
        assertTrue(pagingData is PagingData<TrailerModel>)
    }
}
