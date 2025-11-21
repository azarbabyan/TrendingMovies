package com.arturzarbabyan.data.movies.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MovieDetailsDto(
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
    @SerialName("genres")
    val genres: List<GenreDto>?
)

@Serializable
class GenreDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String?
)