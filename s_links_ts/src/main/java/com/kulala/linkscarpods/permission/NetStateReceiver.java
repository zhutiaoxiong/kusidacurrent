package com.kulala.linkscarpods.permission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.kulala.linkscarpods.blue.KulalaServiceA;
import com.kulala.linkspods.BuildConfig;

public class NetStateReceiver extends BroadcastReceiver {

    //=================================================================
    private static boolean alreadyRegCanDestory = false;
    private static NetStateReceiver _instance;
    public static NetStateReceiver getInstance() {
        if (_instance == null)
            _instance = new NetStateReceiver();
        return _instance;
    }
    private Context mContext;
    private static NetStateReceiver NetStateReceiverThis;
    /**
     * 8.0取消了静态注删，只能动态
     */
    public void initReceiver(Context context) {
        if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            NetStateReceiverThis = new NetStateReceiver();
            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.BOOT_COMPLETED");
                filter.addAction("android.intent.action.USER_PRESENT");
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
                filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
                filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
                filter.addAction("android.intent.action.ANSWER");
                filter.addAction("android.intent.action.CALL");
                mContext = context;
//                context.registerReceiver(NetStateReceiverThis, filter, MytoolsGetPackageName.getBroadCastPermision(),null);
                context.registerReceiver(NetStateReceiverThis, filter);
                 if (BuildConfig.DEBUG) Log.e("NetStateReceiver","reg alreadyRegCanDestory:"+alreadyRegCanDestory);
                alreadyRegCanDestory = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void unRegReceiver(){
        if(alreadyRegCanDestory && NetStateReceiverThis!=null && mContext!=null){
            mContext.unregisterReceiver(NetStateReceiverThis);
            NetStateReceiverThis = null;
            alreadyRegCanDestory = false;
            mContext = null;
             if (BuildConfig.DEBUG) Log.e("NetStateReceiver","unreg alreadyRegCanDestory:"+alreadyRegCanDestory);
        }
    }
    public static void startA(Context content) {
         if (BuildConfig.DEBUG) Log.e("NetStateReceiver", "startA");
        try {
            Intent intentA = new Intent(content, KulalaServiceA.class);
            intentA.setPackage(content.getPackageName());
            content.startService(intentA);
        } catch (Exception e) {
             if (BuildConfig.DEBUG) Log.e("NetStateReceiver", "startA error:" + e.toString());
        }
    }
    @Override
    public void onReceive(Context content, Intent intent) {
        startA(content);
         if (BuildConfig.DEBUG) Log.e("NetStateReceiver", "onReceive: " + intent.getAction());
    }
}
