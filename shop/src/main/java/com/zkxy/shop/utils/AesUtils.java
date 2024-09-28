package com.zkxy.shop.utils;



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
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptData(String data,String randomKey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR); // 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(randomKey.getBytes(), ALGORITHM));// 初始化
		return   Base64.encode(cipher.doFinal(data.getBytes()));
	}

	/**
	 * AES解密
	 * @param base64Data
	 * @return
	 * @throws
	 */
	public static String decryptData(String base64Data,String randomKey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(randomKey.getBytes(), ALGORITHM));
		return new String(cipher.doFinal(Base64.decode(base64Data)));
	}


}
