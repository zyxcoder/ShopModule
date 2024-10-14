package com.zyxcoder.mvvmroot.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

object Bus {

    inline fun <reified T> post(channel: String, value: T) =
        LiveEventBus.get(channel, T::class.java).post(value)

    inline fun <reified T> observe(
        channel: String,
        owner: LifecycleOwner,
        crossinline observer: ((value: T) -> Unit)
    ) = LiveEventBus.get(channel, T::class.java).observe(owner, Observer { observer(it) })

    inline fun <reified T> observeForever(
        channel: String,
        crossinline observer: ((value: T) -> Unit)
    ) = LiveEventBus.get(channel, T::class.java).observeForever { observer(it) }

    inline fun <reified T> observeSticky(
        channel: String,
        owner: LifecycleOwner,
        crossinline observer: ((value: T) -> Unit)
    ) = LiveEventBus.get(channel, T::class.java).observeSticky(owner, Observer { observer(it) })

    inline fun <reified T> observeStickyForever(
        channel: String,
        crossinline observer: ((value: T) -> Unit)
    ) = LiveEventBus.get(channel, T::class.java).observeStickyForever { observer(it) }

    fun <T> observeForJava(
        channel: String,
        owner: LifecycleOwner,
        clazz: Class<T>,
        observer: ((value: T) -> Unit)
    ) = LiveEventBus.get(channel, clazz).observe(owner, Observer { observer(it) })

    fun <T> postForJava(
        channel: String,
        value: T,
        clazz: Class<T>
    ) = LiveEventBus.get(channel, clazz).post(value)
}
