package com.afapps.mazaadyAndroidTask.presentation.features.detailsMove

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.R.layout
import com.afapps.mazaadyAndroidTask.core.fragment.BaseFragment
import com.afapps.mazaadyAndroidTask.databinding.FragmentDetailsMovieBinding
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.state.MovieState
import com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.viewModel.MovieDetailsViewModel
import com.afapps.mazaadyAndroidTask.utilities.isFavouriteMovie
import com.afapps.mazaadyAndroidTask.utilities.kuHide
import com.afapps.mazaadyAndroidTask.utilities.kuRes
import com.afapps.mazaadyAndroidTask.utilities.kuShow
import com.afapps.mazaadyAndroidTask.utilities.kuString
import com.afapps.mazaadyAndroidTask.utilities.setBaseActivityFragmentsToolbar
import com.afapps.mazaadyAndroidTask.utilities.setBindString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val MOVIE_ID = "movie_id"

@AndroidEntryPoint
class DetailsMovieFragment : BaseFragment<FragmentDetailsMovieBinding>() {

    private val mViewModel : MovieDetailsViewModel by viewModels()

    override val layoutResourceLayout: Int
        get() = layout.fragment_details_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = arguments?.getLong(MOVIE_ID)
        movieId?.let {
            mViewModel.getMovieDetails(movieId)
        }?: run {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }

    }

    override fun onFragmentCreated(dataBinder: FragmentDetailsMovieBinding) {
       dataBinder.apply {
           fragment = this@DetailsMovieFragment
           lifecycleOwner = this@DetailsMovieFragment
           movieDetailsToolbar.apply {
               setBaseActivityFragmentsToolbar(getString(R.string.movie_details), toolbar, tvNameToolbar)
           }
       }
    }


    override fun setUpViewModelStateObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.movieState.collectLatest { state ->
                        when (state) {
                            is MovieState.Loading -> {
                                // Show loading indicator
                                dataBinder.apply {
                                    progressBar.kuShow()
                                    lyMovieDetails.kuHide()
                                }
                            }
                            is MovieState.Success -> {
                                // Hide loading indicator
                                val value = state.movie
                                dataBinder.apply {
                                    progressBar.kuHide()
                                    lyMovieDetails.kuShow()
                                    detailsMovie(movie = value!!)
                                }
                            }

                            is MovieState.Error -> {
                                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FragmentDetailsMovieBinding.detailsMovie(movie: Movie){
        poster.load(movie.getImagePath()) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            error(R.drawable.error_placeholder)
        }
        btnFav.setOnClickListener {
            mViewModel.updateFavouriteStatus(movie.id!!, movie)
        }
        tvTitle.setBindString(movie.title)
        tvDate.setBindString(movie.releaseDate)
        val formattedRate = kuRes.getString(R.string.rate, 10, movie.voteAverage, movie.voteCount)
        tvRate.setBindString(formattedRate)
        tvOverview.setBindString(movie.overview)
        btnFav.isFavouriteMovie(movie.isFavourite)

    }

}