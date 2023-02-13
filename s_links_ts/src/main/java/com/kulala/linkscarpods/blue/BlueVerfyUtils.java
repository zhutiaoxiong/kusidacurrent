package com.kulala.linkscarpods.blue;

import android.util.Log;

import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_system.AES;

public class BlueVerfyUtils {
    public static long appTime;
    /**
     * 取第三位到第10位
     * */
    public static String getTime(){
        long currentTime=System.currentTimeMillis();
        String theTime=String.valueOf(currentTime);
        if(BuildConfig.DEBUG) Log.e("加密环节", "当前时间"+theTime);
        String jiqquTime=theTime.substring(2,10);
        appTime=Long.parseLong(jiqquTime);
        if(BuildConfig.DEBUG) Log.e("加密环节", "截取时间"+jiqquTime);
        String eightTime=theTime.substring(2,10);
//        String zeroTime=eightTime.replace("2","0");
//        String newTime=zeroTime+"00000000";
        return  eightTime;
    }
    /**
     * 根据时间和蓝牙名称加密的字节数组取16位
     * */
    public static byte[] getEncryptedData(String passWord){
      return   AES.AESgenerator(getTime(),passWord);
    }

}
