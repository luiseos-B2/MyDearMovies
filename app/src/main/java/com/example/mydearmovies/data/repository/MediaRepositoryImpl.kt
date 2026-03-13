package com.example.mydearmovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mydearmovies.data.model.toCastMemberModel
import com.example.mydearmovies.data.model.toContentModel
import com.example.mydearmovies.data.model.toGenreModel
import com.example.mydearmovies.data.model.toMediaDetailModel
import com.example.mydearmovies.data.model.toWatchProviderModel
import com.example.mydearmovies.data.remote.MediaService
import com.example.mydearmovies.data.util.toDomainError
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaFilter
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.domain.model.WatchProviderModel
import com.example.mydearmovies.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class MediaRepositoryImpl @Inject constructor(
    private val service: MediaService
) : MediaRepository {

    override fun getGenres(mediaType: MediaType): Flow<List<GenreModel>> = flow {
        val response = when (mediaType) {
            MediaType.MOVIE -> service.getMovieGenres()
            MediaType.TV -> service.getTvGenres()
        }
        emit(response.genres.map { it.toGenreModel() })
    }

    override fun getMediaDetails(mediaType: MediaType, mediaId: Int): Flow<DomainResult<MediaDetailModel>> = flow {
        emit(
            runCatching {
                when (mediaType) {
                    MediaType.MOVIE -> {
                        val detail = service.getMovieDetails(mediaId)
                        val credits = service.getMovieCredits(mediaId)
                        val rec = service.getMovieRecommendations(mediaId)
                        val cast = credits.cast?.map { it.toCastMemberModel() }?.take(20) ?: emptyList()
                        val recommendations = rec.results.map { it.toContentModel() }.take(20)
                        detail.toMediaDetailModel(cast, recommendations)
                    }
                    MediaType.TV -> {
                        val detail = service.getTvDetails(mediaId)
                        val credits = service.getTvCredits(mediaId)
                        val rec = service.getTvRecommendations(mediaId)
                        val cast = credits.cast?.map { it.toCastMemberModel() }?.take(20) ?: emptyList()
                        val recommendations = rec.results.map { it.toContentModel() }.take(20)
                        detail.toMediaDetailModel(cast, recommendations)
                    }
                }
            }.fold(
                onSuccess = { DomainResult.Success(it) },
                onFailure = { DomainResult.Failure(it.toDomainError()) }
            )
        )
    }

    override fun getMediaPaging(
        mediaType: MediaType,
        filter: MediaFilter,
        watchProviderIds: Set<Int>,
        sortBy: SortOption,
        advancedFilter: AdvancedFilterState?
    ): Flow<PagingData<ContentModel>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ContentPagingSource { page -> loadPage(mediaType, filter, watchProviderIds, sortBy, advancedFilter, page) } }
        ).flow

    private suspend fun loadPage(
        mediaType: MediaType,
        filter: MediaFilter,
        watchProviderIds: Set<Int>,
        sortBy: SortOption,
        advancedFilter: AdvancedFilterState?,
        page: Int
    ): Pair<List<ContentModel>, Int?> {
        return when (mediaType) {
            MediaType.MOVIE -> loadMoviePage(filter, watchProviderIds, sortBy, advancedFilter, page)
            MediaType.TV -> loadTvPage(filter, watchProviderIds, sortBy, advancedFilter, page)
        }
    }

    private fun buildGenresParam(filter: MediaFilter, advancedFilter: AdvancedFilterState?): String? {
        val fromAdvanced = advancedFilter?.selectedGenreIds?.takeIf { it.isNotEmpty() }?.joinToString(",")
        if (fromAdvanced != null) return fromAdvanced
        return (filter as? MediaFilter.Genre)?.id?.toString()
    }

    private fun buildMonetizationParam(advancedFilter: AdvancedFilterState?): String? {
        if (advancedFilter == null || advancedFilter.searchAllAvailability) return null
        return advancedFilter.availabilityTypes.takeIf { it.isNotEmpty() }?.joinToString("|")
    }

    private suspend fun loadMoviePage(
        filter: MediaFilter,
        watchProviderIds: Set<Int>,
        sortBy: SortOption,
        advancedFilter: AdvancedFilterState?,
        page: Int
    ): Pair<List<ContentModel>, Int?> {
        val watchProvidersParam = watchProviderIds.takeIf { it.isNotEmpty() }?.joinToString(",")
        val sortByParam = sortBy.sortByFor(MediaType.MOVIE)
        val genresParam = buildGenresParam(filter, advancedFilter)
        val useDiscover = genresParam != null ||
            filter is MediaFilter.Popular ||
            watchProvidersParam != null ||
            (advancedFilter != null && advancedFilter.hasActiveFilters())
        val response = when {
            useDiscover -> service.discoverMovies(
                page = page,
                withGenres = genresParam,
                withWatchProviders = watchProvidersParam,
                sortBy = sortByParam,
                primaryReleaseDateGte = advancedFilter?.dateFrom,
                primaryReleaseDateLte = advancedFilter?.dateTo,
                withOriginalLanguage = advancedFilter?.originalLanguage,
                certificationCountry = if (advancedFilter?.certification != null) "BR" else null,
                certificationLte = advancedFilter?.certification,
                withWatchMonetizationTypes = buildMonetizationParam(advancedFilter)
            )
            filter is MediaFilter.NowPlaying -> service.getNowPlayingMovies(page)
            filter is MediaFilter.Upcoming -> service.getUpcomingMovies(page)
            filter is MediaFilter.TopRated -> service.getTopRatedMovies(page)
            else -> service.discoverMovies(
                page = page,
                withGenres = null,
                withWatchProviders = null,
                sortBy = sortByParam,
                primaryReleaseDateGte = advancedFilter?.dateFrom,
                primaryReleaseDateLte = advancedFilter?.dateTo,
                withOriginalLanguage = advancedFilter?.originalLanguage,
                certificationCountry = if (advancedFilter?.certification != null) "BR" else null,
                certificationLte = advancedFilter?.certification,
                withWatchMonetizationTypes = buildMonetizationParam(advancedFilter)
            )
        }
        val items = response.results.map { it.toContentModel() }
        val nextKey = if (response.page < response.totalPages) response.page + 1 else null
        return items to nextKey
    }

    private suspend fun loadTvPage(
        filter: MediaFilter,
        watchProviderIds: Set<Int>,
        sortBy: SortOption,
        advancedFilter: AdvancedFilterState?,
        page: Int
    ): Pair<List<ContentModel>, Int?> {
        val watchProvidersParam = watchProviderIds.takeIf { it.isNotEmpty() }?.joinToString(",")
        val sortByParam = sortBy.sortByFor(MediaType.TV)
        val genresParam = buildGenresParam(filter, advancedFilter)
        val useDiscover = genresParam != null ||
            filter is MediaFilter.Popular ||
            watchProvidersParam != null ||
            (advancedFilter != null && advancedFilter.hasActiveFilters())
        val response = when {
            useDiscover -> service.discoverTv(
                page = page,
                withGenres = genresParam,
                withWatchProviders = watchProvidersParam,
                sortBy = sortByParam,
                firstAirDateGte = advancedFilter?.dateFrom,
                firstAirDateLte = advancedFilter?.dateTo,
                withOriginalLanguage = advancedFilter?.originalLanguage,
                certificationCountry = if (advancedFilter?.certification != null) "BR" else null,
                certificationLte = advancedFilter?.certification,
                withWatchMonetizationTypes = buildMonetizationParam(advancedFilter)
            )
            filter is MediaFilter.AiringToday -> service.getAiringTodayTv(page)
            filter is MediaFilter.OnTheAir -> service.getOnTheAirTv(page)
            filter is MediaFilter.TopRated -> service.getTopRatedTv(page)
            else -> service.discoverTv(
                page = page,
                withGenres = null,
                withWatchProviders = null,
                sortBy = sortByParam,
                firstAirDateGte = advancedFilter?.dateFrom,
                firstAirDateLte = advancedFilter?.dateTo,
                withOriginalLanguage = advancedFilter?.originalLanguage,
                certificationCountry = if (advancedFilter?.certification != null) "BR" else null,
                certificationLte = advancedFilter?.certification,
                withWatchMonetizationTypes = buildMonetizationParam(advancedFilter)
            )
        }
        val items = response.results.map { it.toContentModel() }
        val nextKey = if (response.page < response.totalPages) response.page + 1 else null
        return items to nextKey
    }

    override fun getWatchProviders(mediaType: MediaType): Flow<DomainResult<List<WatchProviderModel>>> = flow {
        emit(
            runCatching {
                val response = when (mediaType) {
                    MediaType.MOVIE -> service.getMovieWatchProviders()
                    MediaType.TV -> service.getTvWatchProviders()
                }
                response.results.map { it.toWatchProviderModel() }
            }.fold(
                onSuccess = { DomainResult.Success(it) },
                onFailure = { DomainResult.Failure(it.toDomainError()) }
            )
        )
    }
}
