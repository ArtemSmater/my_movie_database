package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteMoviesUseCase @Inject constructor(
    private val repository: CategoryMovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getFavouriteMovies()
    }
}