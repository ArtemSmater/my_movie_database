package com.example.mymoviedatabase.presentation.ui.observer

import com.example.mymoviedatabase.domain.entity.Movie
import com.google.android.material.tabs.TabLayout

sealed class ObserverUiActions {

    object BackPressed : ObserverUiActions()
    object LoadNextPage : ObserverUiActions()
    class SelectedCategory(val p0: TabLayout.Tab?) : ObserverUiActions()
    class SelectedMovie(val movie: Movie) : ObserverUiActions()
}