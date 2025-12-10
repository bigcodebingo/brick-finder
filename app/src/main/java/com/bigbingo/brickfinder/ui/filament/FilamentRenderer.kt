package com.bigbingo.brickfinder.ui.filament

import android.content.Context
import android.view.Choreographer
import android.view.SurfaceView
import com.google.android.filament.IndirectLight
import com.google.android.filament.Skybox
import com.google.android.filament.View
import com.google.android.filament.utils.HDRLoader
import com.google.android.filament.utils.IBLPrefilterContext
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer

private const val DEFAULT_HDR_PATH = "envs/lightroom_14b.hdr"

class FilamentRenderer(
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

