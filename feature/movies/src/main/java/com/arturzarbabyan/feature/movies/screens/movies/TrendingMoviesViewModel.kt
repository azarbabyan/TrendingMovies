package com.arturzarbabyan.feature.movies.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arturzarbabyan.domain.movies.usecase.GetTrendingMoviesUseCase
import com.arturzarbabyan.feature.movies.screens.mapper.toUi
import com.arturzarbabyan.feature.movies.screens.model.MovieItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel() {

    val moviesPagingData: Flow<PagingData<MovieItemUi>> =
        getTrendingMoviesUseCase()
            .map { pagingData ->
                pagingData.map { movie ->
                    movie.toUi()
                }
            }
            .cachedIn(viewModelScope)
}
