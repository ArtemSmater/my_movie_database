package com.example.mymoviedatabase.presentation.ui.menu

sealed interface MenuActions {

    data object SearchSelected : MenuActions
    data object CollectionSelected : MenuActions
    data object FavouritesSelected : MenuActions
    data object SettingsSelections : MenuActions
}