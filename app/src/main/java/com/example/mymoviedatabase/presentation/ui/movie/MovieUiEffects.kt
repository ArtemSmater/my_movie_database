package com.example.mymoviedatabase.presentation.ui.movie

sealed interface MovieUiEffects {
    object BackPressed : MovieUiEffects
    class OpenPlayer(val url: String) : MovieUiEffects
}