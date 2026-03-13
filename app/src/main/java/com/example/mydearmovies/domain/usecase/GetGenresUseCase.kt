package com.example.mydearmovies.domain.usecase

import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(mediaType: MediaType): Flow<List<GenreModel>> =
        repository.getGenres(mediaType)
}
