package com.zyxcoder.mvvmroot.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * @author zhangyuxiang
 * @date 2024/5/16
 */
/**
 * 临时附加一个无界面的Fragment，当这个Fragment附加成功之后，调用launch方法，
 * 并在重写的ActivityResultCallback方法里面去回调外面传进来的Lambda
 *
 * @param targerActivity 目标activity
 * @param targerIntent 自定义启动Intent，主要用于系统intent
 * ps ：targerActivity和targerIntent两者二选一
 * @param params intent携带的参数
 * @param callback setResult后的回调
 */
fun AppCompatActivity.launchActivityForResult(
    targerActivity: KClass<out Activity>? = null,
    targerIntent: Intent? = null,
    params: Map<String, Any?>? = null,
    callback: (result: Intent?) -> Unit
) {
    val intent = targerActivity?.let {
        Intent(this, targerActivity.java).putExtras(params)
    } ?: targerIntent
    val ghostFragment = GhostFragment()
    ghostFragment.init(intent) { result: Intent? ->
        //包装一层：在回调执行完成之后把对应的Fragment移除掉
        callback(result)
        supportFragmentManager.beginTransaction().remove(ghostFragment).commitAllowingStateLoss()
    }
    //把Fragment添加进去
    supportFragmentManager.beginTransaction()
        .add(ghostFragment, GhostFragment::class.java.simpleName).commitAllowingStateLoss()
}

/**
 * 一个无界面的Fragment，主要用于接收activity的回调
 */
class GhostFragment : Fragment() {
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null

    private val intentActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //检查resultCode，如果不OK的话，那就直接回传个null
            val result =
                if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) it.data else null
            callback?.invoke(result)
        }

    fun init(intent: Intent?, callback: ((result: Intent?) -> Unit)) {
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //附加到Activity之后马上launch
        intent?.let {
            intentActivityResultLauncher.launch(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
    }
}