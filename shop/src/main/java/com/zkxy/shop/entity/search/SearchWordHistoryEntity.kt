package com.zkxy.shop.entity.search

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 *
 * 搜索词记录表
 */
@Keep
@Entity(tableName = "search_word_history")
data class SearchWordHistoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "search_word") val searchWord: String,
    @ColumnInfo(name = "search_time") var searchTime: Long
)