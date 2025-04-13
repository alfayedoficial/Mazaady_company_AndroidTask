package com.afapps.mazaadyAndroidTask.presentation.features.home.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.afapps.mazaadyAndroidTask.BR
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.databinding.ItemRvMovieBinding
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.utilities.kuGetBindingRow

class MovieAdapter : PagingDataAdapter<Movie, MovieAdapter.ViewHolderMovie>(ItemComparator){

    var onItemClickListener: (Movie) -> Unit = {}
    var onItemFavClickListener: (Movie) -> Unit = {}

    object ItemComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }


    inner class ViewHolderMovie(val binding: ItemRvMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                setVariable(BR.model, movie)
                root.setOnClickListener { onItemClickListener(movie) }
                executePendingBindings()
            }
        }

    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolderMovie, position: Int) {
        val item = getItem(position)?:return
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie =
        ViewHolderMovie(kuGetBindingRow(parent, R.layout.item_rv_movie) as ItemRvMovieBinding)
}