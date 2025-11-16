package com.rz.flickrviewer.ui

sealed interface UiState<out T> {
    data object Uninitialized : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}