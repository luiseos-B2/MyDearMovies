package com.example.mydearmovies.feature.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.mydearmovies.core.paging.InMemoryListPagingSource
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.GenreModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.domain.model.WatchProviderModel
import com.example.mydearmovies.domain.result.DomainResult
import com.example.mydearmovies.domain.result.getOrNull
import com.example.mydearmovies.domain.usecase.GetGenresUseCase
import com.example.mydearmovies.domain.usecase.GetMediaDiscoverUseCase
import com.example.mydearmovies.domain.usecase.GetWatchProvidersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMediaDiscoverUseCase: GetMediaDiscoverUseCase,
    private val getWatchProvidersUseCase: GetWatchProvidersUseCase
) : ViewModel() {

    private val _genres = MutableStateFlow<List<GenreModel>>(emptyList())
    val genres = _genres.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val _selectedWatchProviderIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedWatchProviderIds = _selectedWatchProviderIds.asStateFlow()

    private val _watchProviders = MutableStateFlow<DomainResult<List<WatchProviderModel>>>(DomainResult.Success(emptyList()))
    val watchProviders = _watchProviders.asStateFlow()

    private val _watchProvidersLoading = MutableStateFlow(false)
    val watchProvidersLoading = _watchProvidersLoading.asStateFlow()

    private val _watchProvidersSearchQuery = MutableStateFlow("")
    val watchProvidersSearchQuery = _watchProvidersSearchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val watchProvidersPagingData: Flow<PagingData<WatchProviderModel>> = combine(
        _watchProviders,
        _watchProvidersSearchQuery,
        _selectedWatchProviderIds
    ) { result, query, selectedIds ->
        val fullList = result.getOrNull()
            ?.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
            ?.sortedBy { it.name.lowercase() }
            ?: emptyList()
        val selected = fullList.filter { it.id in selectedIds }
        val rest = fullList.filter { it.id !in selectedIds }
        selected + rest
    }.flatMapLatest { list ->
        Pager(
            config = PagingConfig(pageSize = 40, enablePlaceholders = false),
            pagingSourceFactory = { InMemoryListPagingSource(list, 40) }
        ).flow
    }.cachedIn(viewModelScope)

    private val _selectedSortOption = MutableStateFlow(SortOption.POPULARITY_DESC)
    val selectedSortOption = _selectedSortOption.asStateFlow()

    private val _advancedFilterState = MutableStateFlow(AdvancedFilterState())
    val advancedFilterState = _advancedFilterState.asStateFlow()

    val isAnyFilterActive = combine(
        _selectedWatchProviderIds,
        _selectedSortOption,
        _advancedFilterState
    ) { providerIds, sortOption, advancedFilter ->
        providerIds.isNotEmpty() ||
            sortOption != SortOption.POPULARITY_DESC ||
            advancedFilter.hasActiveFilters()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData: Flow<PagingData<ContentModel>> = combine(
        _selectedTabIndex,
        _selectedWatchProviderIds,
        _selectedSortOption,
        _advancedFilterState,
        _genres
    ) { index, providerIds, sortOption, advancedFilter, genres ->
        Triple(index, providerIds, Triple(sortOption, genres, advancedFilter))
    }.flatMapLatest { (index, providerIds, sortGenresFilter) ->
        val (sortOption, genres, advancedFilter) = sortGenresFilter
        val filter = filterForSeriesTab(index, genres)
        getMediaDiscoverUseCase(MediaType.TV, filter, providerIds, sortOption, advancedFilter)
    }.cachedIn(viewModelScope)

    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            getGenresUseCase(MediaType.TV).collect { list ->
                _genres.update { list }
            }
        }
    }

    fun selectTab(index: Int) {
        val size = _genres.value.size + SERIES_CATEGORY_COUNT
        _selectedTabIndex.value = index.coerceIn(0, (size).coerceAtLeast(1) - 1)
    }

    fun updateSelectedWatchProviders(ids: Set<Int>) {
        _selectedWatchProviderIds.value = ids
    }

    fun onProviderSelected(providerId: Int) {
        _selectedWatchProviderIds.update { current ->
            if (providerId in current) current - providerId else current + providerId
        }
    }

    fun loadWatchProviders() {
        viewModelScope.launch {
            _watchProvidersLoading.value = true
            getWatchProvidersUseCase(MediaType.TV).collect { domainResult ->
                _watchProviders.value = domainResult
                _watchProvidersLoading.value = false
            }
        }
    }

    fun setWatchProvidersSearchQuery(query: String) {
        _watchProvidersSearchQuery.value = query
    }

    fun onSortSelected(sortOption: SortOption) {
        _selectedSortOption.value = sortOption
    }

    fun updateAdvancedFilter(state: AdvancedFilterState) {
        _advancedFilterState.value = state
    }

    fun resetAdvancedFilter() {
        _advancedFilterState.value = AdvancedFilterState()
    }

    fun clearAllFilters() {
        _selectedWatchProviderIds.value = emptySet()
        _selectedSortOption.value = SortOption.POPULARITY_DESC
        _advancedFilterState.value = AdvancedFilterState()
    }
}
