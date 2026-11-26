package com.example.mymoviedatabase.presentation.ui.movie

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mymoviedatabase.App
import com.example.mymoviedatabase.R
import com.example.mymoviedatabase.databinding.DialogReviewBinding
import com.example.mymoviedatabase.databinding.FragmentMovieBinding
import com.example.mymoviedatabase.domain.entity.Review
import com.example.mymoviedatabase.domain.entity.Trailer
import com.example.mymoviedatabase.presentation.ui.viewmodelfactory.ViewModelFactory
import com.example.mymoviedatabase.presentation.utils.FragmentTheme
import com.example.mymoviedatabase.presentation.utils.checkTheme
import kotlinx.coroutines.launch
import javax.inject.Inject


class MovieFragment : Fragment() {

    private val movie: MovieFragmentArgs by navArgs()
    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MovieViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding: FragmentMovieBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.getFragmentSubcomponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
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
        binding.movie = movie.selectedMovie
        observeActions()
        observeChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.effects.collect {
                        checkScreenEffects(it)
                    }
                }

                launch {
                    viewModel.isFavourite.collect {
                        setDrawableStar(checkFavouriteImage(it))
                    }
                }

                launch {
                    viewModel.reviews.collect {
                        setupReviewRecyclerView(it)

                    }
                }

                launch {
                    viewModel.trailers.collect {
                        setupTrailerRecyclerView(it)

                    }
                }
            }
        }
    }

    private fun observeActions() {
        with(binding) {
            tbMovie?.setNavigationOnClickListener {
                viewModel.uiActions(MovieUiActions.BackPressed)
            }

            ivToFavourite.setOnClickListener {
                viewModel.uiActions(MovieUiActions.AddToFavourite)
            }
        }
    }

    private fun setupReviewRecyclerView(list: List<Review>) {
        with(binding) {
            rvReviews.apply {
                if (list.isEmpty()) {
                    tvReviews.visibility = View.GONE
                    rvReviews.visibility = View.GONE
                } else {
                    tvReviews.visibility = View.VISIBLE
                    rvReviews.visibility = View.VISIBLE
                    val adapter = ReviewAdapter(list) {
                        showReviewDialog(it)
                    }
                    rvReviews.adapter = adapter
                }
            }
        }
    }

    private fun setupTrailerRecyclerView(list: List<Trailer>) {
        with(binding) {
            rvTrailers.apply {
                if (list.isEmpty()) {
                    tvTrailers.visibility = View.GONE
                    rvTrailers.visibility = View.GONE
                } else {
                    tvTrailers.visibility = View.VISIBLE
                    rvTrailers.visibility = View.VISIBLE
                    val adapter = TrailerAdapter(list)
                    rvTrailers.adapter = adapter
                    adapter.onTrailerClickListener = {
                        viewModel.uiActions(MovieUiActions.TrailerClicked(it.url.toString()))
                    }
                }
            }
        }
    }

    private fun showReviewDialog(review: Review) {
        val dialog = Dialog(requireContext())
        val dialogLayout = DialogReviewBinding.inflate(
            LayoutInflater.from(requireActivity()),
            binding.root as ViewGroup,
            false
        )

        dialog.setContentView(dialogLayout.root)
        dialogLayout.currentReview = review
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.setCanceledOnTouchOutside(false)
        dialogLayout.btnDialogExit.setOnClickListener { dialog.dismiss() }
        dialog.show()

        // Размер Dialog
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun checkFavouriteImage(isAdded: Boolean): Drawable? {
        return if (isAdded) {
            AppCompatResources.getDrawable(requireActivity(), R.drawable.pressed_star)
        } else {
            AppCompatResources.getDrawable(requireActivity(), R.drawable.unpressed_star)
        }
    }

    private fun setDrawableStar(drawable: Drawable?) {
        binding.ivToFavourite.setImageDrawable(drawable)
    }

    private fun checkScreenEffects(effect: MovieUiEffects) {
        when (effect) {
            is MovieUiEffects.BackPressed -> {
                findNavController().popBackStack()
            }

            is MovieUiEffects.OpenPlayer -> {
                startActivity(Intent(Intent.ACTION_VIEW, effect.url.toUri()))
            }
        }
    }
}