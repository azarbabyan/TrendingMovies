package com.arturzarbabyan.feature.moviescompose.movies

sealed class TrendingMoviesUiState {
    object Idle : TrendingMoviesUiState()
    object Loading : TrendingMoviesUiState()
    data class Error(val message: String) : TrendingMoviesUiState()
}