package com.example.mymoviedatabase.presentation.ui.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


class MenuViewModel @Inject constructor() : ViewModel() {

    private val _effects = MutableSharedFlow<MenuEffects>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun uiAction(actions: MenuActions) {
        when (actions) {
            is MenuActions.SearchSelected -> {
                _effects.tryEmit(MenuEffects.SearchPage)
            }

            is MenuActions.CollectionSelected -> {
                _effects.tryEmit(MenuEffects.CollectionPage)
            }

            is MenuActions.FavouritesSelected -> {
                _effects.tryEmit(MenuEffects.FavouritesPage)
            }

            is MenuActions.SettingsSelections -> {
                _effects.tryEmit(MenuEffects.SettingsPage)
            }
        }
    }
}