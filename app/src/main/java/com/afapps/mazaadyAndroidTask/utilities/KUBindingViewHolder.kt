package com.afapps.mazaadyAndroidTask.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author Ali Al Fayed
 * @param parent of ViewGroup Row
 * @param resId of layout Id
 * @return ViewDataBinding
 * you can case ViewDataBinding to child for get views in layout
 */
fun kuGetBindingRow(parent: ViewGroup, @LayoutRes resId: Int): ViewDataBinding {
    return DataBindingUtil.inflate(LayoutInflater.from(parent.context), resId, parent, false)
}
