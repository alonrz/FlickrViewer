package com.rz.flickrviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rz.flickrviewer.search_images.ui.ImagesScreen
import com.rz.flickrviewer.ui.theme.FlickrViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickrViewerTheme {
                ImagesScreen()
            }
        }
    }
}
