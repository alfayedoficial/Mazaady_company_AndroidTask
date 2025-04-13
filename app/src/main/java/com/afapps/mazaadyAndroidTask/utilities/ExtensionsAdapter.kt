package com.afapps.mazaadyAndroidTask.utilities

import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:bindString")
fun TextView.setBindString(txtString: String?) {
    text = txtString ?: ""
}

@BindingAdapter("app:bindIsFavouriteMovie")
fun ImageButton.isFavouriteMovie(isFavouriteMovie : Boolean) {
    setImageResource(if (isFavouriteMovie) com.afapps.mazaadyAndroidTask.R.drawable.baseline_favorite_24 else com.afapps.mazaadyAndroidTask.R.drawable.baseline_favorite_border_24)
}
