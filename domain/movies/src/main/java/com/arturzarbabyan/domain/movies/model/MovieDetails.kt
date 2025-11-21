package com.arturzarbabyan.domain.movies.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val voteAverage: Double,
    val releaseDate: String?,
    val overview: String,
    val genres: List<String>
)