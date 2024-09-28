package com.zkxy.shop.utils

import android.os.Build
import java.util.Base64
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * @author zhangyuxiang
 * @date 2024/9/28
 */
object RandomKey {
    private const val ALGORITHM = "AES"

    // 生成随机 AES 密钥
    fun generateRandomKey(keySize: Int = 128): String {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(keySize)
        return keyToBase64String(keyGen.generateKey())
    }

    // 将密钥转换为 Base64 编码的字符串
    private fun keyToBase64String(key: SecretKey): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(key.encoded)
        } else {
            android.util.Base64.encodeToString(key.encoded, android.util.Base64.DEFAULT)
        }
    }
}