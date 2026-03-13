package com.example.mydearmovies.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mydearmovies.domain.model.PersonModel

class PeoplePagingSource(
    private val loadPage: suspend (Int) -> Pair<List<PersonModel>, Int?>
) : PagingSource<Int, PersonModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonModel> {
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

    override fun getRefreshKey(state: PagingState<Int, PersonModel>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: INITIAL_PAGE
        }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}
