package com.arturzarbabyan.feature.movies.screens.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arturzarbabyan.feature.movies.databinding.FragmentMovieDetailsBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsViewModel by viewModels()
    private var movieId: Int = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId = args.movieId
        setupBackButton()
        setupRetryButton()
        viewModel.loadMovieDetails(movieId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                renderState(state)
            }
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRetryButton() {
        binding.buttonRetryDetails.setOnClickListener {
            viewModel.loadMovieDetails(movieId)
        }
    }

    private fun renderState(state: MovieDetailsUiState) {
        when (state) {
            is MovieDetailsUiState.Loading -> {
                binding.progressDetails.visibility = View.VISIBLE
                binding.scrollDetails.visibility = View.GONE
                binding.layoutErrorDetails.visibility = View.GONE
            }

            is MovieDetailsUiState.Content -> {
                binding.progressDetails.visibility = View.GONE
                binding.scrollDetails.visibility = View.VISIBLE
                binding.layoutErrorDetails.visibility = View.GONE
                val movie = state.movie
                binding.textHeaderTitle.text = movie.title
                Glide.with(this)
                    .load(movie.posterUrl)
                    .centerCrop()
                    .into(binding.imagePosterDetails)
                binding.textTitleDetails.text = movie.title
                binding.textRatingDetails.text = movie.ratingText
                if (movie.releaseDateText.isNullOrEmpty()) {
                    binding.textReleaseDateDetails.visibility = View.GONE
                } else {
                    binding.textReleaseDateDetails.visibility = View.VISIBLE
                    binding.textReleaseDateDetails.text = movie.releaseDateText
                }
                if (movie.genresText.isEmpty()) {
                    binding.textGenresDetails.visibility = View.GONE
                } else {
                    binding.textGenresDetails.visibility = View.VISIBLE
                    binding.textGenresDetails.text = movie.genresText
                }
                binding.textOverviewDetails.text = movie.overview
            }

            is MovieDetailsUiState.Error -> {
                binding.progressDetails.visibility = View.GONE
                binding.scrollDetails.visibility = View.GONE
                binding.layoutErrorDetails.visibility = View.VISIBLE
                binding.textErrorDetails.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


