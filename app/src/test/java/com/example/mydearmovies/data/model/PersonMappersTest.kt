package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.model.PersonModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonMappersTest {

    @Test
    fun `shouldMapPersonItemDtoToPersonModelWithKnownFor`() {
        // Given
        val dto = PersonItemDto(
            id = 1,
            name = "Actor",
            profilePath = "/actor.jpg",
            knownFor = listOf(
                KnownForItemDto(title = "Movie1", name = null),
                KnownForItemDto(title = null, name = "Show1")
            )
        )

        // When
        val result: PersonModel = dto.toPersonModel()

        // Then
        assertEquals(1, result.id)
        assertEquals("Actor", result.name)
        assertEquals(true, result.profileImageUrl.contains("/actor.jpg"))
        assertEquals("Movie1, Show1", result.knownFor)
    }

    @Test
    fun `shouldMapPersonCastItemDtoToContentModel`() {
        // Given
        val dto = PersonCastItemDto(
            id = 10,
            title = "Movie role",
            name = null,
            posterPath = "/poster.jpg",
            releaseDate = "2020-01-01",
            firstAirDate = null,
            voteAverage = 9.0f,
            mediaType = "movie"
        )

        // When
        val result: ContentModel = dto.toContentModel()

        // Then
        assertEquals(10, result.id)
        assertEquals("Movie role", result.title)
        assertEquals("2020-01-01", result.releaseDate)
        assertEquals(0.9f, result.ratingPercentage)
        assertEquals(true, result.imageUrl.contains("/poster.jpg"))
    }

    @Test
    fun `shouldBuildPersonDetailModelFromDtoAndCredits`() {
        // Given
        val dto = PersonDetailDto(
            id = 5,
            name = "Name",
            biography = "Bio",
            birthday = "1990-01-01",
            placeOfBirth = "Place",
            knownForDepartment = "Acting",
            profilePath = "/profile.jpg"
        )
        val credits = listOf(
            ContentModel(
                id = 1,
                title = "Credit",
                releaseDate = "2020-01-01",
                ratingPercentage = 0.5f,
                imageUrl = "/img.jpg"
            )
        )

        // When
        val result: PersonDetailModel = toPersonDetailModel(dto, credits)

        // Then
        assertEquals(5, result.id)
        assertEquals("Name", result.name)
        assertEquals("Bio", result.biography)
        assertEquals("1990-01-01", result.birthday)
        assertEquals("Place", result.placeOfBirth)
        assertEquals("Acting", result.knownForDepartment)
        assertEquals(true, result.profileImageUrl.contains("/profile.jpg"))
        assertEquals(1, result.knownForCredits.size)
    }
}

