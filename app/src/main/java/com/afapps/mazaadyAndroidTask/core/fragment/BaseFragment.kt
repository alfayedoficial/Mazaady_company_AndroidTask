package com.afapps.mazaadyAndroidTask.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController


/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/4/2026 - 10:25 PM
 */
abstract class BaseFragment<T> : Fragment() where T : ViewDataBinding {

    @get:LayoutRes
    protected abstract val layoutResourceLayout: Int

    private var _navController: NavController? = null
    protected val navController: NavController
        get() = _navController!!

    private var _dataBinder: T? =
        null //  reference to the data binding object associated with this view. onDestroy() will be called when the view is destroyed. after onDestroy() is called, the reference will be set to null.
    protected val dataBinder: T
        get() = _dataBinder!!

    private var _rootView: View? = null
    protected val rootView: View
        get() = _rootView!!


    abstract fun onFragmentCreated(dataBinder: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this@BaseFragment.layoutResourceLayout.let {
            _dataBinder = DataBindingUtil.inflate(inflater, it, container, false)
            _rootView = dataBinder.root
            return rootView
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this@BaseFragment.onFragmentCreated(dataBinder)
        _navController = findNavController()
        setUpViewModelStateObservers()
    }

    abstract fun setUpViewModelStateObservers()

    protected fun backFragment() {
        navController.popBackStack()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        _dataBinder?.unbind()
        _dataBinder = null
        _rootView = null
        _navController = null
        super.onDestroy()
    }
}
