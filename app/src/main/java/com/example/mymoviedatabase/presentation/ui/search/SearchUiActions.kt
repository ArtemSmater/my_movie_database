package com.example.mymoviedatabase.presentation.ui.search

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface SearchUiActions {

    data object BackPressed : SearchUiActions
    data object SetInitial : SearchUiActions
    data object RetryQuery : SearchUiActions
    data object LoadNextPage : SearchUiActions
    data class MovieClicked(val movie: Movie) : SearchUiActions
    data class FieldChanged(val s: CharSequence?) : SearchUiActions
}