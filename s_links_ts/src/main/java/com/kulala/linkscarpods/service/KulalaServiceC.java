package com.kulala.linkscarpods.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.blue.BlueLinkReciverServiceCToServiceA;
import com.kulala.linkscarpods.blue.DataCarInfo;
import com.kulala.linkscarpods.blue.OCtrlSocketMsgBackground;
import com.kulala.linkscarpods.interfaces.OnSocketStateListener;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_system.NotificationUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static com.kulala.linkscarpods.blue.BlueStaticValue.*;

/**
 * @author Administrator
 */
public class KulalaServiceC extends Service {
    public static KulalaServiceC kulalaServiceCThis;
    public static long service1HeartTime = 0;
    private ServiceReceiverC myReceiver;
//    public static boolean isInForground=true;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void onCreate() {
         if (BuildConfig.DEBUG) Log.e("<ServiceC>", "<<<<<onCreate>>>>>>");
        LogMeLinks.init(this);
        try {
            if (myReceiver == null) {
                myReceiver = new ServiceReceiverC();
                IntentFilter filter = new IntentFilter();
                filter.addAction(SERVICE_INIT_SOCKET_GET);
                filter.addAction(SERVICE_INIT_NOTIFI_GET);
                filter.addAction(SERVICE_SEND_MESSAGE);
                filter.addAction(SERVICE_1_HEART_BEET);
//                filter.addAction(IS_ACTIVITYMAIN_FORGROUND);
                filter.addAction(IS_NEED_SERVICEA_TO_SERVICE_C);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction( ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(myReceiver, filter,MytoolsGetPackageName.getBroadCastPermision(),null);
//                registerReceiver(myReceiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SocketConnSer.getInstance().init(this);
        SoundPlay.getInstance().init(this);
        if(SocketUtil.initData(KulalaServiceC.this)){
            SocketConnSer.getInstance().reConnect("SERVICE_A_INIT_SOCKET");//初始化成功后，重连接
        }
        BlueLinkReciverServiceCToServiceA.getInstance().initReceiver(this);
//        ScreenListen.getInstance().registerReceiverOnCreate(this);
        super.onCreate();
    }
    public boolean isAppForeground(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName)) {
            return true;
        }

        return false;
    }
//    private void acquireWakeLock() {
//        if (null != wakeLock) return;
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ServiceCWake");
//        if (null != wakeLock) wakeLock.acquire();
//    }
//    //释放设备电源锁
//    private void releaseWakeLock() {
//        if (null == wakeLock) return;
//        wakeLock.release();
//        wakeLock = null;
//    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         if (BuildConfig.DEBUG) Log.e("<ServiceC>", "<<<<<onStartCommand>>>>>>");
        SocketHeartThread.getInstance().startThread();
//        SocketCheckLinkThread.getInstance().startThread();
        needInitNotification();
        needInitSoki();

        if (kulalaServiceCThis == null){
            NotificationUtils notificationUtils = new NotificationUtils(this);
            if (BuildConfig.DEBUG) Log.e("通知", "ServiceC" );
            Notification      noti              = notificationUtils.sendNotification("酷斯达数字车钥匙提醒您:", "酷斯达数字车钥匙提醒您云启动已开启");
            startForeground(NotificationUtils.NOTI_ID,noti);
        }
        kulalaServiceCThis = this;
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        SocketConnSer.getInstance().setOnConnStateChangeListener(null);
        SocketConnSer.getInstance().close();
        kulalaServiceCThis = null;
        LogMeLinks.e("<ServiceC>", "<<<<<onDestroy>>>>>>");
        unregisterReceiver(myReceiver);//不要发stop,会一直重复
        BlueLinkReciverServiceCToServiceA.getInstance().unRegReceiver();
        super.onDestroy();
    }
    // ==============================

    // Broadcast
    private class ServiceReceiverC extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                 if (BuildConfig.DEBUG) Log.e("------------", "亮屏时重连");
//                SocketConnSer.getInstance().reConnect("亮屏进行重连");
//                releaseWakeLock();
            }else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
//                acquireWakeLock();
            }if (SERVICE_INIT_SOCKET_GET.equals(intent.getAction())) {
                 if (BuildConfig.DEBUG) Log.e("ServiceC","收到初始化数据");
                String jsonObj = intent.getStringExtra("jsonObjPHead");
                initSoki(content,SocketUtil.str2JsonObj(jsonObj));
            }else if (SERVICE_INIT_NOTIFI_GET.equals(intent.getAction())) {
                LogMeLinks.e("ServiceC","收到:"+SERVICE_INIT_NOTIFI_GET);
//                int IconId = intent.getIntExtra("IconId",0);
//                String projName = intent.getStringExtra("projName");
                boolean soundOpen = intent.getBooleanExtra("soundOpen",true);
                boolean vitratorOpen = intent.getBooleanExtra("vitratorOpen",true);
                initNotification(content,soundOpen,vitratorOpen);
            }else if (SERVICE_SEND_MESSAGE.equals(intent.getAction())) {
                //sendMessage(2, "")心跳
                int    cmd  = intent.getIntExtra("cmd", 0);
                String json = intent.getStringExtra("json");
                LogMeLinks.e("TsControl","收到UI发包请求 cmd:"+cmd);
                sendMessage(cmd, json);
            }else if (SERVICE_1_HEART_BEET.equals(intent.getAction())) {
                service1HeartTime = System.currentTimeMillis();
            }
//            else if (IS_ACTIVITYMAIN_FORGROUND.equals(intent.getAction())) {
//                boolean    isForgound  = intent.getBooleanExtra("isForground", false);
//                 if (BuildConfig.DEBUG) Log.e("------------", "收到activity的消息"+"isForground"+ isForgound);
//                isInForground=isForgound;
//
//            }
            else if (IS_NEED_SERVICEA_TO_SERVICE_C.equals(intent.getAction())) {
                DataCarInfo dataCarInfo=new DataCarInfo();
                String    blueToothname  = intent.getStringExtra("blueToothname");
                String    carSign  = intent.getStringExtra("carSign");
                int    isBindBluetooth  = intent.getIntExtra("isBindBluetooth",0);
                int    cmd  = intent.getIntExtra("cmd", 0);
                int    time  = intent.getIntExtra("time", 0);
                long    carId  = intent.getLongExtra("carId", 0);
                long    userId  = intent.getLongExtra("userId", 0);
                dataCarInfo.isBindBluetooth=isBindBluetooth;
                dataCarInfo.bluetoothName=blueToothname;
                dataCarInfo.carsig=carSign;
                dataCarInfo.ide=carId;
                if(SocketConnSer.getInstance().isConnected()){
                    OCtrlSocketMsgBackground.getInstance().ccmd_controlCar(dataCarInfo,cmd,time, KulalaServiceC.kulalaServiceCThis,userId);
                }else{
                    SocketConnSer.getInstance().reConnect("后台socket没连接进行重连");
                    try {
                        Thread.sleep(2000L);
                        if(SocketConnSer.getInstance().isConnected()){
                            OCtrlSocketMsgBackground.getInstance().ccmd_controlCar(dataCarInfo,cmd,time, KulalaServiceC.kulalaServiceCThis,userId);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                 if (BuildConfig.DEBUG) Log.e("------------", "收到socket让socket控制广播" );
            }else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                 if (BuildConfig.DEBUG) Log.e("------------", "网络状态变化" );
               //检测到网络切换
                if(kulalaServiceCThis!=null){
                    ConnectivityManager connectivityManager
                            = (ConnectivityManager) kulalaServiceCThis
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    // 获取当前网络状态信息
                    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                    if (info != null && info.isAvailable()) {
//                    SocketConnSer.getInstance().close();
                        SocketConnSer.getInstance().reConnect("切换网络");
                }
                }
            }
        }
    }

    // ==============================
    public void needInitSoki(){
         if (BuildConfig.DEBUG) Log.e("ServiceC","请求初始化数据");
        Intent broadcast = new Intent(SERVICE_INIT_SOCKET_POST);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        sendBroadcast(broadcast);
    }
    //发送连接状态
    public void sendConnectStatus(String status){
        if (BuildConfig.DEBUG) Log.e("ServiceC","请求初始化数据");
        Intent broadcast = new Intent(SERVICE_IS_CONNECT);
        broadcast.putExtra("isConnect",status);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        sendBroadcast(broadcast);
    }
    public void needInitNotification(){
        Intent broadcast = new Intent(SERVICE_INIT_NOTIFI_POST);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        sendBroadcast(broadcast);
    }
    public void needDataBackUI(JsonObject obj){//dataGet
        Intent broadcast = new Intent(SERVICE_RECEIVE_MESSAGE);
        broadcast.putExtra("service_receive_obj", obj.toString());
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        sendBroadcast(broadcast);
         if (BuildConfig.DEBUG) Log.e("ServiceC","" +
                "DataBackUI");
    }
    public void initSoki(final Context context, final JsonObject jsonSockInitData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocket(context);
                boolean isNewChange =SocketUtil.changeData(context,jsonSockInitData);
                if(isNewChange && !TextUtils.isEmpty(SocketUtil.getUserId(context))){
                    SocketConnSer.getInstance().changeUserId();
                }
            }
        }).start();
    }
    public void initNotification(Context globalContext,boolean soundOpen, boolean vitratorOpen){
//        SocketDataGet.IconId1 = IconId;
//        SocketDataGet.projName1 = projName;
        SocketDataGet.isInitedNoti = true;
        SocketDataGet.openSound1 = soundOpen;
        SocketDataGet.openVibrator1 = vitratorOpen;
    }
    //=================================================================
    public void sendMessage(int cmd,String jsonData){
        if(SocketUtil.getSocketPort(this) == 0){
            needInitSoki();
        }else{
            SocketConnSer.getInstance().sendMessage(cmd, jsonData);
        }
    }
    //=================================================================

    private void initSocket(final Context context1){
        SocketConnSer.getInstance().init(context1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异常断线重连
                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread thread, Throwable ex) {
                        Writer      result      = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(result);
                        ex.printStackTrace(printWriter);
                        String stacktrace = result.toString();
                        if(SocketUtil.getSocketPort(context1)!=0)SocketConnSer.getInstance().reConnect("setDefaultUncaughtExceptionHandler"+stacktrace);
                    }
                });
            }
        }).start();//netWork in main thread
        SocketConnSer.getInstance().setOnConnStateChangeListener(new OnSocketStateListener() {
            @Override
            public void onConnFailed(String failedInfo) {
                Log.e("isconnect", "onConnFailed: " );
                sendConnectStatus("0");
            }
            @Override
            public void onSendOK(int cmd) {
                Log.e("isconnect", "onSendOK: " );
                sendConnectStatus("1");
            }
            @Override
            public void onSendFailed(int cmd,String failedInfo) {
                Log.e("isconnect", "onSendFailed: " );
                sendConnectStatus("0");
            }
        });
    }
    //=================================================================
}
