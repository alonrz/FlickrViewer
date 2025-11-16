package com.rz.flickrviewer.core.network.responses

import com.google.gson.annotations.SerializedName

data class RecentPhotosResponse(
    @SerializedName("photos")
    val photos: PhotosData,
    @SerializedName("stat")
    val stat: String
)

data class PhotosData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("perpage")
    val perpage: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("photo")
    val photo: List<PhotoData>
)

data class PhotoData(
    @SerializedName("id")
    val id: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("server")
    val server: String,
    @SerializedName("farm")
    val farm: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("ispublic")
    val ispublic: Int,
    @SerializedName("isfriend")
    val isfriend: Int,
    @SerializedName("isfamily")
    val isfamily: Int
)
