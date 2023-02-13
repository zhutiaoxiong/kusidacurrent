package com.kulala.staticsfunc.static_system;

/**
 * Created by Administrator on 2016/9/21.
 */
public class OByteConvert {
    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     * bit位正值
     */
    public static byte[] getBitArray(byte b) {
        byte[] array = getBitArrayAnti(b);
        byte[] result = new byte[8];
        for (int i = 0; i <= 7; i++) {
            result[i] = array[7-i];
        }
        return result;
    }
    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     * bit位反值
     */
    public static byte[] getBitArrayAnti(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
}
