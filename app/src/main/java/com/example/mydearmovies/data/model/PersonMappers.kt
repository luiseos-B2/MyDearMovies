package com.example.mydearmovies.data.model

import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.model.PersonModel

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val VOTE_MAX = 10f

fun PersonItemDto.toPersonModel(): PersonModel = PersonModel(
    id = id,
    name = name.orEmpty(),
    profileImageUrl = profilePath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    knownFor = knownFor
        ?.mapNotNull { it.title ?: it.name }
        ?.take(5)
        ?.joinToString(", ")
        .orEmpty()
)

fun PersonCastItemDto.toContentModel(): ContentModel = ContentModel(
    id = id,
    title = title ?: name.orEmpty(),
    releaseDate = releaseDate ?: firstAirDate.orEmpty(),
    ratingPercentage = (voteAverage ?: 0f) / VOTE_MAX,
    imageUrl = posterPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty()
)

/**
 * Monta [PersonDetailModel] a partir do DTO de detalhes e da lista de créditos (cast).
 */
fun toPersonDetailModel(
    dto: PersonDetailDto,
    credits: List<ContentModel>
): PersonDetailModel = PersonDetailModel(
    id = dto.id,
    name = dto.name.orEmpty(),
    biography = dto.biography.orEmpty(),
    birthday = dto.birthday.orEmpty(),
    placeOfBirth = dto.placeOfBirth.orEmpty(),
    knownForDepartment = dto.knownForDepartment.orEmpty(),
    profileImageUrl = dto.profilePath?.let { "$IMAGE_BASE_URL$it" }.orEmpty(),
    knownForCredits = credits
)
