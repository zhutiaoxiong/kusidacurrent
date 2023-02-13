package com.kulala.linkscarpods.blue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/5/11.
 */

public class DataCarBlueLcd {
    public long carId;
    public String keyBlueName;
    public String keySig;
    public int isKeyBind;
    public int isKeyOpen;

    //for shake
    public int isON =  0;
    public int isTheft =  0;
    public int isLock =  0;
    public long userId;
    public String deviceAddressLcd;
    public static void saveLocal(Context context, DataCarBlueLcd car){
        if(context == null || car == null)return;
        SharedPreferences settings  = context.getSharedPreferences("kusida_share_blue", Activity.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        if (editor == null)return;
        editor.putLong("carId", car.carId);
        editor.putString("keyBlueName", car.keyBlueName);
        editor.putString("keySig", car.keySig);
        editor.putInt("isKeyBind", car.isKeyBind);
        editor.putInt("isKeyOpen", car.isKeyOpen);
        editor.putLong("userId", car.userId);
        editor.putInt("isON", car.isON);
        editor.putInt("isTheft", car.isTheft);
        editor.putInt("isLock", car.isLock);
        editor.putString("deviceAddressLcd", car.deviceAddressLcd);
        editor.commit();
    }
    public static DataCarBlueLcd loadLocal(Context context){
        SharedPreferences settings  = context.getSharedPreferences("kusida_share_blue", Activity.MODE_MULTI_PROCESS);
        if (settings == null)return null;
        DataCarBlueLcd car = new DataCarBlueLcd();
        car.carId = settings.getLong("carId", 0);
        car.keyBlueName = settings.getString("keyBlueName", null);
        car.keySig = settings.getString("keySig", null);
        car.isKeyBind = settings.getInt("isKeyBind", 0);
        car.userId = settings.getLong("userId", 0);
        car.isON = settings.getInt("isKeyBind", 0);
        car.isTheft = settings.getInt("isKeyBind", 0);
        car.isLock = settings.getInt("isKeyBind", 0);
        car.deviceAddressLcd = settings.getString("deviceAddressLcd", null);
        return car;
    }
}
