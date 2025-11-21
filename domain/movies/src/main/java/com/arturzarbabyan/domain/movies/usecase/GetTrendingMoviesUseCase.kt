package com.arturzarbabyan.domain.movies.usecase

import androidx.paging.PagingData
import com.arturzarbabyan.domain.movies.model.Movie
import com.arturzarbabyan.domain.movies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> =
        repository.getTrendingMoviesPaging()
}