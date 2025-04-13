package com.afapps.mazaadyAndroidTask.utilities.annotation

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ImageBaseURL(
    /** The name.  */
    val value: String = "imageBaseURL"
)
