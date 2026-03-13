package com.example.mydearmovies.domain.model

enum class SortOption(
    val label: String,
    val movieSortBy: String,
    val tvSortBy: String
) {
    POPULARITY_DESC(
        "Popularidade (maior)",
        "popularity.desc",
        "popularity.desc"
    ),
    POPULARITY_ASC(
        "Popularidade (menor)",
        "popularity.asc",
        "popularity.asc"
    ),
    RATING_DESC(
        "Avaliação (melhor)",
        "vote_average.desc",
        "vote_average.desc"
    ),
    RATING_ASC(
        "Avaliação (pior)",
        "vote_average.asc",
        "vote_average.asc"
    ),
    RELEASE_DESC(
        "Lançamento (novo)",
        "primary_release_date.desc",
        "first_air_date.desc"
    ),
    RELEASE_ASC(
        "Lançamento (antigo)",
        "primary_release_date.asc",
        "first_air_date.asc"
    ),
    TITLE_ASC(
        "Título (A-Z)",
        "original_title.asc",
        "original_title.asc"
    ),
    TITLE_DESC(
        "Título (Z-A)",
        "original_title.desc",
        "original_title.desc"
    );

    fun sortByFor(mediaType: MediaType): String = when (mediaType) {
        MediaType.MOVIE -> movieSortBy
        MediaType.TV -> tvSortBy
    }
}
