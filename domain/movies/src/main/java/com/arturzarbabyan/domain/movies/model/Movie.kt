package com.arturzarbabyan.domain.movies.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val voteAverage: Double
)