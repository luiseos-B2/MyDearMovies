package com.example.mydearmovies.data.remote

import com.example.mydearmovies.data.model.MovieVideosResponseDto
import com.example.mydearmovies.data.model.PagedMovieResponseDto
import com.example.mydearmovies.data.model.PagedTrendingResponseDto
import com.example.mydearmovies.data.model.PagedTvResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): PagedMovieResponseDto

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int
    ): PagedTvResponseDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int
    ): PagedMovieResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int
    ): PagedMovieResponseDto

    @GET("trending/all/{time_window}")
    suspend fun getTrending(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int
    ): PagedTrendingResponseDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): MovieVideosResponseDto

    @GET("tv/{tv_id}/videos")
    suspend fun getTvVideos(
        @Path("tv_id") tvId: Int
    ): MovieVideosResponseDto
}
