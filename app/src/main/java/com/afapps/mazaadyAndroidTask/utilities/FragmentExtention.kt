package com.afapps.mazaadyAndroidTask.utilities

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afapps.mazaadyAndroidTask.core.activity.BaseActivity
import com.afapps.mazaadyAndroidTask.presentation.ui.MainActivity

fun Fragment.setBaseActivityFragmentsToolbar(title: String, toolbar: Toolbar, textView: TextView ) {
    ( activity as BaseActivity<*>).apply {
        setHasOptionsMenu( true)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        if (navController != null && bottomBarConfiguration != null) {
            toolbar.setupWithNavController(navController!!, bottomBarConfiguration!!)
        }
        textView.text = title
    }
}

fun RecyclerView.setListenerRecyclerView(activity: MainActivity?) {
    setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        activity?.apply {
            if (scrollY > oldScrollY) {
                getDataBinder().navBottomAppBar.kuHide()
            } else {
                getDataBinder().navBottomAppBar.kuShow()
            }
        }
    }
}

/**
 * @author Ali Al Fayed
 * @param RecyclerView
 * @param orientation Layout orientation. Should be {HORIZONTAL} or {VERTICAL}.
 * @param rvAdapter Adapter of RecyclerView
 * @param aReverseLayout When set to true, layouts from end to start
 * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
 * @see  : init LinearLayout and Adapter
 */
fun RecyclerView.kuInitLinearLayoutManager(
    orientation: Int,
    rvAdapter: RecyclerView.Adapter<*>,
    aReverseLayout: Boolean = false ,
    hasFixedSize : Boolean = true
) {
    this.apply {
        layoutManager = LinearLayoutManager(context, orientation, aReverseLayout)
        setHasFixedSize(hasFixedSize)
        adapter = rvAdapter
    }
}


/**
 * @author Ali Al Fayed
 * @param RecyclerView
 * @param orientation Layout orientation. Should be {HORIZONTAL} or {VERTICAL}.
 * @param rvAdapter Adapter of RecyclerView
 * @param aReverseLayout When set to true, layouts from end to start
 * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
 * @param spanCount number of columns
 * @see  : init Grid and Adapter
 */
fun RecyclerView.kuInitGridLayoutManager(
    orientation: Int,
    rvAdapter: RecyclerView.Adapter<*>,
    aReverseLayout: Boolean = false,
    hasFixedSize : Boolean = true,
    spanCount: Int = 2
) {
    this.apply {
        layoutManager = GridLayoutManager(context, spanCount, orientation, aReverseLayout)
        setHasFixedSize(hasFixedSize)
        adapter = rvAdapter
    }
}