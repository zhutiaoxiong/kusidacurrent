package com.kulala.staticsfunc.static_system;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * Created by Administrator on 2017/1/9.
 */

public class DES {

    public static final String  KEY_ALGORITHM    = "DES";
    public static final String  CIPHER_ALGORITHM = "DES/ECB/PK CS5Padding";

    //=======================
    private byte[] encryptMessageWithDES(byte[] buffer, boolean isEncrypt) {
        //手加
        if (!isEncrypt) return buffer;
        //手加
        byte[] encrypts      = new byte[3];
        byte[] bufferMessage = buffer;
        // 设置加密标志位
        // 0表示不加密，1表示加密
        if (isEncrypt) {
            encrypts[0] = 1;
        } else {
            encrypts[0] = 0;
        }
        encrypts[1] = -1;// 密钥的长度
        encrypts[2] = -1;// 消息的长度
        String encryptm    = "";// 存放加密后的消息
        String encryptInfo = new String(encrypts);// 存放加密Flag
// 存放DES加密的 Key，用于解密
        String encryptKeys = "s";
        String bufferm     = new String(bufferMessage);
        if (isEncrypt) {
            try {
                byte[] key = initKey();
                // 对DES加密 Key进行Base64编码，防止string转换成byte时由于不同机器的默认编码不同导致，byte再转换成String出现乱码
                String base64keys = Base64.encodeToString(key, 0);
                // 对消息进行DES加密
                byte[] messages = encryptDES(bufferMessage, key);
                // 对DES加密的数组进行Base64编码，便于显示
                String  base64EncryptResult = Base64.encodeToString(messages, 0);
                Integer length              = base64keys.getBytes().length;
                Integer MessageLength       = bufferMessage.length;
                encrypts[1] = length.byteValue();
                encrypts[2] = MessageLength.byteValue();
                String encryptIn = new String(encrypts);
                encryptInfo = encryptIn;
                encryptKeys = base64keys;
                bufferm = base64EncryptResult;
            } catch (Exception e) {
            }
        } else {
            // 没有使用DES加密，也需要对信息进行 Base64编码
            String  base64keys    = Base64.encodeToString(encryptKeys.getBytes(), 0);
            String  base64Message = Base64.encodeToString(bufferMessage, 0);
            Integer length        = base64keys.getBytes().length;
            Integer MessageLength = bufferMessage.length;
            encrypts[1] = length.byteValue();
            encrypts[2] = MessageLength.byteValue();
            String encryptIn = new String(encrypts);
            encryptInfo = encryptIn;
            encryptKeys = base64keys;
            bufferm = base64Message;
        }
        encryptm = encryptInfo;
        encryptm += encryptKeys;
        encryptm += bufferm;
//将加密后的消息转换成byte数组
        return encryptm.getBytes();
    }

    // 对从缓冲区得到的消息进行解密
    private String decryptMessageWithDES(byte[] readBuf) {
        int    bytes           = readBuf.length;
        String originalMessage = "";
        String outputMessage   = "";
        byte[] decryptMessage;
        // 获取加密标志位
        if (readBuf[0] == 1) {
            // 获取密钥长度
            int keyLength = readBuf[1];
            // 获取消息长度
            int    messageLength = readBuf[2];
            String keys          = new String(readBuf, 3, keyLength);
            byte[] base64keys    = Base64.decode(keys, 0);
            String encryptMessage = new String(readBuf, keyLength + 3,
                    keyLength + messageLength + 3);
            byte[] base64decryptResult = Base64.decode(encryptMessage, 0);
            try {
                decryptMessage = decryptDES(base64decryptResult, base64keys);
                String decryptedMessage = new String(decryptMessage);
                originalMessage = decryptedMessage;
                outputMessage = originalMessage;
                outputMessage += "\n";
                outputMessage += encryptMessage;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            int    keyLength        = readBuf[1];
            int    messageLength    = readBuf[2];
            int    encryptLength    = 3 + keyLength;
            String noEncryptMessage = new String(readBuf, encryptLength, encryptLength + messageLength);
            byte[] base64decryptResult = Base64.decode
                    (noEncryptMessage, 0);
            String base64decryptMessage = new String(base64decryptResult);
            originalMessage = base64decryptMessage;
            outputMessage = originalMessage;
        }
        return outputMessage;
    }

    //=============================
    private static Key toKey(byte[] key) throws Exception {
        // 实例化DES 加密材料
        DESKeySpec dks = new DESKeySpec(key);
        // 实例化私密密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.
                getInstance(KEY_ALGORITHM);
        // 生成私密密钥
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    // 生成密钥
    public static byte[] initKey() throws Exception {
        // 实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器
        kg.init(56);
        // 生成秘密密钥
        SecretKey secretKey = kg.generateKey();
        // 获得密钥的二进制编码形式
        return secretKey.getEncoded();
    }

    //  加密消息，以数组的形式返回加密后的结果
    public static byte[] encryptDES(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        //IvParameterSpec zeroIv = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);//,  zeroIv);
        return cipher.doFinal(data);
    }

    // 解密消息，以数组的形式返回解密后的结果
    // 加密与解密用的key需要保持一致
    public static byte[] decryptDES(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化设置加密模式
        cipher.init(Cipher.DECRYPT_MODE, k);//,  zeroIv);
        //执行操作
        return cipher.doFinal(data);
    }
}
