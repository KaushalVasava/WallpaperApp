package com.lahsuak.apps.wallpaperapp.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.lahsuak.apps.wallpaperapp.model.ImageModel
import com.lahsuak.apps.wallpaperapp.network.ApiInstance
import com.lahsuak.apps.wallpaperapp.network.ApiService
import kotlinx.coroutines.flow.Flow

class MainViewModel() : ViewModel() {
    var retroService: ApiService = ApiInstance.getRetroInstance()
//    val listData = Pager(PagingConfig(pageSize=1)){
//        ImagePagingSource(retroService)
//    }.flow.cachedIn(viewModelScope) //WORKING PERFECTLY

    private var currentSearch: String = ""
    private var pagingSource: ImagePagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = ImagePagingSource(retroService, currentSearch)
            }
            return field
        }

    fun getListData(query: String,isSearch: Boolean): Flow<PagingData<ImageModel>> {
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
