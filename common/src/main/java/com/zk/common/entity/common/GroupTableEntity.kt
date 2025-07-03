package com.zk.common.entity.common

import androidx.annotation.Keep
import com.zk.common.common.providers.inter.ServerKeyInner

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
@Keep
data class GroupTableEntity(
    val tables: ArrayList<out ServerKeyInner>,
    val paddingBottom: Int? = 20
)