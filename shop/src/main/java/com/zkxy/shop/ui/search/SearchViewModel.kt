package com.zkxy.shop.ui.search

import com.zkxy.shop.room.database.ShopDatabase
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchViewModel : BaseViewModel() {

    val searchWords = ShopDatabase.getInstance().searchHistoryDao().getAllSearchWords()
}