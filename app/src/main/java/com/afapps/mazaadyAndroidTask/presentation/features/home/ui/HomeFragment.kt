package com.afapps.mazaadyAndroidTask.presentation.features.home.ui

import android.content.Intent
import android.provider.Settings
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.R.*
import com.afapps.mazaadyAndroidTask.core.fragment.BaseFragment
import com.afapps.mazaadyAndroidTask.databinding.FragmentHomeBinding
import com.afapps.mazaadyAndroidTask.presentation.features.home.adapter.MovieAdapter
import com.afapps.mazaadyAndroidTask.presentation.features.home.adapter.MovieLoadStateAdapter
import com.afapps.mazaadyAndroidTask.presentation.features.home.viewModel.HomeViewModel
import com.afapps.mazaadyAndroidTask.presentation.ui.MainActivity
import com.afapps.mazaadyAndroidTask.utilities.kuHide
import com.afapps.mazaadyAndroidTask.utilities.kuInitGridLayoutManager
import com.afapps.mazaadyAndroidTask.utilities.kuInitLinearLayoutManager
import com.afapps.mazaadyAndroidTask.utilities.kuShow
import com.afapps.mazaadyAndroidTask.utilities.kuString
import com.afapps.mazaadyAndroidTask.utilities.setBaseActivityFragmentsToolbar
import com.afapps.mazaadyAndroidTask.utilities.setListenerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val mViewModel : HomeViewModel by viewModels()
    private val adapterMovieRv by lazy { MovieAdapter() }

    override val layoutResourceLayout: Int
        get() = layout.fragment_home

    override fun onFragmentCreated(dataBinder: FragmentHomeBinding) {
       dataBinder.apply {
           fragment = this@HomeFragment
           lifecycleOwner = this@HomeFragment

           homeToolbar.apply {
               setBaseActivityFragmentsToolbar(kuString(string.app_name), toolbar, tvNameToolbar)
           }

           rvHome.setListenerRecyclerView(activity as? MainActivity)
           initSwipeRefresh()
           btnLinearRv.setOnClickListener {
               mViewModel.updateRecyclerViewOrientation(RecyclerView.VERTICAL)
           }
           btnGridRv.setOnClickListener {
               mViewModel.updateRecyclerViewOrientation(RecyclerView.HORIZONTAL)
           }
       }
    }

    private fun FragmentHomeBinding.initSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            adapterMovieRv.refresh()
        }
    }

    private fun FragmentHomeBinding.initRecyclerview(orientation: Int) {
        rvHome.apply {
            val adapterWithFooter = adapterMovieRv.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { adapterMovieRv.retry() },
                footer = MovieLoadStateAdapter { adapterMovieRv.retry() }
            )


            if (orientation == RecyclerView.VERTICAL) {
                kuInitLinearLayoutManager(RecyclerView.VERTICAL , adapterWithFooter)
                dataBinder.btnLinearRv.updateStyle(isSelected = true)
                dataBinder.btnGridRv.updateStyle(isSelected = false)
            }else{
                kuInitGridLayoutManager(RecyclerView.VERTICAL , adapterWithFooter)
                dataBinder.btnGridRv.updateStyle(isSelected = true)
                dataBinder.btnLinearRv.updateStyle(isSelected = false)
            }

            adapterMovieRv.onItemClickListener = { movie ->
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToDetailsMovieFragment(movie.id!!))
            }

            adapterMovieRv.onItemFavClickListener = { movie ->
                mViewModel.updateFavouriteStatus(movie.id!! , !movie.isFavourite)
            }

        }

    }

    override fun setUpViewModelStateObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    mViewModel.networkConnectionManager.isNetworkConnectedFlow.collectLatest { status->
                        dataBinder.apply {
                            if (!status){
                                lyNoMovies.root.kuHide()
                                lyNoInternet.root.kuShow()
                                lyNoInternet.btnOpenConnection.setOnClickListener {
                                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                                }
                                rvHome.kuHide()
                            }else{
                                adapterMovieRv.refresh()
                            }
                        }

                    }
                }

                launch {
                    mViewModel.recyclerViewOrientation.collectLatest { orientation->
                        dataBinder.initRecyclerview(orientation)
                    }
                }

                launch {
                    mViewModel.discoverMovies.collectLatest {
                        adapterMovieRv.submitData(it)
                    }
                }

                launch {
                    adapterMovieRv.loadStateFlow.collectLatest { loadStates ->

                        dataBinder.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading

                        when (val refreshState = loadStates.refresh) {
                            is LoadState.Loading -> {
                                // Show shimmer or loading spinner
                            }
                            is LoadState.NotLoading -> {
                                val isEmptyList = adapterMovieRv.itemCount == 0
                                if (isEmptyList) {
                                    // Show "No Data Found" view
                                    dataBinder.apply {
                                        rvHome.kuHide()
                                        lyNoMovies.root.kuShow()
                                        lyNoInternet.root.kuHide()
                                    }
                                } else {
                                    // Show data list
                                    dataBinder.apply {
                                        rvHome.kuShow()
                                        lyNoMovies.root.kuHide()
                                        lyNoInternet.root.kuHide()
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                if (!mViewModel.networkConnectionManager.isNetworkConnected){
                                    dataBinder.apply {
                                        lyNoMovies.root.kuHide()
                                        lyNoInternet.root.kuShow()
                                        lyNoInternet.btnOpenConnection.setOnClickListener {
                                            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                                        }
                                        rvHome.kuHide()
                                    }
                                    return@collectLatest
                                }
                                // Show retry button and error message
                                val error = refreshState.error
                                // log error.localizedMessage
                                val isEmptyList = adapterMovieRv.itemCount == 0
                                if (isEmptyList) {
                                    // Show "No Data Found" view
                                    dataBinder.apply {
                                        rvHome.kuHide()
                                        lyNoMovies.root.kuShow()
                                        lyNoMovies.tvErrorMsg.text = error.localizedMessage
                                        lyNoMovies.btnRetry.kuShow()
                                        lyNoMovies.btnRetry.setOnClickListener {
                                            adapterMovieRv.retry()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun ImageButton.updateStyle(isSelected: Boolean) {
        val context = this.context
        val selectedBg = R.drawable.selected_button_background
        val unSelectedBg = R.drawable.un_selected_button_background
        val selectedTint = color.white
        val unSelectedTint = color.black

        setBackgroundResource(if (isSelected) selectedBg else unSelectedBg)
        imageTintList = ContextCompat.getColorStateList(context, if (isSelected) selectedTint else unSelectedTint)
    }


}