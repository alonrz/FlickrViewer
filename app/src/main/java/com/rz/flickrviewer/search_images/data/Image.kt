package com.rz.flickrviewer.search_images.data

data class Image(
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
)
