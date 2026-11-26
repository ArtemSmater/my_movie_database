package com.example.mymoviedatabase.presentation.ui.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviedatabase.App
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.databinding.FragmentSearchBinding
import com.example.mymoviedatabase.presentation.ui.observer.ObserverUiEffects
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelFactory
import com.example.mymoviedatabase.presentation.utils.FragmentTheme
import com.example.mymoviedatabase.presentation.utils.Transform
import com.example.mymoviedatabase.presentation.utils.checkTheme
import com.example.mymoviedatabase.presentation.utils.hideKeyboard
import com.example.mymoviedatabase.presentation.utils.moveGuideline
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max


class FragmentSearch : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }
    private val movieAdapter by lazy {
        SearchAdapter()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("Unable to find FragmentSearchBinding")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.getFragmentSubcomponent().inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            requireActivity().window.isNavigationBarContrastEnforced = false
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime())
            val maxValue = max(
                Transform.dpToPx(bars.bottom.toFloat(), requireActivity()),
                Transform.dpToPx(210f, requireActivity()) - imeHeight.bottom
            )
            moveGuideline(maxValue, v)
            v.updatePadding(top = bars.top, bottom = max(bars.bottom, imeHeight.bottom))
            insets
        }
        recyclerSettings()
        observeActions()
        observeChanges()
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        checkUiStates(it)
                    }
                }

                launch {
                    viewModel.effects.collect {
                        checkUiEffects(it)
                    }
                }
            }
        }
    }

    private fun recyclerSettings() {
        with(binding) {
            rvMovies.adapter = movieAdapter
            rvMovies.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy <= 0) return
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                        val totalItems = layoutManager.itemCount
                        if (lastVisiblePosition >= totalItems - 4) {
                            viewModel.uiActions(SearchUiActions.LoadNextPage)
                        }
                    }
                }
            )
        }
    }

    private fun observeActions() {
        with(binding) {
            rvMovies.adapter = movieAdapter
            ivClear.isVisible = etSearch.text.isNotEmpty()
            etSearch.doOnTextChanged { text, _, _, _ ->
                ivClear.isVisible = !text.isNullOrEmpty()
                viewModel.uiActions(SearchUiActions.FieldChanged(text))
            }

            tbSearch.setNavigationOnClickListener {
                viewModel.uiActions(SearchUiActions.BackPressed)
            }

            ivClear.setOnClickListener {
                viewModel.uiActions(SearchUiActions.SetInitial)
            }

            btnToUpload.setOnClickListener {
                viewModel.uiActions(SearchUiActions.RetryQuery)
            }

            movieAdapter.onTrackClickListener = {
                viewModel.uiActions(SearchUiActions.MovieClicked(it))
            }
        }
    }

    private fun checkUiEffects(effect: ObserverUiEffects) {
        when (effect) {
            is ObserverUiEffects.BackPressed -> {
                findNavController().popBackStack()
            }

            is ObserverUiEffects.MoviePressed -> {
                findNavController().navigate(
                    FragmentSearchDirections.actionFragmentSearchToMovieFragment(effect.movie)
                )
            }
        }
    }

    private fun checkUiStates(state: SearchUiStates) {
        when (state) {
            is SearchUiStates.Initial -> {
                defaultField(binding.etSearch)
                binding.pbSearch.isVisible = false
                checkErrorState(SearchFragmentErrors.HideSearchErrors)
                movieAdapter.submitList(emptyList())
            }

            is SearchUiStates.Loading -> {
                binding.etSearch.hideKeyboard(requireActivity())
                binding.pbSearch.isVisible = true
                checkErrorState(SearchFragmentErrors.HideSearchErrors)
            }

            is SearchUiStates.Movies -> {
                binding.pbSearch.isVisible = false
                checkErrorState(SearchFragmentErrors.HideSearchErrors)
                movieAdapter.submitList(state.movies)
            }

            is SearchUiStates.Error -> {
                movieAdapter.submitList(emptyList())
                binding.pbSearch.isVisible = false
                when (state.error) {
                    is SearchFragmentErrors.EmptyResponse -> checkErrorState(
                        SearchFragmentErrors.EmptyResponse(
                            getErrorString(false),
                            getDrawableRes(false)
                        )
                    )

                    is SearchFragmentErrors.InternetConnection -> checkErrorState(
                        SearchFragmentErrors.InternetConnection(
                            getErrorString(true),
                            getDrawableRes(true)
                        )
                    )

                    is SearchFragmentErrors.HideSearchErrors -> checkErrorState(
                        SearchFragmentErrors.HideSearchErrors
                    )
                }
            }
        }
    }

    private fun getDrawableRes(isConnection: Boolean): Drawable? {
        return if (isConnection) {
            AppCompatResources.getDrawable(requireActivity(), R.drawable.error_movie_light)
        } else {
            AppCompatResources.getDrawable(requireActivity(), R.drawable.empty_movie_light)
        }
    }

    private fun getErrorString(isConnection: Boolean): String {
        return if (isConnection) {
            requireActivity().getString(R.string.internet_error)
        } else {
            requireActivity().getString(R.string.download_error)
        }
    }

    private fun defaultField(editText: EditText) {
        editText.hideKeyboard(requireActivity())
        editText.setText("")
        editText.clearFocus()
    }

    private fun checkErrorState(error: SearchFragmentErrors) {
        with(binding) {
            ivErrorPlaceholder.isVisible = error.ivErrorVisibility
            tvErrorMessage.isVisible = error.tvErrorVisibility
            btnToUpload.isVisible = error.btErrorVisibility
            ivErrorPlaceholder.setImageDrawable(error.drawableRes)
            tvErrorMessage.text = error.stringRes
        }
    }
}