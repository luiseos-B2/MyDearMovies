package com.example.mydearmovies.core.common.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydearmovies.core.theme.AppTabsDarkBorder
import com.example.mydearmovies.core.theme.AppTabsDarkContainerBackground
import com.example.mydearmovies.core.theme.AppTabsDarkSelectedText
import com.example.mydearmovies.core.theme.AppTabsDarkUnselectedText
import com.example.mydearmovies.core.theme.AppTabsLightBorder
import com.example.mydearmovies.core.theme.AppTabsLightContainerBackground
import com.example.mydearmovies.core.theme.AppTabsLightSelectedText
import com.example.mydearmovies.core.theme.AppTabsLightUnselectedText
import com.example.mydearmovies.core.theme.DarkScreenBackground
import com.example.mydearmovies.core.theme.MyDearMoviesTheme

private const val FOCUSED_WEIGHT = 2f
private const val UNFOCUSED_WEIGHT = 1f

@Composable
fun AppTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    focusedIndex: Int? = null
) {
    val isDarkMode = MaterialTheme.colorScheme.background == DarkScreenBackground

    val containerBackground = if (isDarkMode) {
        AppTabsDarkContainerBackground
    } else {
        AppTabsLightContainerBackground
    }

    val borderColor = if (isDarkMode) {
        AppTabsDarkBorder
    } else {
        AppTabsLightBorder
    }

    val selectedTextColor = if (isDarkMode) {
        AppTabsDarkSelectedText
    } else {
        AppTabsLightSelectedText
    }

    val unselectedTextColor = if (isDarkMode) {
        AppTabsDarkUnselectedText
    } else {
        AppTabsLightUnselectedText
    }

    val selectedBackgroundColor = borderColor

    val componentHeight = 44.dp
    val borderWidth = 1.5.dp
    val containerPadding = 4.dp
    val tabHorizontalPadding = 4.dp

    val outerRadius = componentHeight / 2
    val outerPillShape = RoundedCornerShape(outerRadius)

    val internalHeight = componentHeight - (borderWidth * 2) - (containerPadding * 2)
    val innerRadius = internalHeight / 2
    val innerPillShape = RoundedCornerShape(innerRadius)

    val baseFontSize = MaterialTheme.typography.bodyMedium.fontSize
    val narrowScreenThreshold = 360.dp
    val unfocusedFontSize = 11.sp
    val unfocusedPadding = 2.dp

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val baseTabFontSize = if (maxWidth < narrowScreenThreshold) 12.sp else baseFontSize

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .height(componentHeight)
            .background(containerBackground, outerPillShape)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = outerPillShape
            )
            .padding(containerPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tabText ->
                val isSelected = index == selectedIndex
                val isFocused = focusedIndex != null && focusedIndex == index

                val targetWeight = when {
                    focusedIndex == null -> 1f
                    isFocused -> FOCUSED_WEIGHT
                    else -> UNFOCUSED_WEIGHT
                }
                val weight by animateFloatAsState(
                    targetValue = targetWeight,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "weight_animation"
                )

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) selectedBackgroundColor else Color.Transparent,
                    animationSpec = spring(
                        dampingRatio = 0.75f,
                        stiffness = 300f
                    ),
                    label = "background_animation"
                )

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) selectedTextColor else unselectedTextColor,
                    animationSpec = tween(durationMillis = 300),
                    label = "text_color_animation"
                )

                val tabPadding = if (isFocused) tabHorizontalPadding else unfocusedPadding
                val textSize = if (isFocused) baseTabFontSize else unfocusedFontSize

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .background(backgroundColor, innerPillShape)
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = tabPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = textSize,
                            fontWeight = FontWeight.Normal
                        ),
                        color = textColor,
                        maxLines = 1,
                        softWrap = false,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
    }
}

@Preview(
    name = "AppTabs - 2 abas (Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun AppTabsTwoTabsLightPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            AppTabs(
                tabs = listOf("Filmes", "Séries"),
                selectedIndex = 0,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 2 abas (Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun AppTabsTwoTabsDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            AppTabs(
                tabs = listOf("Filmes", "Séries"),
                selectedIndex = 1,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 4 abas (Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun AppTabsFourTabsLightPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 0,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 4 abas (Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun AppTabsFourTabsDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 2,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 4 abas tela estreita 320dp (Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 320
)
@Composable
private fun AppTabsFourTabsNarrowLightPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 0,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 4 abas tela estreita 320dp (Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212,
    widthDp = 320
)
@Composable
private fun AppTabsFourTabsNarrowDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 1,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 320dp última aba (Nos Cinemas)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 320
)
@Composable
private fun AppTabsNarrowLastTabPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 3,
                onTabSelected = {}
            )
        }
    }
}

@Preview(
    name = "AppTabs - 320dp foco na 1ª aba",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 320
)
@Composable
private fun AppTabsFocusFirstPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 0,
                onTabSelected = {},
                focusedIndex = 0
            )
        }
    }
}

@Preview(
    name = "AppTabs - 320dp foco na 3ª aba (Prox. Lançamentos)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 320
)
@Composable
private fun AppTabsFocusThirdPreview() {
    MyDearMoviesTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 0,
                onTabSelected = {},
                focusedIndex = 2
            )
        }
    }
}

@Preview(
    name = "AppTabs - 320dp foco na 4ª aba (Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212,
    widthDp = 320
)
@Composable
private fun AppTabsFocusFourthDarkPreview() {
    MyDearMoviesTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            AppTabs(
                tabs = listOf("Streaming", "Na TV", "Prox. Lançamentos", "Nos Cinemas"),
                selectedIndex = 2,
                onTabSelected = {},
                focusedIndex = 3
            )
        }
    }
}
