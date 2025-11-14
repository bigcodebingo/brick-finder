package com.bigbingo.brickfinder.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex

@Composable
fun SlideAnimation(
    visible: Boolean,
    direction: Slider,    content: @Composable () -> Unit
) {
    val enterOffset: (fullWidth: Int) -> Int
    val exitOffset: (fullWidth: Int) -> Int

    when (direction) {
        Slider.LEFT -> {
            enterOffset = { -it }
            exitOffset = { -it }
        }
        Slider.RIGHT -> {
            enterOffset = { it }
            exitOffset = { it }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = enterOffset, animationSpec = tween(300)),
        exit = slideOutHorizontally(targetOffsetX = exitOffset, animationSpec = tween(300)),
        modifier = Modifier.zIndex(1f)
    ) {
        content()
    }
}
