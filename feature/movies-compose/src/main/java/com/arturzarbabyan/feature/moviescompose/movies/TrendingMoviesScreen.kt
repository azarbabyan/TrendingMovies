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
    LaunchedEffect(movies.loadState) {
        val refresh = movies.loadState.refresh
        when {
            refresh is LoadState.Loading -> onLoading()
            refresh is LoadState.NotLoading -> onContent()
            refresh is LoadState.Error -> onError(refresh.error.message ?: "Unknown error")
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(movies.itemCount) { index ->
                val item = movies[index] ?: return@items
                MovieListItem(
                    movie = item,
                    onClick = { onMovieClick(item.id) }
                )
            }

            movies.apply {
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

        when (uiState) {
            TrendingMoviesUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is TrendingMoviesUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
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
            TrendingMoviesUiState.Idle -> Unit
        }
    }
}

@Composable
private fun MovieListItem(
    movie: MovieItemUi,
    onClick: () -> Unit
) {
    val shape = MaterialTheme.shapes.medium
    val context = LocalContext.current

    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 6.dp,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 2.4f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000) // semi-black at bottom
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
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

