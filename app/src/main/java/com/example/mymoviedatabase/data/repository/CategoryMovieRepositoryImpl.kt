package com.example.mymoviedatabase.data.repository

import com.example.mymoviedatabase.data.database.dao.MovieDao
import com.example.mymoviedatabase.data.mappers.returnQueryMapByCategory
import com.example.mymoviedatabase.data.mappers.toEntityList
import com.example.mymoviedatabase.data.mappers.toFavouriteMovie
import com.example.mymoviedatabase.data.mappers.toMovieDomain
import com.example.mymoviedatabase.data.mappers.toMovieList
import com.example.mymoviedatabase.data.mappers.toMovies
import com.example.mymoviedatabase.data.network.MovieApi
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.MovieCategories
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CategoryMovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao
) : CategoryMovieRepository {

    private val pagingMutex = Mutex()
    private var currentCategory: MovieCategories? = null
    private var currentPage = 0
    private var totalPages = 0
    private var hasNextPage = true

    override fun observeMoviesByCategory(category: MovieCategories): Flow<List<Movie>> {
        return dao.observeMovies(category = category.value).map { it.toMovieList() }
    }

    override suspend fun getFirstPage(category: MovieCategories) {
        pagingMutex.withLock {
            currentCategory = category
            resetInitParams()
            loadMoviePage(1, category)
        }
    }

    override suspend fun getUploadedMovies() {
        pagingMutex.withLock {
            val category = currentCategory ?: return
            if (!hasNextPage) return
            loadMoviePage(currentPage + 1, category)
        }
    }

    private suspend fun loadMoviePage(innerPage: Int, innerCategory: MovieCategories) {
        val response = api.getMovieList(
            returnQueryMapByCategory(innerCategory),
            innerPage
        )

        val movies = response.results.toEntityList(
            innerCategory,
            innerPage
        )

        if (innerPage == 1) {
            dao.updateCategory(innerCategory, movies)
        } else {
            dao.insertMovies(movies)
        }

        updateSearchParams(response.page, response.totalPages)
    }

    private fun updateSearchParams(page: Int, maxPage: Int) {
        currentPage = page
        totalPages = maxPage
        hasNextPage = currentPage < totalPages
    }

    private fun resetInitParams() {
        currentPage = 0
        totalPages = 0
        hasNextPage = true
    }

    override fun getFavouriteMovies(): Flow<List<Movie>> {
        return dao.observeFavouriteMovies().map { it.toMovies() }
    }

    override suspend fun getFavouriteMovie(movieId: String): Movie? {
        return dao.getFavouriteMovie(movieId)?.toMovieDomain()
    }

    override suspend fun insertFavouriteMovie(movie: Movie) {
        return dao.insertFavouriteMovie(movie.toFavouriteMovie())
    }

    override suspend fun deleteFavouriteMovie(movieId: String) {
        dao.deleteFavouriteMovie(movieId)
    }
}