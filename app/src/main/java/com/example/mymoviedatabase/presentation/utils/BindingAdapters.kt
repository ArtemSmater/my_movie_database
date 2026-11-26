package com.example.mymoviedatabase.presentation.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.Review
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@BindingAdapter("moviePoster")
fun bindTrackPoster(imageView: ImageView, movie: Movie) {
    val multiTransformation = MultiTransformation(
        RoundedCorners(Transform.dpToPx(8f, imageView.context))
    )

    Glide.with(imageView)
        .load(movie.bigPosterPath)
        .apply(RequestOptions.bitmapTransform(multiTransformation))
        .placeholder(R.drawable.baseline_local_movies_24)
        .into(imageView)
}

@BindingAdapter("movieRating")
fun bindDoubleAsString(textView: TextView, movie: Movie) {
    textView.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)
}

@BindingAdapter("userAvatar")
fun bindUserAvatar(imageView: ImageView, review: Review) {
    val multiTransformation = MultiTransformation(
        RoundedCorners(Transform.dpToPx(8f, imageView.context))
    )

    Glide.with(imageView)
        .load(review.avatarPath)
        .apply(RequestOptions.bitmapTransform(multiTransformation))
        .placeholder(R.drawable.baseline_person_24)
        .into(imageView)
}

@BindingAdapter("userRating")
fun bindUserRating(textView: TextView, review: Review) {
    textView.apply {
        isVisible = review.rating != null
        text = String.format(
            Locale.getDefault(),
            "${textView.context.getString(R.string.rating)}: ${review.rating.toString()}"
        )
    }
}
@BindingAdapter("reviewRelease")
fun bindReviewDate(textView: TextView, review: Review) {
    val input = review.createdAt

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    val result = formatter.format(Instant.parse(input))
    textView.text = result.toString()
}