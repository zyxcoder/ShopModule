package com.zyxcoder.mvvmroot.delegate

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zyxcoder.mvvmroot.ext.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Create by zyx_coder on 2023/4/23
 */
class FragmentArgumentDelegate<T : Any> : ReadWriteProperty<Fragment, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return (thisRef.arguments?.get(property.name) as? T)
            ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        args.put(property.name, value)
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentArgumentDelegate()