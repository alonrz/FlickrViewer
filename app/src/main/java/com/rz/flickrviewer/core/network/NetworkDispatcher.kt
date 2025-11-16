package com.rz.flickrviewer.core.network

import android.util.Log
import com.rz.flickrviewer.BuildConfig
import com.rz.flickrviewer.core.network.responses.PhotoData
import com.rz.flickrviewer.search_images.data.Image
import okhttp3.internal.toLongOrDefault
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkDispatcher {

    private val apiKey = BuildConfig.FLICKR_API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val flickrApiService = retrofit.create(FlickrApiService::class.java)

    suspend fun getRecentPhotos(page: Int = 1, perPage: Int = DEFAULT_PER_PAGE): Resource<List<Image>> {
        return try {
            val response = flickrApiService.getRecentPhotos(
                apiKey = apiKey,
                page = page,
                perPage = perPage
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.Success(data = body.photos.photo.map { it.toImage() })
            } else {
                val errorMsg =
                    "Failed to fetch recent photos (HTTP ${response.code()}): ${response.message()}"
                Log.e(TAG, errorMsg)
                Resource.Error(errorMessage = errorMsg)
            }

        } catch (e: Exception) {
            val errorMsg = "Network error while fetching recent photos: ${e.message}"
            Log.e(TAG, errorMsg, e)
            Resource.Error(errorMessage = "Could not fetch recent photos. Please check your connection.")
        }
    }

    suspend fun searchPhotos(
        query: String,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE,
    ): Resource<List<Image>> {
        return try {
            val response = flickrApiService.searchPhotos(
                apiKey = apiKey,
                searchText = query,
                page = page,
                perPage = perPage
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.Success(data = body.photos.photo.map { it.toImage() })
            } else {
                val errorMsg =
                    "Failed to search photos (HTTP ${response.code()}): ${response.message()}"
                Log.e(TAG, errorMsg)
                Resource.Error(errorMessage = errorMsg)
            }

        } catch (e: Exception) {
            val errorMsg = "Network error while searching photos: ${e.message}"
            Log.e(TAG, errorMsg, e)
            Resource.Error(errorMessage = "Could not search photos. Please check your connection.")
        }
    }

    private fun PhotoData.toImage(): Image {
        val photoUrl = "https://live.staticflickr.com/$server/${id}_${secret}.jpg"
        // For thumbnails, we use _q suffix for smaller size and makes it square
        val thumbnailUrl = "https://live.staticflickr.com/$server/${id}_${secret}_q.jpg"

        return Image(
            id = id.toLongOrDefault(0),
            title = title,
            url = photoUrl,
            thumbnailUrl = thumbnailUrl
        )
    }

    companion object {
        private const val TAG = "NetworkDispatcher"
        const val BASE_URL = "https://www.flickr.com/services/"
        const val DEFAULT_PER_PAGE = 30
    }
}