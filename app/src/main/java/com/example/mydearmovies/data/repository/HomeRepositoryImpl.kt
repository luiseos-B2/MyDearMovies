package com.example.mydearmovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mydearmovies.data.model.toContentModel
import com.example.mydearmovies.data.model.VideoResultDto
import com.example.mydearmovies.data.remote.HomeService
import com.example.mydearmovies.domain.model.ContentModel
import com.example.mydearmovies.domain.model.TrailerModel
import com.example.mydearmovies.domain.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 20
private const val TRENDING_DAY = "day"
private const val TRENDING_WEEK = "week"
private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"
private const val SUBTITLE_SUFFIX = " | Trailer oficial"

class HomeRepositoryImpl @Inject constructor(
    private val service: HomeService
) : HomeRepository {

    override fun getPopularMovies(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getPopularMovies(page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getPopularTvShows(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getPopularTvShows(page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getUpcomingMovies(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getUpcomingMovies(page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getInCinemasMovies(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getNowPlayingMovies(page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getTrendingToday(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getTrending(TRENDING_DAY, page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getTrendingWeek(): Flow<PagingData<ContentModel>> =
        createPager { page ->
            val response = service.getTrending(TRENDING_WEEK, page)
            response.results.map { it.toContentModel() } to nextPage(response.page, response.totalPages)
        }

    override fun getLatestTrailers(category: Int): Flow<PagingData<TrailerModel>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { TrailerPagingSource { page -> loadTrailersPage(category, page) } }
        ).flow

    private suspend fun loadTrailersPage(category: Int, page: Int): Pair<List<TrailerModel>, Int?> =
        coroutineScope {
            when (category) {
                0 -> loadMovieTrailersPage { service.getPopularMovies(page) }
                1 -> loadTvTrailersPage { service.getPopularTvShows(page) }
                2 -> loadMovieTrailersPage { service.getUpcomingMovies(page) }
                3 -> loadMovieTrailersPage { service.getNowPlayingMovies(page) }
                else -> loadMovieTrailersPage { service.getPopularMovies(page) }
            }
        }

    private suspend fun loadMovieTrailersPage(
        fetchPage: suspend () -> com.example.mydearmovies.data.model.PagedMovieResponseDto
    ): Pair<List<TrailerModel>, Int?> {
        val response = fetchPage()
        val trailers = coroutineScope {
            response.results.map { movie ->
                async {
                    val videos = runCatching { service.getMovieVideos(movie.id) }.getOrNull() ?: return@async null
                    val key = firstYouTubeTrailerKey(videos.results) ?: return@async null
                    TrailerModel(
                        id = movie.id,
                        title = movie.title.orEmpty(),
                        subtitle = movie.title.orEmpty() + SUBTITLE_SUFFIX,
                        backdropUrl = movie.backdropPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty().ifEmpty {
                            movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty()
                        },
                        videoKey = key
                    )
                }
            }.awaitAll().filterNotNull()
        }
        return trailers to nextPage(response.page, response.totalPages)
    }

    private suspend fun loadTvTrailersPage(
        fetchPage: suspend () -> com.example.mydearmovies.data.model.PagedTvResponseDto
    ): Pair<List<TrailerModel>, Int?> {
        val response = fetchPage()
        val trailers = coroutineScope {
            response.results.map { tv ->
                async {
                    val videos = runCatching { service.getTvVideos(tv.id) }.getOrNull() ?: return@async null
                    val key = firstYouTubeTrailerKey(videos.results) ?: return@async null
                    TrailerModel(
                        id = tv.id,
                        title = tv.name.orEmpty(),
                        subtitle = tv.name.orEmpty() + SUBTITLE_SUFFIX,
                        backdropUrl = tv.backdropPath?.let { "$IMAGE_BASE_URL$it" }.orEmpty().ifEmpty {
                            tv.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty()
                        },
                        videoKey = key
                    )
                }
            }.awaitAll().filterNotNull()
        }
        return trailers to nextPage(response.page, response.totalPages)
    }

    private fun firstYouTubeTrailerKey(results: List<VideoResultDto>): String? =
        results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key

    private fun createPager(
        loadPage: suspend (Int) -> Pair<List<ContentModel>, Int?>
    ): Flow<PagingData<ContentModel>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ContentPagingSource(loadPage) }
        ).flow

    private fun nextPage(current: Int, totalPages: Int): Int? =
        if (current < totalPages) current + 1 else null
}
