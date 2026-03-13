package com.example.mydearmovies.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mydearmovies.core.theme.GradientBlackEnd
import com.example.mydearmovies.core.theme.GradientBlackStart

private const val BACKDROP_ASPECT_RATIO = 16f / 9f
private const val CARD_CORNER_RADIUS_DP = 12
private const val PLAY_ICON_SIZE_DP = 56
private const val TEXT_PADDING_HORIZONTAL_DP = 12
private const val TEXT_PADDING_BOTTOM_DP = 10

@Composable
fun TrailerCard(
    backdropUrl: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(CARD_CORNER_RADIUS_DP.dp)
    val overlayGradient = Brush.verticalGradient(
        colors = listOf(GradientBlackStart, GradientBlackEnd),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = modifier
            .width(280.dp)
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(BACKDROP_ASPECT_RATIO)
                .clip(shape)
        ) {
            if (backdropUrl.isNotEmpty()) {
                AsyncImage(
                    model = backdropUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            if (backdropUrl.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .background(overlayGradient)
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier
                    .size(PLAY_ICON_SIZE_DP.dp)
                    .align(Alignment.Center),
                tint = Color.White
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = TEXT_PADDING_HORIZONTAL_DP.dp,
                        end = TEXT_PADDING_HORIZONTAL_DP.dp,
                        bottom = TEXT_PADDING_BOTTOM_DP.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
