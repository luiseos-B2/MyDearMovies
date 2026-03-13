package com.example.mydearmovies.core.common.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import coil.compose.SubcomposeAsyncImage
import com.example.mydearmovies.core.theme.ContentCardRatingGreen
import com.example.mydearmovies.core.theme.ContentCardRatingNavy
import com.example.mydearmovies.core.theme.ContentCardRatingNavyDark
import com.example.mydearmovies.core.theme.ContentCardRatingRed
import com.example.mydearmovies.core.theme.MyDearMoviesTheme
import com.example.mydearmovies.core.theme.MyDearYellowRating

private const val POSTER_ASPECT_RATIO = 2f / 3f
private const val RATING_BADGE_SIZE_DP = 40
private const val RATING_BADGE_OFFSET_Y_DP = -12

private const val HORIZONTAL_PADDING_DP = 16

private const val SPACING_BETWEEN_ITEMS_DP = 16

private const val TARGET_VISIBLE_ITEMS = 3
private fun contentCardWidthDp(screenWidthDp: Int): Float =
    (screenWidthDp - (HORIZONTAL_PADDING_DP * 2) - (SPACING_BETWEEN_ITEMS_DP * (TARGET_VISIBLE_ITEMS - 1))) /
        TARGET_VISIBLE_ITEMS.toFloat()

@Composable
fun ContentCard(
    imageUrl: String,
    title: String,
    releaseDate: String,
    ratingPercentage: Float,
    onClick: () -> Unit,
    cardWidthDp: Float? = null,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val widthDp = cardWidthDp ?: contentCardWidthDp(configuration.screenWidthDp)

    val shape = RoundedCornerShape(12.dp)
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
    val imageTopShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)

    Card(
        modifier = modifier
            .width(widthDp.dp)
            .clickable(onClick = onClick),
        shape = shape,
        colors = cardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(POSTER_ASPECT_RATIO)
            ) {
                if (imageUrl.isBlank()) {
                    PosterPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                            .clip(imageTopShape)
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                            .clip(imageTopShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            PosterPlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                                    .clip(imageTopShape)
                            )
                        },
                        error = {
                            PosterPlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                                    .clip(imageTopShape)
                            )
                        }
                    )
                }

                RatingBadge(
                    percentage = ratingPercentage,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp)
                        .offset(y = RATING_BADGE_OFFSET_Y_DP.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = releaseDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun PosterPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun RatingBadge(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    val sizePx = RATING_BADGE_SIZE_DP.dp
    val backgroundColor = ContentCardRatingNavy
    val trackColor = ContentCardRatingNavy
    val progressColor = when {
        percentage < 0.4f -> ContentCardRatingRed
        percentage < 0.8f -> MyDearYellowRating
        else -> ContentCardRatingGreen
    }
    val textColor = Color.White

    Box(
        modifier = modifier
            .size(sizePx)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizePx)) {
            val strokeWidth = 3.dp.toPx()
            val radius = (this.size.minDimension / 2f) - strokeWidth / 2f
            val startAngle = 270f
            val sweepFilled = (percentage * 360f).coerceIn(0f, 360f)

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                size = Size(radius * 2f, radius * 2f),
                style = Stroke(width = strokeWidth)
            )
            if (sweepFilled > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = startAngle,
                    sweepAngle = sweepFilled,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        Text(
            text = "${(percentage * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            color = textColor
        )
    }
}

@Preview(
    name = "ContentCard - Bordas e título",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardBordersAndTitlePreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Avatar: O Caminho da Água - Título Longo para Testar Ellipsis",
                releaseDate = "17 de set de 2019",
                ratingPercentage = 0.9f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardLightPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "1917",
                releaseDate = "17 de set de 2019",
                ratingPercentage = 0.9f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun ContentCardDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Resgate",
                releaseDate = "17 de set de 2019",
                ratingPercentage = 0.9f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard - Rating Vermelho (<40%)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardRatingRedPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.background)) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Filme Baixa Avaliação",
                releaseDate = "01 de jan de 2020",
                ratingPercentage = 0.35f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard - Rating Amarelo (40-80%)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardRatingYellowPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.background)) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Filme Média Avaliação",
                releaseDate = "15 de jun de 2021",
                ratingPercentage = 0.65f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard - Rating Verde (>80%)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardRatingGreenPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.background)) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Filme Alta Avaliação",
                releaseDate = "28 de fev de 2024",
                ratingPercentage = 0.92f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard - Placeholder (URL vazia / falha)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardPlaceholderPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.background)) {
            ContentCard(
                imageUrl = "",
                title = "Filme sem poster",
                releaseDate = "01 de jan de 2020",
                ratingPercentage = 0.5f,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "ContentCard - 3 itens (360dp)",
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun ContentCardThreeItemsPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ContentCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/1Z0C2IlGqLzR3byz3GgH4YgqL.jpg",
                title = "Título do Filme",
                releaseDate = "17 de set de 2019",
                ratingPercentage = 0.85f,
                onClick = {}
            )
        }
    }
}
