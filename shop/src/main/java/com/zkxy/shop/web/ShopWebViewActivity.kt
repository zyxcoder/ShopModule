package com.zkxy.shop.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityShopWebBinding

/**
 * @author zhangyuxiang
 * @date 2024/7/26
 */
class ShopWebViewActivity : BaseViewBindActivity<ShopWebViewViewModel, ActivityShopWebBinding>() {

    companion object {
        private const val WEB_TITLE = "web_title"
        private const val WEB_LOAD_URL = "web_load_url"
        fun startActivity(context: Context, title: String?, loadUrl: String) {
            context.startActivity(Intent(context, ShopWebViewActivity::class.java).apply {
                putExtra(WEB_TITLE, title)
                putExtra(WEB_LOAD_URL, loadUrl)
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun init(savedInstanceState: Bundle?) {
        mViewBind.toobarLayout.setTitleContent(intent.getStringExtra(WEB_TITLE))
        mViewBind.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    mViewModel.loadingChange.showDialog.value = "加载中..."
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mViewModel.loadingChange.dismissDialog.value = true
                }
            }
            loadUrl(intent.getStringExtra(WEB_LOAD_URL) ?: "")
        }
        onBackPressedDispatcher.addCallback(this) {
            // 如果WebView可以回退，则回退到上一页
            if (mViewBind.webView.canGoBack()) {
                mViewBind.webView.goBack()
            } else {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mViewBind.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mViewBind.webView.onResume()
    }

    override fun onDestroy() {
        mViewBind.webView.apply {
            loadUrl("about:blank")
            clearHistory()
            destroy()
        }
        super.onDestroy()
    }
}