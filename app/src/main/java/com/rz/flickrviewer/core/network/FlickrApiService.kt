package com.rz.flickrviewer.core.network

import com.rz.flickrviewer.core.network.responses.RecentPhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {

    companion object {
        const val METHOD_GET_RECENT = "flickr.photos.getRecent"
        const val METHOD_SEARCH = "flickr.photos.search"
        const val FORMAT_JSON = "json"
        const val NO_JSON_CALLBACK = 1
        const val DEFAULT_PER_PAGE = 30
        const val DEFAULT_PAGE = 1
    }

    @GET("rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String = METHOD_GET_RECENT,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = FORMAT_JSON,
        @Query("nojsoncallback") noJsonCallback: Int = NO_JSON_CALLBACK,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE,
        @Query("page") page: Int = DEFAULT_PAGE,
    ): Response<RecentPhotosResponse>

    @GET("rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = METHOD_SEARCH,
        @Query("api_key") apiKey: String,
        @Query("text") searchText: String,
        @Query("format") format: String = FORMAT_JSON,
        @Query("nojsoncallback") noJsonCallback: Int = NO_JSON_CALLBACK,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE,
        @Query("page") page: Int = DEFAULT_PAGE,
    ): Response<RecentPhotosResponse>
}