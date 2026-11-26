package com.example.mymoviedatabase.presentation.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelCreator
import javax.inject.Inject
import javax.inject.Provider

class MenuViewModelCreator @Inject constructor(
    private val provider: Provider<MenuViewModel>
): ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return provider.get()
    }
}