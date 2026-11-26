package com.example.mymoviedatabase.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mymoviedatabase.data.database.tables.FavouriteMovieDbm
import com.example.mymoviedatabase.data.database.tables.MovieDbm
import com.example.mymoviedatabase.domain.entity.MovieCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM local_movies WHERE category =:category ORDER BY pageNumber ASC")
    fun observeMovies(category: String): Flow<List<MovieDbm>>

    @Query("SELECT * FROM favourite_movies ORDER BY uniqueId ASC")
    fun observeFavouriteMovies(): Flow<List<FavouriteMovieDbm>>

    @Query("SELECT * FROM favourite_movies WHERE id =:movieId LIMIT 1")
    suspend fun getFavouriteMovie(movieId: String): FavouriteMovieDbm?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieDbm>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteMovie(favouriteMovieEntity: FavouriteMovieDbm)


    @Query("DELETE FROM local_movies WHERE category =:category")
    suspend fun deleteMovies(category: String)

    @Query("DELETE FROM favourite_movies WHERE id =:movieId")
    suspend fun deleteFavouriteMovie(movieId: String)


    @Transaction
    suspend fun updateCategory(categories: MovieCategories, movies: List<MovieDbm>) {
        deleteMovies(categories.value)
        insertMovies(movies)
    }
}