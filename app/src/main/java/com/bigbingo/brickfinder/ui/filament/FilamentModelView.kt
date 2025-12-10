package com.bigbingo.brickfinder.ui.filament

import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun FilamentModelView(
    modelPath: String,
    modifier: Modifier = Modifier,
    onLoadFailed: () -> Unit = {},
) {
    val context = LocalContext.current
    val surfaceView = remember { SurfaceView(context) }
    val renderer = remember(surfaceView) { FilamentRenderer(context, surfaceView) }
    var modelLoaded by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(modelPath) {
        modelLoaded = null
        val loaded = renderer.loadModel(modelPath)
        modelLoaded = loaded
        if (!loaded) onLoadFailed()
    }

    DisposableEffect(modelLoaded) {
        if (modelLoaded == true) {
            renderer.start()
        }
        onDispose { 
            renderer.stop()
        }
    }

    DisposableEffect(Unit) {
        onDispose { 
            renderer.destroy()
        }
    }

    if (modelLoaded == null) {
        Box(
            modifier = modifier
                .background(Color.Transparent)
        )
    } else if (modelLoaded == true) {
        Box(
            modifier = modifier
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { surfaceView },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Box(
            modifier = modifier
                .background(Color.Transparent),
        )
    }
}

