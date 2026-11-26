package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import javax.inject.Inject

class InsertFavouriteMovieUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        return repository.insertFavouriteMovie(movie)
    }
}