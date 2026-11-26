package com.example.mymoviedatabase.presentation.ui.observer

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface ObserverUiEffects {
    object BackPressed : ObserverUiEffects
    class MoviePressed(val movie: Movie) : ObserverUiEffects
}