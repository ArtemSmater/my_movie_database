package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import javax.inject.Inject

class GetNextTitledMoviesUseCase @Inject constructor(
    private val repository: SearchMovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getMoviesByTitleNext()
    }
}