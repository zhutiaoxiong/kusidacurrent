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

public class BlueLinkReciverForSocket extends BroadcastReceiver {
    private Context mContext;
    private static boolean alreadyRegCanDestory = false;
    //    /**8.0取消了静态注删，只能动态*/
    public static BlueLinkReciverForSocket blueLinkReceiverThis;
    private static BlueLinkReciverForSocket _instance;

    public static BlueLinkReciverForSocket getInstance() {
        if (_instance == null) {
            _instance = new BlueLinkReciverForSocket();
        }
        return _instance;
    }

    //=================================================================
    public void initReceiver(Context context) {
        if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            blueLinkReceiverThis = new BlueLinkReciverForSocket();
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

    public void sendSocket(long carId,String blueToothname,String carSign,int isBindBluetooth,int cmd,int time ,long userId) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.IS_NEED_SERVICEA_TO_SERVICE_C);
        broadcast.putExtra("blueToothname",blueToothname );
        broadcast.putExtra("carSign", carSign);
        broadcast.putExtra("isBindBluetooth", isBindBluetooth);
        broadcast.putExtra("cmd", cmd);
        broadcast.putExtra("time",time );
        broadcast.putExtra("carId",carId );
        broadcast.putExtra("userId",userId );
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        mContext.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        mContext.sendBroadcast(broadcast);
         if (BuildConfig.DEBUG) Log.e("------------", "发给socket让socket控制广播" );
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
