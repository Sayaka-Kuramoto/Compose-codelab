/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.racetracker.ui.test

import com.example.racetracker.ui.RaceParticipant
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RaceParticipantTest {
    private val raceParticipant = RaceParticipant(
        name = "Test",
        maxProgress = 100,
        progressDelayMillis = 500L,
        initialProgress = 0,
        progressIncrement = 1
    )
    @Test
    fun raceParticipant_RaceStarted_ProgressUpdated() = runTest{
        val expectedProgress = 1

        // 非同期実行
        launch { raceParticipant.run() }

        // run()の中のdelay(500)で500ミリ秒の待ち時間が発生するので、テストでは500ミリ秒時間を進めたことにする。
        advanceTimeBy(raceParticipant.progressDelayMillis)

        // 指定された時間にスケジュール設定されたタスクを実行しないので、保留中のタスクをすべて実行する命令を出す。
        runCurrent()

        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }
    @Test
    fun raceParticipant_RaceFinished_ProgressUpdated() = runTest{
        launch{ raceParticipant.run()}
        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(100,raceParticipant.currentProgress)
    }
}
