package com.afapps.mazaadyAndroidTask.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author Ali Al Fayed
 * @param View
 * @see  :set view a VISIBLE
 */
fun View.kuShow() {
    visibility = View.VISIBLE
}
/**
 * @author Ali Al Fayed
 * @param View
 * @see  :set view a GONE
 */
fun View.kuHide() {
    visibility = View.GONE
}
/**
 * @author Ali Al Fayed
 * @param View
 * @see  :set view a INVISIBLE
 */
fun View.kuInShow() {
    visibility = View.INVISIBLE
}
/**
 * @author Ali Al Fayed
 * @param View
 * @see  :set view if condition == True -> a VISIBLE  else -> a GONE
 */
fun View.kuShowIf(condition: Boolean) = if (condition) kuShow() else kuHide()


/**
 * @author Ali Al Fayed
 * @param View
 * @return resources
 */
val View.kuRes: Resources get() = resources

/**
 * @author Ali Al Fayed
 * @param Context
 * @return resources
 */
val Context.kuRes: Resources get() = resources

/**
 * @author Ali Al Fayed
 * @param Activity
 * @return resources
 */
val Activity.kuRes: Resources get() = resources

/**
 * @author Ali Al Fayed
 * @param Fragment
 * @return resources
 */
val Fragment.kuRes: Resources get() = resources

/**
 * @author Ali Al Fayed
 * @param Application
 * @return resources
 */
val Application.kuRes: Resources get() = resources


/**
 * @author Ali Al Fayed
 * @param View
 * @return string
 */
fun View.kuString(id: Int): String = kuRes.getString(id)

/**
 * @author Ali Al Fayed
 * @param Context
 * @return string
 */
fun Context.kuString(id: Int): String = kuRes.getString(id)

/**
 * @author Ali Al Fayed
 * @param Activity
 * @return string
 */
fun Activity.kuString(id: Int): String = kuRes.getString(id)

/**
 * @author Ali Al Fayed
 * @param Fragment
 * @return string
 */
fun Fragment.kuString(id: Int): String = kuRes.getString(id)

/**
 * @author Ali Al Fayed
 * @param Application
 * @return string
 */
fun Application.kuString(id: Int): String = kuRes.getString(id)