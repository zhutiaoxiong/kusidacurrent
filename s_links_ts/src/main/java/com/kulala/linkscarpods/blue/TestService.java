package com.kulala.linkscarpods.blue;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_system.NotificationUtils;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.ArrayList;
import java.util.List;

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
         if (BuildConfig.DEBUG) Log.e("查看扫描", "onCreate: " );
        //以前台服务的方式启动，要调用startForeground，否则会出现arn异常
//        Notification notification = new Notification.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_logo))
//                .build();
//        startForeground(110, notification);
        NotificationUtils notificationUtils = new NotificationUtils(this);
        long now=System.currentTimeMillis();
        Notification      noti              = notificationUtils.sendNotification("酷斯达数字车钥匙提醒您:", "已经到蓝牙范围内了"+ ODateTime.time2StringHHmmss(now));
        startForeground(NotificationUtils.NOTI_ID, noti);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null&&intent.getAction() == null) {
            return super.onStartCommand(intent,flags,startId);
        }
        if(intent!=null){
             if (BuildConfig.DEBUG) Log.e("查看扫描", "onStartCommand: " );
            //获取返回的错误码
            int errorCode = intent.getIntExtra(BluetoothLeScanner.EXTRA_ERROR_CODE, -1);//ScanSettings.SCAN_FAILED_*
             if (BuildConfig.DEBUG) Log.e("查看扫描", "errorCode "+ errorCode);
            //获取到的蓝牙设备的回调类型
            int callbackType = intent.getIntExtra(BluetoothLeScanner.EXTRA_CALLBACK_TYPE, -1);//ScanSettings.CALLBACK_TYPE_*
            if (errorCode == -1) {
                //扫描到蓝牙设备信息
                List<ScanResult> scanResults = (List<ScanResult>) intent.getSerializableExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT);
                if (scanResults != null) {
                    List<BluetoothDevice> deviceList=new ArrayList<>();
                    for (ScanResult result : scanResults) {
                        BluetoothDevice device=result.getDevice();
                        deviceList.add(device);
                        //打印所有设备的地址
//                    String address = result.getDevice().getAddress();
//                    if(KulalaServiceA.KulalaServiceAThis==null){
//                        Intent intentA = new Intent(this, KulalaServiceA.class);
//                        intentA.setPackage(this.getPackageName());
//                        this.startService(intentA);
//                        Log.i("haha", "device address " + address);
//                    }

                    }
//                blueDeviceList=deviceList;
                     if (BuildConfig.DEBUG) Log.e("查看扫描", "blueDeviceList "+ deviceList.size() );
                }
            } else {
                //此处为扫描失败的错误处理
                 if (BuildConfig.DEBUG) Log.e("查看扫描", "唤醒的蓝牙扫描失败" );
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
         if (BuildConfig.DEBUG) Log.e("查看扫描", "onDestroy: " );
        super.onDestroy();
    }
}
