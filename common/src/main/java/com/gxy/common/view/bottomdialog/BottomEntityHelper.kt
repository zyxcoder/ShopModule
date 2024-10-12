package com.gxy.common.view.bottomdialog

import com.gxy.common.entity.common.BottomListEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/2
 * 由于底部弹窗通过put传递数据会出现超内存的情况，故使用单例传递数据
 *
 * 用底部弹窗的title作为key,数据作为value
 */
class BottomEntityHelper private constructor() {
    private val bottomEntityDatas = hashMapOf<String?, ArrayList<BottomListEntity>?>()

    companion object {
        val instance: BottomEntityHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BottomEntityHelper()
        }
    }

    fun setBottomEntityData(title: String?, data: ArrayList<BottomListEntity>?) {
        bottomEntityDatas.put(title, data)
    }

    fun getBottomEntity(title: String?): ArrayList<BottomListEntity>? {
        return bottomEntityDatas[title]
    }
}