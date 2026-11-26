package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import javax.inject.Inject

class GetMoviesByTitleUseCase @Inject constructor(
    private val repository: SearchMovieRepository
) {
    suspend operator fun invoke(title: String): List<Movie> {
        return repository.getMoviesByTitle(title)
    }
}