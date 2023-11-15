package com.lahsuak.apps.wallpaperapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.lahsuak.apps.wallpaperapp.network.ApiInstance
import com.lahsuak.apps.wallpaperapp.network.ApiService
import com.lahsuak.apps.wallpaperapp.util.AppConstants.PAGE_COUNT
import com.lahsuak.apps.wallpaperapp.util.ImagePagingSource
import kotlinx.coroutines.flow.collectLatest

private const val PAGE_SIZE = 10

class MainViewModel : ViewModel() {
    private val retroService: ApiService = ApiInstance.getRetroInstance()

    private var currentSearch: String = ""
    val imagePager = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_COUNT,
            enablePlaceholders = true
        )
    ) {
        ImagePagingSource(retroService, currentSearch)
    }.flow.cachedIn(viewModelScope)
}