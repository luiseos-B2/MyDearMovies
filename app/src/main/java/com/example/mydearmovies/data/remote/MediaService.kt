package com.example.mydearmovies.data.remote

import com.example.mydearmovies.data.model.CreditsResponseDto
import com.example.mydearmovies.data.model.GenresResponseDto
import com.example.mydearmovies.data.model.MovieDetailDto
import com.example.mydearmovies.data.model.PagedMovieResponseDto
import com.example.mydearmovies.data.model.PagedTvResponseDto
import com.example.mydearmovies.data.model.TvDetailDto
import com.example.mydearmovies.data.model.WatchProvidersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface MediaService {

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenresResponseDto

    @GET("genre/tv/list")
    suspend fun getTvGenres(): GenresResponseDto

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("with_watch_providers") withWatchProviders: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("watch_region") watchRegion: String = "BR",
        @Query("primary_release_date.gte") primaryReleaseDateGte: String? = null,
        @Query("primary_release_date.lte") primaryReleaseDateLte: String? = null,
        @Query("with_original_language") withOriginalLanguage: String? = null,
        @Query("certification_country") certificationCountry: String? = null,
        @Query("certification.lte") certificationLte: String? = null,
        @Query("with_watch_monetization_types") withWatchMonetizationTypes: String? = null
    ): PagedMovieResponseDto

    @GET("discover/tv")
    suspend fun discoverTv(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("with_watch_providers") withWatchProviders: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("watch_region") watchRegion: String = "BR",
        @Query("first_air_date.gte") firstAirDateGte: String? = null,
        @Query("first_air_date.lte") firstAirDateLte: String? = null,
        @Query("with_original_language") withOriginalLanguage: String? = null,
        @Query("certification_country") certificationCountry: String? = null,
        @Query("certification.lte") certificationLte: String? = null,
        @Query("with_watch_monetization_types") withWatchMonetizationTypes: String? = null
    ): PagedTvResponseDto

    @GET("watch/providers/movie")
    suspend fun getMovieWatchProviders(@Query("watch_region") watchRegion: String = "BR"): WatchProvidersResponseDto

    @GET("watch/providers/tv")
    suspend fun getTvWatchProviders(@Query("watch_region") watchRegion: String = "BR"): WatchProvidersResponseDto

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): PagedMovieResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): PagedMovieResponseDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): PagedMovieResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): PagedMovieResponseDto

    @GET("tv/popular")
    suspend fun getPopularTv(@Query("page") page: Int): PagedTvResponseDto

    @GET("tv/airing_today")
    suspend fun getAiringTodayTv(@Query("page") page: Int): PagedTvResponseDto

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTv(@Query("page") page: Int): PagedTvResponseDto

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(@Query("page") page: Int): PagedTvResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetailDto

    @GET("tv/{tv_id}")
    suspend fun getTvDetails(@Path("tv_id") tvId: Int): TvDetailDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int): CreditsResponseDto

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCredits(@Path("tv_id") tvId: Int): CreditsResponseDto

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1
    ): PagedMovieResponseDto

    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvRecommendations(
        @Path("tv_id") tvId: Int,
        @Query("page") page: Int = 1
    ): PagedTvResponseDto
}
