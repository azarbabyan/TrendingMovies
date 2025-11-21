package com.arturzarbabyan.feature.moviescompose.movies


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arturzarbabyan.domain.movies.usecase.GetTrendingMoviesUseCase
import com.arturzarbabyan.feature.moviescompose.mapper.toUi
import com.arturzarbabyan.feature.moviescompose.model.MovieItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrendingMoviesUiState>(TrendingMoviesUiState.Idle)
    val uiState: StateFlow<TrendingMoviesUiState> = _uiState

    val moviesPagingData: Flow<PagingData<MovieItemUi>> =
        getTrendingMoviesUseCase()
            .map { pagingData ->
                pagingData.map { movie -> movie.toUi() }
            }
            .cachedIn(viewModelScope)

    fun onLoading() {
        _uiState.value = TrendingMoviesUiState.Loading
    }

    fun onError(message: String) {
        _uiState.value = TrendingMoviesUiState.Error(message)
    }

    fun onContent() {
        _uiState.value = TrendingMoviesUiState.Idle
    }
}
