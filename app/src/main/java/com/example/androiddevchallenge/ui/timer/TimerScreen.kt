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
package com.example.androiddevchallenge.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.TimerViewModel
import com.example.androiddevchallenge.ui.components.InnerTimerButton
import com.example.androiddevchallenge.ui.components.TimeUnit
import com.example.androiddevchallenge.ui.components.TimeUnitList
import com.example.androiddevchallenge.ui.components.TimerAnimatedCircle
import com.example.androiddevchallenge.ui.components.TimerUnit
import com.example.androiddevchallenge.ui.theme.countAlpha
import com.example.androiddevchallenge.ui.theme.typography

/**
 * The Timer main screen
 */
@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    viewModel: TimerViewModel
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.cool_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.timer_header),
                style = typography.h4,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(30.dp))

            val hours = remember { mutableStateOf(0) }
            val minutes = remember { mutableStateOf(0) }
            val isControlContainerVisible = remember { mutableStateOf(true) }
            var backgroundColorState by remember { mutableStateOf(Color.Black) }

            Box(
                modifier = Modifier.height(300.dp)
            ) {

                TimerAnimatedCircle(
                    modifier = Modifier
                        .height(300.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    remainingTime = viewModel.millisLeft.toFloat(),
                    totalTime = viewModel.initialMillis.toFloat(),
                    secondsCount = viewModel.secondsInMillis.toFloat()
                )

                val playButtonVisibility = remember { mutableStateOf(true) }
                val pauseButtonVisibility = remember { mutableStateOf(false) }

                InnerTimerButton(
                    modifier = Modifier.align(Alignment.Center),
                    imageId = R.drawable.play_button,
                    isVisible = playButtonVisibility,
                ) {
                    viewModel.startTimer(hours.value, minutes.value)
                }

                InnerTimerButton(
                    modifier = Modifier.align(Alignment.Center),
                    imageId = R.drawable.pause_button,
                    isVisible = pauseButtonVisibility
                ) {
                    viewModel.pauseTimer()
                }

                viewModel.timerState

                when (viewModel.timerState) {
                    TimerViewModel.TimerState.PAUSED -> {
                        pauseButtonVisibility.value = false
                        playButtonVisibility.value = true
                    }
                    TimerViewModel.TimerState.STARTED -> {
                        pauseButtonVisibility.value = true
                        playButtonVisibility.value = false
                        isControlContainerVisible.value = false
                    }
                    TimerViewModel.TimerState.STOPPED -> {
                        pauseButtonVisibility.value = false
                        playButtonVisibility.value = true
                        isControlContainerVisible.value = true
                    }
                    TimerViewModel.TimerState.UNDEFINED -> {
                        backgroundColorState = Color.Black
                    }
                }

                Text(
                    text = viewModel.currentTimer,
                    color = Color.White,
                    style = typography.h3,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(
                visible = isControlContainerVisible.value
            ) {
                HourMinuteController(hours, minutes, backgroundColorState)
            }

            AnimatedVisibility(
                visible = !isControlContainerVisible.value
            ) {
                Image(
                    painter = painterResource(R.drawable.stop_button),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable(role = Role.Image) {
                            viewModel.stopTimer()
                        }
                )
            }
        }
    }
}

@Composable
fun HourMinuteController(
    hours: MutableState<Int>,
    minutes: MutableState<Int>,
    backgroundState: Color
) {
    Box {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TimeUnitList(modifier = Modifier.align(Alignment.Center), unitValue = hours)
                HourMinuteBackground(modifier = Modifier.align(Alignment.Center), colorState = backgroundState)
                TimeUnit(modifier = Modifier.align(Alignment.Center), TimerUnit.Hours, hours)
            }
            Box(modifier = Modifier.weight(1f)) {
                TimeUnitList(modifier = Modifier.align(Alignment.Center), unitValue = minutes)
                HourMinuteBackground(modifier = Modifier.align(Alignment.Center), colorState = backgroundState)
                TimeUnit(modifier = Modifier.align(Alignment.Center), TimerUnit.Minutes, minutes)
            }
        }
    }
}

@Composable
fun HourMinuteBackground(
    modifier: Modifier = Modifier,
    colorState: Color,
    colorState2: BackgroundState = BackgroundState.IDLE
) {
    val transitionDefinition = updateTransition(colorState2)

    // TODO: animate counter when values are zero
    val color by transitionDefinition.animateColor(
        transitionSpec = {
            when {
                BackgroundState.IDLE isTransitioningTo BackgroundState.ACTIVE ->
                    spring(stiffness = 50f)
                else ->
                    tween(durationMillis = 100)
            }
        }
    ) { state ->
        when (state) {
            BackgroundState.IDLE -> MaterialTheme.colors.primary
            BackgroundState.ACTIVE -> MaterialTheme.colors.background
        }
    }

    Box(
        modifier = modifier
            .background(color = countAlpha)
            .height(70.dp)
            .fillMaxWidth()
    )
}

enum class BackgroundState {
    IDLE, ACTIVE
}
