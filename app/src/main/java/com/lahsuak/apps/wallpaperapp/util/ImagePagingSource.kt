package com.lahsuak.apps.wallpaperapp.util


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lahsuak.apps.wallpaperapp.util.Constants.PAGE_COUNT
import com.lahsuak.apps.wallpaperapp.model.ImageModel
import com.lahsuak.apps.wallpaperapp.network.ApiService

private const val TAG = "Load"

class ImagePagingSource(
    private val apiService: ApiService,private val query:String,
) :
    PagingSource<Int, ImageModel>() {
    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        return try {
            val currentPage = params.key ?: FIRST_PAGE_INDEX
            var data :List<ImageModel> = emptyList()
            if(query==""){
                val response = apiService.getImages2(currentPage, PAGE_COUNT)
                data = response.body() ?: emptyList()
            }else{
                val response =  apiService.getSearchImages2(query,currentPage, PAGE_COUNT)
                data = response.first().results?: emptyList()
                Log.d(TAG, "load: inside ${data.size}")
            }
            val responseData = mutableListOf<ImageModel>()
            responseData.addAll(data)
            Log.d(TAG, "load: ${data.size}")
            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

}