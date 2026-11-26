package com.example.mymoviedatabase.presentation.ui.menu

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.mymoviedatabase.App
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.databinding.FragmentMenuBinding
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelFactory
import com.example.mymoviedatabase.presentation.utils.configureSystemBars
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentMenu : Fragment(), View.OnClickListener {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MenuViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding ?: throw RuntimeException("FragmentMenuBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.getFragmentSubcomponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
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
        configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
        observeChanges()
        observeActions()
    }

    fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.effects.collect {
                        checkMenuEffects(it)
                    }
                }
            }
        }
    }

    fun observeActions() {
        with(binding) {
            btnSearchScreen.setOnClickListener(this@FragmentMenu)
            btnObserverScreen.setOnClickListener(this@FragmentMenu)
            btnFavouriteScreen.setOnClickListener(this@FragmentMenu)
            btnSettingsScreen.setOnClickListener(this@FragmentMenu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkMenuEffects(effects: MenuEffects) {
        when (effects) {
            is MenuEffects.SearchPage -> {
                findNavController().navigate(
                    FragmentMenuDirections.actionFragmentMenuToFragmentSearch()
                )
            }

            is MenuEffects.CollectionPage -> {
                findNavController().navigate(
                    FragmentMenuDirections.actionFragmentMenuToFragmentObserver()
                )
            }

            is MenuEffects.FavouritesPage -> {
                findNavController().navigate(
                    FragmentMenuDirections.actionFragmentMenuToFragmentFavourites()
                )
            }

            is MenuEffects.SettingsPage -> {
                findNavController().navigate(
                    FragmentMenuDirections.actionFragmentMenuToFragmentSettings()
                )
            }
        }
    }

    override fun onClick(p0: View?) {
        when ((p0 as Button).id) {
            R.id.btnSearchScreen -> {
                viewModel.uiAction(MenuActions.SearchSelected)
            }

            R.id.btnObserverScreen -> {
                viewModel.uiAction(MenuActions.CollectionSelected)
            }

            R.id.btnFavouriteScreen -> {
                viewModel.uiAction(MenuActions.FavouritesSelected)
            }

            R.id.btnSettingsScreen -> {
                viewModel.uiAction(MenuActions.SettingsSelections)
            }
        }
    }
}