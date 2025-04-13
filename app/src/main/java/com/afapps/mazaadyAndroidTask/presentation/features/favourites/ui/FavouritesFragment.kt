package com.afapps.mazaadyAndroidTask.presentation.features.favourites.ui


import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.afapps.mazaadyAndroidTask.R.*
import com.afapps.mazaadyAndroidTask.core.fragment.BaseFragment
import com.afapps.mazaadyAndroidTask.databinding.FragmentFavouritesBinding
import com.afapps.mazaadyAndroidTask.presentation.features.favourites.adapter.FavouritesAdapter
import com.afapps.mazaadyAndroidTask.presentation.features.favourites.viewModel.FavouritesViewModel
import com.afapps.mazaadyAndroidTask.presentation.ui.MainActivity
import com.afapps.mazaadyAndroidTask.utilities.kuHide
import com.afapps.mazaadyAndroidTask.utilities.kuInitLinearLayoutManager
import com.afapps.mazaadyAndroidTask.utilities.kuShow
import com.afapps.mazaadyAndroidTask.utilities.kuString
import com.afapps.mazaadyAndroidTask.utilities.setBaseActivityFragmentsToolbar
import com.afapps.mazaadyAndroidTask.utilities.setListenerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>(){

    private val mViewModel : FavouritesViewModel by viewModels()
    private val adapterFavouritesRv by lazy { FavouritesAdapter() }

    override val layoutResourceLayout: Int
        get() = layout.fragment_favourites

    override fun onFragmentCreated(dataBinder: FragmentFavouritesBinding) {
       dataBinder.apply {
           fragment = this@FavouritesFragment
           lifecycleOwner = this@FavouritesFragment

           favouritesToolbar.apply {
               setBaseActivityFragmentsToolbar(kuString(string.favourites), toolbar, tvNameToolbar)
           }

           rvFavourites.setListenerRecyclerView(activity as? MainActivity)
           initRecyclerview()
       }
    }

    override fun setUpViewModelStateObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.favouritesMovies.collect {
                        if (it.isEmpty()) {
                            dataBinder.apply {
                                lyNoFavourites.root.kuShow()
                                lyNoFavourites.btnSelectNow.setOnClickListener {
                                    navController.navigate(FavouritesFragmentDirections.actionFavouritesFragmentToHomeFragment())
                                }
                                rvFavourites.kuHide()
                            }
                        } else {
                            dataBinder.apply {
                                lyNoFavourites.root.kuHide()
                                rvFavourites.kuShow()
                            }
                        }
                        adapterFavouritesRv.submitList(it)
                    }
                }
            }
        }
    }


    private fun FragmentFavouritesBinding.initRecyclerview() {
        rvFavourites.apply {

            kuInitLinearLayoutManager(RecyclerView.VERTICAL , adapterFavouritesRv)

            adapterFavouritesRv.onItemClickListener = { movie ->
                navController.navigate(FavouritesFragmentDirections.actionFavouritesFragmentToDetailsMovieFragment(movie.id!!))
            }

            adapterFavouritesRv.onItemFavClickListener = { movie ->
                mViewModel.removeMovieFromFavourites(movie.id!!)
            }

        }

    }

}