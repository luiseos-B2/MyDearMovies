package com.example.mydearmovies.domain.repository

import androidx.paging.PagingData
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.TrailerModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getPopularMovies(): Flow<PagingData<ContentModel>>

    fun getPopularTvShows(): Flow<PagingData<ContentModel>>

    fun getUpcomingMovies(): Flow<PagingData<ContentModel>>

    fun getInCinemasMovies(): Flow<PagingData<ContentModel>>

    fun getTrendingToday(): Flow<PagingData<ContentModel>>

    fun getTrendingWeek(): Flow<PagingData<ContentModel>>

    fun getLatestTrailers(category: Int): Flow<PagingData<TrailerModel>>
}
