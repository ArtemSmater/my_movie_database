package com.example.mymoviedatabase.presentation.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.usecases.DeleteFavouriteMovieUseCase
import com.example.mymoviedatabase.domain.usecases.GetFavouriteMoviesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavouritesViewModel @Inject constructor(
    getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
) : ViewModel() {

    val observableMovies = getFavouriteMoviesUseCase()
    private val _effects = MutableSharedFlow<FavouritesUiEffects>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun uiActions(action: FavouritesUiActions) {
        when (action) {
            is FavouritesUiActions.RemoveMovie -> {
                deleteFromFavourites(action.movie)
            }

            is FavouritesUiActions.SelectMovie -> {
                selectMovie(action.movie)
            }

            is FavouritesUiActions.BackPressed -> {
                backPressed()
            }
        }
    }

    private fun selectMovie(movie: Movie) {
        _effects.tryEmit(FavouritesUiEffects.SelectMovie(movie))
    }

    private fun backPressed() {
        _effects.tryEmit(FavouritesUiEffects.BackPressed)
    }

    private fun deleteFromFavourites(movie: Movie) {
        viewModelScope.launch {
            deleteFavouriteMovieUseCase(movie.id.toString())
        }
    }
}