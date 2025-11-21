package com.arturzarbabyan.data.movies.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arturzarbabyan.core.network.helper.safeApiCall
import com.arturzarbabyan.data.movies.mapper.toDomain
import com.arturzarbabyan.data.movies.remote.api.MoviesApi
import com.arturzarbabyan.domain.movies.model.Movie
import javax.inject.Inject

class TrendingMoviesPagingSource @Inject constructor(
    private val api: MoviesApi
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1

        return safeApiCall {
            val response = api.getTrendingMovies(page = page)
            response.results.map { it.toDomain() }
        }.fold(
            onSuccess = { movies ->
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movies.isEmpty()) null else page + 1
                )
            },
            onFailure = { throwable ->
                LoadResult.Error(throwable)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
