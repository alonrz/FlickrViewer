package com.rz.flickrviewer.search_images.data

import com.rz.flickrviewer.core.network.Resource
import com.rz.flickrviewer.search_images.domain.ImageEntity
import com.rz.flickrviewer.search_images.domain.SearchRepository
import com.rz.flickrviewer.ui.UiState

class SearchRepositoryImpl(
    private val localDataSource: SearchLocalDataSource,
    private val remoteDataSource: SearchRemoteDataSource,
) : SearchRepository {
    override suspend fun search(query: String, page: Int): UiState<List<ImageEntity>> {
        val remoteResource = remoteDataSource.fetchSearchResults(query, page)

        return when (remoteResource) {
            is Resource.Success -> {
                UiState.Success(
                    data = remoteResource.data.map { image ->
                        ImageEntity.fromImage(image)
                    }
                )
            }

            is Resource.Error -> {
                UiState.Error(message = remoteResource.errorMessage)
            }
        }
    }
}