package com.arturzarbabyan.feature.moviescompose.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.arturzarbabyan.feature.moviescompose.moviedetails.MovieDetailsRoute
import com.arturzarbabyan.feature.moviescompose.movies.TrendingMoviesRoute

private const val ROUTE_TRENDING = "trending"
private const val ROUTE_DETAILS = "details/{movieId}"

@Composable
fun MoviesComposeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_TRENDING,
        modifier = modifier
    ) {
        composable(ROUTE_TRENDING) {
            TrendingMoviesRoute(
                onMovieClick = { id ->
                    navController.navigate("details/$id")
                }
            )
        }

        composable(
            route = ROUTE_DETAILS,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetailsRoute(
                movieId = movieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
