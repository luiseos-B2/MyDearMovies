package com.example.mydearmovies.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class InMemoryListPagingSource<T : Any>(
    private val list: List<T>,
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: INITIAL_PAGE
        val start = page * pageSize
        if (start >= list.size) {
            return LoadResult.Page(
                data = emptyList<T>(),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = null
            )
        }
        val end = (start + pageSize).coerceAtMost(list.size)
        val pageData: List<T> = list.subList(start, end)
        val nextKey = if (end < list.size) page + 1 else null
        return LoadResult.Page(
            data = pageData,
            prevKey = if (page > 0) page - 1 else null,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: INITIAL_PAGE
        }

    private companion object {
        const val INITIAL_PAGE = 0
        const val DEFAULT_PAGE_SIZE = 40
    }
}
