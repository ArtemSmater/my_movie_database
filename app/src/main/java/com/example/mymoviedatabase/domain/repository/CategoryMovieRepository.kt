package com.example.mymoviedatabase.domain.repository

import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.MovieCategories
import kotlinx.coroutines.flow.Flow

interface CategoryMovieRepository {
    fun observeMoviesByCategory(category: MovieCategories): Flow<List<Movie>>
    suspend fun getFirstPage(category: MovieCategories)
    suspend fun getUploadedMovies()
    fun getFavouriteMovies(): Flow<List<Movie>>
    suspend fun getFavouriteMovie(movieId: String): Movie?
    suspend fun insertFavouriteMovie(movie: Movie)
    suspend fun deleteFavouriteMovie(movieId: String)
}