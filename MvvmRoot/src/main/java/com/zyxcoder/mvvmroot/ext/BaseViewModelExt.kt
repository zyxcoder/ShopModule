package com.zyxcoder.mvvmroot.ext

import androidx.lifecycle.viewModelScope
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.network.ExceptionHandle
import com.zyxcoder.mvvmroot.utils.loge
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Create by zyx_coder on 2022/11/18
 */

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

/**
 * 创建并执行协程
 * @param block 协程中执行
 * @param error 错误时执行
 * @param cancel 取消时执行
 * @param showErrorToast 是否展示后端返回的code错误提示吐司(不包含网络连接相关的错误)
 * @return Job
 */
fun <T> BaseViewModel.request(
    block: Block<Unit>,
    error: Error? = null,
    cancel: Cancel? = null,
    showErrorToast: Boolean = true
): Job {
    return viewModelScope.launch {
        try {
            block.invoke()
        } catch (e: Exception) {
            e.message.toString().loge(BaseViewModel::class.java.simpleName)
            when (e) {
                is CancellationException -> {
                    cancel?.invoke(e)
                }
                else -> {
                    if (showErrorToast) {
                        ExceptionHandle.handleException(e)
                    }
                    error?.invoke(e)
                }
            }
        }
    }
}

/**
 * 创建并执行协程
 * @param block 协程中执行
 * @return Deferred<T>
 */
fun <T> BaseViewModel.async(block: Block<T>): Deferred<T> {
    return viewModelScope.async {
        block.invoke()
    }
}

/**
 * 取消协程
 * @param job 协程job
 */
fun BaseViewModel.cancelJob(job: Job?) {
    if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
        job.cancel()
    }
}