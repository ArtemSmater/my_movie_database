package com.example.mymoviedatabase.presentation.ui.favourites

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviedatabase.App
import com.example.mymoviedatabase.databinding.FragmentFavouritesBinding
import com.example.mymoviedatabase.presentation.ui.search.SearchAdapter
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelFactory
import com.example.mymoviedatabase.presentation.utils.FragmentTheme
import com.example.mymoviedatabase.presentation.utils.checkTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentFavourites : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FavouritesViewModel by viewModels {
        viewModelFactory
    }

    private val favouritesAdapter by lazy {
        SearchAdapter()
    }

    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavouritesBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.getFragmentSubcomponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            requireActivity().window.isNavigationBarContrastEnforced = false
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }

        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
        recyclerSettings()
        observeActions()
        observeChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.observableMovies.collect {
                        favouritesAdapter.submitList(it)
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

    private fun checkEffects(effect: FavouritesUiEffects) {
        when (effect) {
            is FavouritesUiEffects.BackPressed -> {
                findNavController().popBackStack()
            }

            is FavouritesUiEffects.SelectMovie -> {
                findNavController().navigate(
                    FragmentFavouritesDirections.actionFragmentFavouritesToMovieFragment(
                        effect.movie
                    )
                )
            }
        }
    }

    private fun observeChanges() {
        binding.tbFavourites.setNavigationOnClickListener {
            viewModel.uiActions(FavouritesUiActions.BackPressed)
        }

        favouritesAdapter.onTrackClickListener = {
            viewModel.uiActions(FavouritesUiActions.SelectMovie(it))
        }
    }

    private fun recyclerSettings() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val item = favouritesAdapter.currentList[viewHolder.absoluteAdapterPosition]
                viewModel.uiActions(FavouritesUiActions.RemoveMovie(item))
            }
        }

        binding.rvFavourites.adapter = favouritesAdapter
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvFavourites)
    }
}