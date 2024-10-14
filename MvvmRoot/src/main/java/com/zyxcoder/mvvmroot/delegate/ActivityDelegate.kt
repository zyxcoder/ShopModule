package com.zyxcoder.mvvmroot.delegate

import android.app.Activity
import com.zyxcoder.mvvmroot.ext.get
import com.zyxcoder.mvvmroot.ext.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Create by zyx_coder on 2023/4/23
 */

class ActivityArgumentDelegate<T : Any> : ReadWriteProperty<Activity, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return (thisRef.intent?.get<T>(property.name))
            ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        thisRef.intent?.put(property.name, value)
    }
}

fun <T : Any> extraData(): ReadWriteProperty<Activity, T> = ActivityArgumentDelegate()
