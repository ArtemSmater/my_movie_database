package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.MovieCategories
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    operator fun invoke(category: MovieCategories) = repository.observeMoviesByCategory(category)
}