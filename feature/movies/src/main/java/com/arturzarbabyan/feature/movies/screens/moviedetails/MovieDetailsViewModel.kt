package com.arturzarbabyan.feature.movies.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturzarbabyan.core.ui.errors.toUIMessage
import com.arturzarbabyan.domain.movies.usecase.GetMovieDetailsUseCase
import com.arturzarbabyan.feature.movies.screens.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    fun loadMovieDetails(movieId: Int) {
        _uiState.value = MovieDetailsUiState.Loading
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId)
                .onSuccess { details ->
                    _uiState.value = MovieDetailsUiState.Content(details.toUi())
                }
                .onFailure { throwable ->
                    _uiState.value = MovieDetailsUiState.Error(
                        throwable.toUIMessage()
                    )
                }
        }
    }
}
