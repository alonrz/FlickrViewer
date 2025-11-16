package com.rz.flickrviewer.search_images.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchLocalDataSource {
    suspend fun fetchSearchResults(query: String): List<Image> {
        return withContext(Dispatchers.IO) {
            // Improvement - to be used for offline support and caching
            emptyList()
        }
    }
}