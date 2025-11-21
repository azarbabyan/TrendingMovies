package com.arturzarbabyan.feature.movies.screens.moviedetails

import com.arturzarbabyan.feature.movies.screens.model.MovieDetailsUi

sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Content(val movie: MovieDetailsUi) : MovieDetailsUiState()
    data class Error(val message: String) : MovieDetailsUiState()
}