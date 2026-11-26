package com.example.mymoviedatabase.presentation.ui.menu

sealed interface MenuEffects {
    data object SearchPage : MenuEffects
    data object CollectionPage : MenuEffects
    data object FavouritesPage : MenuEffects
    data object SettingsPage : MenuEffects
}