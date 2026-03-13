package com.example.mydearmovies.feature.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.example.mydearmovies.R
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.mydearmovies.core.common.components.ContentCard
import com.example.mydearmovies.core.common.components.GenrePills
import com.example.mydearmovies.core.common.components.ShimmerItem
import com.example.mydearmovies.domain.model.ContentModel

private const val GRID_PADDING_DP = 16
private const val GRID_SPACING_DP = 16
private const val GRID_COLUMNS = 3

private fun gridCardWidthDp(screenWidthDp: Int): Float =
    (screenWidthDp - (GRID_PADDING_DP * 2) - (GRID_SPACING_DP * (GRID_COLUMNS - 1))) /
        GRID_COLUMNS.toFloat()

private const val HEADER_ICON_SPACING_DP = 4

@Composable
fun MediaHeader(
    title: String,
    hasStreamingFilterActive: Boolean = false,
    hasSortFilterActive: Boolean = false,
    hasAdvancedFilterActive: Boolean = false,
    isAnyFilterActive: Boolean = false,
    onClearAllFilters: () -> Unit = {},
    onStreamingFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(HEADER_ICON_SPACING_DP.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = isAnyFilterActive) {
                IconButton(
                    onClick = onClearAllFilters,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.media_filter_clear),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onStreamingFilterClick) {
                Icon(
                    imageVector = Icons.Filled.GridView,
                    contentDescription = stringResource(R.string.media_filter_where_watch),
                    modifier = Modifier.size(24.dp),
                    tint = if (hasStreamingFilterActive) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onSortClick) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = stringResource(R.string.media_filter_sort),
                    modifier = Modifier.size(24.dp),
                    tint = if (hasSortFilterActive) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = stringResource(R.string.media_filter_filter),
                    modifier = Modifier.size(24.dp),
                    tint = if (hasAdvancedFilterActive) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MediaScreenContent(
    title: String,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    pagingItems: LazyPagingItems<ContentModel>,
    onItemClick: (ContentModel) -> Unit,
    hasStreamingFilterActive: Boolean = false,
    hasSortFilterActive: Boolean = false,
    hasAdvancedFilterActive: Boolean = false,
    isAnyFilterActive: Boolean = false,
    onClearAllFilters: () -> Unit = {},
    onStreamingFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val cardWidthDp = gridCardWidthDp(configuration.screenWidthDp)
    val loadState = pagingItems.loadState

    Column(modifier = modifier.fillMaxSize()) {
        MediaHeader(
            title = title,
            hasStreamingFilterActive = hasStreamingFilterActive,
            hasSortFilterActive = hasSortFilterActive,
            hasAdvancedFilterActive = hasAdvancedFilterActive,
            isAnyFilterActive = isAnyFilterActive,
            onClearAllFilters = onClearAllFilters,
            onStreamingFilterClick = onStreamingFilterClick,
            onSortClick = onSortClick,
            onFilterClick = onFilterClick
        )

        GenrePills(
            items = tabs,
            selectedIndex = selectedTabIndex,
            onItemSelected = onTabSelected,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when {
            loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COLUMNS),
                    contentPadding = PaddingValues(GRID_PADDING_DP.dp),
                    horizontalArrangement = Arrangement.spacedBy(GRID_SPACING_DP.dp),
                    verticalArrangement = Arrangement.spacedBy(GRID_SPACING_DP.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(9) { ShimmerItem() }
                }
            }
            loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (loadState.refresh as LoadState.Error).error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.media_error_load, error.message.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COLUMNS),
                    contentPadding = PaddingValues(GRID_PADDING_DP.dp),
                    horizontalArrangement = Arrangement.spacedBy(GRID_SPACING_DP.dp),
                    verticalArrangement = Arrangement.spacedBy(GRID_SPACING_DP.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val item = pagingItems[index]
                        if (item != null) {
                            ContentCard(
                                imageUrl = item.imageUrl,
                                title = item.title,
                                releaseDate = item.releaseDate,
                                ratingPercentage = item.ratingPercentage,
                                onClick = { onItemClick(item) },
                                cardWidthDp = cardWidthDp
                            )
                        } else {
                            ShimmerItem()
                        }
                    }
                }
            }
        }
    }
}
