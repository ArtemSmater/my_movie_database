package com.example.mymoviedatabase.presentation.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviedatabase.databinding.TrailerItemBinding
import com.example.mymoviedatabase.domain.entity.Trailer

class TrailerAdapter(private val trailers: List<Trailer>) :
    RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {
    var onTrailerClickListener: ((Trailer) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailerViewHolder {
        val binding = TrailerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrailerViewHolder,
        position: Int
    ) {
        val trailer = trailers[position]
        holder.bind(trailer)
        holder.itemView.setOnClickListener { onTrailerClickListener?.invoke(trailer) }
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    class TrailerViewHolder(private val binding: TrailerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Trailer) {
            with(binding) {
                tvTrailerName.apply {
                    text = item.name
                    isSelected = true
                }
            }
        }
    }
}