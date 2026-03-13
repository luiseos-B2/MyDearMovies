package com.example.mydearmovies.feature.people.ui

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mydearmovies.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.mydearmovies.core.common.components.ContentCard
import com.example.mydearmovies.core.common.components.PersonDetailsShimmer
import com.example.mydearmovies.core.theme.GradientBlackEnd
import com.example.mydearmovies.core.theme.GradientBlackStart
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.feature.people.viewmodel.PersonDetailsState
import com.example.mydearmovies.feature.people.viewmodel.PersonDetailsViewModel

private const val HEADER_ASPECT = 3f / 4f
private const val BIOGRAPHY_PREVIEW_MAX_CHARS = 400
private const val HORIZONTAL_PADDING_DP = 16
private const val ROW_SPACING_DP = 16
private const val ROW_ITEMS_VISIBLE = 3

private fun lazyRowCardWidthDp(screenWidthDp: Int): Float =
    (screenWidthDp - (HORIZONTAL_PADDING_DP * 2) - (ROW_SPACING_DP * (ROW_ITEMS_VISIBLE - 1))) /
        ROW_ITEMS_VISIBLE.toFloat()

@Composable
fun PersonDetailsScreen(
    onBackClick: () -> Unit,
    onVerMaisClick: (personId: Int, name: String, biography: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val cardWidthDp = lazyRowCardWidthDp(configuration.screenWidthDp)

    when (val s = state) {
        is PersonDetailsState.Loading -> {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                PersonDetailsShimmer(modifier = Modifier.fillMaxSize())
            }
        }
        is PersonDetailsState.Error -> {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(s.errorMessageRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        is PersonDetailsState.Success -> {
            PersonDetailsContent(
                detail = s.detail,
                cardWidthDp = cardWidthDp,
                onBackClick = onBackClick,
                onVerMaisClick = onVerMaisClick
            )
        }
    }
}

@Composable
private fun PersonDetailsContent(
    detail: PersonDetailModel,
    cardWidthDp: Float,
    onBackClick: () -> Unit,
    onVerMaisClick: (personId: Int, name: String, biography: String) -> Unit
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
    val hasMoreBiography = detail.biography.length > BIOGRAPHY_PREVIEW_MAX_CHARS
    val biographyPreview = if (hasMoreBiography) {
        detail.biography.take(BIOGRAPHY_PREVIEW_MAX_CHARS).trimEnd() + "..."
    } else {
        detail.biography
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    (LocalConfiguration.current.screenWidthDp / HEADER_ASPECT).dp
                )
        ) {
            if (detail.profileImageUrl.isNotBlank()) {
                SubcomposeAsyncImage(
                    model = detail.profileImageUrl,
                    contentDescription = detail.name,
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
                    contentDescription = stringResource(R.string.people_back),
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, end = 20.dp, bottom = 24.dp, top = 16.dp)
            ) {
                Text(
                    text = detail.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.White
                )
                if (detail.placeOfBirth.isNotBlank()) {
                    Text(
                        text = detail.placeOfBirth,
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
            if (detail.birthday.isNotBlank() || detail.knownForDepartment.isNotBlank()) {
                val infos = buildList {
                    if (detail.birthday.isNotBlank()) add("Nascimento: ${detail.birthday}")
                    if (detail.knownForDepartment.isNotBlank()) add("Departamento: ${detail.knownForDepartment}")
                }
                infos.forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(bottom = 12.dp))
            }

            Text(
                text = stringResource(R.string.people_biography),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = biographyPreview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
            if (hasMoreBiography) {
                Text(
                    text = stringResource(R.string.people_ver_mais),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onVerMaisClick(detail.id, detail.name, detail.biography) }
                )
            }

            if (detail.knownForCredits.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.people_known_for),
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
                    items(detail.knownForCredits, key = { it.id }) { item ->
                        ContentCard(
                            imageUrl = item.imageUrl,
                            title = item.title,
                            releaseDate = item.releaseDate,
                            ratingPercentage = item.ratingPercentage,
                            onClick = { },
                            cardWidthDp = cardWidthDp,
                            modifier = Modifier.width(cardWidthDp.dp)
                        )
                    }
                }
            }
        }
    }
    }
}
