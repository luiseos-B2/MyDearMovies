package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.CastMemberModel
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaDetailMappersTest {

    @Test
    fun `shouldMapMovieDetailDtoToMediaDetailModel`() {
        // Given
        val dto = MovieDetailDto(
            id = 1,
            title = "Movie",
            overview = "Overview",
            backdropPath = "/back.jpg",
            posterPath = "/poster.jpg",
            releaseDate = "2024-01-01",
            runtime = 120,
            voteAverage = 7.5f
        )
        val cast = listOf(
            CastMemberModel(
                id = 10,
                name = "Actor",
                profileImageUrl = "/actor.jpg",
                character = "Hero"
            )
        )
        val recommendations = listOf(
            ContentModel(
                id = 2,
                title = "Rec",
                releaseDate = "2023-01-01",
                ratingPercentage = 0.8f,
                imageUrl = "/img.jpg"
            )
        )

        // When
        val result: MediaDetailModel = dto.toMediaDetailModel(cast, recommendations)

        // Then
        assertEquals(1, result.id)
        assertEquals(MediaType.MOVIE, result.mediaType)
        assertEquals("Movie", result.title)
        assertEquals("2024", result.year)
        assertEquals("Overview", result.overview)
        assertEquals(true, result.backdropUrl.contains("/back.jpg"))
        assertEquals(7.5f, result.rating)
        assertEquals("120 min", result.runtimeOrSeasons)
        assertEquals(cast, result.cast)
        assertEquals(recommendations, result.recommendations)
    }

    @Test
    fun `shouldMapTvDetailDtoToMediaDetailModel`() {
        // Given
        val dto = TvDetailDto(
            id = 3,
            name = "Show",
            overview = "Tv overview",
            backdropPath = "/tv_back.jpg",
            posterPath = "/tv_poster.jpg",
            firstAirDate = "2020-05-05",
            numberOfSeasons = 3,
            voteAverage = 9.0f
        )
        val cast = emptyList<CastMemberModel>()
        val recommendations = emptyList<ContentModel>()

        // When
        val result = dto.toMediaDetailModel(cast, recommendations)

        // Then
        assertEquals(3, result.id)
        assertEquals(MediaType.TV, result.mediaType)
        assertEquals("Show", result.title)
        assertEquals("2020", result.year)
        assertEquals("Tv overview", result.overview)
        assertEquals(true, result.backdropUrl.contains("/tv_back.jpg"))
        assertEquals(9.0f, result.rating)
        assertEquals("3 temporada(s)", result.runtimeOrSeasons)
    }
}

