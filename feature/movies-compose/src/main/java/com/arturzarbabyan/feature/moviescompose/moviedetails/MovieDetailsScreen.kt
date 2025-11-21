package com.arturzarbabyan.feature.moviescompose.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun MovieDetailsRoute(
    movieId: Int,
    onBack: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    MovieDetailsScreen(
        state = uiState,
        onBack = onBack,
        onRetry = { viewModel.loadMovie(movieId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    when (state) {
        MovieDetailsUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is MovieDetailsUiState.Error -> {
            Scaffold(
                contentWindowInsets = WindowInsets(0),
                topBar = {
                    TopAppBar(
                        title = { Text("Movie details") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
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

        is MovieDetailsUiState.Content -> {
            val movie = state.movie
            val scrollState = rememberScrollState()
            val context = LocalContext.current

            Scaffold(
                contentWindowInsets = WindowInsets(0),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = movie.title,
                                maxLines = 1
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 2.4f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(movie.posterUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = movie.ratingText,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }

                                if (!movie.releaseDateText.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = movie.releaseDateText!!,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            if (movie.genresText.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = movie.genresText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
