package com.gxy.common.common.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.gxy.common.common.providers.CardIdBinder
import com.gxy.common.common.providers.CheckBinder
import com.gxy.common.common.providers.DialogSelectBinder
import com.gxy.common.common.providers.FileBinder
import com.gxy.common.common.providers.IconTittleBinder
import com.gxy.common.common.providers.InputBinder
import com.gxy.common.common.providers.InputPhoneBinder
import com.gxy.common.common.providers.MoneyBinder
import com.gxy.common.common.providers.SelectTimeBinder
import com.gxy.common.common.providers.TitleBinder
import com.gxy.common.common.providers.TxtBinder
import com.gxy.common.entity.common.CardIdItemEntity
import com.gxy.common.entity.common.CheckItemEntity
import com.gxy.common.entity.common.DialogSelectItemEntity
import com.gxy.common.entity.common.FileItemEntity
import com.gxy.common.entity.common.IconTitleItemEntity
import com.gxy.common.entity.common.InputItemEntity
import com.gxy.common.entity.common.InputPhoneItemEntity
import com.gxy.common.entity.common.MoneyItemEntity
import com.gxy.common.entity.common.SelectTimeItemEntity
import com.gxy.common.entity.common.TitleItemEntity
import com.gxy.common.entity.common.TxtItemEntity

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