package common;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.client.proj.kusida.BuildConfig;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.LogMe;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.wearkulala.www.wearfunc.WearReg;

import java.lang.ref.WeakReference;

import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.lcdkey.MyOnBlueStateListenerRoll;
import view.view4me.lcdkey.StringToMacUtil;
import view.view4me.myblue.DataReceive;
import view.view4me.myblue.MyLcdBlueAdapter;

import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;

//import android.support.multidex.MultiDex;


public class GlobalContext extends Application implements Thread.UncaughtExceptionHandler {
    private static Context context;
    private static WeakReference<Activity> sCurrentActivityWeakRef;
    public static boolean IS_DEBUG_MODEL = false;


    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static int activityAount = 0;//可见的activity数量
    private static boolean isBackApp = false;
    private static boolean isInBindLcdKeyPage=false;//是否在绑定蓝牙钥匙页面
    private static boolean isInLcdKeyPage=false;//是否在绑定蓝牙钥匙页面
    public static boolean isUserExitUser = false;



    @Override
//    public void attachBaseContextByDaemon(Context base) {
//        super.attachBaseContextByDaemon(base);
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
    /**
     * 一取过值，立即重置
     */
    public static boolean getIsInBindLcdKey() {
        return isInBindLcdKeyPage;
    }
    public static void setIsInBindLcdKey(boolean isIn) {
        isInBindLcdKeyPage=isIn;
    }
    public static void setIsInLcdKeyPage(boolean isIn) {
        isInLcdKeyPage=isIn;
    }

    /**
     * 一取过值，立即重置
     */
    public static boolean getIsBackApp() {
        boolean result = isBackApp;
        isBackApp = false;
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        WearReg.getInstance().setGlobalContextListener(new WearReg.GlobalContextListener() {
            @Override
            public Activity getCurrentActivity() {
                return GlobalContext.this.getCurrentActivity();
            }
        });
        ApplicationInfo info = context.getApplicationInfo();
        IS_DEBUG_MODEL = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        Logger.addLogAdapter(new AndroidLogAdapter());
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
        SDKInitializer.setAgreePrivacy(this,true);
        SDKInitializer.initialize(this);//百度地图初始化
        LogMe.init(getApplicationContext());
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        BuglyInitSet.initBuglyUpdate();
        Bugly.init(getApplicationContext(), "9cc32a1493", false);
//        CrashReport.initCrashReport(this, "9cc32a1493",false );
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                activityAount--;
            }

            @Override
            public void onActivityStarted(final Activity activity) {
                if (activityAount == 0) {
                    isBackApp = true;
                    //app回到前台 自動連接藍牙鑰匙
//                    Timer timer=new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            //app在綁定藍牙頁面不自動重連，被逼退的情況下不自動重連
////                             if (BuildConfig.DEBUG) Log.e("GlobalContext", "isIsUserExitUser(): =="+isUserExitUser+"isInBindLcdKeyPage"+isInBindLcdKeyPage+"isInLcdKeyPage"+isInLcdKeyPage);
//                            if (!isInLcdKeyPage&&!isInBindLcdKeyPage&&!isUserExitUser) {
//                                autoLinkLCDKeyBlue(activity);
//                            }
//                        }
//                    }, 1000, 3000);
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            //app在綁定藍牙頁面不自動重連，被逼退的情況下不自動重連
////                             if (BuildConfig.DEBUG) Log.e("GlobalContext", "isIsUserExitUser(): =="+isUserExitUser+"isInBindLcdKeyPage"+isInBindLcdKeyPage+"isInLcdKeyPage"+isInLcdKeyPage);
//                            if (!isInLcdKeyPage&&!isInBindLcdKeyPage&&!isUserExitUser) {
//                                autoLinkLCDKeyBlue(activity);
//                            }
//                        }
//                    }, 100, 1500);
                }
                activityAount++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (sCurrentActivityWeakRef != null) sCurrentActivityWeakRef.clear();
                sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
                System.out.println("<<<<当前启动activity>>> : " + activity.getClass().getName());
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 进主页自动连接蓝牙钥匙
     */

    private void autoLinkLCDKeyBlue( Activity activity) {
         DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return;
         int isBind=carInfo.isKeyBind;
         int isKeyOpen=carInfo.isKeyOpen;
         String blueAderess=carInfo.keyBlueName;
        String keySig=carInfo.keySig;
         if (BuildConfig.DEBUG) Log.e("GlobalContext", "蓝牙名称"+blueAderess+"  isBind"+isBind+"  isKeyOpen"+isKeyOpen+"  验证串keySig"+keySig+"carInfo.ide"+keySig+carInfo.ide);
            if (!TextUtils.isEmpty(blueAderess)&&isKeyOpen==1&&isBind==1&&!TextUtils.isEmpty(keySig)) {
                int isPermision = BluePermission.checkPermission(activity);
                if (isPermision != 1) {
                    return;
                }
                MyLcdBlueAdapter.getInstance().initializeOK(GlobalContext.getContext());
                MyLcdBlueAdapter.getInstance().setOnBlueStateListener(new MyOnBlueStateListenerRoll(new MyOnBlueStateListenerRoll.OnonDescriptorWriteLister() {
                    @Override
                    public void onDescriptorWrite() {
//                    DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
                        //蓝牙验证串
                        DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
                        String keySig=carInfo.keySig;
                        if(!TextUtils.isEmpty(keySig)){
                            byte[] bytesig = keySig.getBytes();
                            byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytesig);
                            String datasend=bytesToHexString(mess);
                            MyLcdBlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
                             if (BuildConfig.DEBUG) Log.e("GlobalContext", "发绑定数据"+datasend);
                        }
//                        MyLcdBlueAdapter.getInstance().sendMessage("01 02 01 03 F8");
                    }

                    @Override
                    public void onConnectedFailed() {
                    }
                }));
                String reealMacAderess= StringToMacUtil.collapseString(blueAderess, 2, ":");
                if(BluetoothAdapter.checkBluetoothAddress(reealMacAderess)){
                    MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(reealMacAderess);
                }
            }
//            else{
//                //   //綁定藍牙  藍牙地址不對直接斷掉
//                if (MyLcdBlueAdapter.getInstance().getIsConnectted()) {
//                    MyLcdBlueAdapter.getInstance().closeBlueReal();
//                }
//            }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null || context == null)
            return false;
        final String crashReport = getCrashReport(context, ex);
        LogMe.e("handleException", "全局异常" + crashReport);
        return true;
    }

    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + pinfo.versionName + "("
                + pinfo.versionCode + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    public static Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    /**
     * @return the context
     */
    public static Context getContext() {
        return context;
    }

//    @Override
//    public void callback(String key, Object value) {
//        if (SocketConnSer.SOCKET_RECEIVE_MESSAGE.equals(key)) {
//            final JsonObject obj = (JsonObject) value;
//            if (obj == null) return;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int cmd = OJsonGet.getInteger(obj, "cmd");
//                    if (cmd == 101) return;// 第一个消息还未初始化
//                    // 判断是否提示消息
//                    int mType = OJsonGet.getInteger(obj, "mType");
//                    if (cmd == 3 && mType == 4) {// 消息推送
//                        int isNotice = OJsonGet.getInteger(obj, "isNotice");
//                        if (isNotice == 1) {
//                            JsonObject data       = OJsonGet.getJsonObject(obj, "data");
//                            int        alertType  = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
//                            String     content    = OJsonGet.getString(data, "content");
//                            long       createTime = OJsonGet.getLong(data, "createTime");
//                            Intent     broadcastB = new Intent();
//                            broadcastB.setAction("SERVICE_B_NEED_SENDMESSAGE");
//                            broadcastB.putExtra("title", "酷斯达数字车钥匙消息提醒:");
//                            broadcastB.putExtra("alertType", alertType);
//                            broadcastB.putExtra("info", content + "  " + ODateTime.time2StringHHmm(createTime));
//                            sendBroadcast(broadcastB);
//                            try {
//                                Thread.sleep(400L);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    // 返回数据给UI
//                    Intent broadcast = new Intent();
//                    broadcast.setAction("android.intent.action.SERVICE_A_BACKMESSAGE");
//                    broadcast.putExtra("SERVICE_A_BACKMESSAGE_OBJ", obj.toString());
//                    sendBroadcast(broadcast);
//                }
//            }).start();
//        }
//    }
    /*public static boolean isInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                //BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;//后台
                } else {//前台
                    return false;
                }
            }
        }
        return true;
    }*/
    //==============================
}
