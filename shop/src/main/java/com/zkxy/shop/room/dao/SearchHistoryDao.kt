package com.zkxy.shop.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zkxy.shop.entity.search.SearchWordHistoryEntity

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchWord(searchHistoryEntity: SearchWordHistoryEntity)

    @Query("DELETE FROM SEARCH_WORD_HISTORY")
    fun deleteAllSearchWords()

    @Query("SELECT * FROM SEARCH_WORD_HISTORY ORDER BY SEARCH_TIME DESC")
    fun getAllSearchWords(): LiveData<List<SearchWordHistoryEntity>>
}