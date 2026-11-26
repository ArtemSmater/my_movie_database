package com.example.mymoviedatabase.presentation.ui.observer

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
import androidx.navigation.fragment.findNavController
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.databinding.FragmentObserverBinding
import com.example.mymoviedatabase.domain.entity.MovieCategories
import com.example.mymoviedatabase.presentation.ui.observer.container.CategoryPagerAdapter
import com.example.mymoviedatabase.presentation.utils.FragmentTheme
import com.example.mymoviedatabase.presentation.utils.checkTheme
import com.google.android.material.tabs.TabLayoutMediator

class FragmentObserver : Fragment() {
    private var _binding: FragmentObserverBinding? = null
    private val binding: FragmentObserverBinding
        get() = _binding ?: throw RuntimeException("Unable to find FragmentObserverBinding")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObserverBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            requireActivity().window.isNavigationBarContrastEnforced = false
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        setViewPagerSettings()
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = false))
    }

    private fun setViewPagerSettings() {
        with(binding) {
            val adapter = CategoryPagerAdapter(this@FragmentObserver)
            vp2CategoryContainer.adapter = adapter
            TabLayoutMediator(tlObserver, vp2CategoryContainer) { tab, position ->
                tab.text = getString(
                    when (MovieCategories.entries[position]) {
                        MovieCategories.NOW_PLAYING -> R.string.playing
                        MovieCategories.UPCOMING -> R.string.upcoming
                        MovieCategories.TOP_RATED -> R.string.top_rated
                        MovieCategories.POPULAR -> R.string.popular
                    }
                )
            }.attach()

            tbObserver.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}