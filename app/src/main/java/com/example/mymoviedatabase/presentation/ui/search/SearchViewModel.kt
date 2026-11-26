package com.example.mymoviedatabase.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviedatabase.domain.entity.Movie
import com.example.mymoviedatabase.domain.usecases.GetMoviesByTitleUseCase
import com.example.mymoviedatabase.domain.usecases.GetNextTitledMoviesUseCase
import com.example.mymoviedatabase.presentation.ui.observer.ObserverUiEffects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel @Inject constructor(
    private val getMoviesByTitleUseCase: GetMoviesByTitleUseCase,
    private val getNextTitledMoviesUseCase: GetNextTitledMoviesUseCase
) : ViewModel() {

    // main search fragment screens
    private val _state = MutableStateFlow<SearchUiStates>(SearchUiStates.Initial)
    val state = _state.asStateFlow()

    // main search fragment effects
    private val _effects = MutableSharedFlow<ObserverUiEffects>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    // query for movie searching
    private val queryValue = MutableStateFlow("")

    // download process indicator
    private var isDownloading = false

    private lateinit var lastQuery: String

    init {
        getMovieList()
    }

    fun uiActions(action: SearchUiActions) {
        when (action) {
            is SearchUiActions.FieldChanged -> {
                queryValue.tryEmit(action.s.toString())
            }

            is SearchUiActions.MovieClicked -> {
                _effects.tryEmit(ObserverUiEffects.MoviePressed(action.movie))
            }

            is SearchUiActions.RetryQuery -> {
                retryQuery()
            }

            is SearchUiActions.BackPressed -> {
                _effects.tryEmit(ObserverUiEffects.BackPressed)
            }

            is SearchUiActions.SetInitial -> {
                _state.value = SearchUiStates.Initial
            }

            is SearchUiActions.LoadNextPage -> {
                getUploadedMovies()
            }
        }
    }

    private fun retryQuery() {
        viewModelScope.launch {
            createSearchRequest(lastQuery).collect {
                _state.value = it
            }
        }
    }

    private fun getUploadedMovies() {
        if (isDownloading) return
        isDownloading = true
        viewModelScope.launch {
            try {
                val movies = getNextTitledMoviesUseCase()
                _state.emit(SearchUiStates.Movies(movies))
            } catch (c: kotlinx.coroutines.CancellationException) {
                throw c
            } finally {
                isDownloading = false
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun getMovieList() {
        viewModelScope.launch {
            queryValue
                .debounce(2000.milliseconds)
                .map { it.trim() }
                .distinctUntilChanged()
                .flatMapLatest { createSearchObservable(it) }
                .collect { _state.value = it }
        }
    }

    private fun createSearchObservable(query: String): Flow<SearchUiStates> {
        return createSearchRequest(query).takeWhile { query.length > 2 }
    }

    private fun createSearchRequest(query: String): Flow<SearchUiStates> = flow {
        lastQuery = query
        emit(SearchUiStates.Loading)
        val movies = getMoviesByTitleUseCase(query)
        emit(createSuccessResponse(movies))
    }.catch {
        if (it is CancellationException) throw it
        emit(SearchUiStates.Error(SearchFragmentErrors.InternetConnection(null, null)))
    }

    private fun createSuccessResponse(movies: List<Movie>): SearchUiStates {
        return if (movies.isNotEmpty()) {
            SearchUiStates.Movies(movies)
        } else {
            SearchUiStates.Error(SearchFragmentErrors.EmptyResponse(null, null))
        }
    }
}