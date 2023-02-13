package com.kulala.staticsfunc.static_system;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    /**
     * AES加密
     * @param info 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] AESgenerator(String info, String encryptKey)   {
        if(encryptKey == null  || encryptKey.equals(""))return null;
        try{
            String iv        = "0102030405060708";
            Cipher cipher    = Cipher.getInstance("AES/CBC/NoPadding");
            int    blockSize = cipher.getBlockSize();

            byte[] dataBytes = info.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec   keyspec = new SecretKeySpec(encryptKey.getBytes(), "AES");
            IvParameterSpec ivspec  = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解密AES加密过的字符串
     * @param content  AES加密过过的内容
     * @param password  加密时的密码
     * @return  明文
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            String iv        = "0102030405060708";
            // 创建 AES 的 key 生产者
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            // 利用用户密码作为随机数初始化出128位的key生产者。SecureRandom 是生产安全随机数序列，password.getBytes()是种子
            // 只要种子相同，序列就一样，所以解密只要有password就行
            keyGenerator.init(128, new SecureRandom(password.getBytes()));
            // 根据用户密码生成一个密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 返回基本编码格式的密钥。如果此密钥不支持编码，则返回null
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为 AES 专用密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(),"AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec ivspec  = new IvParameterSpec(iv.getBytes());
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivspec);
            // 加密
            byte[] result = cipher.doFinal(content);
            // 返回明文
            return result;
        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
