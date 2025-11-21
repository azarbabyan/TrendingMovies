package com.arturzarbabyan.feature.movies.screens.mapper

import android.annotation.SuppressLint
import com.arturzarbabyan.domain.movies.model.Movie
import com.arturzarbabyan.domain.movies.model.MovieDetails
import com.arturzarbabyan.feature.movies.screens.model.MovieDetailsUi
import com.arturzarbabyan.feature.movies.screens.model.MovieItemUi
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
private fun formatRating(voteAverage: Double): String =
    String.format("⭐ %.1f", voteAverage)

private fun formatGenres(genres: List<String>): String =
    if (genres.isEmpty()) "" else genres.joinToString(", ")

fun Movie.toUi(): MovieItemUi {
    return MovieItemUi(
        id = id,
        title = title,
        posterUrl = posterUrl,
        ratingText = formatRating(voteAverage)
    )
}

fun MovieDetails.toUi(): MovieDetailsUi {
    return MovieDetailsUi(
        id = id,
        title = title,
        posterUrl = posterUrl,
        ratingText = formatRating(voteAverage),
        releaseDateText = formatReleaseDate(releaseDate),
        overview = overview,
        genresText = formatGenres(genres)
    )
}

private fun formatReleaseDate(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val output = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = input.parse(raw)
        if (date != null) output.format(date) else raw
    } catch (e: Exception) {
        raw
    }
}

