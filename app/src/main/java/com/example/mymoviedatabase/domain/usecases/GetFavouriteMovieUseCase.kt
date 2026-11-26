package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import javax.inject.Inject

class GetFavouriteMovieUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    suspend operator fun invoke(movieId: String): Movie? {
        return repository.getFavouriteMovie(movieId)
    }
}