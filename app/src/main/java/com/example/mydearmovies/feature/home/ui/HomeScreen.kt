package com.example.mydearmovies.feature.home.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mydearmovies.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.mydearmovies.core.common.components.AppTabs
import com.example.mydearmovies.core.common.components.ContentCard
import com.example.mydearmovies.core.common.components.TrailerCard
import com.example.mydearmovies.core.theme.GradientBlackEnd
import com.example.mydearmovies.core.theme.GradientBlackStart
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.feature.home.viewmodel.HomeViewModel

private const val LIST_PLACEHOLDER_HEIGHT_DP = 200

@Composable
fun HomeScreen(
    onMediaClick: (mediaId: Int, mediaType: MediaType) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val selectedTrendTabIndex by viewModel.selectedTrendTabIndex.collectAsStateWithLifecycle()
    val selectedTrailerTabIndex by viewModel.selectedTrailerTabIndex.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val trendingPagingItems = viewModel.trendingPagingData.collectAsLazyPagingItems()
    val trailerPagingItems = viewModel.latestTrailersPagingData.collectAsLazyPagingItems()

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            HomeContent(
                selectedTabIndex = selectedTabIndex,
                viewModel = viewModel,
                pagingItems = pagingItems,
                onMediaClick = onMediaClick,
                modifier = Modifier.fillMaxWidth()
            )

            TrendsContent(
                selectedTrendTabIndex = selectedTrendTabIndex,
                viewModel = viewModel,
                pagingItems = trendingPagingItems,
                onMediaClick = onMediaClick,
                modifier = Modifier.fillMaxWidth()
            )

            LatestTrailersContent(
                selectedTrailerTabIndex = selectedTrailerTabIndex,
                viewModel = viewModel,
                pagingItems = trailerPagingItems,
                onTrailerClick = { trailer -> openYoutubeTrailer(context, trailer.videoKey) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun openYoutubeTrailer(context: Context, videoKey: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoKey"))
    context.startActivity(intent)
}

@Composable
fun HomeContent(
    pagingItems: LazyPagingItems<ContentModel>,
    viewModel: HomeViewModel,
    selectedTabIndex: Int,
    onMediaClick: (mediaId: Int, mediaType: MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadState = pagingItems.loadState

    val mainTabs = listOf(
        stringResource(R.string.home_tab_filmes),
        stringResource(R.string.home_tab_na_tv),
        stringResource(R.string.home_tab_lancamentos),
        stringResource(R.string.home_tab_cinemas)
    )
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.home_section_popular),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        AppTabs(
            tabs = mainTabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = viewModel::selectTab,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        when {
            loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (loadState.refresh as LoadState.Error).error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.home_error_load, error.message.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                ContentLazyRow(
                    pagingItems = pagingItems,
                    onMediaClick = onMediaClick
                )
            }
        }
    }
}

@Composable
private fun TrendsContent(
    selectedTrendTabIndex: Int,
    viewModel: HomeViewModel,
    pagingItems: LazyPagingItems<ContentModel>,
    onMediaClick: (mediaId: Int, mediaType: MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadState = pagingItems.loadState

    val trendsTabs = listOf(
        stringResource(R.string.home_trends_hoje),
        stringResource(R.string.home_trends_semana)
    )
    Column(modifier = modifier.padding(top = 24.dp)) {
        Text(
            text = stringResource(R.string.home_section_trends),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        AppTabs(
            tabs = trendsTabs,
            selectedIndex = selectedTrendTabIndex,
            onTabSelected = viewModel::selectTrendTab,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        when {
            loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (loadState.refresh as LoadState.Error).error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.home_error_trends, error.message.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                ContentLazyRow(
                    pagingItems = pagingItems,
                    onMediaClick = onMediaClick
                )
            }
        }
    }
}

@Composable
private fun LatestTrailersContent(
    selectedTrailerTabIndex: Int,
    viewModel: HomeViewModel,
    pagingItems: LazyPagingItems<TrailerModel>,
    onTrailerClick: (TrailerModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadState = pagingItems.loadState
    val sectionGradient = Brush.verticalGradient(
        colors = listOf(GradientBlackStart, GradientBlackEnd),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val trailerTabs = listOf(
        stringResource(R.string.home_trailers_streaming),
        stringResource(R.string.home_trailers_tv),
        stringResource(R.string.home_trailers_alugar),
        stringResource(R.string.home_trailers_cinemas)
    )
    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .background(sectionGradient)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.home_section_trailers),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
        )

        AppTabs(
            tabs = trailerTabs,
            selectedIndex = selectedTrailerTabIndex,
            onTabSelected = viewModel::selectTrailerTab,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        when {
            loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (loadState.refresh as LoadState.Error).error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LIST_PLACEHOLDER_HEIGHT_DP.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.home_error_trailers, error.message.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val item = pagingItems[index]
                        if (item != null) {
                            TrailerCard(
                                backdropUrl = item.backdropUrl,
                                title = item.title,
                                subtitle = item.subtitle,
                                onClick = { onTrailerClick(item) }
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(280.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val CONTENT_ROW_PADDING_DP = 16
private const val CONTENT_ROW_SPACING_DP = 16
private const val CONTENT_ROW_VISIBLE_ITEMS = 3

@Composable
private fun ContentLazyRow(
    pagingItems: LazyPagingItems<ContentModel>,
    onMediaClick: (mediaId: Int, mediaType: MediaType) -> Unit
) {
    val configuration = LocalConfiguration.current
    val cardWidthForLazyRowDp = (
        configuration.screenWidthDp - (CONTENT_ROW_PADDING_DP * 2) -
            (CONTENT_ROW_SPACING_DP * (CONTENT_ROW_VISIBLE_ITEMS - 1))
    ) / CONTENT_ROW_VISIBLE_ITEMS.toFloat()

    LazyRow(
        contentPadding = PaddingValues(CONTENT_ROW_PADDING_DP.dp),
        horizontalArrangement = Arrangement.spacedBy(CONTENT_ROW_SPACING_DP.dp),
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
                    onClick = {
                        val type = item.mediaType ?: MediaType.MOVIE
                        onMediaClick(item.id, type)
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(cardWidthForLazyRowDp.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(
    name = "HomeScreen - Light",
    showBackground = true
)
@Composable
private fun HomeScreenPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(
    name = "HomeScreen - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeScreenDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        HomeScreen()
    }
}
