package com.zkxy.shop.utils;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {


    //用于加密公钥
    public static final String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoMdGavxSxj+wVw1ipiHHv1MgmuEJaS4OFbxWKncTqGHuxTWM4LrIv4YZ/ySaWX8Fuu8VW5m5S/WnK006QKNkJA4WQkxYvJQGdxig5X9aHcswa+WQ83bnfBYkC1DP6S0ek56Wr7GJt7AuxuvFaYJFZ+i11W5exuDvhbhUWcDcN+EP7dRVnz961Sfxd1K6ZT4uhSyIQ5evFZ5kJD2+xCcWA50J1qbItcaq1VUeXU9HsZXoW3C2FBK0/xGI6G8INjNOkFMDCfNvhxHbqJMXpMxM3GqyVSpiODwFHWhwu085pIBal7klp5DogC6XoBvML4Mseq2nohDppk2Jts0HiI3zCQIDAQAB";

    /**
     * 公钥加密.
     *
     * @param text 待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String text) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(android.util.Base64.decode(publicKeyString, android.util.Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(text.getBytes());
            return new String(android.util.Base64.encode(result, android.util.Base64.NO_WRAP));
        } catch (Exception e) {
            return null;
        }
    }

    public static String decryptByPrivateKey(String text) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(android.util.Base64.decode(publicKeyString, android.util.Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(text.getBytes());
            return new String(android.util.Base64.encode(result, android.util.Base64.NO_WRAP));
        } catch (Exception e) {
            return null;
        }
    }
}
