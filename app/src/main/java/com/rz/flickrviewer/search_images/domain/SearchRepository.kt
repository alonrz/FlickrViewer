package com.rz.flickrviewer.search_images.domain

import com.rz.flickrviewer.ui.UiState

interface SearchRepository {
    suspend fun search(query: String, page: Int = 1): UiState<List<ImageEntity>>
}