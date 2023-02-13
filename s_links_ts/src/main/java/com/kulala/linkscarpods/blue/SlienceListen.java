package com.kulala.linkscarpods.blue;

/**
 * 摇一摇功能,for后台，不可用，会杀前台
 * serviceA,receiver
 * OShake.preShakeTime
 * 已确认，传感器会停掉
 */

public class SlienceListen {
//    private static long lastMoveTime = 0;
//    private SensorManager sensorManager;
//    private PowerManager.WakeLock wakeLock;
//    // ========================out======================
//    private static SlienceListen _instance;
//    private SlienceListen() {}
//    public static SlienceListen getInstance() {
//        if (_instance == null)
//            _instance = new SlienceListen();
//        return _instance;
//    }
//    // ========================out======================
//    public void register(Context context) {
//        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        wakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SlienceListen");// CPU保存运行
//        wakeLock.acquire();
//        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//    }
//    public void unregister() {
//        if(wakeLock!=null)wakeLock.release();
//        if (sensorManager != null) sensorManager.unregisterListener(sensorEventListener);
//    }
//    public boolean isMiniuesMoved(){
//        if(System.currentTimeMillis() - lastMoveTime< 60*1000L)return true;
//        return false;
//    }
//
//    //===================================================================
//
//    /**
//     * 重力感应监听
//     */
//    private float lastX;
//    private float lastY;
//    private float lastZ;
//    private static final int    UPTATE_INTERVAL_TIME = 200;
//    private long countNum = 0;
//    private long lastUpdateTime;
//    private SensorEventListener sensorEventListener = new SensorEventListener() {
//
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            long currentUpdateTime = System.currentTimeMillis();
//            long timeInterval      = currentUpdateTime - lastUpdateTime;
//            if (timeInterval < UPTATE_INTERVAL_TIME) return;
//            lastUpdateTime = currentUpdateTime;
//            float[] values = event.values;
//            float   x      = values[0]; // x轴方向的重力加速度，向右为正
//            float   y      = values[1]; // y轴方向的重力加速度，向前为正
//            float   z      = values[2]; // z轴方向的重力加速度，向上为正
////            Log.i("Shake", "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
//            float deltaX = x - lastX;
//            float deltaY = y - lastY;
//            float deltaZ = z - lastZ;
//            lastX = x;
//            lastY = y;
//            lastZ = z;
//            double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval) * 100;
//            if(speed > 1)lastMoveTime = currentUpdateTime;
//            countNum++;
//            if(countNum%50 == 0)
//            LogMeLinks.e("SlienceListen", "传感器 speed:" + speed);
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//        }
//    };

}

