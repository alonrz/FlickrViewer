package com.rz.flickrviewer.search_images.domain

import com.rz.flickrviewer.search_images.data.Image

data class ImageEntity(
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
) {
    companion object {
        fun fromImage(image: Image): ImageEntity {
            return ImageEntity(
                id = image.id,
                title = image.title,
                url = image.url,
                thumbnailUrl = image.thumbnailUrl ?: "",
            )
        }
    }
}
