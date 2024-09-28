package com.zkxy.shop.utils;


import android.annotation.SuppressLint;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {


    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    /**
     * AES加密
     */
    public static String encryptData(String data, String randomKey) {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(ALGORITHM_STR); // 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(randomKey.getBytes(), ALGORITHM));// 初始化
            return Base64.encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES解密
     */
    public static String decryptData(String base64Data, String randomKey) {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(randomKey.getBytes(), ALGORITHM));
            return new String(cipher.doFinal(Base64.decode(base64Data)));
        } catch (Exception e) {
            return null;
        }
    }


}
