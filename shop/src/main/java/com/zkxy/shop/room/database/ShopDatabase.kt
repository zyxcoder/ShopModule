package com.zkxy.shop.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zkxy.shop.room.dao.SearchHistoryDao
import com.zyxcoder.mvvmroot.base.appContext

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */

@Database(
    entities = [SearchWordHistoryEntity::class], version = 1, exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var instance: ShopDatabase? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                appContext, ShopDatabase::class.java, "shop_database"
            ).allowMainThreadQueries().build().apply {
                instance = this
            }
        }
    }
}