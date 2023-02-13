package com.kulala.linkscarpods.blue;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
/**
 * Created by Administrator on 2017/7/14.
 */

public class BluePermission {
    //先检查所有蓝牙许可，不通就false,全通就true
    /**
     * 1.成功
     * 2."设备需要android4.4以上手机"
     * 3."没有蓝牙模块"
     * 4."传值错"
     * 9.activity去请求权限
     * 0.蓝牙未开，去打开
     */
    public static int checkPermission(Service context){
        int result = checkPermissions(context);
        if(result == 9)return 0;
        return result;
    }
    public static int checkPermission(Activity context){
        int result = checkPermissions(context);
        if(result == 9) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
                context.requestPermissions(new String[]{
                        android.Manifest.permission.BLUETOOTH
                        , android.Manifest.permission.BLUETOOTH_ADMIN
                        , android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
            }
            return 0;
        }
        return result;
    }
    private static int checkPermissions(Context context){
        if(context == null)return 4;
        //====================step 1 检测是否支持蓝牙4.0========================
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return 2;//api>=19 and4.4 才有4.0蓝牙
        //====================step 2 是否打开蓝牙========================
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) return 3;//没有蓝牙模块
        boolean result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
            int permission1 = context.checkSelfPermission(android.Manifest.permission.BLUETOOTH);
            int permission2 = context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_ADMIN);
            int permission4 = context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int permission5 = context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //蓝牙权限
            if (permission1 != PackageManager.PERMISSION_GRANTED ||
                    permission2 != PackageManager.PERMISSION_GRANTED
                    || permission4 != PackageManager.PERMISSION_GRANTED
                    || permission5 != PackageManager.PERMISSION_GRANTED
                    ) {
                return 9;
            } else {
                result = bluetoothAdapter.isEnabled();
            }
        } else {//<6.0
            result = bluetoothAdapter.isEnabled();
        }
        if(result == false){
            return 0;
        }else {
            return 1;
        }
    }

//    int result = BluePermission.checkPermission(ActivityBlueMain.this);
//    switch (result){
//        case 0:BluePermission.openBlueTooth(ActivityBlueMain.this);
    public static void openBlueTooth(Activity context) {
//        bluetoothAdapter.enable();
        //可以去打开系统蓝牙界面
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 设置蓝牙可见性，最多300秒
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);//[1<->3600] s ，为 0 时，表示打开 Bluetooth 之后一直可见，设置小于0或者大于3600 时，系统会默认为 120s。
        context.startActivity(intent);
    }
}
