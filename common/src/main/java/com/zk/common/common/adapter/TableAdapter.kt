package com.zk.common.common.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.zk.common.common.providers.CardIdBinder
import com.zk.common.common.providers.CheckBinder
import com.zk.common.common.providers.DialogSelectBinder
import com.zk.common.common.providers.FileBinder
import com.zk.common.common.providers.IconTittleBinder
import com.zk.common.common.providers.InputBinder
import com.zk.common.common.providers.InputPhoneBinder
import com.zk.common.common.providers.MoneyBinder
import com.zk.common.common.providers.SelectTimeBinder
import com.zk.common.common.providers.TitleBinder
import com.zk.common.common.providers.TxtBinder
import com.zk.common.entity.common.CardIdItemEntity
import com.zk.common.entity.common.CheckItemEntity
import com.zk.common.entity.common.DialogSelectItemEntity
import com.zk.common.entity.common.FileItemEntity
import com.zk.common.entity.common.IconTitleItemEntity
import com.zk.common.entity.common.InputItemEntity
import com.zk.common.entity.common.InputPhoneItemEntity
import com.zk.common.entity.common.MoneyItemEntity
import com.zk.common.entity.common.SelectTimeItemEntity
import com.zk.common.entity.common.TitleItemEntity
import com.zk.common.entity.common.TxtItemEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/10
 */
class TableAdapter(
    onUploadFileClickListener: ((ivHodler: ImageView) -> Unit)? = null,
    onUpLoadRightSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null,
    onUpLoadReverseSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null
) : BaseBinderAdapter() {
    init {
        addItemBinder(InputItemEntity::class.java, InputBinder())
        addItemBinder(InputPhoneItemEntity::class.java, InputPhoneBinder())
        addItemBinder(CheckItemEntity::class.java, CheckBinder())
        addItemBinder(FileItemEntity::class.java, FileBinder(onUploadFileClickListener))
        addItemBinder(TitleItemEntity::class.java, TitleBinder())
        addItemBinder(DialogSelectItemEntity::class.java, DialogSelectBinder())
        addItemBinder(MoneyItemEntity::class.java, MoneyBinder())
        addItemBinder(TxtItemEntity::class.java, TxtBinder())
        addItemBinder(SelectTimeItemEntity::class.java, SelectTimeBinder())
        addItemBinder(IconTitleItemEntity::class.java, IconTittleBinder())
        addItemBinder(
            CardIdItemEntity::class.java,
            CardIdBinder(
                onUpLoadRightSidePic = onUpLoadRightSidePic,
                onUpLoadReverseSidePic = onUpLoadReverseSidePic
            )
        )
    }
}