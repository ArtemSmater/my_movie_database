package com.example.mymoviedatabase.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelCreator
import javax.inject.Inject
import javax.inject.Provider

class SearchViewModelCreator @Inject constructor(
    private val provider: Provider<SearchViewModel>
) : ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return provider.get()
    }
}