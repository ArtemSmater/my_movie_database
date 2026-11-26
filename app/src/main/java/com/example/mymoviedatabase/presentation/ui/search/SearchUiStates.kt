package com.example.mymoviedatabase.presentation.ui.search

import com.example.mymoviedatabase.domain.entity.Movie

sealed interface SearchUiStates {

    object Initial : SearchUiStates
    object Loading : SearchUiStates
    class Error(val error: SearchFragmentErrors) : SearchUiStates
    class Movies(val movies: List<Movie>) : SearchUiStates
}