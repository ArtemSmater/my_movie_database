package com.example.mymoviedatabase.data.repository

import com.example.mymoviedatabase.data.mappers.toDomainList
import com.example.mymoviedatabase.data.mappers.toReviewList
import com.example.mymoviedatabase.data.mappers.toTrailerList
import com.example.mymoviedatabase.data.network.MovieApi
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.entity.Trailer
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class SearchMovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : SearchMovieRepository {

    private var title: String? = null
    private var currentPage = 0
    private var totalPages = 0
    private var hasNextPage = false
    private val uploadedMovies = mutableListOf<Movie>()
    private val pagingMutex = Mutex()

    override suspend fun getMoviesByTitle(movieTitle: String): List<Movie> {
        val query = movieTitle.trim()
        if (query.isBlank()) return emptyList()
        pagingMutex.withLock {
            title = query
            resetSearchParams()
            return getTitledMovies(query, 1)
        }
    }

    override suspend fun getMoviesByTitleNext(): List<Movie> {
        pagingMutex.withLock {
            if (!hasNextPage) return uploadedMovies.toList()
            val innerTitle = title ?: return uploadedMovies.toList()
            return getTitledMovies(innerTitle, currentPage + 1)
        }
    }

    private suspend fun getTitledMovies(innerTitle: String, innerPage: Int): List<Movie> {
        val response = api.getFoundMovies(innerTitle, innerPage)
        val movies = response.results.toDomainList()

        uploadedMovies.addAll(movies)

        updateSearchParams(response.page, response.totalPages)


        return uploadedMovies.toList()
    }

    private fun updateSearchParams(page: Int, maxPages: Int) {
        currentPage = page
        totalPages = maxPages
        hasNextPage = currentPage < totalPages
    }

    private fun resetSearchParams() {
        uploadedMovies.clear()
        currentPage = 0
        totalPages = 0
        hasNextPage = false
    }

    override suspend fun getMovieReviews(movieId: String): List<Review> {
        return api.getMovieReviews(movieId)
            .result
            .orEmpty()
            .toReviewList()
    }

    override suspend fun getMovieTrailer(movieId: String): List<Trailer> {
        return api.getMovieTrailer(movieId)
            .results
            .orEmpty()
            .toTrailerList()
    }
}