package com.arturzarbabyan.domain.movies.repository

import androidx.paging.PagingData
import com.arturzarbabyan.domain.movies.model.Movie
import com.arturzarbabyan.domain.movies.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
    fun getTrendingMoviesPaging(): Flow<PagingData<Movie>>
}