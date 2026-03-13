package com.example.mydearmovies.feature.people.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.mydearmovies.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.mydearmovies.core.common.components.PersonCard
import com.example.mydearmovies.core.common.components.PersonShimmerItem
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.feature.people.viewmodel.PeopleViewModel

private val LIST_PADDING_HORIZONTAL = 16.dp

@Composable
fun PeopleScreen(
    onPersonClick: (PersonModel) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: PeopleViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val loadState = pagingItems.loadState

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.people_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LIST_PADDING_HORIZONTAL, vertical = 12.dp)
        )

        when {
            loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0 -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = LIST_PADDING_HORIZONTAL),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(8) { PersonShimmerItem() }
                }
            }
            loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (loadState.refresh as LoadState.Error).error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.people_error_load, error.message.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = LIST_PADDING_HORIZONTAL),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val item = pagingItems[index]
                        if (item != null) {
                            PersonCard(
                                profileImageUrl = item.profileImageUrl,
                                name = item.name,
                                knownFor = item.knownFor,
                                onClick = { onPersonClick(item) }
                            )
                        } else {
                            PersonShimmerItem()
                        }
                    }
                }
            }
        }
    }
}
