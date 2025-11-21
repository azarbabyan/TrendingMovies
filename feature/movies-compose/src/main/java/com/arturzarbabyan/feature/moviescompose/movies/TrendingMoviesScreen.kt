package com.arturzarbabyan.feature.moviescompose.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import androidx.paging.LoadState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arturzarbabyan.feature.moviescompose.model.MovieItemUi
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun TrendingMoviesRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: TrendingMoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val movies = viewModel.moviesPagingData.collectAsLazyPagingItems()

    TrendingMoviesScreen(
        uiState = uiState,
        movies = movies,
        onMovieClick = onMovieClick,
        onRetry = { movies.retry() },
        onLoading = { viewModel.onLoading() },
        onContent = { viewModel.onContent() },
        onError = { msg -> viewModel.onError(msg) }
    )
}

@Composable
private fun TrendingMoviesScreen(
    uiState: TrendingMoviesUiState,
    movies: LazyPagingItems<MovieItemUi>,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onLoading: () -> Unit,
    onContent: () -> Unit,
    onError: (String) -> Unit
) {
    val loadState = movies.loadState
    val refreshState = loadState.refresh

    LaunchedEffect(refreshState) {
        when (refreshState) {
            is LoadState.Loading -> onLoading()
            is LoadState.NotLoading -> onContent()
            is LoadState.Error -> onError(refreshState.error.message ?: "Unknown error")
        }
    }

    val isRefreshing = refreshState is LoadState.Loading
    val isError = refreshState is LoadState.Error
    val isNotLoading = refreshState is LoadState.NotLoading
    val isEmpty = isNotLoading && !isError && movies.itemCount == 0

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            isRefreshing && movies.itemCount == 0 -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(5) { idx ->
                        MovieListItem(
                            movie = MovieItemUi(
                                id = idx,
                                title = "",
                                posterUrl = null,
                                ratingText = ""
                            ),
                            onClick = {},
                            isPlaceholder = true
                        )
                    }
                }
            }

            isEmpty -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No movies found")
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movies.itemCount) { index ->
                        val item = movies[index] ?: return@items
                        MovieListItem(
                            movie = item,
                            onClick = { onMovieClick(item.id) }
                        )
                    }

                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }

        if (isError && uiState is TrendingMoviesUiState.Error) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.message)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onRetry() }
                )
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: MovieItemUi,
    onClick: () -> Unit,
    isPlaceholder: Boolean = false
) {
    val shape = MaterialTheme.shapes.medium
    val context = LocalContext.current

    Surface(
        tonalElevation = if (isPlaceholder) 0.dp else 2.dp,
        shadowElevation = if (isPlaceholder) 0.dp else 6.dp,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .let {
                if (isPlaceholder) it else it.clickable(onClick = onClick)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 2.4f)
        ) {
            AsyncImage(
                model = if (isPlaceholder) null else ImageRequest.Builder(context)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = if (isPlaceholder) null else movie.title,
                modifier = Modifier
                    .matchParentSize()
                    .placeholder(
                        visible = isPlaceholder,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                contentScale = ContentScale.Crop
            )

            if (!isPlaceholder) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xCC000000)
                                )
                            )
                        )
                )

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(999.dp),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = movie.ratingText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

