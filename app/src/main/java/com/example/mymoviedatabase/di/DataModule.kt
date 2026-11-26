package com.example.mymoviedatabase.di

import android.app.Application
import androidx.room.Room
import com.example.mymoviedatabase.data.database.MovieDatabase
import com.example.mymoviedatabase.data.database.dao.MovieDao
import com.example.mymoviedatabase.data.network.ApiFactory
import com.example.mymoviedatabase.data.network.MovieApi
import com.example.mymoviedatabase.di.annotations.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        private const val DATABASE_NAME = "move_db_name.db"

        @Provides
        @ApplicationScope
        fun provideGsonObject(): Gson = Gson()

        @Provides
        @ApplicationScope
        fun provideMovieDatabase(
            application: Application,
        ): MovieDatabase {
            return Room
                .databaseBuilder(application, MovieDatabase::class.java, DATABASE_NAME)
                .build()
        }

        @Provides
        @ApplicationScope
        fun provideDatabaseDto(database: MovieDatabase): MovieDao {
            return database.getMovieDao()
        }

        @Provides
        @ApplicationScope
        fun provideRemoteDatasource(): MovieApi {
            return ApiFactory.movieApi
        }
    }
}