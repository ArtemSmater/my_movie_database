package com.example.mymoviedatabase.presentation.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.entity.Trailer
import com.example.mymoviedatabase.domain.usecases.DeleteFavouriteMovieUseCase
import com.example.mymoviedatabase.domain.usecases.GetFavouriteMovieUseCase
import com.example.mymoviedatabase.domain.usecases.GetMovieReviewsUseCase
import com.example.mymoviedatabase.domain.usecases.GetMovieTrailerUseCase
import com.example.mymoviedatabase.domain.usecases.InsertFavouriteMovieUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val insertFavouriteMovieUseCase: InsertFavouriteMovieUseCase,
    private val getFavouriteMovieUseCase: GetFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
) : ViewModel() {

    private val movie: Movie = checkNotNull(savedStateHandle["selectedMovie"])

    private val _effects = MutableSharedFlow<MovieUiEffects>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite = _isFavourite.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _trailers = MutableStateFlow<List<Trailer>>(emptyList())
    val trailers = _trailers.asStateFlow()

    init {
        getMovieReviews()
        getMovieTrailers()
        getFavouriteMovie()
    }

    fun uiActions(action: MovieUiActions) {
        when (action) {
            is MovieUiActions.BackPressed -> {
                _effects.tryEmit(MovieUiEffects.BackPressed)
            }

            is MovieUiActions.AddToFavourite -> {
                if (!_isFavourite.value) {
                    insertFavouriteMovie()
                } else {
                    deleteFavouriteMovie()
                }
            }

            is MovieUiActions.TrailerClicked -> {
                _effects.tryEmit(MovieUiEffects.OpenPlayer(action.url))
            }
        }
    }

    private fun insertFavouriteMovie() {
        viewModelScope.launch {
            insertFavouriteMovieUseCase(movie)
            getFavouriteMovie()
        }
    }

    private fun deleteFavouriteMovie() {
        viewModelScope.launch {
            deleteFavouriteMovieUseCase(movie.id.toString())
            getFavouriteMovie()
        }
    }


    private fun getFavouriteMovie() {
        viewModelScope.launch {
            val favouriteMovie = getFavouriteMovieUseCase(movie.id.toString())
            _isFavourite.value = favouriteMovie != null
        }
    }

    private fun getMovieReviews() {
        viewModelScope.launch {
            try {
                val reviews = getMovieReviewsUseCase(movieId = movie.id.toString())
                _reviews.value = reviews
            } catch (c: CancellationException) {
                throw c
            } catch (_: Exception) {
                _reviews.value = emptyList()
            }
        }
    }

    private fun getMovieTrailers() {
        viewModelScope.launch {
            try {
                val trailers = getMovieTrailerUseCase(movieId = movie.id.toString())
                _trailers.value = trailers
            } catch (c: CancellationException) {
                throw c
            } catch (_: Exception) {
                _trailers.value = emptyList()
            }
        }
    }
}