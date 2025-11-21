package com.arturzarbabyan.data.movies.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TrendingMoviesResponseDto(
    @SerialName("results")
    val results: List<MovieDto>
)


@Serializable
data class MovieDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null
)