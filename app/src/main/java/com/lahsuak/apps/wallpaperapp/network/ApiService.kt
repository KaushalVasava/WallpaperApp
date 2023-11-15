package com.lahsuak.apps.wallpaperapp.network

import com.lahsuak.apps.wallpaperapp.BuildConfig
import com.lahsuak.apps.wallpaperapp.model.ImageModel
import com.lahsuak.apps.wallpaperapp.model.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "Accept: application/json",
        "Authorization: Client-ID ${BuildConfig.API_KEY}"
    )
    @GET("/photos")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<ImageModel>>

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun getSearchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<SearchModel>

}