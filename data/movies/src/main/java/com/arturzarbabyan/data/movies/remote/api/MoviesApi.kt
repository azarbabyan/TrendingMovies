package com.arturzarbabyan.data.movies.remote.api

import com.arturzarbabyan.data.movies.remote.dto.MovieDetailsDto
import com.arturzarbabyan.data.movies.remote.dto.TrendingMoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1
    ): TrendingMoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailsDto
}