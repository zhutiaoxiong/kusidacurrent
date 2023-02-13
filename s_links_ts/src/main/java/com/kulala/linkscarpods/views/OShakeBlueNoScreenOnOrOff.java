package com.kulala.linkscarpods.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.kulala.linkscarpods.blue.BlueLinkControl;
import com.kulala.linkscarpods.blue.BlueStaticValue;
import com.kulala.linkscarpods.blue.DataCarBlue;
import com.kulala.linkspods.BuildConfig;

import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * 摇一摇功能,for后台，不可用，会杀前台
 * serviceA,receiver
 * OShake.preShakeTime
 */

public class OShakeBlueNoScreenOnOrOff {
    private SensorManager sensorManager;
    private static boolean isScreenOff = false;
    private Context       contextT;
    public static boolean isHaveShakeSensor = false;//是否有震动传感器
    public static boolean getIsScreenOff(){return isScreenOff;}
    // ========================out======================
    private static OShakeBlueNoScreenOnOrOff _instance;
    private OShakeBlueNoScreenOnOrOff() {}
    public static OShakeBlueNoScreenOnOrOff getInstance() {
        if (_instance == null)
            _instance = new OShakeBlueNoScreenOnOrOff();
        return _instance;
    }
    // ========================out======================
    public void registerReceiverOnCreate(Context context) {
//         if (BuildConfig.DEBUG) Log.e("OShakeBlue", "registerReceiverOnCreate");
        if(contextT!=null)return;
        contextT = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        //电源键监听
////         if (BuildConfig.DEBUG) Log.e("OShakeBlue", "电源键监听OFF");
//        final IntentFilter filterOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        context.registerReceiver(mBatInfoReceiver, filterOff);
//        //电源键监听
////         if (BuildConfig.DEBUG) Log.e("OShakeBlue", "电源键监听ON");
//        final IntentFilter filterOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        context.registerReceiver(mBatInfoReceiver, filterOn);
        List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (listSensor != null && listSensor.size() > 0) {
            Log.e("OShakeBlue", "開啓搖一搖");
            isHaveShakeSensor = true;
            openShake(context);
        } else  if (BuildConfig.DEBUG) Log.e("OShakeBlue", "没有重力感应器");
    }
    public void activityDestoryStopThis() {
//        contextT.unregisterReceiver(mBatInfoReceiver);
        contextT = null;
        closeShake();
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action    = intent.getAction();
            long         now       = System.currentTimeMillis();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                closeShake();
                openShake(context);
////                testNum++;
//                isScreenOff = true;
//                if (BuildConfig.DEBUG) Log.e("ScreenTest", "Screen Off:");
//                setWifiNeverSleep(context);
//                // 当屏幕关闭时，启动一个像素的Activity
////                Intent activity = new Intent(context,OnePxActivity.class);
////                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                context.startActivity(activity);
//                if(now - prePowerTime<CAN_SHAKE_TIME){
//                    prePowerTime = 0;
//                    openShake(context);
//                }else{
//                    prePowerTime = now;
//                }

            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                testNum++;
                isScreenOff = false;
                if (BuildConfig.DEBUG) Log.e("ScreenTest", "Screen On:");
                // 用户解锁，关闭Activity
                // 这里发个广播是什么鬼，其实看下面OnePxAcitivity里面的代码就知道了，发这个广播就是为了finish掉OnePxActivity
//                Intent broadcast = new Intent("FinishOnePPActivity");
//                context.sendBroadcast(broadcast);//发送对应的广播
                if(now - prePowerTime<CAN_SHAKE_TIME){
                    prePowerTime = 0;
                    openShake(context);
                }else{
                    prePowerTime = now;
                }
            }
//            if(now  - testTime>5000L)testNum = 0;
//            if(testNum>=3){
//                LogMeLinks.e("ScreenTest", "Open Blue Link");
//                BlueLinkControl.getInstance().activeTest();
//                testNum = 0;
//            }
//            testTime = now;
        }
    };
    //===================================================================
    private       long              prePowerTime     = 0;
    private       boolean           shakeOpened      = false;
    public static long              preShakeTime     = 0;
    private static long              CAN_SHAKE_TIME     = 4500L;
    // 停止监听器
    private void closeShake() {
        shakeOpened = false;
        if (sensorManager != null) sensorManager.unregisterListener(sensorEventListener);
    }
    // 注册监听器
    private void openShake(Context context) {
        if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "开启摇一摇" );
        DataCarBlue carInfo = BlueLinkControl.getInstance().getDataCar();
//        if (carInfo == null || carInfo.deviceName == null || carInfo.deviceName.length() == 0 || !carInfo.isShakeOpen) {
//            if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "此时主机设备为空，或者摇一摇未开启" );
//            return;
//        }
//        else if (!BlueLinkControl.getInstance().getIsBlueConnOK())return;//无连接

        long now = System.currentTimeMillis();
        if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "判断短时间摇晃" );
        if (now - preShakeTime < CAN_SHAKE_TIME)return;//短时间二次
        if (shakeOpened)return;//已开启
        if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "--------openShake--------成功");
        if (sensorManager == null)sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        shakeOpened = true;
        preShakeTime = now;
        boolean regOK = sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "--------regOK"+"-----------"+regOK);
//        new TimeDelayTask().runTask(3500L, new TimeDelayTask.OnTimeEndListener() {
//            @Override
//            public void onTimeEnd() {
//                closeShake();
//            }
//        });
    }

    /**
     * 重力感应监听
     */
    private float lastX;
    private float lastY;
    private float lastZ;
    private static final int    UPTATE_INTERVAL_TIME = 200;
    private static  double SPEED_SHRESHOLD      = 7;//这个值调节灵敏度
    private              int    SPEED_OK_COUNT       = 0;//要连摇三次，才算成功
    private long lastUpdateTime;
    public static long controlSuccessTime;
    private double allCount;
    private double pinjunzhi;
    private int i;
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
//            if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "onSensorChanged" );
            long currentUpdateTime = System.currentTimeMillis();
            long timeInterval      = currentUpdateTime - lastUpdateTime;
//             if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "--sensorEventListener--timeInterval:" + timeInterval + " UPTATE_INTERVAL_TIME:" + UPTATE_INTERVAL_TIME);
            if (timeInterval < UPTATE_INTERVAL_TIME) return;
            lastUpdateTime = currentUpdateTime;
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float   x      = values[0]; // x轴方向的重力加速度，向右为正
            float   y      = values[1]; // y轴方向的重力加速度，向前为正
            float   z      = values[2]; // z轴方向的重力加速度，向上为正
//            Log.i("Shake", "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / UPTATE_INTERVAL_TIME) * 100;
            if(speed>1){
                i++;
                allCount=allCount+speed;
                pinjunzhi=allCount/i;
                if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "speed:" + speed + " SPEED_SHRESHOLD:" + SPEED_SHRESHOLD);
                if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "次数" + i + "平均值" + pinjunzhi);
            }
            DataCarBlue carInfo = BlueLinkControl.getInstance().getDataCar();
            if(TextUtils.isEmpty(carInfo.shakeLevel)||carInfo.shakeLevel.equals("中")){
                SPEED_SHRESHOLD=10;
            }else if(carInfo.shakeLevel.equals("轻")){
                SPEED_SHRESHOLD=6;
            }else if(carInfo.shakeLevel.equals("重")){
                SPEED_SHRESHOLD=16;
            }else if(carInfo.shakeLevel.equals("最重")){
                SPEED_SHRESHOLD=20;
            }
             if (speed >= SPEED_SHRESHOLD) {
                SPEED_OK_COUNT++;
            } else {
                SPEED_OK_COUNT = 0;
            }
//            Log.i(">>>OShakeBlue>>>", "speed 1:");
            if (SPEED_OK_COUNT >= 3) {
//                Log.i(">>>OShakeBlue>>>", "speed 2:");
                //判断是否允许摇
//                DataCarBlue carInfo = BlueLinkControl.getInstance().getDataCar();
                if (carInfo == null || !carInfo.isShakeOpen) return;
//                Log.i(">>>OShakeBlue>>>", "speed 4:");
                if (carInfo.isON == 1 && carInfo.isTheft == 0) return;//启动非防盗不起作用
//                Log.i(">>>OShakeBlue>>>", "speed 5:");
                //成功摇一次就先停止
//                closeShake();
                long thetime=System.currentTimeMillis();
                if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "BlueLinkControl.getInstance().getIsBlueConnOK()"+BlueLinkControl.getInstance().getIsBlueConnOK() );
                if (BlueLinkControl.getInstance().getIsBlueConnOK()) {
//                    Log.i(">>>OShakeBlue>>>", "speed 6:");
                    if((thetime-controlSuccessTime)>1000){
                        if (carInfo.isLock == 0) {//铁秋要求isLock改为isTheft,撤防0 执行设防，isTheft 1,2,3其它的都算设防
                            if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "检测到摇晃，执行操作______________上锁 isLock:" + carInfo.isLock);
                            controlSuccessTime=thetime;
                            //
                        controlCar(3);
                        } else {
                            controlSuccessTime=thetime;
                            if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "检测到摇晃，执行操作______________解锁 isLock:" + carInfo.isLock);
                        controlCar(4);//lock3 unlock 4
                        }
                    }else{
                        if (BuildConfig.DEBUG) Log.e(">>>OShakeBlue>>>", "检测到摇晃，执行操作控制時間沒有1.5秒" );
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private void controlCar(int controlCmd) {
        BlueLinkControl.preControlCmd = controlCmd;
        BlueLinkControl.getInstance().sendMessage(BlueStaticValue.getControlCmdByID(controlCmd), false);//发送指令
//        ViewControlPanelControl.preControlCmd = controlCmd;
//        OCtrlCar.getInstance().ccmd1233_controlCar(ManagerCarList.getInstance().getCurrentCar(), controlCmd, 0);
    }

    //修改Wifi休眠策略值
    public static void setWifiNeverSleep(Context context) {
        int wifiSleepPolicy = 0;
        wifiSleepPolicy = Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
//         if (BuildConfig.DEBUG) Log.e("WIFI", "---> 修改前的Wifi休眠策略值 WIFI_SLEEP_POLICY=" + wifiSleepPolicy);

        Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_NEVER);

        wifiSleepPolicy = Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.Global.WIFI_SLEEP_POLICY,
                Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
//         if (BuildConfig.DEBUG) Log.e("WIFI", "---> 修改后的Wifi休眠策略值 WIFI_SLEEP_POLICY=" + wifiSleepPolicy);
    }


}

