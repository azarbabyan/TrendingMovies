package com.arturzarbabyan.feature.movies.screens.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arturzarbabyan.feature.movies.databinding.ItemMovieBinding
import com.arturzarbabyan.feature.movies.screens.model.MovieItemUi
import com.bumptech.glide.Glide

class TrendingMoviesAdapter(
    private val onMovieClick: (Int) -> Unit
) : PagingDataAdapter<MovieItemUi, TrendingMoviesAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieItemUi) = with(binding) {
            textTitle.text = item.title
            textRating.text = item.ratingText
            Glide.with(imagePoster.context)
                .load(item.posterUrl)
                .centerCrop()
                .into(imagePoster)

            root.setOnClickListener {
                onMovieClick(item.id)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MovieItemUi>() {
            override fun areItemsTheSame(oldItem: MovieItemUi, newItem: MovieItemUi): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieItemUi, newItem: MovieItemUi): Boolean =
                oldItem == newItem
        }
    }
}

