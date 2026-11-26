package com.example.mymoviedatabase.presentation.ui.observer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.entity.MovieCategories
import com.example.mymoviedatabase.domain.usecases.GetInitialMoviesUseCase
import com.example.mymoviedatabase.domain.usecases.GetMoviesByCategoryUseCase
import com.example.mymoviedatabase.domain.usecases.GetUploadedMoviesUseCase
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ObserverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val getInitialMoviesUseCase: GetInitialMoviesUseCase,
    private val getUploadedMoviesUseCase: GetUploadedMoviesUseCase
) : ViewModel() {

    private val categoryName: String = checkNotNull(
        savedStateHandle.get<String>("category")
    ) { "Movie category was not passed to CategoriesFragment" }

    private val category: MovieCategories = MovieCategories.entries.first {
        it.value == categoryName
    }

    // double downloader helper
    private var isDownloading = false

    // short actions for view observers
    private val _effects = MutableSharedFlow<ObserverUiEffects>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    // movies for vies
    private val _state = MutableStateFlow<ObserverUiState>(ObserverUiState.Loading)
    val state = _state.asStateFlow()

    init {
        downloadInitialPage(category)
    }

    fun uiActions(action: ObserverUiActions) {
        when (action) {
            is ObserverUiActions.SelectedCategory -> {
                selectCategory(action.p0)
            }

            is ObserverUiActions.LoadNextPage -> {
                downloadNextPage()
            }

            is ObserverUiActions.SelectedMovie -> {
                selectedMovie(action.movie)
            }

            is ObserverUiActions.BackPressed -> {
                backPressed()
            }
        }
    }

    private fun selectCategory(p0: TabLayout.Tab?) {
        val position = p0?.position ?: 0
        downloadInitialPage(MovieCategories.entries[position])
    }

    private fun downloadInitialPage(category: MovieCategories) {
        viewModelScope.launch {
            getMoviesByCategoryUseCase(category)
                .map { movies ->
                    parseToScreenState(movies)
                }
                .onStart {
                    _state.value = ObserverUiState.Loading
                }
                .catch { _ ->
                    _state.value = ObserverUiState.Error
                }
                .collect { newState ->
                    _state.value = newState
                }
        }

        viewModelScope.launch {
            try {
                getInitialMoviesUseCase(category)
            } catch (c: CancellationException) {
                throw c
            } catch (e: Exception) {
                Log.e("MY_TAG", "Network refresh failed", e)
            }
        }
    }


    private fun downloadNextPage() {
        if (isDownloading) return
        isDownloading = true
        viewModelScope.launch {
            try {
                getUploadedMoviesUseCase()
            } catch (c: CancellationException) {
                throw c
            } catch (e: Exception) {
                Log.e("MY_TAG", "Loading next page failed ${e.message}", e)
            } finally {
                isDownloading = false
            }
        }
    }

    private fun parseToScreenState(movies: List<Movie>): ObserverUiState {
        return if (movies.isNotEmpty()) {
            ObserverUiState.ShowMovies(movies)
        } else {
            ObserverUiState.EmptyList
        }
    }

    private fun selectedMovie(movie: Movie) {
        _effects.tryEmit(ObserverUiEffects.MoviePressed(movie))
    }

    private fun backPressed() {
        _effects.tryEmit(ObserverUiEffects.BackPressed)
    }
}