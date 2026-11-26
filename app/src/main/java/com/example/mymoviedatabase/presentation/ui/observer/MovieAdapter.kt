package com.example.mymoviedatabase.presentation.ui.observer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymoviedatabase.databinding.MovieItemBinding
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.presentation.utils.MovieDiffCallback

class MovieAdapter : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    var onTrackClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onTrackClickListener?.invoke(getItem(position)) }
    }

    class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(binding) {
                Glide.with(binding.root)
                    .load(movie.smallPosterPath)
                    .placeholder(com.example.mymoviedatabase.R.drawable.baseline_local_movies_24)
                    .into(ivSmallPoster)
            }
        }
    }
}