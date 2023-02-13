package com.kulala.linkscarpods.permission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kulala.linkscarpods.blue.KulalaServiceA;
import com.kulala.linkspods.BuildConfig;

public class BootReceiver extends BroadcastReceiver {

    public static void startA(Context content) {
           if (BuildConfig.DEBUG) Log.e("BootReceiver", "startA");
        try {
            Intent intentA = new Intent(content, KulalaServiceA.class);
            intentA.setPackage(content.getPackageName());
            content.startService(intentA);
        } catch (Exception e) {
               if (BuildConfig.DEBUG) Log.e("BootReceiver", "startA error:" + e.toString());
        }
    }
    @Override
    public void onReceive(Context content, Intent intent) {
        startA(content);
          if (BuildConfig.DEBUG) Log.e("BootReceiver", "onReceive: " + intent.getAction());
    }
}
