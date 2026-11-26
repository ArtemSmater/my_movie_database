package com.example.mymoviedatabase.presentation.ui.movie

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface MovieUiActions {
    object BackPressed : MovieUiActions
    object AddToFavourite : MovieUiActions
    class TrailerClicked(val url: String) : MovieUiActions
}