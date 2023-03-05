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

package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.security.PrivateKey

/**
 * View Model to retrieve all items in the Room database.
 */
class HomeViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // 構成変更（デバイスの回転など）のようなライフサイクル イベントが発生すると、アクティビティが再作成されます。
    // これにより、再コンポーズと Flow からの収集が再度行われます。
    // ライフサイクル イベント間で既存のデータが失われないように、値を状態としてキャッシュに保存することをおすすめします。
    // ViewModel から Flow を公開するには、StateFlow を使用することをおすすめします。
    // StateFlow を使用すると、UI のライフサイクルに関係なくデータを保存でき、監視できます
    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            //stateIn 演算子を使用して Flow を StateFlow に変換します
            .stateIn(
                //StateFlow のライフサイクル viewModelScope がキャンセルされるとStateFlowもキャンセル
                scope = viewModelScope,
                // UIが表示されている場合にのみアクティブ
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Item> = listOf())
