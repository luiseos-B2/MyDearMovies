package com.example.mydearmovies.core.common.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

private const val POSTER_ASPECT_RATIO = 2f / 3f
private const val GRID_PADDING_DP = 16
private const val GRID_SPACING_DP = 16
private const val GRID_COLUMNS = 3

private fun gridCardWidthDp(screenWidthDp: Int): Float =
    (screenWidthDp - (GRID_PADDING_DP * 2) - (GRID_SPACING_DP * (GRID_COLUMNS - 1))) /
        GRID_COLUMNS.toFloat()

@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val widthDp = gridCardWidthDp(configuration.screenWidthDp)
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    )
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = modifier.width(widthDp.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(POSTER_ASPECT_RATIO)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(brush = brush)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(brush = brush)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(bottom = 4.dp)
                        .background(brush = brush, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .background(brush = brush, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

private val PERSON_SHIMMER_PROFILE_SIZE_DP = 85.dp

/**
 * Esqueleto de carregamento para o layout em linha do [PersonCard].
 * Círculo à esquerda (85.dp) e dois blocos retangulares (nome + subtítulo) à direita.
 */
@Composable
fun PersonShimmerItem(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    )
    val infiniteTransition = rememberInfiniteTransition(label = "personShimmer")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(PERSON_SHIMMER_PROFILE_SIZE_DP)
                    .clip(CircleShape)
                    .background(brush = brush)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(bottom = 6.dp)
                        .background(brush = brush, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .background(brush = brush, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp)
                )
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
    }
}

private const val PERSON_DETAILS_IMAGE_ASPECT = 3f / 4f

/**
 * Shimmer para a tela de detalhes da pessoa: bloco de imagem grande + linhas de texto.
 * Usa [MaterialTheme.colorScheme.surfaceVariant] para o shimmer e [surface] como fundo.
 */
@Composable
fun PersonDetailsShimmer(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    )
    val infiniteTransition = rememberInfiniteTransition(label = "personDetailsShimmer")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PERSON_DETAILS_IMAGE_ASPECT)
                .background(brush = brush)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 8.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 14.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 16.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 8.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 10.dp)
            )
        }
    }
    }
}

private const val MEDIA_DETAILS_HEADER_ASPECT = 16f / 9f

/**
 * Shimmer para a tela de detalhes de filme/série: bloco de imagem (backdrop) no topo +
 * linhas de texto (título, infos, sinopse) + círculos para elenco.
 */
@Composable
fun MediaDetailsShimmer(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    )
    val infiniteTransition = rememberInfiniteTransition(label = "mediaDetailsShimmer")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MEDIA_DETAILS_HEADER_ASPECT)
                .background(brush = brush)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(bottom = 8.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 14.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 16.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 8.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(bottom = 24.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(bottom = 12.dp)
                    .background(brush = brush, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                repeat(4) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(brush = brush)
                        )
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .background(brush = brush, shape = RoundedCornerShape(4.dp))
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
