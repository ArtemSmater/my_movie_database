package com.example.mymoviedatabase.presentation.ui.observer

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface ObserverUiState {
    object Loading : ObserverUiState
    object EmptyList : ObserverUiState
    object Error: ObserverUiState
    class ShowMovies(val movies: List<Movie>) : ObserverUiState
}