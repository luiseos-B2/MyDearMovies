package com.example.mydearmovies.robolectric.base

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
abstract class BaseRobolectricTest {

    @get:Rule
    val composeRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createAndroidComposeRule()

    protected fun <T : Any> loadingPagingFlow(): Flow<PagingData<T>> = flow {
        awaitCancellation()
    }

    protected fun <T : Any> successPagingFlow(items: List<T>): Flow<PagingData<T>> =
        flowOf(PagingData.from(items))

    protected fun <T : Any> errorPagingFlow(message: String): Flow<PagingData<T>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ErrorPagingSource<T>(message) }
        ).flow

    private class ErrorPagingSource<T : Any>(
        private val message: String
    ) : PagingSource<Int, T>() {
        override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
            LoadResult.Error(IllegalStateException(message))
    }
}
