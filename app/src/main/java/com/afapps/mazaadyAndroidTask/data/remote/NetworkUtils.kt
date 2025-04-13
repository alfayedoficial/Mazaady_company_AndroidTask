package com.afapps.mazaadyAndroidTask.data.remote



enum class NetworkLinks(val type: String) {
  /**-----------------------   GET   ---------------------*/
  GetDiscoverMovies("/3/discover/movie")
}

fun String.getApiLink(endPoint: String) = this.plus(endPoint)
fun String.getImageUrl(imageUrl: String) = this.plus(imageUrl)
