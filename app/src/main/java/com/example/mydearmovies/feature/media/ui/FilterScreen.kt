package com.example.mydearmovies.feature.media.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydearmovies.R
import com.example.mydearmovies.domain.model.AdvancedFilterState
import com.example.mydearmovies.domain.model.GenreModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val CERTIFICATION_OPTIONS = listOf("L", "10", "12", "14", "16", "18")

private val LANGUAGES = listOf(
    "pt" to "Português",
    "en" to "Inglês",
    "es" to "Espanhol",
    "fr" to "Francês",
    "de" to "Alemão",
    "it" to "Italiano",
    "ja" to "Japonês",
    "ko" to "Coreano",
    "zh" to "Chinês",
    "hi" to "Hindi",
    "ru" to "Russo",
    "ar" to "Árabe",
    "nl" to "Holandês",
    "pl" to "Polonês",
    "tr" to "Turco"
)

private val AVAILABILITY_LABELS = listOf(
    "flatrate" to "Stream",
    "free" to "Grátis",
    "ads" to "Propagandas",
    "rent" to "Alugar",
    "buy" to "Comprar"
)

private const val DATE_FORMAT_API = "yyyy-MM-dd"
private const val DATE_FORMAT_DISPLAY = "MM/dd/yyyy"
private const val DATE_PICKER_FIELD_CORNER_RADIUS_DP = 8
private const val DATE_PICKER_FIELD_BORDER_WIDTH_DP = 1
private const val CONTENT_PADDING_HORIZONTAL_DP = 16
private const val CHIP_SPACING_DP = 8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    filterState: AdvancedFilterState,
    genres: List<GenreModel>,
    isMovies: Boolean,
    onFilterChange: (AdvancedFilterState) -> Unit,
    onApply: () -> Unit,
    onClearFilter: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showDateFromPicker by remember { mutableStateOf(false) }
    var showDateToPicker by remember { mutableStateOf(false) }

    val showMeLabels = if (isMovies) {
        listOf("Todos", "Filmes que não assisti", "Filmes que já assisti")
    } else {
        listOf("Todos", "Séries que não assisti", "Séries que já assisti")
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CONTENT_PADDING_HORIZONTAL_DP.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.filter_screen_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = onClearFilter) {
                        Text(
                            text = stringResource(R.string.filter_clear_btn),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.filter_close),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = CONTENT_PADDING_HORIZONTAL_DP.dp,
                    end = CONTENT_PADDING_HORIZONTAL_DP.dp,
                    bottom = 100.dp
                )
            ) {
                item {
                    SectionTitle(stringResource(R.string.filter_show_me))
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        showMeLabels.forEachIndexed { index, label ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onFilterChange(filterState.copy(showMeIndex = index)) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = filterState.showMeIndex == index,
                                    onClick = { onFilterChange(filterState.copy(showMeIndex = index)) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                item {
                    SectionTitle(stringResource(R.string.filter_availability))
                    Column(modifier = Modifier.animateContentSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFilterChange(
                                        filterState.copy(searchAllAvailability = !filterState.searchAllAvailability)
                                    )
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = filterState.searchAllAvailability,
                                onCheckedChange = {
                                    onFilterChange(filterState.copy(searchAllAvailability = it))
                                },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = stringResource(R.string.filter_search_availability),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (!filterState.searchAllAvailability) {
                            AVAILABILITY_LABELS.forEach { (key, label) ->
                                val checked = key in filterState.availabilityTypes
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val newSet = if (checked) {
                                                filterState.availabilityTypes - key
                                            } else {
                                                filterState.availabilityTypes + key
                                            }
                                            onFilterChange(filterState.copy(availabilityTypes = newSet))
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = {
                                            val newSet = if (it) filterState.availabilityTypes + key
                                            else filterState.availabilityTypes - key
                                            onFilterChange(filterState.copy(availabilityTypes = newSet))
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                item {
                    SectionTitle(stringResource(R.string.filter_dates))
                    Column(modifier = Modifier.animateContentSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFilterChange(
                                        filterState.copy(searchAllEpisodes = !filterState.searchAllEpisodes)
                                    )
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = filterState.searchAllEpisodes,
                                onCheckedChange = {
                                    onFilterChange(filterState.copy(searchAllEpisodes = it))
                                },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = stringResource(R.string.filter_search_episodes),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        AnimatedVisibility(
                            visible = filterState.searchAllEpisodes,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                DatePickerField(
                                    label = stringResource(R.string.filter_date_from),
                                    value = filterState.dateFrom,
                                    displayFormat = DATE_FORMAT_DISPLAY,
                                    onClick = { showDateFromPicker = true },
                                    modifier = Modifier.weight(1f)
                                )
                                DatePickerField(
                                    label = stringResource(R.string.filter_date_to),
                                    value = filterState.dateTo,
                                    displayFormat = DATE_FORMAT_DISPLAY,
                                    onClick = { showDateToPicker = true },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                item {
                    SectionTitle(stringResource(R.string.filter_genres))
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(CHIP_SPACING_DP.dp),
                        verticalArrangement = Arrangement.spacedBy(CHIP_SPACING_DP.dp)
                    ) {
                        genres.forEach { genre ->
                            val selected = genre.id in filterState.selectedGenreIds
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    val newSet = if (selected) {
                                        filterState.selectedGenreIds - genre.id
                                    } else {
                                        filterState.selectedGenreIds + genre.id
                                    }
                                    onFilterChange(filterState.copy(selectedGenreIds = newSet))
                                },
                                label = { Text(genre.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                item {
                    SectionTitle(stringResource(R.string.filter_rating))
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(CHIP_SPACING_DP.dp),
                        verticalArrangement = Arrangement.spacedBy(CHIP_SPACING_DP.dp)
                    ) {
                        CERTIFICATION_OPTIONS.forEach { cert ->
                            val selected = filterState.certification == cert
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    onFilterChange(
                                        filterState.copy(certification = if (selected) null else cert)
                                    )
                                },
                                label = { Text(cert) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.filter_language),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Help,
                            contentDescription = stringResource(R.string.filter_language_hint),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { showLanguageSheet = true }
                            .padding(12.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = filterState.originalLanguage?.let { code ->
                                LANGUAGES.find { it.first == code }?.second ?: code
                            } ?: stringResource(R.string.filter_none),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (filterState.originalLanguage != null)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CONTENT_PADDING_HORIZONTAL_DP.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onClick = onClearFilter) {
                    Text(
                        text = "Limpar",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.filter_apply),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        if (showLanguageSheet) {
            LanguageBottomSheet(
                currentCode = filterState.originalLanguage,
                languages = LANGUAGES,
                onSelect = { code -> onFilterChange(filterState.copy(originalLanguage = code)) },
                onDismiss = { showLanguageSheet = false }
            )
        }
        if (showDateFromPicker) {
            DatePickerDialog(
                initialDate = filterState.dateFrom,
                onDateSelected = { dateStr -> onFilterChange(filterState.copy(dateFrom = dateStr)) },
                onDismiss = { showDateFromPicker = false }
            )
        }
        if (showDateToPicker) {
            DatePickerDialog(
                initialDate = filterState.dateTo,
                onDateSelected = { dateStr -> onFilterChange(filterState.copy(dateTo = dateStr)) },
                onDismiss = { showDateToPicker = false }
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
@Composable
private fun DatePickerField(
    label: String,
    value: String?,
    displayFormat: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayValue = value?.let { str ->
        runCatching {
            val sdfApi = SimpleDateFormat(DATE_FORMAT_API, Locale.US)
            val sdfDisplay = SimpleDateFormat(displayFormat, Locale.US)
            sdfDisplay.format(sdfApi.parse(str) ?: return@runCatching null)
        }.getOrNull()
    } ?: ""
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .clip(RoundedCornerShape(DATE_PICKER_FIELD_CORNER_RADIUS_DP.dp))
                .border(
                    DATE_PICKER_FIELD_BORDER_WIDTH_DP.dp,
                    if (displayValue.isNotEmpty()) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(DATE_PICKER_FIELD_CORNER_RADIUS_DP.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayValue.ifEmpty { " " },
                style = MaterialTheme.typography.bodyLarge,
                color = if (displayValue.isNotEmpty())
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.filter_calendar),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialDate: String?,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = initialDate?.let { str ->
        runCatching {
            SimpleDateFormat(DATE_FORMAT_API, Locale.US).parse(str)?.time
        }.getOrNull()
    } ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { ms ->
                        val sdf = SimpleDateFormat(DATE_FORMAT_API, Locale.US)
                        onDateSelected(sdf.format(ms))
                    }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.filter_date_confirm))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageBottomSheet(
    currentCode: String?,
    languages: List<Pair<String, String>>,
                onSelect: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, languages) {
        if (query.isBlank()) languages
        else languages.filter { (_, name) -> name.contains(query, ignoreCase = true) }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_language_original),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.filter_language_search)) }
            )
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(null)
                                onDismiss()
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.filter_none),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (currentCode == null) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                items(filtered) { (code, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(code)
                                onDismiss()
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (code == currentCode) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
