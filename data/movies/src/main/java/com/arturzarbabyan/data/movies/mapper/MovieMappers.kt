package com.arturzarbabyan.data.movies.mapper

import com.arturzarbabyan.data.movies.remote.dto.MovieDetailsDto
import com.arturzarbabyan.data.movies.remote.dto.MovieDto
import com.arturzarbabyan.domain.movies.model.Movie
import com.arturzarbabyan.domain.movies.model.MovieDetails


private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

private fun buildPosterUrl(path: String?): String? {
    return path?.let { "$IMAGE_BASE_URL$it" }
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterUrl = buildPosterUrl(posterPath),
        voteAverage = voteAverage ?: 0.0
    )
}

fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title.orEmpty(),
        posterUrl = buildPosterUrl(posterPath),
        voteAverage = voteAverage ?: 0.0,
        releaseDate = releaseDate,
        overview = overview.orEmpty(),
        genres = genres?.mapNotNull { it.name } ?: emptyList()
    )
}
