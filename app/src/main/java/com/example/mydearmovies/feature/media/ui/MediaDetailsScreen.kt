package com.example.mydearmovies.feature.media.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.mydearmovies.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.mydearmovies.core.common.components.ContentCard
import com.example.mydearmovies.core.common.components.MediaDetailsShimmer
import com.example.mydearmovies.core.common.components.PersonCircleCard
import com.example.mydearmovies.core.theme.GradientBlackEnd
import com.example.mydearmovies.core.theme.GradientBlackStart
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.feature.media.viewmodel.MediaDetailsState
import com.example.mydearmovies.feature.media.viewmodel.MediaDetailsViewModel

private const val HEADER_ASPECT = 16f / 9f
private const val BIOGRAPHY_PREVIEW_MAX_CHARS = 400
private const val HORIZONTAL_PADDING_DP = 16
private const val ROW_SPACING_DP = 16
private const val ROW_ITEMS_VISIBLE = 3

private fun lazyRowCardWidthDp(screenWidthDp: Int): Float =
    (screenWidthDp - (HORIZONTAL_PADDING_DP * 2) - (ROW_SPACING_DP * (ROW_ITEMS_VISIBLE - 1))) /
        ROW_ITEMS_VISIBLE.toFloat()

@Composable
fun MediaDetailsScreen(
    onBackClick: () -> Unit,
    onVerMaisClick: (mediaType: MediaType, mediaId: Int) -> Unit,
    onMediaClick: (mediaType: MediaType, mediaId: Int) -> Unit,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val cardWidthDp = lazyRowCardWidthDp(configuration.screenWidthDp)

    when (val s = state) {
        is MediaDetailsState.Loading -> {
            MediaDetailsShimmer(modifier = modifier.fillMaxSize())
        }
        is MediaDetailsState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(s.errorMessageRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is MediaDetailsState.Success -> {
            MediaDetailsContent(
                detail = s.detail,
                cardWidthDp = cardWidthDp,
                onBackClick = onBackClick,
                onVerMaisClick = onVerMaisClick,
                onMediaClick = onMediaClick,
                onPersonClick = onPersonClick
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun MediaDetailsContent(
    detail: MediaDetailModel,
    cardWidthDp: Float,
    onBackClick: () -> Unit,
    onVerMaisClick: (mediaType: MediaType, mediaId: Int) -> Unit,
    onMediaClick: (mediaType: MediaType, mediaId: Int) -> Unit,
    onPersonClick: (personId: Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    val gradient = Brush.verticalGradient(
        colors = listOf(
            GradientBlackStart,
            Color.Black.copy(alpha = 0.6f),
            GradientBlackEnd
        )
    )
    val hasMoreOverview = detail.overview.length > BIOGRAPHY_PREVIEW_MAX_CHARS
    val overviewPreview = if (hasMoreOverview) {
        detail.overview.take(BIOGRAPHY_PREVIEW_MAX_CHARS).trimEnd() + "..."
    } else {
        detail.overview
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenWidthDp / HEADER_ASPECT).dp)
        ) {
            if (detail.backdropUrl.isNotBlank()) {
                SubcomposeAsyncImage(
                    model = detail.backdropUrl,
                    contentDescription = detail.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .background(gradient)
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.media_back),
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, end = 20.dp, bottom = 24.dp, top = 16.dp)
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.White
                )
                if (detail.year.isNotBlank()) {
                    Text(
                        text = detail.year,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = HORIZONTAL_PADDING_DP.dp,
                    end = HORIZONTAL_PADDING_DP.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
        ) {
            val infos = buildList {
                add("★ ${String.format("%.1f", detail.rating)}")
                if (detail.runtimeOrSeasons.isNotBlank()) add(detail.runtimeOrSeasons)
                detail.certification?.takeIf { it.isNotBlank() }?.let { add(it) }
            }
            if (infos.isNotEmpty()) {
                Text(
                    text = infos.joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Text(
                text = stringResource(R.string.media_synopsis),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = overviewPreview.ifBlank { stringResource(R.string.media_synopsis_unavailable) },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
            if (hasMoreOverview) {
                Text(
                    text = stringResource(R.string.media_ver_mais),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onVerMaisClick(detail.mediaType, detail.id) }
                )
            }

            if (detail.cast.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.media_cast),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(ROW_SPACING_DP.dp)
                ) {
                    items(detail.cast, key = { it.id }) { member ->
                        PersonCircleCard(
                            profileImageUrl = member.profileImageUrl,
                            name = member.name,
                            character = member.character,
                            onClick = { onPersonClick(member.id) }
                        )
                    }
                }
            }

            if (detail.recommendations.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.media_recommendations),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(ROW_SPACING_DP.dp)
                ) {
                    items(detail.recommendations, key = { it.id }) { item ->
                        ContentCard(
                            imageUrl = item.imageUrl,
                            title = item.title,
                            releaseDate = item.releaseDate,
                            ratingPercentage = item.ratingPercentage,
                            onClick = { onMediaClick(detail.mediaType, item.id) },
                            cardWidthDp = cardWidthDp,
                            modifier = Modifier.width(cardWidthDp.dp)
                        )
                    }
                }
            }
        }
    }
}
