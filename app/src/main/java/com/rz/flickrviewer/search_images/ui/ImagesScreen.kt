package com.rz.flickrviewer.search_images.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.rz.flickrviewer.search_images.domain.ImageEntity
import com.rz.flickrviewer.ui.UiState

private const val GRID_COLUMNS = 3
private const val LOAD_MORE_THRESHOLD = 6

@Composable
fun ImagesScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    ImageScreenInner(
        modifier = modifier,
        uiState = uiState,
        isLoadingMore = isLoadingMore,
        onSearch = viewModel::searchImages,
        onLoadMore = viewModel::loadNextPage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageScreenInner(
    modifier: Modifier = Modifier,
    uiState: UiState<List<ImageEntity>>,
    isLoadingMore: Boolean,
    onSearch: (query: String) -> Unit,
    onLoadMore: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Flickr Viewer")
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.imePadding(),
        content = { innerPadding ->
            SearchBox(onSearch = onSearch)
            when (uiState) {
                is UiState.Error -> {
                    // Error UI is handled by LaunchedEffect above
                    LoadingIndicator(modifier = Modifier.padding(innerPadding))
                }

                is UiState.Success -> {
                    ImageGrid(
                        images = uiState.data,
                        isLoadingMore = isLoadingMore,
                        onLoadMore = onLoadMore,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                is UiState.Loading,
                is UiState.Uninitialized -> {
                    LoadingIndicator(modifier = Modifier.padding(innerPadding))
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    onSearch: (query: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf("") }
    SearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = { text = it },
                onSearch = { onSearch(text) },
                expanded = false,
                onExpandedChange = { /* no-op */ },
                placeholder = { Text("Search") }
            )
        },
        expanded = false,
        onExpandedChange = {},
        content = {},
    )
}

@Composable
fun ImageGrid(
    images: List<ImageEntity>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem to totalItems
        }.collect { (lastVisibleItem, totalItems) ->
            // Trigger load more when we're near the end
            if (totalItems > 0 && lastVisibleItem >= totalItems - LOAD_MORE_THRESHOLD && !isLoadingMore) {
                onLoadMore()
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        state = gridState,
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
    ) {
        items(items = images, key = { it.id }) { image ->
            AsyncImage(
                model = image.thumbnailUrl,
                contentDescription = image.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.padding(4.dp)
            )
        }

        if (isLoadingMore) {
            item(span = { GridItemSpan(GRID_COLUMNS) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun NavigationIconPreview() {
    ImageScreenInner(
        uiState = UiState.Success(emptyList()),
        isLoadingMore = false,
        onSearch = {},
        onLoadMore = {}
    )
}