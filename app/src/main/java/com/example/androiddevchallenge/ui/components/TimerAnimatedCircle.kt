/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.components

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.circleStrokeReverse
import com.example.androiddevchallenge.ui.theme.timerAlpha
import kotlin.math.min

@Composable
fun TimerAnimatedCircle(
    modifier: Modifier = Modifier,
    remainingTime: Float,
    totalTime: Float,
    secondsCount: Float
) {
    val context = LocalContext.current
    val strokeDecrease = with(LocalDensity.current) { Stroke(width = 6.dp.toPx()) }
    val strokeIncrease = with(LocalDensity.current) { Stroke(width = 1.5.dp.toPx()) }
    val strokeSeconds = with(LocalDensity.current) {
        Stroke(
            width = 10.dp.toPx()
//            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 24f), 0f)
//            pathEffect = PathEffect.dashPathEffect(floatArrayOf(context.dpToPixels(7f).toFloat(), context.dpToPixels(7f).toFloat()), 0f)
        )
    }

    val decreaseSweep = remember { mutableStateOf(0F) }
    decreaseSweep.value = min(1f, remainingTime / totalTime)
    val increaseSweep = 1f - decreaseSweep.value

    val secondsSweep = remember { mutableStateOf(0F) }
    secondsSweep.value = min(1f, secondsCount / 60000)
    val increaseSecondsSweep = 1f - secondsSweep.value

    Canvas(
        modifier = modifier
    ) {
        val innerRadiusSeconds = (size.minDimension - strokeSeconds.width) / 2 - 50
        val innerRadiusMain = (size.minDimension - strokeDecrease.width) / 2
        val halfSize = size / 2.0f

        val topLeftMain = Offset(
            halfSize.width - innerRadiusMain,
            halfSize.height - innerRadiusMain
        )

        val topLeftSeconds = Offset(
            halfSize.width - innerRadiusSeconds,
            halfSize.height - innerRadiusSeconds
        )

        val sizeMainArc = Size(innerRadiusMain * 2, innerRadiusMain * 2)
        val sizeSecondsArc = Size(innerRadiusSeconds * 2, innerRadiusSeconds * 2)

        // Decreasing arc
        drawArc(
            color = timerAlpha,
            startAngle = 270f,
            sweepAngle = if (!decreaseSweep.value.isNaN()) decreaseSweep.value * 360f else 360f,
            useCenter = false,
            size = sizeMainArc,
            topLeft = topLeftMain,
            style = strokeDecrease
        )

        // Increasing arc
        drawArc(
            color = circleStrokeReverse,
            startAngle = 270f,
            sweepAngle = -increaseSweep * 360f,
            useCenter = false,
            size = sizeMainArc,
            topLeft = topLeftMain,
            style = strokeIncrease
        )

        // Seconds arc
        if (!decreaseSweep.value.isNaN()) {
            drawArc(
                color = circleStrokeReverse,
                startAngle = 270f,
                sweepAngle = -increaseSecondsSweep * 360f,
                useCenter = false,
                size = sizeSecondsArc,
                topLeft = topLeftSeconds,
                style = strokeSeconds
            )
        }
    }
}

fun Context.dpToPixels(dp: Float): Int {
    val displayMetrics = this.resources.displayMetrics
    val px = (displayMetrics.densityDpi / 160f) * dp
    return px.toInt()
}
