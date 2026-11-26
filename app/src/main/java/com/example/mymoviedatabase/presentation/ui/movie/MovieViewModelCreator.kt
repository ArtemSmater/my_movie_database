package com.example.mymoviedatabase.presentation.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mymoviedatabase.domain.usecases.DeleteFavouriteMovieUseCase
import com.example.mymoviedatabase.domain.usecases.GetFavouriteMovieUseCase
import com.example.mymoviedatabase.domain.usecases.GetMovieReviewsUseCase
import com.example.mymoviedatabase.domain.usecases.GetMovieTrailerUseCase
import com.example.mymoviedatabase.domain.usecases.InsertFavouriteMovieUseCase
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelCreator
import javax.inject.Inject

class MovieViewModelCreator @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val insertFavouriteMovieUseCase: InsertFavouriteMovieUseCase,
    private val getFavouriteMovieUseCase: GetFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
) : ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return MovieViewModel(
            extras.createSavedStateHandle(),
            getMovieReviewsUseCase,
            getMovieTrailerUseCase,
            insertFavouriteMovieUseCase,
            getFavouriteMovieUseCase,
            deleteFavouriteMovieUseCase
        )
    }
}