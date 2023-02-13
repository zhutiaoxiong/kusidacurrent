package common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.staticsfunc.static_assistant.UtilFileSave;

import java.io.File;
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//<receiver
//            android:name="common.VersionApkBroadCastReceiver"
//                    android:enabled="true">
//<intent-filter>
//<action android:name="android.intent.action.PACKAGE_ADDED" />
//<action android:name="android.intent.action.PACKAGE_REPLACED" />
//<action android:name="android.intent.action.PACKAGE_REMOVED" />
//
//<data android:scheme="package" />
//</intent-filter>
//</receiver>
public class VersionApkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "update.apk");
            UtilFileSave.RecursionDeleteFile(file);
             if (BuildConfig.DEBUG) Log.e("VersionApkReceiver","监听到系统广播添加");
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "update.apk");
            UtilFileSave.RecursionDeleteFile(file);
             if (BuildConfig.DEBUG) Log.e("VersionApkReceiver","监听到系统广播移除");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "update.apk");
            UtilFileSave.RecursionDeleteFile(file);
             if (BuildConfig.DEBUG) Log.e("VersionApkReceiver","监听到系统广播替换");
        }
    }
}