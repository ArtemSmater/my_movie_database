package com.example.mymoviedatabase.presentation.ui.observer.container

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mymoviedatabase.domain.entity.MovieCategories

class CategoryPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        val category = MovieCategories.entries[position].value
        val fragment = CategoriesFragment.newInstance(category)
        return fragment
    }

    override fun getItemCount(): Int {
        return MovieCategories.entries.size
    }
}