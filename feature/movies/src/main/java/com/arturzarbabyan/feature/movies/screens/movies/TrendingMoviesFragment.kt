package com.arturzarbabyan.feature.movies.screens.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.arturzarbabyan.core.ui.errors.toUIMessage
import com.arturzarbabyan.feature.movies.databinding.FragmentTrendingMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingMoviesFragment : Fragment() {

    private var _binding: FragmentTrendingMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrendingMoviesViewModel by viewModels()
    private lateinit var adapter: TrendingMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRetryButton()
        collectPagingData()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        adapter = TrendingMoviesAdapter { movieId ->
            val action =
                TrendingMoviesFragmentDirections
                    .actionTrendingMoviesFragmentToMovieDetailsFragment(movieId)
            findNavController().navigate(action)
        }
        binding.recyclerTrendingMovies.adapter = adapter
    }

    private fun setupRetryButton() {
        binding.buttonRetryTrending.setOnClickListener {
            adapter.retry()
        }
    }

    private fun collectPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesPagingData.collectLatest { pagingData ->
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun observeLoadState() {
        adapter.addLoadStateListener { loadState ->
            val refreshState = loadState.refresh

            val isLoading = refreshState is LoadState.Loading
            val isError = refreshState is LoadState.Error
            val isNotLoading = refreshState is LoadState.NotLoading
            if (isLoading && adapter.itemCount == 0) {
                binding.shimmerTrending.visibility = View.VISIBLE
                binding.shimmerTrending.startShimmer()

                binding.recyclerTrendingMovies.visibility = View.GONE
                binding.layoutErrorTrending.visibility = View.GONE
                binding.layoutEmptyTrending.visibility = View.GONE
                binding.progressTrending.visibility = View.GONE // optional: no more spinner
            } else {
                binding.shimmerTrending.stopShimmer()
                binding.shimmerTrending.visibility = View.GONE
            }
            if (isError) {
                val error = refreshState.error
                binding.layoutErrorTrending.visibility = View.VISIBLE
                binding.recyclerTrendingMovies.visibility = View.GONE
                binding.layoutEmptyTrending.visibility = View.GONE
                binding.progressTrending.visibility = View.GONE

                binding.textErrorTrending.text = error.toUIMessage()
            }
            if (isNotLoading) {
                binding.layoutErrorTrending.visibility = View.GONE
                binding.progressTrending.visibility = View.GONE

                if (adapter.itemCount == 0) {
                    binding.recyclerTrendingMovies.visibility = View.GONE
                    binding.layoutEmptyTrending.visibility = View.VISIBLE
                } else {
                    binding.recyclerTrendingMovies.visibility = View.VISIBLE
                    binding.layoutEmptyTrending.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
