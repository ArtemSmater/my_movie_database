package com.example.mymoviedatabase.presentation.ui.favourites

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface FavouritesUiEffects {
    object BackPressed : FavouritesUiEffects
    class SelectMovie(val movie: Movie) : FavouritesUiEffects
}