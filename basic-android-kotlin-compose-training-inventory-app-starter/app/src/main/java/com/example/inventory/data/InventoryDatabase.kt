package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * roomデータベースのインスタンスを作成する
 */

// ItemクラスのKotlinリフレクションオブジェクトを配列形式で渡している
@Database(entities = [Item::class], version =  1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase()
{
    abstract fun itemDao() :ItemDao
    companion object{

        // volatile 変数の値はキャッシュに保存されません。
        // つまり、あるスレッドが Instance に加えた変更が、すぐに他のすべてのスレッドに反映されます。
        @Volatile
        private var Instance:InventoryDatabase?=null

        fun getDatabase(context: Context):InventoryDatabase {
            return Instance ?: synchronized(this){
                // synchronized ブロックで囲むと、このコードブロックには一度に 1 つのスレッドしか入ることができず、
                // データベースは一度だけ初期化されます
                Room.databaseBuilder(context,InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()// 新しいパラメータの追加など、エンティティ クラスでなんらかの変更を行った場合、アプリでデータベースの削除と再初期化を行えるようにする
                    .build()
                    .also { Instance = it }
            }
        }
    }
}