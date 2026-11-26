package com.example.mymoviedatabase.presentation.ui.favourites

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface FavouritesUiActions {
    object BackPressed : FavouritesUiActions
    class SelectMovie(val movie: Movie) : FavouritesUiActions
    class RemoveMovie(val movie: Movie) : FavouritesUiActions
}