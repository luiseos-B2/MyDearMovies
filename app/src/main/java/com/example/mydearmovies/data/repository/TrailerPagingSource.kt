package com.example.mydearmovies.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mydearmovies.domain.model.TrailerModel

class TrailerPagingSource(
    private val loadPage: suspend (Int) -> Pair<List<TrailerModel>, Int?>
) : PagingSource<Int, TrailerModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrailerModel> {
        val page = params.key ?: INITIAL_PAGE
        return try {
            val (items, nextKey) = loadPage(page)
            LoadResult.Page(
                data = items,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TrailerModel>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: INITIAL_PAGE
        }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}
