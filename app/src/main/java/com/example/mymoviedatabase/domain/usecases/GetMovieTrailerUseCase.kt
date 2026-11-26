package com.example.mymoviedatabase.domain.usecases

import com.example.mymoviedatabase.domain.entity.Trailer
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import javax.inject.Inject

class GetMovieTrailerUseCase @Inject constructor(
    private val repository: SearchMovieRepository
) {
    suspend operator fun invoke(movieId: String): List<Trailer> {
        return repository.getMovieTrailer(movieId)
    }
}