package com.zyxcoder.mvvmroot.delegate

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zyxcoder.mvvmroot.ext.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Create by zyx_coder on 2023/4/23
 */
class FragmentNullableArgumentDelegate<T : Any?> : ReadWriteProperty<Fragment, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return thisRef.arguments?.get(property.name) as? T
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        value?.let {
            args.put(property.name, it)
        } ?: args.remove(property.name)
    }
}

fun <T : Any?> argumentNullable(): ReadWriteProperty<Fragment, T?> =
    FragmentNullableArgumentDelegate()