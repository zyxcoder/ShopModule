package com.zyxcoder.mvvmroot.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import jp.wasabeef.glide.transformations.BitmapTransformation
import java.security.MessageDigest

/**
 * Create by zyx_coder on 2022/12/8
 */


/**
 * 图片加载，默认为view的Context
 * @param [url] 图片链接
 * @param [imageOptions] 加载配置选项
 * @param [onSuccess] 成功回调
 * @param [onFailure] 失败回调
 */
@JvmOverloads
fun ImageView.loadImage(
    url: String?,
    imageOptions: ImageOptions? = null,
    fragment: Fragment? = null,
    fragmentActivity: FragmentActivity? = null,
    activity: Activity? = null,
    context: Context? = null,
    onSuccess: (() -> Unit)? = null,
    onFailure: (() -> Unit)? = null
) {
    try {
        when {
            fragment != null -> Glide.with(fragment)
            fragmentActivity != null -> Glide.with(fragmentActivity)
            activity != null -> Glide.with(activity)
            context != null -> Glide.with(context)
            else -> Glide.with(this)
        }.load(url)
            .apply(requestOptions(imageOptions))
            .transition(
                DrawableTransitionOptions.with(
                    DrawableCrossFadeFactory
                        .Builder(300)
                        .setCrossFadeEnabled(imageOptions?.crossFadeEnable ?: true)
                        .build()
                )
            )
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onSuccess?.invoke()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onFailure?.invoke()
                    return false
                }
            })
            .into(this)
    } catch (e: Exception) {
        Log.d("ImageLoader", "图片加载异常")
    }
}

fun ImageView.loadImage(uri: Uri, imageOptions: ImageOptions? = null) {
    try {
        Glide.with(context)
            .load(uri)
            .apply(requestOptions(imageOptions))
            .into(this)
    } catch (e: Exception) {
        Log.d("ImageLoader", "图片加载异常")
    }
}

fun ImageView.loadImage(drawable: Drawable, imageOptions: ImageOptions? = null) {
    try {
        Glide.with(context)
            .load(drawable)
            .apply(requestOptions(imageOptions))
            .into(this)
    } catch (e: Exception) {
        Log.d("ImageLoader", "图片加载异常")
    }
}

fun ImageView.loadImage(@DrawableRes drawableRes: Int, imageOptions: ImageOptions? = null) {
    try {
        Glide.with(context)
            .load(drawableRes)
            .apply(requestOptions(imageOptions))
            .into(this)
    } catch (e: Exception) {
        Log.d("ImageLoader", "图片加载异常")
    }
}

@SuppressLint("CheckResult")
private fun requestOptions(imageOptions: ImageOptions?) = RequestOptions().apply {
    imageOptions?.let {
        if (it.topLeftRadius > 0f || it.topRightRadius > 0f || it.bottomLeftRadius > 0f || it.bottomRightRadius > 0f) {
            //有不同的圆角值
            transform(
                MultiTransformation(
                    if (it.centerCrop) {
                        CenterCrop()
                    } else {
                        FitXY()
                    }, GranularRoundedCorners(
                        it.topLeftRadius,
                        it.topRightRadius,
                        it.bottomRightRadius,
                        it.bottomLeftRadius
                    )
                )
            )
        } else if (it.cornersRadius > 0) {
            transform(
                MultiTransformation(
                    if (it.centerCrop) {
                        CenterCrop()
                    } else {
                        FitXY()
                    }, RoundedCorners(it.cornersRadius)
                )
            )
        } else if (it.cornersRadius <= 0 && it.centerCrop) {
            transform(CenterCrop())
        }
        placeholder(it.placeholder)
        error(it.error)
        fallback(it.fallback)
        if (it.circleCrop) {
            circleCrop()
        }
    }
}

class ImageOptions {
    var placeholder = 0         // 加载占位图
    var error = 0               // 错误占位图
    var fallback = 0            // null占位图
    var cornersRadius = 0       // 圆角半径
    var topLeftRadius = 0f      // 左上圆角半径
    var topRightRadius = 0f     // 右上圆角半径
    var bottomLeftRadius = 0f   // 左下圆角半径
    var bottomRightRadius = 0f  // 右下圆角半径
    var circleCrop = false      // 是否裁剪为圆形
    var crossFadeEnable = true  // 是否透明渐变加载
    var centerCrop = true       // 是否居中裁剪
}

/**
 * 将bitmap填满控件
 */
class FitXY : BitmapTransformation() {
    override fun equals(other: Any?): Boolean = other is FitXY

    override fun hashCode(): Int = "com.kotlin.utils.FitXY".hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("com.kotlin.utils.FitXY".toByteArray(Charsets.UTF_8))
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        inBitmap: Bitmap,
        width: Int,
        height: Int
    ): Bitmap {
        if (inBitmap.width == width && inBitmap.height == height) {
            return inBitmap
        }
        val matrix = Matrix().also {
            it.setScale(
                width.toFloat() / inBitmap.width.toFloat(),
                height.toFloat() / inBitmap.height.toFloat()
            )
        }
        val result = pool.get(
            width, height, if (inBitmap.config != null) {
                inBitmap.config
            } else {
                Bitmap.Config.ARGB_8888
            }
        )
        TransformationUtils.setAlpha(inBitmap, result)
        try {
            Canvas(result).apply {
                drawBitmap(inBitmap, matrix, Paint(PAINT_FLAGS))
                setBitmap(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}

/**
 * 暂停加载图片
 */
fun pauseLoadImage(context: Context) {
    try {
        Glide.with(context).pauseRequests()
    } catch (e: Exception) {
    }
}

/**
 * 恢复图片加载
 */
fun resumeLoadImage(context: Context) {
    try {
        Glide.with(context).resumeRequests()
    } catch (e: Exception) {
    }
}