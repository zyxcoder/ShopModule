package com.gxy.common.ext

import com.gxy.common.entity.common.BottomListEntity

/**
 * @author zhangyuxiang
 * @date 2024/3/19
 */
/**
 * 将后端返回的List数组转为底部弹窗需要的BottomListEntity数组
 */
fun List<HashMap<String?, String?>>?.listConvertToBottomListEntity(): ArrayList<BottomListEntity> {
    return ArrayList(this?.map {
        BottomListEntity(
            id = it.entries.firstOrNull()?.key?.toIntOrNull(), name = it.entries.firstOrNull()?.value
        )
    } ?: arrayListOf())
}

fun List<HashMap<Int?, String?>>?.listConvertIntToBottomListEntity(): ArrayList<BottomListEntity> {
    return ArrayList(this?.map {
        BottomListEntity(
            id = it.entries.firstOrNull()?.key, name = it.entries.firstOrNull()?.value
        )
    } ?: arrayListOf())
}