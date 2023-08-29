package com.lahsuak.apps.wallpaperapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.lahsuak.apps.wallpaperapp.model.ImageModel
import com.lahsuak.apps.wallpaperapp.network.ApiInstance
import com.lahsuak.apps.wallpaperapp.network.ApiService
import com.lahsuak.apps.wallpaperapp.util.ImagePagingSource
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {
    private val retroService: ApiService = ApiInstance.getRetroInstance()

    private var currentSearch: String = ""
    private var pagingSource: ImagePagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = ImagePagingSource(retroService, currentSearch)
            }
            return field
        }

    init {
        getListData("",false)
    }
   private fun getListData(query: String, isSearch: Boolean): Flow<PagingData<ImageModel>> {
        return Pager(config = PagingConfig(pageSize = 1, maxSize = 30),
            pagingSourceFactory = { ImagePagingSource(retroService,query) }).flow.cachedIn(viewModelScope)
    }
    fun userSearch(query: String){
        currentSearch = query
        pagingSource?.invalidate()
    }
    val flow = Pager(config = PagingConfig(pageSize = 30, enablePlaceholders = false)
    ) {
        pagingSource!!
    }.flow.cachedIn(viewModelScope)
}
