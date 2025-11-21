package com.arturzarbabyan.feature.moviescompose.model

data class MovieItemUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val ratingText: String
)

data class MovieDetailsUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val ratingText: String,
    val releaseDateText: String?,
    val overview: String,
    val genresText: String
)
