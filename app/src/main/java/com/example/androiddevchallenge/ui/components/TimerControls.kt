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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.unitAlpha
import com.example.androiddevchallenge.ui.theme.whiteAlpha20
import com.example.androiddevchallenge.ui.theme.whiteAlpha40

sealed class TimerUnit(val unit: String) {
    object Hours : TimerUnit("Hours")
    object Minutes : TimerUnit("Minutes")
}

@ExperimentalAnimationApi
@Composable
fun InnerTimerButton(
    modifier: Modifier = Modifier,
    imageId: Int,
    isVisible: MutableState<Boolean>,
    actionHandler: () -> Unit
) {
    AnimatedVisibility(
        enter = fadeIn(),
        exit = fadeOut(),
        visible = isVisible.value
    ) {
        Box {
            Image(
                painter = painterResource(imageId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .fillMaxSize()
                    .clickable(role = Role.Image) {
                        actionHandler.invoke()
                    }
            )
        }
    }
}

@Composable
fun TimeUnitList(
    modifier: Modifier = Modifier,
    unitValue: MutableState<Int>
) {
    var itemColorState by remember { mutableStateOf(whiteAlpha20) }
    val animateColor by animateColorAsState(
        targetValue = itemColorState,
        animationSpec = tween(durationMillis = 120)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scrollState = rememberLazyListState()
        itemColorState = if (scrollState.isScrollInProgress) {
            whiteAlpha40
        } else {
            whiteAlpha20
        }

        unitValue.value = scrollState.firstVisibleItemIndex
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .width(70.dp)
                .height(210.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(65) {
                HoursListItem(colorState = animateColor)
                Divider(color = Color.Transparent, thickness = 8.dp)
            }
        }
    }
}

@Composable
fun HoursListItem(
    colorState: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RectangleShape)
                .background(colorState)
        )
    }
}

@Composable
fun TimeUnit(
    modifier: Modifier = Modifier,
    unitType: TimerUnit,
    unitValue: MutableState<Int>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${unitValue.value}",
            style = TextStyle(
                color = Color.White,
                fontSize = 35.sp
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
        )
        Text(
            text = unitType.unit,
            style = TextStyle(
                color = unitAlpha,
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}
