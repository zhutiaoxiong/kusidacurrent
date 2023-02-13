package com.kulala.staticsfunc.static_system;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

public class WhiteListCheck {
    public static final int REQUEST_IGNORE_BATTERY_CODE = 1009;

    /**
     * 是否加入了省电白名单，系统白名单我们加不了
     * true : package name is on the device's power whitelist
     */
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        }
        return false;
    }

    /**
     * 拉起请求加入省电白名单
     * //有些手机不给弹窗，5次后就要给进去
     * <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"/>
     */
    public static void gotoSettingIgnoringBatteryOptimizations(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                activity.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_IGNORE_BATTERY_CODE) {
//                Log.d("Hello World!", "开启省电模式成功");
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            if (requestCode == REQUEST_IGNORE_BATTERY_CODE) {
//                Toast.makeText(this, "请用户开启忽略电池优化~", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }
}
