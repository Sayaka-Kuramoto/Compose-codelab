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

package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * 与えられたデータソースから[Item]の挿入、更新、削除、取得を提供するリポジトリ。
 * AppContainer
 */
interface ItemsRepository{
    /**
     * 指定されたデータソースからすべての項目を取得する。
     */
    fun getAllItemsStream(): Flow<List<Item>>

    /**
     * 与えられたデータソースから、[id]に一致する項目を取得する。
     */
    fun getItemStream(id: Int): Flow<Item?>

    /**
     * データソースに項目を挿入する
     */
    suspend fun insertItem(item: Item)

    /**
     * データソースから項目を削除する
     */
    suspend fun deleteItem(item: Item)

    /**
     * データソースの項目を更新する
     */
    suspend fun updateItem(item: Item)
}

