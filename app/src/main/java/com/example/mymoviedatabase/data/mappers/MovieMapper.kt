package com.example.mymoviedatabase.data.mappers

import com.example.mymoviedatabase.data.database.tables.MovieDbm
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.MovieCategories
import com.example.mymoviedatabase.data.database.tables.FavouriteMovieDbm
import com.example.mymoviedatabase.data.dto.movie.MovieDto
import com.example.mymoviedatabase.data.dto.review.ReviewDto
import com.example.mymoviedatabase.data.dto.trailer.TrailerDto
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.entity.Trailer

fun MovieDto.toEntity(
    category: MovieCategories,
    page: Int
): MovieDbm {
    return MovieDbm(
        category = category.value,
        backdropPath = backdropPath,
        id = id,
        pageNumber = page,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        title = title,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        backdropPath = backdropPath,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        smallPosterPath = BASE_IMAGE_URL + POSTER_SIZE_SMALL + posterPath,
        bigPosterPath = BASE_IMAGE_URL + POSTER_SIZE_BIG + posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun MovieDbm.toMovie(): Movie {
    return Movie(
        backdropPath = backdropPath,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        smallPosterPath = BASE_IMAGE_URL + POSTER_SIZE_SMALL + posterPath,
        bigPosterPath = BASE_IMAGE_URL + POSTER_SIZE_BIG + posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun FavouriteMovieDbm.toMovieDomain(): Movie {
    return Movie(
        backdropPath,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        smallPosterPath,
        bigPosterPath,
        releaseDate,
        title,
        voteAverage,
        voteCount
    )
}

fun Movie.toFavouriteMovie(): FavouriteMovieDbm {
    return FavouriteMovieDbm(
        0,
        backdropPath,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        smallPosterPath,
        bigPosterPath,
        releaseDate,
        title,
        voteAverage,
        voteCount
    )
}

fun ReviewDto.toReview(): Review {
    return Review(
        this.authorDetails?.username,
        "https://image.tmdb.org/t/p/w185" + this.authorDetails?.avatarPath,
        this.authorDetails?.rating,
        this.content,
        this.createdAt
    )
}

fun TrailerDto.toTrailer(): Trailer {
    return Trailer(
        this.name,
        "https://www.youtube.com/watch?v=" + this.key
    )
}

fun List<FavouriteMovieDbm>.toMovies(): List<Movie> =
    map { it.toMovieDomain() }

fun List<MovieDto>.toDomainList(): List<Movie> =
    map { it.toDomain() }

fun List<MovieDbm>.toMovieList(): List<Movie> =
    map { it.toMovie() }

fun List<MovieDto>.toEntityList(category: MovieCategories, page: Int): List<MovieDbm> =
    map { it.toEntity(category, page) }

fun List<ReviewDto>.toReviewList(): List<Review> =
    map { it.toReview() }

fun List<TrailerDto>.toTrailerList(): List<Trailer> =
    map { it.toTrailer() }

private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE_SMALL = "w185"
private const val POSTER_SIZE_BIG = "w780"