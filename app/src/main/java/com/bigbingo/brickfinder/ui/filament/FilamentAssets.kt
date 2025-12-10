package com.bigbingo.brickfinder.ui.filament

import android.content.res.AssetManager

object FilamentAssets {
    private const val MODELS_DIR = "models"

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

