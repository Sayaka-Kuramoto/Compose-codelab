package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * データアクセスオブジェクト
 * データベースインスタンスのプロパティ
 * データベースへの操作をする
 */
@Dao
interface ItemDao {
    // onConflict プライマリーキーが競合した時の処理。IGNOREは競合したら既存の行のまま
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item:Item)

    @Delete
    suspend fun delete(item: Item)

    // 戻り値の型として Flow を指定すると、データベース内のデータが変更されるたびに通知が届きます
    // つまり、データを明示的に取得する必要があるのは一度だけです。
    // 戻り値の型が Flow であるため、Room はバックグラウンド スレッドでクエリを実行します。
    // suspend 関数にしてコルーチン スコープ内で呼び出す必要はありません。
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}