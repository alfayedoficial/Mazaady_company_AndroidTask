package com.afapps.mazaadyAndroidTask.core.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.utilities.KUPreferences
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.DEFAULT_LANGUAGE_CODE
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.LOCALE_LANGUAGE_CODE
import com.afapps.mazaadyAndroidTask.utilities.ContextUtils
import java.util.Locale
import javax.inject.Inject

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : Base Activity for all activities
 * Date 12/4/2026 - 9:25 PM
 */

abstract class BaseActivity<T> : AppCompatActivity() where T : ViewDataBinding {

    @Inject
    lateinit var  appPreferences: KUPreferences

    var navController: NavController? = null
    var bottomBarConfiguration: AppBarConfiguration? = null

    @get:LayoutRes
    protected abstract val layoutResourceId: Int

    private var _dataBinder: T? = null
    protected val dataBinder: T
        get() = _dataBinder!!

    abstract fun onActivityCreated(dataBinder: T)

    final override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        this@BaseActivity.initial()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this@BaseActivity.initial()
        setUpViewModelStateObservers()
    }

    private fun initial() {
        this@BaseActivity.layoutResourceId.let {
            val dataBinder = DataBindingUtil.setContentView<T>(this@BaseActivity, it)
            this._dataBinder = dataBinder
            this@BaseActivity.onActivityCreated(dataBinder)
        }
    }


    override fun attachBaseContext(newBase: Context) {
        val sharedPref = newBase.getSharedPreferences(newBase.getString(R.string.app_name) + "1", Context.MODE_PRIVATE)
        val lang = sharedPref.getString(LOCALE_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE) ?: DEFAULT_LANGUAGE_CODE
        super.attachBaseContext(ContextUtils.wrap(newBase, lang))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateLanguage()
    }

    private fun updateLanguage() {
        val lang = appPreferences.getStringValue(LOCALE_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE)
        ContextUtils.updateLanguage(this, Locale(lang))
    }


    open fun setUpViewModelStateObservers() {}


    override fun onDestroy() {
        _dataBinder?.unbind()
        _dataBinder = null
        navController = null
        super.onDestroy()
    }


}