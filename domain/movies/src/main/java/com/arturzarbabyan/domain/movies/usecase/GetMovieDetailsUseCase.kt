package com.arturzarbabyan.domain.movies.usecase

import com.arturzarbabyan.domain.movies.model.MovieDetails
import com.arturzarbabyan.domain.movies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetails> {
        return repository.getMovieDetails(movieId)
    }
}