package com.example.mydearmovies.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydearmovies.R
import coil.compose.SubcomposeAsyncImage

private val PROFILE_SIZE_DP = 85.dp

@Composable
fun PersonCard(
    profileImageUrl: String,
    name: String,
    knownFor: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(PROFILE_SIZE_DP)
                    .clip(CircleShape)
            ) {
                if (profileImageUrl.isBlank()) {
                    PersonPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = profileImageUrl,
                        contentDescription = name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            PersonPlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                            )
                        },
                        error = {
                            PersonPlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                if (knownFor.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.people_known_for_label, knownFor),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
    }
}

/** Largura fixa para uso em LazyRow (elenco). */
private val PERSON_CIRCLE_CARD_WIDTH_DP = 88.dp
private val PERSON_CIRCLE_AVATAR_SIZE_DP = 72.dp

/**
 * Card circular para lista horizontal de elenco: avatar + nome + personagem.
 * Use em LazyRow na tela de detalhes de filme/série.
 */
@Composable
fun PersonCircleCard(
    profileImageUrl: String,
    name: String,
    character: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(PERSON_CIRCLE_CARD_WIDTH_DP)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(PERSON_CIRCLE_AVATAR_SIZE_DP)
                .clip(CircleShape)
        ) {
            if (profileImageUrl.isBlank()) {
                PersonPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize()
                )
            } else {
                SubcomposeAsyncImage(
                    model = profileImageUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    loading = {
                        PersonPlaceholder(
                            modifier = Modifier
                                .fillMaxWidth()
                                .matchParentSize()
                        )
                    },
                    error = {
                        PersonPlaceholder(
                            modifier = Modifier
                                .fillMaxWidth()
                                .matchParentSize()
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (character.isNotBlank()) {
            Text(
                text = character,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun PersonPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}
