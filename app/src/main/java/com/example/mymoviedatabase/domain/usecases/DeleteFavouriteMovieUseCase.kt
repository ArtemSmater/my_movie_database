package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import javax.inject.Inject

class DeleteFavouriteMovieUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    suspend operator fun invoke(movieId: String) {
        return repository.deleteFavouriteMovie(movieId)
    }
}