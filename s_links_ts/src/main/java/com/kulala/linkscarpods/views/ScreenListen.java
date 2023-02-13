package com.kulala.linkscarpods.views;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ScreenListen {
//    private static boolean screen_off = false;//是否熄屏了
//    private SensorManager sensorManager;
//    private Context       contextT;
//    public static boolean isHaveShakeSensor = false;//是否有震动传感器
//    // ========================out======================
//    private static ScreenListen _instance;
//    private ScreenListen() {}
//    public static ScreenListen getInstance() {
//        if (_instance == null)
//            _instance = new ScreenListen();
//        return _instance;
//    }
//    // ========================out======================
//    public void registerReceiverOnCreate(Context context) {
//        contextT = context;
//        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        //电源键监听
//         if (BuildConfig.DEBUG) Log.e("ScreenListen", "电源键监听OFF");
//        final IntentFilter filterOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        context.registerReceiver(screenOnOffReceiver, filterOff);
//        //电源键监听
//         if (BuildConfig.DEBUG) Log.e("ScreenListen", "电源键监听ON");
//        final IntentFilter filterOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        context.registerReceiver(screenOnOffReceiver, filterOn);
//        List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
//        if (listSensor != null && listSensor.size() > 0) isHaveShakeSensor = true;
//    }
//    public void activityDestoryStopThis() {
//        contextT.unregisterReceiver(screenOnOffReceiver);
//        contextT = null;
//    }
//
//    private final BroadcastReceiver screenOnOffReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            final String action    = intent.getAction();
//            long         now       = System.currentTimeMillis();
//            boolean      isControl = false;
//            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                 if (BuildConfig.DEBUG) Log.e("ScreenListen", "电源键关");
//                screen_off = true;
//                // 当屏幕关闭时，启动一个像素的Activity
//                Intent activity = new Intent(context,OnePxActivity.class);
//                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(activity);
//            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                 if (BuildConfig.DEBUG) Log.e("ScreenListen", "电源键开");
//                screen_off = false;
//                // 用户解锁，关闭Activity
//                // 这里发个广播是什么鬼，其实看下面OnePxAcitivity里面的代码就知道了，发这个广播就是为了finish掉OnePxActivity
//                Intent broadcast = new Intent("FinishOnePPActivity");
//                context.sendBroadcast(broadcast);//发送对应的广播
//            }
//        }
//    };
}
