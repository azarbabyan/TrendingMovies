package com.arturzarbabyan.feature.moviescompose.moviedetails

import com.arturzarbabyan.feature.moviescompose.model.MovieDetailsUi


sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Content(val movie: MovieDetailsUi) : MovieDetailsUiState()
    data class Error(val message: String) : MovieDetailsUiState()
}