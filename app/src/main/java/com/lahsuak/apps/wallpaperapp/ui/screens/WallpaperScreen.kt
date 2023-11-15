package com.lahsuak.apps.wallpaperapp.ui.screens

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.lahsuak.apps.wallpaperapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun WallpaperScreen(imageUrl: String, navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }
    val context = LocalContext.current
    Box(Modifier.fillMaxSize()) {
        ZoomableImage(imageUrl = imageUrl)
        TextButton(modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray.copy(0.7f),
                contentColor = Color.White
            ),
            onClick = {
                setWallpaper(context, imageUrl = imageUrl)
            }) {
            Text(stringResource(R.string.set_wallpaper))
        }
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .alpha(0.7f)
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
        }
    }
}

@Composable
fun ZoomableImage(imageUrl: String) {
    val scale = remember { mutableFloatStateOf(1f) }
//    val rotationState = remember { mutableFloatStateOf(1f) }
    Box(
        modifier = Modifier
            .clip(RectangleShape) // Clip the box content
            .fillMaxSize() // Give the size you want...
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
//                    rotationState.value += rotation
                }
            }
    ) {

        AsyncImage(
            model = imageUrl,
            null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center) // keep the image centralized into the Box
                .graphicsLayer(
                    // adding some zoom limits (min 50%, max 200%)
                    scaleX = maxOf(.5f, minOf(3f, scale.value)),
                    scaleY = maxOf(.5f, minOf(3f, scale.value)),
//                    rotationZ = rotationState.value
                )
        )
    }
}

fun urlToBitmap(
    scope: CoroutineScope,
    imageURL: String,
    context: Context,
    onSuccess: (bitmap: Bitmap) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    var bitmap: Bitmap? = null
    val loadBitmap = scope.launch(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageURL)
            .allowHardware(false)
            .build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            bitmap = (result.drawable as BitmapDrawable).bitmap
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Wallpaper set!", Toast.LENGTH_SHORT).show()
            }
        } else if (result is ErrorResult) {
            cancel(result.throwable.localizedMessage ?: "ErrorResult", result.throwable)
        }
    }
    loadBitmap.invokeOnCompletion { throwable ->
        bitmap?.let {
            onSuccess(it)
        } ?: throwable?.let {
            onError(it)
        } ?: onError(Throwable("Undefined Error"))
    }
}

private fun setWallpaper(context: Context, imageUrl: String) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    try {
        val scope = CoroutineScope(Dispatchers.Main)
        urlToBitmap(scope, imageUrl, context, onSuccess = { image ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(image, null, true, WallpaperManager.FLAG_SYSTEM)
                wallpaperManager.setWallpaperOffsetSteps(1F, 1F)
            } else {
                wallpaperManager.setBitmap(image)
            }
        }) {
            Log.d("TAG", "setWallpaper: ${it.message}")
        }
    } catch (e: IOException) {
        println(e)
    }
}