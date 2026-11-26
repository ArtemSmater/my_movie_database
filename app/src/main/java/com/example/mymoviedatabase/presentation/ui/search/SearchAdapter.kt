package com.example.mymoviedatabase.presentation.ui.search

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.databinding.SearchMovieItemBinding
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.presentation.utils.MovieDiffCallback
import com.example.mymoviedatabase.presentation.utils.Transform
import java.util.Locale

class SearchAdapter : ListAdapter<Movie, SearchAdapter.SearchViewHolder>(MovieDiffCallback()) {

    var onTrackClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding =
            SearchMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onTrackClickListener?.invoke(getItem(position))
        }
    }

    class SearchViewHolder(
        private val binding: SearchMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {

            val multiTransformation = MultiTransformation(
                CenterCrop(),
                RoundedCorners(Transform.dpToPx(8f, itemView.context))
            )

            with(binding) {
                tvSMovieTitle.apply {
                    text = item.title
                    isSelected = true
                }
                tvSOriginalTitle.apply {
                    text = item.originalTitle
                    isSelected = true
                }
                tvSMovieRelease.text = Transform.getYear(item.releaseDate ?: "")
                tvSMovieRating.apply {
                    background = getDrawableGrade(item.voteAverage ?: 0.1)
                    text = String.format(Locale.getDefault(), "%.1f", item.voteAverage)
                }
                Glide.with(binding.root)
                    .load(item.smallPosterPath)
                    .apply(RequestOptions.bitmapTransform(multiTransformation))
                    .placeholder(R.drawable.baseline_local_movies_24)
                    .into(ivSearchPage)
            }
        }

        fun getDrawableGrade(value: Double): Drawable? {
            val resId = when (value) {
                in 0.1..1.0 -> {
                    R.drawable.grade_null_background
                }

                in 1.0..3.0 -> {
                    R.drawable.grade_worse_background
                }

                in 3.0..5.0 -> {
                    R.drawable.grade_bad_background
                }

                in 5.0..6.0 -> {
                    R.drawable.grade_middle_background
                }

                in 6.0..7.8 -> {
                    R.drawable.grade_norm_background
                }

                in 7.8..10.1 -> {
                    R.drawable.grade_perfect_background
                }

                else -> R.drawable.grade_null_background
            }
            return AppCompatResources.getDrawable(itemView.context, resId)
        }
    }
}