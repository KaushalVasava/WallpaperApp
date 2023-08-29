package com.lahsuak.apps.wallpaperapp.model

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("urls")
    val urls: UrlModel,
)
