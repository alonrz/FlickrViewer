package com.rz.flickrviewer.search_images.data

import com.rz.flickrviewer.core.network.NetworkDispatcher
import com.rz.flickrviewer.core.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRemoteDataSource(private val networkDispatcher: NetworkDispatcher) {
    suspend fun fetchSearchResults(query: String, page: Int = 1): Resource<List<Image>> {
        return if (query.isBlank()) {
            networkDispatcher.getRecentPhotos(page = page)
        } else {
            networkDispatcher.searchPhotos(query, page = page)
        }
    }
}
