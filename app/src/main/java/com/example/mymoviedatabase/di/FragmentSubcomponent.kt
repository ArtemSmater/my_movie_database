package com.example.mymoviedatabase.di

import com.example.mymoviedatabase.di.annotations.FragmentScope
import com.example.mymoviedatabase.presentation.ui.favourites.FragmentFavourites
import com.example.mymoviedatabase.presentation.ui.menu.FragmentMenu
import com.example.mymoviedatabase.presentation.ui.movie.MovieFragment
import com.example.mymoviedatabase.presentation.ui.observer.container.CategoriesFragment
import com.example.mymoviedatabase.presentation.ui.search.FragmentSearch
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [DomainModule::class, ViewModelModule::class])
interface FragmentSubcomponent {

    fun inject(fragment: FragmentFavourites)
    fun inject(fragment: FragmentMenu)
    fun inject(fragment: MovieFragment)
    fun inject(fragment: CategoriesFragment)
    fun inject(fragment: FragmentSearch)
}