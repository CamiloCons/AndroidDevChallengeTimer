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
package com.example.androiddevchallenge

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {

    var currentTimer by mutableStateOf("00:00:00")
        private set

    var timerState by mutableStateOf<TimerState>(TimerState.STOPPED)
        private set

    var millisLeft by mutableStateOf(0L)
        private set

    var totalMillis by mutableStateOf(0L)
        private set

    var initialMillis by mutableStateOf(0L)
        private set

    var secondsInMillis by mutableStateOf(60000)
        private set

    private lateinit var countDownTimer: CountDownTimer

    fun startTimer(hours: Int, minutes: Int) {
        totalMillis = millisLeft
        if (timerState == TimerState.STOPPED || timerState == TimerState.UNDEFINED) {
            val hoursInMillis = TimeUnit.HOURS.toMillis(hours.toLong())
            val minutesInMillis = TimeUnit.MINUTES.toMillis(minutes.toLong())
            totalMillis = hoursInMillis + minutesInMillis
            initialMillis = totalMillis
        }

        if (totalMillis > 0) {
            countDownTimer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    millisLeft = millisUntilFinished
                    if (millisLeft > 0 && secondsInMillis == 0) {
                        secondsInMillis = 60000
                    }

                    Log.i("SECONDSDEMENCIAL", "$timerState")
                    if (timerState == TimerState.STARTED) {
                        secondsInMillis -= 1000
                    }
//                    Log.i("SECONDSDEMENCIAL", "$secondsInMillis : $millisUntilFinished : $millisLeft")
                    val formattedTime = String.format(
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    )

                    currentTimer = formattedTime
                    timerState = TimerState.STARTED
                }

                override fun onFinish() {
                    currentTimer = DEFAULT_TIMER
                    secondsInMillis = 0
                    millisLeft = 0
                }
            }
            countDownTimer.start()
        } else {
            timerState = TimerState.UNDEFINED
        }
    }

    fun pauseTimer() {
        countDownTimer.cancel()
        timerState = TimerState.PAUSED
    }

    fun stopTimer() {
        countDownTimer.cancel()
        timerState = TimerState.STOPPED
        currentTimer = DEFAULT_TIMER
        initialMillis = 0L
        secondsInMillis = 60000
    }

    sealed class TimerState {
        object STARTED : TimerState()
        object STOPPED : TimerState()
        object PAUSED : TimerState()
        object UNDEFINED : TimerState()
    }

    companion object {
        private const val DEFAULT_TIMER = "00:00:00"
    }
}
