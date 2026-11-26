package com.example.mymoviedatabase.presentation.ui.observer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mymoviedatabase.domain.usecases.GetInitialMoviesUseCase
import com.example.mymoviedatabase.domain.usecases.GetMoviesByCategoryUseCase
import com.example.mymoviedatabase.domain.usecases.GetUploadedMoviesUseCase
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelCreator
import javax.inject.Inject

class ObserverViewModelCreator @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val getInitialMoviesUseCase: GetInitialMoviesUseCase,
    private val getUploadedMoviesUseCase: GetUploadedMoviesUseCase
) : ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return ObserverViewModel(
            extras.createSavedStateHandle(),
            getMoviesByCategoryUseCase,
            getInitialMoviesUseCase,
            getUploadedMoviesUseCase
        )
    }
}