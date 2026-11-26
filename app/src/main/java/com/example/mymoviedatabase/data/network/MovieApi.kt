package com.example.mymoviedatabase.data.network

import com.example.mymoviedatabase.data.dto.movie.MovieResponse
import com.example.mymoviedatabase.data.dto.review.ReviewResponse
import com.example.mymoviedatabase.data.dto.trailer.TrailerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.Locale

interface MovieApi {

    @GET("discover/movie")
    suspend fun getMovieList(
        @QueryMap searchType: Map<String, String>,
        @Query(PARAM_PAGE) pageNumber: Int,
        @Query(PARAM_ADULT) isAdult: Boolean = true,
        @Query(PARAM_LANGUAGE) language: String = VALUE_LANGUAGE,
        @Query(PARAM_API_KEY) apiKey: String = VALUE_OF_API_KEY,
    ): MovieResponse

    @GET("search/movie")
    suspend fun getFoundMovies(
        @Query(PARAM_QUERY) movieTitle: String,
        @Query(PARAM_PAGE) pageNumber: Int = 1,
        @Query(PARAM_ADULT) isAdult: Boolean = true,
        @Query(PARAM_LANGUAGE) language: String = VALUE_LANGUAGE,
        @Query(PARAM_API_KEY) apiKey: String = VALUE_OF_API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path(PARAM_MOVIE_ID) movieId: String,
        @Query(PARAM_LANGUAGE) language: String = VALUE_LANGUAGE,
        @Query(PARAM_API_KEY) apiKey: String = VALUE_OF_API_KEY
    ): ReviewResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path(PARAM_MOVIE_ID) movieId: String,
        @Query(PARAM_LANGUAGE) language: String = VALUE_LANGUAGE,
        @Query(PARAM_API_KEY) apiKey: String = VALUE_OF_API_KEY
    ): TrailerResponse

    companion object {
        private const val PARAM_API_KEY = "api_key"
        private const val PARAM_LANGUAGE = "language"
        private const val PARAM_PAGE = "page"
        private const val PARAM_QUERY = "query"
        private const val PARAM_ADULT = "include_adult"
        private const val PARAM_MOVIE_ID = "movie_id"
        private const val VALUE_OF_API_KEY = "5ace56630990e820784f6677aab74194"
        private var VALUE_LANGUAGE = Locale.getDefault().language
    }
}