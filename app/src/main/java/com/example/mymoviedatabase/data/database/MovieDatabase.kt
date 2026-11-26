package com.example.mymoviedatabase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymoviedatabase.data.database.dao.MovieDao
import com.example.mymoviedatabase.data.database.tables.FavouriteMovieDbm
import com.example.mymoviedatabase.data.database.tables.MovieDbm

@Database(
    entities = [MovieDbm::class, FavouriteMovieDbm::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}