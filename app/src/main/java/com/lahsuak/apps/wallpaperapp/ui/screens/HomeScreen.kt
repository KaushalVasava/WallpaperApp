package com.lahsuak.apps.wallpaperapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.lahsuak.apps.wallpaperapp.model.ImageModel
import com.lahsuak.apps.wallpaperapp.ui.navigation.NavigationItem
import com.lahsuak.apps.wallpaperapp.ui.viewmodel.MainViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, navigator: NavController) {
    val images = viewModel.imagePager.collectAsLazyPagingItems()

    AnimatedVisibility(images.itemCount != 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp),
                content = {
                    items(
                        count = images.itemCount,
                        key = images.itemKey {
                            it.urls.regular
                        },
                        contentType = images.itemContentType {
                            "Image"
                        }
                    ) {
                        val image = images[it]
                        image?.let {
                            Row(modifier = Modifier.animateItemPlacement()) {
                                PhotoImage(it) {
                                    val encodedUrl =
                                        URLEncoder.encode(
                                            it.urls.regular,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    navigator.navigate("${NavigationItem.ViewImage.route}/$encodedUrl")
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun PhotoImage(image: ImageModel, onClick: () -> Unit) {
    AsyncImage(
        model = image.urls.regular,
        null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .clickable {
                onClick()
            }
    )
}