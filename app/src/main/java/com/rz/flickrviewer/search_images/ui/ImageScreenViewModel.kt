package com.rz.flickrviewer.search_images.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rz.flickrviewer.core.network.NetworkDispatcher
import com.rz.flickrviewer.search_images.data.SearchLocalDataSource
import com.rz.flickrviewer.search_images.data.SearchRemoteDataSource
import com.rz.flickrviewer.search_images.data.SearchRepositoryImpl
import com.rz.flickrviewer.search_images.domain.ImageEntity
import com.rz.flickrviewer.search_images.domain.SearchRepository
import com.rz.flickrviewer.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageScreenViewModel(
    private val repository: SearchRepository = SearchRepositoryImpl(
        localDataSource = SearchLocalDataSource(),
        remoteDataSource = SearchRemoteDataSource(NetworkDispatcher())
    )
) : ViewModel() {

    private var _uiState = MutableStateFlow<UiState<List<ImageEntity>>>(UiState.Uninitialized)
    val uiState = _uiState.asStateFlow()

    private var _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private var currentQuery: String = ""
    private var currentPage: Int = 1
    private var canLoadMore: Boolean = true

    init {
        loadRecentPhotos()
    }

    fun loadRecentPhotos() {
        searchImages("")
    }

    fun searchImages(query: String) {
        currentQuery = query
        currentPage = 1
        canLoadMore = true
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            _uiState.update { repository.search(query, page = 1) }
        }
    }

    fun loadNextPage() {
        if (!canLoadMore || _isLoadingMore.value) return

        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            _isLoadingMore.update { true }
            val nextPage = currentPage + 1
            val result = repository.search(currentQuery, page = nextPage)

            when (result) {
                is UiState.Success -> {
                    if (result.data.isEmpty()) {
                        canLoadMore = false
                    } else {
                        currentPage = nextPage

                        val updatedList = (currentState.data + result.data).distinctBy { it.id }
                        _uiState.update { UiState.Success(updatedList) }
                    }
                }

                is UiState.Error -> {
                    _uiState.update { UiState.Error(result.message) }
                }

                else -> {}
            }
            _isLoadingMore.update { false }
        }
    }
}