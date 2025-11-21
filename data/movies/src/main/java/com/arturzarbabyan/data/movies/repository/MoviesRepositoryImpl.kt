package com.arturzarbabyan.data.movies.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arturzarbabyan.core.network.helper.safeApiCall
import com.arturzarbabyan.data.movies.mapper.toDomain
import com.arturzarbabyan.data.movies.remote.api.MoviesApi
import com.arturzarbabyan.data.movies.remote.datasource.TrendingMoviesPagingSource
import com.arturzarbabyan.domain.movies.model.Movie
import com.arturzarbabyan.domain.movies.model.MovieDetails
import com.arturzarbabyan.domain.movies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: MoviesApi
) : MoviesRepository {

    override fun getTrendingMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrendingMoviesPagingSource(api)
            }
        ).flow
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return safeApiCall {
            val dto = api.getMovieDetails(movieId)
            dto.toDomain()
        }
    }
}
