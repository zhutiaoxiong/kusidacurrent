package com.kulala.staticsfunc.static_system;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.io.File;
import java.util.UUID;

public class SystemMe {
    /**
     * 锟斤拷取锟斤拷前锟斤拷锟斤拷陌姹撅拷锟�
     **/
    public static  String UTF8        = "utf-8";
    private static String UUID_USE    = "";
    private static String versionName = "";

    public static String getVersionNum(Context context) {
        if (!versionName.equals("")) return versionName;
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()锟斤拷锟姐当前锟斤拷陌锟斤拷锟斤拷锟�0锟斤拷锟斤拷锟角伙拷取锟芥本锟斤拷息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    public static String getUUID(Context context) {
        if (UUID_USE != null && UUID_USE.length() > 0) return UUID_USE;
//        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//        BluetoothAdapter bluetoothAdapter;
//        if (Build.VERSION.SDK_INT >= 19) {
//            bluetoothAdapter = bluetoothManager.getAdapter();
//        } else {
//            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        }
//        String address = bluetoothAdapter.getAddress();
//        if (address != null && address.length() > 0) {
//            UUID_USE = address;
//             if (BuildConfig.DEBUG) Log.e("getUUID", "UI 生成了一个新的UUID:" + UUID_USE);
//        } else {
            String data = ODBHelper.getInstance(context).queryCommonInfo("UUID");
            if (data == null || data.length() == 0) {
                UUID uuid = UUID.randomUUID();
                UUID_USE = uuid.toString();//6b003d48-1dc2-41ca-8f0c-4ff5104531e4
                 if (BuildConfig.DEBUG) Log.e("getUUID", "UI 生成了一个新的UUID:" + UUID_USE);//9d50c7e9-0ffd-4b34-9a9e-47e01b18fddc
                ODBHelper.getInstance(context).changeCommonInfo("UUID", UUID_USE);
            } else {
                UUID_USE = data;
            }
//        }
        return UUID_USE;
    }

//    /**
//     * 会有重复
//     **/
//    private static String IMEINum = "";
//
//    public static String getIMEInum(Context context) {
//        if (!IMEINum.equals("")) return IMEINum;
//        String num = "";
//        if ((num == null) || (num.equals(""))) {
//            String ok = "" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length()
//                    % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
//                    + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
//            IMEINum = ok;
//        }
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        num = tm.getDeviceId();
//        if ((num != null) && !(num.equals(""))) {
//            IMEINum = num;
//        }
//        return IMEINum;
//    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String      version  = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int         version  = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 锟斤拷锟斤拷欠锟街э拷锟絚amera硬锟斤拷
     **/
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            return true;
        return false;
    }

    /**
     * 判断网络连接
     **/
    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) return false;
        return true;
    }

    //=====================================================
    //    <!-- 锟侥硷拷锟斤拷锟斤拷权锟斤拷 -->
    //    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    //    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    //    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    //    <uses-permission android:name="android.permission.WRITE_USER_DICTIONARY" />
    public static void getFileDirectory(Context context) {
        String state = Environment.getExternalStorageState();//锟角凤拷锟斤拷锟斤拷獠匡拷娲�
        if (state.equals(Environment.MEDIA_MOUNTED)) {
        }//只锟叫达拷时锟斤拷锟角匡拷锟矫碉拷
        File dir = Environment.getExternalStorageDirectory();//私锟叫碉拷
        dir = context.getExternalCacheDir();//锟斤拷锟斤拷锟�
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//锟斤拷锟叫碉拷 shared
        dir = Environment.getDataDirectory();///data
        dir = context.getExternalFilesDir(null);///mnt/sdcard/Android/data/com.example.client/files 锟斤拷锟斤拷锟斤拷sdcard锟较凤拷锟斤拷拇娲⒙凤拷锟斤拷锟斤拷锟剿碉拷锟斤拷锟斤拷锟斤拷锟叫讹拷爻锟斤拷锟绞蹦柯家诧拷岜簧撅拷锟�;
        //锟节诧拷锟斤拷
        dir = context.getFilesDir();///data/data/com.example.client/files锟斤拷锟斤拷锟絛ata目录锟斤拷files锟斤拷目录锟斤拷锟斤拷锟斤拷锟叫★拷募锟斤拷锟絪dcard锟街诧拷锟斤拷锟斤拷时锟斤拷锟斤拷选锟斤拷锟斤拷锟斤拷铩�
        dir = context.getCacheDir();///data/data/com.example.client/cache
        File file1 = new File(dir + File.separator + "IMG_123465.jpg");
        //    	Environment.getDataDirectory() = /data
        //		Environment.getDownloadCacheDirectory() = /cache
        //		Environment.getExternalStorageDirectory() = /mnt/sdcard
        //		Environment.getExternalStoragePublicDirectory(锟斤拷test锟斤拷) = /mnt/sdcard/test
        //		Environment.getRootDirectory() = /system
        //		getPackageCodePath() = /data/app/com.my.app-1.apk
        //		getPackageResourcePath() = /data/app/com.my.app-1.apk
        //		getCacheDir() = /data/data/com.my.app/cache
        //		getDatabasePath(锟斤拷test锟斤拷) = /data/data/com.my.app/databases/test
        //		getDir(锟斤拷test锟斤拷, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
        //		getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
        //		getExternalFilesDir(锟斤拷test锟斤拷) = /mnt/sdcard/Android/data/com.my.app/files/test
        //		getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
        //		getFilesDir() = /data/data/com.my.app/files
    }

    public static String getFileSaveDir() {
        String state = Environment.getExternalStorageState();//锟角凤拷锟斤拷锟斤拷獠匡拷娲�
        File   dir;
        dir = Environment.getExternalStorageDirectory();
        if (state.equals(Environment.MEDIA_MOUNTED)) {//只锟叫达拷时锟斤拷锟角匡拷锟矫碉拷
            dir = Environment.getExternalStorageDirectory();//私锟叫碉拷
        } else {
            //    		dir = ActivityKulala.getFilesDir();
        }
        return dir.getAbsolutePath();
    }

    public static String getImageSaveDir() {
        String state = Environment.getExternalStorageState();//锟角凤拷锟斤拷锟斤拷獠匡拷娲�
        File   dir;
        if (state.equals(Environment.MEDIA_MOUNTED)) {//只锟叫达拷时锟斤拷锟角匡拷锟矫碉拷
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            dir = Environment.getDataDirectory();
        }
        return dir.getAbsolutePath();
    }
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public static boolean isWifiOn(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
            return true;
        return false;
    }
    //修改Wifi休眠策略值
    public static void setWifiNeverSleep(Context context){
        int wifiSleepPolicy=0;
        wifiSleepPolicy= Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
//         if (BuildConfig.DEBUG) Log.e("WIFI","---> 修改前的Wifi休眠策略值 WIFI_SLEEP_POLICY="+wifiSleepPolicy);


        Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_NEVER);


        wifiSleepPolicy=Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
//         if (BuildConfig.DEBUG) Log.e("WIFI","---> 修改后的Wifi休眠策略值 WIFI_SLEEP_POLICY="+wifiSleepPolicy);
    }
}
