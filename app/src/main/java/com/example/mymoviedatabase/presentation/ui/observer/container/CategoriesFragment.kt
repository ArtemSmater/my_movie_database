package com.example.mymoviedatabase.presentation.ui.observer.container

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviedatabase.App
import com.example.mymoviedatabase.databinding.FragmentCategoriesBinding
import com.example.mymoviedatabase.presentation.ui.observer.FragmentObserverDirections
import com.example.mymoviedatabase.presentation.ui.observer.MovieAdapter
import com.example.mymoviedatabase.presentation.ui.observer.ObserverUiActions
import com.example.mymoviedatabase.presentation.ui.observer.ObserverUiEffects
import com.example.mymoviedatabase.presentation.ui.observer.ObserverUiState
import com.example.mymoviedatabase.presentation.ui.observer.ObserverViewModel
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

class CategoriesFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ObserverViewModel by viewModels {
        viewModelFactory
    }

    private val adapter by lazy {
        MovieAdapter()
    }

    private var _binding: FragmentCategoriesBinding? = null
    private val binding: FragmentCategoriesBinding
        get() = _binding ?: throw RuntimeException("FragmentCategoriesBinding is null!")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.getFragmentSubcomponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerSettings()
        observeChanges()
        observeActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun recyclerSettings() {
        with(binding) {
            rvCategoryFilms.layoutManager = GridLayoutManager(requireActivity(), getColumnCount())
            rvCategoryFilms.adapter = adapter
            rvCategoryFilms.addOnScrollListener(
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
                            viewModel.uiActions(ObserverUiActions.LoadNextPage)
                        }
                    }
                }
            )
        }
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        checkStates(it)
                    }
                }

                launch {
                    viewModel.effects.collect {
                        checkEffects(it)
                    }
                }
            }
        }
    }

    private fun checkStates(state: ObserverUiState) {
        with(binding) {
            when (state) {
                is ObserverUiState.Loading -> {
                    pbCategory.isVisible = true
                    showError(false)
                }

                is ObserverUiState.EmptyList -> {
                    pbCategory.isVisible = false
                    adapter.submitList(emptyList())
                    showError(true)
                }

                is ObserverUiState.ShowMovies -> {
                    pbCategory.isVisible = false
                    showError(false)
                    adapter.submitList(state.movies)
                }

                is ObserverUiState.Error -> {
                    Toast.makeText(
                        requireActivity(),
                        "Internet problems!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showError(visibility: Boolean) {
        binding.ivCategoryError.isVisible = visibility
        binding.tvErrorMessage.isVisible = visibility
    }

    private fun checkEffects(effect: ObserverUiEffects) {
        when (effect) {
            is ObserverUiEffects.MoviePressed -> {
                findNavController().navigate(
                    FragmentObserverDirections.actionFragmentObserverToMovieFragment(effect.movie)
                )
            }

            is ObserverUiEffects.BackPressed -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeActions() {
        adapter.onTrackClickListener = {
            viewModel.uiActions(ObserverUiActions.SelectedMovie(it))
        }
    }

    private fun getColumnCount(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return max(width / 185, 2)
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String) =
            CategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
    }
}