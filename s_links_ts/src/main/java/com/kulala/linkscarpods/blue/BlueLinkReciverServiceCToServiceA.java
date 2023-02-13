package com.kulala.linkscarpods.blue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkspods.BuildConfig;

/**
 * 从前台发车辆状态到後臺的广播
 */

public class BlueLinkReciverServiceCToServiceA extends BroadcastReceiver {
    private Context mContext;
    private static boolean alreadyRegCanDestory = false;
    //    /**8.0取消了静态注删，只能动态*/
    public static BlueLinkReciverServiceCToServiceA blueLinkReceiverThis;
    private static BlueLinkReciverServiceCToServiceA _instance;

    public static BlueLinkReciverServiceCToServiceA getInstance() {
        if (_instance == null) {
            _instance = new BlueLinkReciverServiceCToServiceA();
        }
        return _instance;
    }

    //=================================================================
    public void initReceiver(Context context) {
        if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            blueLinkReceiverThis = new BlueLinkReciverServiceCToServiceA();
            try {
                IntentFilter filter = new IntentFilter();
                mContext = context;
                context.registerReceiver(blueLinkReceiverThis, filter,MytoolsGetPackageName.getBroadCastPermision(),null);
//                context.registerReceiver(blueLinkReceiverThis, filter);
                alreadyRegCanDestory = true;
                 if (BuildConfig.DEBUG) Log.e("BlueLinkReceiver", "initReceiver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegReceiver() {
        if (alreadyRegCanDestory && blueLinkReceiverThis != null && mContext != null) {
            mContext.unregisterReceiver(blueLinkReceiverThis);
            blueLinkReceiverThis = null;
            alreadyRegCanDestory = false;
            mContext = null;
             if (BuildConfig.DEBUG) Log.e("BlueLinkReceiver", "unRegReceiver");
        }
    }

    public void sendCarstatusServiceCToServiceA(String carStatus ) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.SEND_CAR_STATUS_SERVICEC_TO_SERVICECA);
        broadcast.putExtra("carStatusInfo",carStatus );
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        mContext.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        mContext.sendBroadcast(broadcast);
         if (BuildConfig.DEBUG) Log.e("------------", "从后台发车辆状态给蓝牙的service" );
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
