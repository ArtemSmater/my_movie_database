package com.example.mymoviedatabase.domain.repository

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.entity.Trailer

interface SearchMovieRepository {

    suspend fun getMoviesByTitle(movieTitle: String): List<Movie>
    suspend fun getMoviesByTitleNext(): List<Movie>
    suspend fun getMovieReviews(movieId: String): List<Review>
    suspend fun getMovieTrailer(movieId: String): List<Trailer>
}