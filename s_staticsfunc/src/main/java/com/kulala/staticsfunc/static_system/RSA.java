package com.kulala.staticsfunc.static_system;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSA {

    public static final  String KEY_ALGORITHM     = "RSA";
    private static final int    MAX_ENCRYPT_BLOCK = 117;
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA1XDAOhAknxaLVhbf9+JX6GfI85s3QySLHtWGKgJjsE/yB9kVfEGTU8mcOxUctpc/ozdQH3UbaeglKfMYcwzayrjy2/P00d+vZ94QqL48Ll09wDUvHNCUCt7GJUiEF8h+CTYv1aPW8tGLV+kJDdKYBpWAAA5CMPvz3SA0YNcfiQIDAQAB";

    public static String testStr = "{\"userId\":1,\"deviceToken\":\"111111\",\"platform\":2,\"cversion\":1}";

    public static byte[] RSAgenerator(String msg, String publicKeyy) throws Exception {
        byte[] data = msg.getBytes();
        byte[]             keyBytes    = Base64.decode(publicKey.getBytes(), Base64.NO_PADDING);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory         keyFactory  = KeyFactory.getInstance(KEY_ALGORITHM);
        Key                publicK     = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");//keyFactory.getAlgorithm()
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int                   inputLen = data.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    // ===================================================================
    private static final int    MAX_DECRYPT_BLOCK = 128;
    public static               String pkey              = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIDVcMA6ECSfFotWFt/34lfoZ8jzmzdDJIse1YYqAmOwT/IH2RV8QZNTyZw7FRy2lz+jN1AfdRtp6CUp8xhzDNrKuPLb8/TR369n3hCovjwuXT3ANS8c0JQK3sYlSIQXyH4JNi/Vo9by0YtX6QkN0pgGlYAADkIw+/PdIDRg1x+JAgMBAAECgYA5sVtAeb/o5mUOGIs92J+/hg+T6aNEaAjhU/mCjyVqxvXGCMWp3W7wLsFF+R9HMG0izlBizPOARzjyC4sVE9BZN2lFTccsVvdrdTDaXU+qbkVw2R/cydQNY7TtHqieRtrSsC/91a5lPvBwuuupdFytsweAUzjtryR1OIs9y0v+0QJBALjVFEeJXiPpMnyWLRPqmu6hqlz5KXM7gW2Wb+wbTBCcTReMfEf8yTPPwdhG073Z2RZ9qgVN+pz0IfhDMtNMjM8CQQCycJTKzusiMGMPHKCSDXKU+WBjePYNFgEQ5K3XAuMbOckUh/p5NDwSEbxHu9mvzOdqKyL2K1qp0bMlTdwyWpQnAkAUTOLjUIUUcKJo6J2e2F+X4g2yFHOnAlweyy0Tw5PBJ0mYUoe76Fm45RKnTHApI+oh/D9WQdBKuM2aVNcaOgglAkEAsMriYN4KEWGng711Sb+RGxDI80VRYz296KLpqfHuWXczRuhCvfuG2NBBn0D7OTbmwUXj6cqilUZzHC68UBd2WQJAe+yGxwohsmGeR9GuK4GxVBtP4Y6w85mNqVJQIDncdoILIsFmEjx+/Iw2jcYuH5QehajHunUNNe3qdnzzx9DyCg==";

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[]              keyBytes     = Base64.decode(privateKey,Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory          keyFactory   = KeyFactory.getInstance(KEY_ALGORITHM);
        Key                 privateK     = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher              cipher       = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int                   inputLen = encryptedData.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

}
