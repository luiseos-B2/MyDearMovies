package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class ContentMappersTest {

    @Test
    fun `shouldMapMovieItemDtoToContentModelCorrectly`() {
        // Given
        val dto = MovieItemDto(
            id = 1,
            title = "Title",
            posterPath = "/poster.jpg",
            backdropPath = "/back.jpg",
            voteAverage = 8.0f,
            releaseDate = "2024-01-01"
        )

        // When
        val result: ContentModel = dto.toContentModel()

        // Then
        assertEquals(1, result.id)
        assertEquals("Title", result.title)
        assertEquals("2024-01-01", result.releaseDate)
        assertEquals(0.8f, result.ratingPercentage)
        assertEquals(true, result.imageUrl.contains("/poster.jpg"))
        assertEquals(MediaType.MOVIE, result.mediaType)
    }

    @Test
    fun `shouldMapTvItemDtoToContentModelCorrectly`() {
        // Given
        val dto = TvItemDto(
            id = 10,
            name = "Show",
            posterPath = "/tv_poster.jpg",
            backdropPath = "/tv_back.jpg",
            voteAverage = 5.0f,
            firstAirDate = "2023-10-10"
        )

        // When
        val result = dto.toContentModel()

        // Then
        assertEquals(10, result.id)
        assertEquals("Show", result.title)
        assertEquals("2023-10-10", result.releaseDate)
        assertEquals(0.5f, result.ratingPercentage)
        assertEquals(true, result.imageUrl.contains("/tv_poster.jpg"))
        assertEquals(MediaType.TV, result.mediaType)
    }

    @Test
    fun `shouldMapTrendingItemDtoToContentModelUsingTitleAndMediaType`() {
        // Given
        val dto = TrendingItemDto(
            id = 20,
            mediaType = "movie",
            title = "Movie trending",
            name = null,
            posterPath = "/trend.jpg",
            voteAverage = 7.0f,
            releaseDate = "2022-01-01",
            firstAirDate = null
        )

        // When
        val result = dto.toContentModel()

        // Then
        assertEquals(20, result.id)
        assertEquals("Movie trending", result.title)
        assertEquals("2022-01-01", result.releaseDate)
        assertEquals(0.7f, result.ratingPercentage)
        assertEquals(true, result.imageUrl.contains("/trend.jpg"))
        assertEquals(MediaType.MOVIE, result.mediaType)
    }

    @Test
    fun `shouldFallbackToNameAndFirstAirDateWhenTrendingItemTitleAndReleaseDateNull`() {
        // Given
        val dto = TrendingItemDto(
            id = 21,
            mediaType = "tv",
            title = null,
            name = "Tv trending",
            posterPath = null,
            voteAverage = null,
            releaseDate = null,
            firstAirDate = "2020-05-05"
        )

        // When
        val result = dto.toContentModel()

        // Then
        assertEquals("Tv trending", result.title)
        assertEquals("2020-05-05", result.releaseDate)
        assertEquals(0f, result.ratingPercentage)
        assertEquals("", result.imageUrl)
        assertEquals(MediaType.TV, result.mediaType)
    }
}

