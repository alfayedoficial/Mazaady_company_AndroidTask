package com.afapps.mazaadyAndroidTask.presentation.ui

import android.view.Gravity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.R.id
import com.afapps.mazaadyAndroidTask.core.activity.BaseActivity
import com.afapps.mazaadyAndroidTask.databinding.ActivityMainBinding
import com.afapps.mazaadyAndroidTask.utilities.kuShowIf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main


    override fun onActivityCreated(dataBinder: ActivityMainBinding) {
        dataBinder.apply {
            activity = this@MainActivity
            lifecycleOwner = this@MainActivity

            val navHostFragment =
                supportFragmentManager.findFragmentById(id.navMainGraph) as NavHostFragment
            navController = navHostFragment.navController

            bottomBarConfiguration = AppBarConfiguration(
                topLevelDestinationIds = setOf(
                    // set all your top level destinations in here
                    id.homeFragment,
                    id.favouritesFragment,
                )
            )

            navBottomAppBar.setupWithNavController(navController!!)


            // listen to navigation events
            navController?.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    id.detailsMovieFragment -> {
                        // hide the bottom navigation
                        toggleLyNavBottom(false)
                    }

                    else -> {
                        // show the bottom navigation
                        toggleLyNavBottom(true)
                    }

                }
            }

        }
    }


    private fun toggleLyNavBottom(visibilityStatus: Boolean) {
        dataBinder.apply {
            val transition: Transition = Slide(Gravity.BOTTOM)
            transition.duration = 600
            transition.addTarget(navBottomAppBar)
            TransitionManager.beginDelayedTransition(lyParent, transition)
            navBottomAppBar.kuShowIf(visibilityStatus)
        }
    }


    fun getDataBinder(): ActivityMainBinding {
        return dataBinder
    }
}