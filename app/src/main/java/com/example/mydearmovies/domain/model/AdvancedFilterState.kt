package com.example.mydearmovies.domain.model


data class AdvancedFilterState(
    val showMeIndex: Int = 0,
    val searchAllAvailability: Boolean = false,
    val availabilityTypes: Set<String> = emptySet(),
    val searchAllEpisodes: Boolean = false,
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val selectedGenreIds: Set<Int> = emptySet(),
    val certification: String? = null,
    val originalLanguage: String? = null
) {
    fun hasActiveFilters(): Boolean =
        selectedGenreIds.isNotEmpty() ||
            certification != null ||
            originalLanguage != null ||
            (searchAllEpisodes && (dateFrom != null || dateTo != null)) ||
            (!searchAllAvailability && availabilityTypes.isNotEmpty())
}
