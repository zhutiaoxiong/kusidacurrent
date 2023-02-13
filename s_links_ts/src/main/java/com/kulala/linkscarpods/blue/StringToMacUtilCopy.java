package com.kulala.linkscarpods.blue;

import android.text.TextUtils;

public class StringToMacUtilCopy {


    /**

     * 在一个字符串的相同指定长度间隔处插入指定字符串

     *

     * @param target 需要处理的字符串

     *

     * @param size 指定长度

     *

     * @param insert 插入的字符串，默认为 "<br/>"

     * @return

     */

    public static String collapseString(String target, int size, String insert){
        if(TextUtils.isEmpty(target)) return target; //目标字符串为空，返回目标字符串
        int target_length = target.length();
        if(target_length <= size) return target; //目标字符串长度等于间隔长度 ， 返回目标字符串
        insert = TextUtils.isEmpty(insert) ? "<br/>" : insert;
        //插入次数
        int times = 0;
        if(target_length % size == 0){
            times = target_length / size - 1;
        }else{
            times = target_length / size;
        }
        //结果字符集
        char [] result_chars = new char[target_length + times];
        //目标字符集
        char [] target_chars = new char[target_length];
        //将字符串数据装入目标字符集
        target.getChars(0, target_length, target_chars, 0);
        //遍历目标字符集，将值插入到结果字符集
        int j = 0;
        for(int i = 0; i < target_chars.length; i++){
            //间隔处插入值
            if(i > 0 && i % size == 0){

                result_chars[j] = '`';

                j = j + 1;
            }
            result_chars[j] = target_chars[i];
            j = j + 1;
        }
        String resultStr = new String(result_chars);
        return resultStr.replaceAll("`", insert);
    }
}
