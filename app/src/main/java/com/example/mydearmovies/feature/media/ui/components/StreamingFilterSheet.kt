package com.example.mydearmovies.feature.media.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.example.mydearmovies.R
import androidx.paging.compose.LazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.mydearmovies.domain.model.WatchProviderModel

private const val SHEET_GRID_COLUMNS = 4
private const val SHEET_PADDING_DP = 16
private const val SHEET_SPACING_DP = 12
private const val ITEM_CORNER_RADIUS_DP = 12
private const val SHEET_HEIGHT_FRACTION = 0.7f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamingFilterSheet(
    pagingItems: LazyPagingItems<WatchProviderModel>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedIds: Set<Int>,
    isLoading: Boolean,
    onProviderSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val sheetHeightDp = (configuration.screenHeightDp * SHEET_HEIGHT_FRACTION).dp
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val loadState = pagingItems.loadState

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeightDp)
                .imePadding()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = stringResource(R.string.filter_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SHEET_PADDING_DP.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.filter_search_provider),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.filter_clear),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                else -> {
                    val appendLoading = loadState.append is LoadState.Loading
                    val itemCount = pagingItems.itemCount + if (appendLoading) 8 else 0

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(SHEET_GRID_COLUMNS),
                        contentPadding = PaddingValues(SHEET_PADDING_DP.dp),
                        horizontalArrangement = Arrangement.spacedBy(SHEET_SPACING_DP.dp),
                        verticalArrangement = Arrangement.spacedBy(SHEET_SPACING_DP.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(
                            count = itemCount,
                            key = { index ->
                                if (index < pagingItems.itemCount) {
                                    pagingItems.peek(index)?.id?.toString() ?: "load_$index"
                                } else {
                                    "shimmer_$index"
                                }
                            }
                        ) { index ->
                            if (index < pagingItems.itemCount) {
                                val provider = pagingItems[index]
                                if (provider != null) {
                                    StreamingItem(
                                        provider = provider,
                                        isSelected = provider.id in selectedIds,
                                        onClick = { onProviderSelected(provider.id) },
                                        modifier = Modifier.animateItem(
                                            fadeInSpec = null,
                                            fadeOutSpec = null,
                                            placementSpec = tween(durationMillis = 400)
                                        )
                                    )
                                } else {
                                    StreamingShimmerPlaceholder(
                                        modifier = Modifier.animateItem(
                                            fadeInSpec = null,
                                            fadeOutSpec = null,
                                            placementSpec = tween(durationMillis = 400)
                                        )
                                    )
                                }
                            } else {
                                StreamingShimmerPlaceholder(
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = null,
                                        fadeOutSpec = null,
                                        placementSpec = tween(durationMillis = 400)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreamingShimmerPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(ITEM_CORNER_RADIUS_DP.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    )
}

@Composable
private fun StreamingItem(
    provider: WatchProviderModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(ITEM_CORNER_RADIUS_DP.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .clickable(onClick = onClick)
    ) {
        if (provider.logoUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = provider.logoUrl,
                contentDescription = provider.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(ITEM_CORNER_RADIUS_DP.dp)),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        } else {
            Text(
                text = provider.name.take(2).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(22.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
