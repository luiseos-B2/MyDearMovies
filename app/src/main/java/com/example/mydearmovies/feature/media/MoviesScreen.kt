package com.example.mydearmovies.feature.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mydearmovies.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mydearmovies.domain.model.SortOption
import com.example.mydearmovies.feature.media.ui.FilterScreen
import com.example.mydearmovies.feature.media.ui.components.SortBottomSheet
import com.example.mydearmovies.feature.media.ui.components.StreamingFilterSheet

@Composable
fun MoviesScreen(
    onItemClick: (com.example.mydearmovies.domain.model.ContentModel) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    var showStreamingSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }
    var showFilterScreen by remember { mutableStateOf(false) }
    val genres by viewModel.genres.collectAsStateWithLifecycle()
    val tabs = listOf(
        stringResource(R.string.media_tab_popular),
        stringResource(R.string.media_tab_now_playing),
        stringResource(R.string.media_tab_upcoming),
        stringResource(R.string.media_tab_top_rated)
    ) + genres.map { it.name }
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val selectedWatchProviderIds by viewModel.selectedWatchProviderIds.collectAsStateWithLifecycle()
    val selectedSortOption by viewModel.selectedSortOption.collectAsStateWithLifecycle()
    val advancedFilterState by viewModel.advancedFilterState.collectAsStateWithLifecycle()
    val isAnyFilterActive by viewModel.isAnyFilterActive.collectAsStateWithLifecycle()
    val watchProvidersLoading by viewModel.watchProvidersLoading.collectAsStateWithLifecycle()
    val watchProvidersSearchQuery by viewModel.watchProvidersSearchQuery.collectAsStateWithLifecycle()
    val watchProvidersPagingItems = viewModel.watchProvidersPagingData.collectAsLazyPagingItems()
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    Box(modifier = modifier.fillMaxSize()) {
        MediaScreenContent(
            title = stringResource(R.string.media_title_movies),
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = viewModel::selectTab,
            pagingItems = pagingItems,
            onItemClick = onItemClick,
            hasStreamingFilterActive = selectedWatchProviderIds.isNotEmpty(),
            hasSortFilterActive = selectedSortOption != SortOption.POPULARITY_DESC,
            hasAdvancedFilterActive = advancedFilterState.hasActiveFilters(),
            isAnyFilterActive = isAnyFilterActive,
            onClearAllFilters = viewModel::clearAllFilters,
            onStreamingFilterClick = {
                showStreamingSheet = true
                viewModel.loadWatchProviders()
            },
            onSortClick = { showSortSheet = true },
            onFilterClick = { showFilterScreen = true },
            modifier = Modifier.fillMaxSize()
        )
        if (showStreamingSheet) {
            StreamingFilterSheet(
                pagingItems = watchProvidersPagingItems,
                searchQuery = watchProvidersSearchQuery,
                onSearchQueryChange = viewModel::setWatchProvidersSearchQuery,
                selectedIds = selectedWatchProviderIds,
                isLoading = watchProvidersLoading,
                onProviderSelected = viewModel::onProviderSelected,
                onDismiss = { showStreamingSheet = false }
            )
        }
        if (showSortSheet) {
            SortBottomSheet(
                currentSort = selectedSortOption,
                onSortSelected = viewModel::onSortSelected,
                onDismiss = { showSortSheet = false }
            )
        }
        if (showFilterScreen) {
            FilterScreen(
                filterState = advancedFilterState,
                genres = genres,
                isMovies = true,
                onFilterChange = viewModel::updateAdvancedFilter,
                onApply = { },
                onClearFilter = viewModel::resetAdvancedFilter,
                onDismiss = { showFilterScreen = false }
            )
        }
    }
}
