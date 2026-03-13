package com.example.mydearmovies.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private val PILL_SHAPE = RoundedCornerShape(50)
private val PILL_PADDING_HORIZONTAL = 16.dp
private val PILL_PADDING_VERTICAL = 10.dp
private val ROW_CONTENT_PADDING_HORIZONTAL = 16.dp
private val ROW_SPACING = 8.dp

@Composable
fun GenrePills(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = ROW_CONTENT_PADDING_HORIZONTAL),
        horizontalArrangement = Arrangement.spacedBy(ROW_SPACING)
    ) {
        itemsIndexed(items) { index, label ->
            val selected = index == selectedIndex
            val backgroundColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                modifier = Modifier
                    .clip(PILL_SHAPE)
                    .background(backgroundColor)
                    .clickable { onItemSelected(index) }
                    .padding(
                        horizontal = PILL_PADDING_HORIZONTAL,
                        vertical = PILL_PADDING_VERTICAL
                    )
            )
        }
    }
}
