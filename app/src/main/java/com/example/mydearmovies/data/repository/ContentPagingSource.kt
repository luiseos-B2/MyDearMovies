package com.example.mydearmovies.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mydearmovies.domain.model.ContentModel

class ContentPagingSource(
    private val loadPage: suspend (Int) -> Pair<List<ContentModel>, Int?>
) : PagingSource<Int, ContentModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentModel> {
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

    override fun getRefreshKey(state: PagingState<Int, ContentModel>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: INITIAL_PAGE
        }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}
