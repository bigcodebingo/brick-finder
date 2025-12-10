package com.bigbingo.brickfinder.ui.filament

import android.content.Context
import android.content.res.AssetManager
import android.view.Choreographer
import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.filament.IndirectLight
import com.google.android.filament.Skybox
import com.google.android.filament.View
import com.google.android.filament.utils.HDRLoader
import com.google.android.filament.utils.IBLPrefilterContext
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer

/**
 * Утилиты для работы с Filament внутри Compose.
 */
object FilamentAssets {
    private const val MODELS_DIR = "models"

    /**
     * Ищет glb-файл модели для номера детали. Сначала пытается найти точное
     * совпадение <partNum>.glb, затем – первый файл, который содержит partNum.
     */
    fun findModelAsset(assetManager: AssetManager, partNum: String): String? {
        val trimmed = partNum.trim()
        val files = runCatching { assetManager.list(MODELS_DIR)?.toList() }
            .getOrNull()
            .orEmpty()

        val exact = files.firstOrNull {
            it.equals("$trimmed.glb", ignoreCase = true) ||
                it.equals(trimmed, ignoreCase = true)
        }
        if (exact != null) {
            val fileName = if (exact.endsWith(".glb", ignoreCase = true)) exact else "$exact.glb"
            return "$MODELS_DIR/$fileName"
        }

        val partial = files.firstOrNull { it.contains(trimmed, ignoreCase = true) }
        return partial?.let { "$MODELS_DIR/$it" }
    }
}

private const val DEFAULT_HDR_PATH = "envs/lightroom_14b.hdr"

private class FilamentRenderer(
    private val context: Context,
    private val surfaceView: SurfaceView,
    hdrPath: String = DEFAULT_HDR_PATH,
) {
    companion object {
        init {
            Utils.init()
        }
    }

    private val modelViewer: ModelViewer = ModelViewer(surfaceView)
    private val choreographer: Choreographer = Choreographer.getInstance()
    private val frameCallback = FrameCallback()
    private var indirectLight: IndirectLight? = null
    private var skybox: Skybox? = null
    private var hasModel = false

    init {
        surfaceView.setOnTouchListener { _, motionEvent ->
            modelViewer.onTouchEvent(motionEvent)
            true
        }
        configureView(modelViewer.view)
        createIndirectLight(hdrPath)
    }
    fun start() {
        choreographer.postFrameCallback(frameCallback)
    }
    fun stop() {
        choreographer.removeFrameCallback(frameCallback)
    }
    fun destroy() {
        stop()
    }

    fun clearModel() {
        if (hasModel) {
            runCatching { modelViewer.destroyModel() }
            hasModel = false
        }
    }
    fun loadModel(assetPath: String): Boolean {
        val buffer = runCatching { readAsset(assetPath) }.getOrNull() ?: return false
        modelViewer.destroyModel()
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
        hasModel = true
        return true
    }

    private fun configureView(view: View) {
        view.renderQuality = view.renderQuality.apply {
            hdrColorBuffer = View.QualityLevel.MEDIUM
        }

        view.dynamicResolutionOptions = view.dynamicResolutionOptions.apply {
            enabled = true
            quality = View.QualityLevel.MEDIUM
        }

        view.multiSampleAntiAliasingOptions = view.multiSampleAntiAliasingOptions.apply {
            enabled = true
        }

        view.antiAliasing = View.AntiAliasing.FXAA

        view.ambientOcclusionOptions = view.ambientOcclusionOptions.apply {
            enabled = true
        }

        view.bloomOptions = view.bloomOptions.apply {
            enabled = true
        }
    }

    private fun createIndirectLight(hdrPath: String) {
        val engine = modelViewer.engine
        val scene = modelViewer.scene

        val buffer = runCatching { readAsset(hdrPath) }.getOrNull() ?: return
        val equirect = HDRLoader.createTexture(engine, buffer) ?: return

        val context = IBLPrefilterContext(engine)
        val equirectToCubemap = IBLPrefilterContext.EquirectangularToCubemap(context)
        val skyboxTexture = equirectToCubemap.run(equirect)!!
        engine.destroyTexture(equirect)

        val specularFilter = IBLPrefilterContext.SpecularFilter(context)
        val reflections = specularFilter.run(skyboxTexture)

        val ibl = IndirectLight.Builder()
            .reflections(reflections)
            .intensity(30_000.0f)
            .build(engine)

        val sky = Skybox.Builder().environment(skyboxTexture).build(engine)

        destroyEnvironment()
        scene.indirectLight = ibl
        scene.skybox = sky

        indirectLight = ibl
        skybox = sky

        specularFilter.destroy()
        equirectToCubemap.destroy()
        context.destroy()
    }

    private fun destroyEnvironment() {
        indirectLight?.let { modelViewer.engine.destroyIndirectLight(it) }
        skybox?.let { modelViewer.engine.destroySkybox(it) }
        indirectLight = null
        skybox = null
    }

    private fun readAsset(path: String): ByteBuffer {
        val bytes = context.assets.open(path).use { it.readBytes() }
        return ByteBuffer.wrap(bytes)
    }

    private inner class FrameCallback : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)

            modelViewer.animator?.apply {
                if (animationCount > 0) {
                    val elapsedSeconds = (frameTimeNanos - startTime) / 1_000_000_000f
                    applyAnimation(0, elapsedSeconds)
                }
                updateBoneMatrices()
            }

            modelViewer.render(frameTimeNanos)
        }
    }
}

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
        val loaded = renderer.loadModel(modelPath)
        modelLoaded = loaded
        if (!loaded) onLoadFailed()
    }

    DisposableEffect(Unit) {
        renderer.start()
        onDispose { renderer.destroy() }
    }

    Box(
        modifier = modifier
            .background(Color(0xFF0F0F0F)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { surfaceView },
            modifier = Modifier.fillMaxSize()
        )
        when (modelLoaded) {
            null -> CircularProgressIndicator()
            false -> Text(
                text = "Не удалось загрузить 3D-модель",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            else -> Unit
        }
    }
}

