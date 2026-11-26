package com.example.mymoviedatabase.presentation.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mymoviedatabase.databinding.ReviewItemBinding
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.presentation.utils.Transform
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ReviewAdapter(
    private val reviews: List<Review>,
    private val onReviewClick: (Review) -> Unit
) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder,
        position: Int
    ) {
        val review = reviews[position]
        holder.bind(review)
        holder.itemView.setOnClickListener {
            onReviewClick.invoke(review)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }


    class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            with(binding) {

                tvUserName.apply {
                    isVisible = !review.username.isNullOrEmpty()
                    text = review.username
                }

                tvUserRating.apply {
                    visibility = if (review.rating == null) {
                        View.GONE
                    } else {
                        text = review.rating.toString()
                        View.VISIBLE
                    }
                }

                tvReviewContent.apply {
                    isVisible = review.content.toString().isNotEmpty()
                    text = review.content
                }

                val input = review.createdAt

                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    .withZone(ZoneId.systemDefault())

                val result = formatter.format(Instant.parse(input))

                tvReviewDate.apply {
                    isVisible = review.createdAt.toString().isNotEmpty()
                    text = result
                }

                val multiTransformation = MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(Transform.dpToPx(8f, itemView.context))
                )

                Glide.with(binding.root)
                    .load(review.avatarPath)
                    .apply(RequestOptions.bitmapTransform(multiTransformation))
                    .placeholder(com.example.mymoviedatabase.R.drawable.baseline_person_24)
                    .into(ivUserIcon)
            }
        }
    }
}