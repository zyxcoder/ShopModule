package com.zyxcoder.mvvmroot.base

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.IntentFilter
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import com.zyxcoder.mvvmroot.callback.lifecycle.ActivityLifecycleObserver
import com.zyxcoder.mvvmroot.callback.lifecycle.AppLifeObserver
import com.zyxcoder.mvvmroot.network.manager.NetworkStateReceive

/**
 * Create by zyx_coder on 2022/11/17
 */
val appContext: Application by lazy {
    ObserverContentProvider.application
}

class ObserverContentProvider : ContentProvider() {
    companion object {
        lateinit var application: Application
        private var networkStateReceive: NetworkStateReceive? = null
        var watchActivityLifecycle = true
        var watchAppLifecycle = true
    }

    override fun onCreate(): Boolean {
        val applicationContext = context!!.applicationContext as Application
        initApplication(applicationContext)
        return true
    }

    private fun initApplication(applicationContext: Application) {
        application = applicationContext
        networkStateReceive = NetworkStateReceive()
        application.registerReceiver(
            networkStateReceive, IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )
        if (watchActivityLifecycle) {
            application.registerActivityLifecycleCallbacks(ActivityLifecycleObserver())
        }
        if (watchAppLifecycle) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeObserver)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

}