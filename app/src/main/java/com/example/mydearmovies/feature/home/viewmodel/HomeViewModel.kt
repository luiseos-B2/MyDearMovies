package com.example.mydearmovies.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.domain.usecase.GetInCinemasUseCase
import com.example.mydearmovies.domain.usecase.GetLatestTrailersUseCase
import com.example.mydearmovies.domain.usecase.GetPopularMoviesUseCase
import com.example.mydearmovies.domain.usecase.GetPopularTvShowsUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingTodayUseCase
import com.example.mydearmovies.domain.usecase.GetTrendingWeekUseCase
import com.example.mydearmovies.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

enum class HomeTab(val index: Int) {
    FILMES(0),
    NA_TV(1),
    PROX_LANCAMENTOS(2),
    NOS_CINEMAS(3)
}
enum class TrendTab(val index: Int) {
    HOJE(0),
    ESTA_SEMANA(1)
}

enum class TrailerTab(val index: Int) {
    STREAMING(0),
    NA_TV(1),
    PARA_ALUGAR(2),
    NOS_CINEMAS(3)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getInCinemasUseCase: GetInCinemasUseCase,
    private val getTrendingTodayUseCase: GetTrendingTodayUseCase,
    private val getTrendingWeekUseCase: GetTrendingWeekUseCase,
    private val getLatestTrailersUseCase: GetLatestTrailersUseCase
) : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(HomeTab.FILMES.index)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _selectedTrendTabIndex = MutableStateFlow(TrendTab.HOJE.index)
    val selectedTrendTabIndex: StateFlow<Int> = _selectedTrendTabIndex.asStateFlow()

    private val _selectedTrailerTabIndex = MutableStateFlow(TrailerTab.STREAMING.index)
    val selectedTrailerTabIndex: StateFlow<Int> = _selectedTrailerTabIndex.asStateFlow()

    val mainTabsSize: Int = 4
    val trendsTabsSize: Int = 2
    val trailerTabsSize: Int = 4

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData: Flow<PagingData<ContentModel>> = _selectedTabIndex
        .flatMapLatest { index ->
            when (index) {
                HomeTab.FILMES.index -> getPopularMoviesUseCase()
                HomeTab.NA_TV.index -> getPopularTvShowsUseCase()
                HomeTab.PROX_LANCAMENTOS.index -> getUpcomingMoviesUseCase()
                HomeTab.NOS_CINEMAS.index -> getInCinemasUseCase()
                else -> getPopularMoviesUseCase()
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingPagingData: Flow<PagingData<ContentModel>> = _selectedTrendTabIndex
        .flatMapLatest { index ->
            when (index) {
                TrendTab.HOJE.index -> getTrendingTodayUseCase()
                TrendTab.ESTA_SEMANA.index -> getTrendingWeekUseCase()
                else -> getTrendingTodayUseCase()
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val latestTrailersPagingData: Flow<PagingData<TrailerModel>> = _selectedTrailerTabIndex
        .flatMapLatest { index -> getLatestTrailersUseCase(index) }
        .cachedIn(viewModelScope)

    fun selectTab(index: Int) {
        _selectedTabIndex.value = index.coerceIn(0, (mainTabsSize - 1).coerceAtLeast(0))
    }

    fun selectTrendTab(index: Int) {
        _selectedTrendTabIndex.value = index.coerceIn(0, (trendsTabsSize - 1).coerceAtLeast(0))
    }

    fun selectTrailerTab(index: Int) {
        _selectedTrailerTabIndex.value = index.coerceIn(0, (trailerTabsSize - 1).coerceAtLeast(0))
    }
}
