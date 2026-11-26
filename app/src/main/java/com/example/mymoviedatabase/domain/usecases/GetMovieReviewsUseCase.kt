package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val repository: SearchMovieRepository
) {
    suspend operator fun invoke(movieId: String): List<Review> {
        return repository.getMovieReviews(movieId)
    }
}