package com.example.mymoviedatabase.di

import com.example.mymoviedatabase.data.repository.CategoryMovieRepositoryImpl
import com.example.mymoviedatabase.data.repository.SearchMovieRepositoryImpl
import com.example.mymoviedatabase.di.annotations.ApplicationScope
import com.example.mymoviedatabase.di.annotations.FragmentScope
import com.example.mymoviedatabase.domain.repository.CategoryMovieRepository
import com.example.mymoviedatabase.domain.repository.SearchMovieRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    @FragmentScope
    fun bindCategoryRepository(impl: CategoryMovieRepositoryImpl): CategoryMovieRepository

    @Binds
    @FragmentScope
    fun bindSearchRepository(impl: SearchMovieRepositoryImpl): SearchMovieRepository
}