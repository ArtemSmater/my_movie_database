package com.example.mymoviedatabase.di

import com.example.mymoviedatabase.di.annotations.ViewModelKey
import com.example.mymoviedatabase.presentation.ui.favourites.FavouriteViewModelCreator
import com.example.mymoviedatabase.presentation.ui.favourites.FavouritesViewModel
import com.example.mymoviedatabase.presentation.ui.menu.MenuViewModel
import com.example.mymoviedatabase.presentation.ui.menu.MenuViewModelCreator
import com.example.mymoviedatabase.presentation.ui.movie.MovieViewModel
import com.example.mymoviedatabase.presentation.ui.movie.MovieViewModelCreator
import com.example.mymoviedatabase.presentation.ui.observer.ObserverViewModel
import com.example.mymoviedatabase.presentation.ui.observer.ObserverViewModelCreator
import com.example.mymoviedatabase.presentation.ui.search.SearchViewModel
import com.example.mymoviedatabase.presentation.ui.search.SearchViewModelCreator
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelCreator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    fun bindFavouriteViewModelCreator(impl: FavouriteViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    fun bindMenuViewModelCreator(impl: MenuViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    fun bindMovieViewModelCreator(impl: MovieViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(ObserverViewModel::class)
    fun bindObserverViewModelCreator(impl: ObserverViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModelCreator(impl: SearchViewModelCreator): ViewModelCreator
}