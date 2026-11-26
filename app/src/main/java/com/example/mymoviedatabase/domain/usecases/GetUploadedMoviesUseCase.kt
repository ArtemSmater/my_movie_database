package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import javax.inject.Inject

class GetUploadedMoviesUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    suspend operator fun invoke() = repository.getUploadedMovies()
}