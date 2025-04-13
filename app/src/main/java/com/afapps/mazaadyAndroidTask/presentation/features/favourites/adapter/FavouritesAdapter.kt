package com.afapps.mazaadyAndroidTask.presentation.features.favourites.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.afapps.mazaadyAndroidTask.BR
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.core.adapter.BaseViewHolder
import com.afapps.mazaadyAndroidTask.databinding.ItemRvMovieBinding
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.utilities.kuGetBindingRow

class FavouritesAdapter : ListAdapter<Movie, FavouritesAdapter.FavouritesViewHolder>(ItemComparator) {

    var onItemClickListener: (Movie) -> Unit = {}
    var onItemFavClickListener: (Movie) -> Unit = {}

    object ItemComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }


    inner class FavouritesViewHolder(val binding: ItemRvMovieBinding) : BaseViewHolder<Movie>(binding) {

        override fun bind(item: Movie) {
            binding.apply {
                setVariable(BR.model, item)
                root.setOnClickListener { onItemClickListener(item) }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(kuGetBindingRow(parent, R.layout.item_rv_movie) as ItemRvMovieBinding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(item)
            itemView.tag = item
            binding.poster.load(item.getImagePath()) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                error(R.drawable.error_placeholder)
            }
            binding.btnFav.setOnClickListener { onItemFavClickListener(item) }
        }
    }

}

