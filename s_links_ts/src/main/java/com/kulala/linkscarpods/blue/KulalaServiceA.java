package com.kulala.linkscarpods.blue;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll;
import com.kulala.linkscarpods.service.SoundPlay;
import com.kulala.linkscarpods.views.OShakeBlueNoScreenOnOrOff;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.AES;
import com.kulala.staticsfunc.static_system.NotificationUtils;
import com.kulala.staticsfunc.time.TimeDelayTask;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR_DATA;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_CHANGE_LCDKEY;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_CLEAR_BLUETOOTH;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_GET_CARINFO;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_SENDMESSAGE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_SENDMESSAGE_LCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_STOPCONN_CLEARDATA;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_STOPCONN_CLEARDATA_LCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUE_STATE_CHANGE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONCONNECTEDFAILED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONCONNECTEDFAILEDLCDKEY;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDATARECEIVED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDISCOVEROK;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDISCOVEROKLCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONMESSAGESENDED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SEND_CAR_STATUS_BLUETOOTH_PROGRESS;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SEND_CAR_STATUS_SERVICEC_TO_SERVICECA;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_2_HEART_BEET;
import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;
import static com.kulala.linkscarpods.blue.ConvertHexByte.hexStringToBytes;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CLOSEED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_OK;


public class KulalaServiceA extends Service {
    public static KulalaServiceA KulalaServiceAThis;
    public static long service2HeartTime = 0;
    //    public static boolean isInForground;
    private ServiceReceiverA myReceiver;
    PowerManager.WakeLock wakeLock = null;
    public static long userId;

    //=============================================
    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    public class MyBinder extends Binder {
        public KulalaServiceA getService() {
            return KulalaServiceA.this;
        }
    }

    public boolean getBlueConnected() {
        return BlueLinkControl.getInstance().getIsBlueConnOK();
    }

    private MyBinder binder = new MyBinder();

    //=============================================
    public void onCreate() {
        if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<onCreate>>>>>>");
        LogMeLinks.init(this);
        try {
            if (myReceiver == null) {
                myReceiver = new ServiceReceiverA();
                IntentFilter filter = new IntentFilter();
                filter.addAction(BLUETOOTH_NEED_STOPCONN_CLEARDATA);
                filter.addAction(BLUETOOTH_NEED_SENDMESSAGE);
                filter.addAction(BLUETOOTH_NEED_CHANGE_CAR);
                filter.addAction(BLUETOOTH_NEED_CHANGE_CAR_DATA);
//                filter.addAction(SERVICE_1_SOUND_CONTROL);
                filter.addAction(SERVICE_2_HEART_BEET);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction(BLUETOOTH_NEED_CHANGE_LCDKEY);
                filter.addAction(BLUETOOTH_NEED_STOPCONN_CLEARDATA_LCD);
                filter.addAction(BLUETOOTH_NEED_SENDMESSAGE_LCD);
//                filter.addAction(IS_ACTIVITYMAIN_FORGROUND);
                filter.addAction(SEND_CAR_STATUS_BLUETOOTH_PROGRESS);
                filter.addAction(SEND_CAR_STATUS_SERVICEC_TO_SERVICECA);
                filter.addAction(BLUE_STATE_CHANGE);
                filter.addAction(BLUETOOTH_NEED_CLEAR_BLUETOOTH);

                registerReceiver(myReceiver, filter,MytoolsGetPackageName.getBroadCastPermision(),null);
//                registerReceiver(myReceiver, filter);
            }
            SoundPlay.getInstance().init(this);
            BlueLinkReciverForSocket.getInstance().initReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (null != wakeLock) return;
        if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<acquireWakeLock>>>>>>");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ServiceAWake");
        if (null != wakeLock) wakeLock.acquire();
    }

    //    //?????????????????????
    private void releaseWakeLock() {
        if (null == wakeLock) return;
        if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<releaseWakeLock>>>>>>");
        wakeLock.release();
        wakeLock = null;
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

    //    private long preStartTime = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        long now = System.currentTimeMillis();
//        if (now - preStartTime < 2000L) return START_STICKY;
//        preStartTime = now;
//         if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<onStartCommand>>>>>>");
        LogMeLinks.e("HeartA", "ServiceA <<<<<onStartCommand>>>>>>");
        if (BuildConfig.DEBUG) Log.e("<ServiceA>", "ServiceA <<<<<onStartCommand>>>>>>");
//        OShakeBlue.getInstance().registerReceiverOnCreate(this);
//        OShakeBlueThreeTimes.getInstance().registerReceiverOnCreate(this);
        ServiceAHeartThread.getInstance().startThread();
        if (KulalaServiceAThis == null) {
            NotificationUtils notificationUtils = new NotificationUtils(this);
            Notification noti = notificationUtils.sendNotification("?????????????????????????????????:", "???????????????????????????????????????????????????");
            startForeground(NotificationUtils.NOTI_ID, noti);
        }
        KulalaServiceAThis = this;
        boolean isInited = BlueLinkControl.getInstance().init(this);
        boolean isInitedB = BlueLinkControlLcdKey.getInstance().init(this);
        if (!isInited) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent broadcast = new Intent(BLUETOOTH_NEED_GET_CARINFO);
                    if (BuildConfig.DEBUG) Log.e("?????????", "run: ");
                    broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
                    sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//                    sendBroadcast(broadcast);
                }
            }).start();
        }
//        if (!isInitedB) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(500L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Intent broadcast = new Intent(BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY);
//                    broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
//                    sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
////                    sendBroadcast(broadcast);
//                }
//            }).start();
//        }
        OShakeBlueNoScreenOnOrOff.getInstance().registerReceiverOnCreate(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        releaseWakeLock();
        LogMeLinks.e("<ServiceA>", "<<<<<onDestroy>>>>>>8.0????????????");
        unregisterReceiver(myReceiver);//?????????stop,???????????????
        BlueLinkReciverForSocket.getInstance().unRegReceiver();
//        OShakeBlue.getInstance().activityDestoryStopThis();
//        OShakeBlueThreeTimes.getInstance().activityDestoryStopThis();
        OShakeBlueNoScreenOnOrOff.getInstance().activityDestoryStopThis();
        ServiceAHeartThread.getInstance().stopThread();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        LogMeLinks.e("<ServiceA>", "<<<<<onLowMemory>>>>>>");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        LogMeLinks.e("<ServiceA>", "<<<<<onTrimMemory>>>>>>" + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LogMeLinks.e("<ServiceA>", "<<<<<onTaskRemoved>>>>>>");
        super.onTaskRemoved(rootIntent);
    }

    // ==============================
    // Broadcast
    private class ServiceReceiverA extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                releaseWakeLock();
//                ServiceAHeartThread.getInstance().stopPlayMusic();
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
//                acquireWakeLock();
//                ServiceAHeartThread.getInstance().waitPlayMusic();
            } else if (BLUETOOTH_NEED_STOPCONN_CLEARDATA.equals(intent.getAction())) {
                LogMeLinks.e("ServiceA", "A??????:" + BLUETOOTH_NEED_STOPCONN_CLEARDATA);
                String info = intent.getStringExtra("info");
                BlueLinkControl.getInstance().closeBlueConnReal(info);
            } else if (BLUETOOTH_NEED_SENDMESSAGE.equals(intent.getAction())) {
                Log.e("pppppppp", "A??????:" + BLUETOOTH_NEED_SENDMESSAGE);
                String hexStr = intent.getStringExtra("hexStr");
                boolean needMessageSendedInfo = intent.getBooleanExtra("needMessageSendedInfo", false);
                BlueLinkControl.getInstance().sendMessage(hexStr, needMessageSendedInfo);
            } else if (BLUETOOTH_NEED_CHANGE_CAR.equals(intent.getAction())) {
                Log.e("????????????", "????????????1");
                LogMeLinks.e("ServiceA", "A??????:" + BLUETOOTH_NEED_CHANGE_CAR);
                long carId = intent.getLongExtra("carId", 0);
                String deviceName = intent.getStringExtra("deviceName");
                String carSign = intent.getStringExtra("carSign");
                boolean isUseBlueModel = intent.getBooleanExtra("isUseBlueModel", false);
                boolean isShakeOpen = intent.getBooleanExtra("isShakeOpen", false);
                boolean vibratorOpen = intent.getBooleanExtra("vibratorOpen", false);
                int isBindBluetooth = intent.getIntExtra("isBindBluetooth", 0);
                String carsig = intent.getStringExtra("carsig");
                int isMyCar = intent.getIntExtra("isMyCar",0);
                String shakeLevel = intent.getStringExtra("shakeLevel");
                BlueLinkControl.getInstance().changeCar(carId, deviceName, carSign, isUseBlueModel, isShakeOpen, vibratorOpen, isBindBluetooth, carsig,isMyCar,shakeLevel);
            } else if (BLUETOOTH_NEED_CHANGE_LCDKEY.equals(intent.getAction())) {
                LogMeLinks.e("ServiceA", "A??????:" + BLUETOOTH_NEED_CHANGE_LCDKEY);
                long carId = intent.getLongExtra("carId", 0);
                String keyBlueName = intent.getStringExtra("keyBlueName");
                String keySig = intent.getStringExtra("keySig");
                int isKeyBind = intent.getIntExtra("isKeyBind", 0);
                int isKeyOpen = intent.getIntExtra("isKeyOpen", 0);
                long userId = intent.getLongExtra("userId", 0);
                BlueLinkControlLcdKey.getInstance().changeLcdKey(carId, keyBlueName, keySig, isKeyBind, isKeyOpen, userId);
            } else if (BLUETOOTH_NEED_STOPCONN_CLEARDATA_LCD.equals(intent.getAction())) {
                String info = intent.getStringExtra("info");
                BlueLinkControlLcdKey.getInstance().closeBlueConnReal(info);
            } else if (BLUETOOTH_NEED_SENDMESSAGE_LCD.equals(intent.getAction())) {
                LogMeLinks.e("ServiceA", "A??????:?????????????????????" + BLUETOOTH_NEED_SENDMESSAGE);
                String hexStr = intent.getStringExtra("hexStr");
                boolean needMessageSendedInfo = intent.getBooleanExtra("needMessageSendedInfo", false);
                BlueLinkControlLcdKey.getInstance().sendMessage(hexStr, needMessageSendedInfo);
            } else if (SERVICE_2_HEART_BEET.equals(intent.getAction())) {
                service2HeartTime = System.currentTimeMillis();
            }
//            else if (IS_ACTIVITYMAIN_FORGROUND.equals(intent.getAction())) {
//                boolean    isForgound  = intent.getBooleanExtra("isForground", false);
//                 if (BuildConfig.DEBUG) Log.e("------------", "??????activityisForgound "+isForgound);
//                isInForground=isForgound;
//            }
            else if (SEND_CAR_STATUS_BLUETOOTH_PROGRESS.equals(intent.getAction())) {
                if (BuildConfig.DEBUG) Log.e("??????????????????", "?????????");
                String carStatus = intent.getStringExtra("carStatus");
                if (BuildConfig.DEBUG) Log.e("??????????????????", "??????????????? " + carStatus);
                ManagerCurrentCarStatus.getInstance().setCarStatus(carStatus);
//                DataCarStatus dataCarStatus=ManagerCurrentCarStatus.getInstance().getCarStatus();
//                if(dataCarStatus!=null){
//                    BlueLinkControl.getInstance().getDataCar().isON=dataCarStatus.isON;
//                }
            } else if (SEND_CAR_STATUS_SERVICEC_TO_SERVICECA.equals(intent.getAction())) {
                if (BuildConfig.DEBUG) Log.e("??????????????????", "??????socket?????????");
                String carStatus = intent.getStringExtra("carStatusInfo");
                if (BuildConfig.DEBUG) Log.e("??????????????????", "??????socket??????????????? " + carStatus);
                Gson gson = new Gson();
                JsonObject object = gson.fromJson(carStatus, JsonObject.class);
                DataCarStatus dataCarStatus = DataCarStatus.fromJsonObject(object);
                if (BuildConfig.DEBUG) Log.e("??????????????????", "??????socket??????????????? " + dataCarStatus);
                if (dataCarStatus == null) return;
                if (dataCarStatus.carId != BlueLinkControl.getInstance().getDataCar().carId) return;
                ManagerCurrentCarStatus.getInstance().setCarStatus(dataCarStatus);
                if (BuildConfig.DEBUG) Log.e("??????????????????", "dataCarStatus.ison" + dataCarStatus.isON);
//                BlueLinkControl.getInstance().getDataCar().isON=dataCarStatus.isON;
                if (MyLcdBlueAdapterBack.current_blue_state >= STATE_DISCOVER_OK && BlueAdapter.current_blue_state < STATE_DISCOVER_OK) {
                    DataCarStatus currentStatus = ManagerCurrentCarStatus.getInstance().getCarStatus();
                    if (currentStatus == null) return;
                    BlueLinkControlLcdKey.getInstance().sendMessage(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(currentStatus)), true);
                }
            }else if(BLUE_STATE_CHANGE.equals(intent.getAction())){
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_OFF){
//                    BlueAdapter.getInstance().closeBlueReal();
                    BlueAdapter.current_blue_state = STATE_CLOSEED;
                }
            }else if(BLUETOOTH_NEED_CLEAR_BLUETOOTH.equals(intent.getAction())){
                BlueLinkControl.getInstance().clearCacheBlue();
            } else if (BLUETOOTH_NEED_CHANGE_CAR_DATA.equals(intent.getAction())) {
                Log.e("????????????", "????????????2");
                LogMeLinks.e("ServiceA", "A??????:" + BLUETOOTH_NEED_CHANGE_CAR);
                long carId = intent.getLongExtra("carId", 0);
                String deviceName = intent.getStringExtra("deviceName");
                String carSign = intent.getStringExtra("carSign");
                boolean isUseBlueModel = intent.getBooleanExtra("isUseBlueModel", false);
                boolean isShakeOpen = intent.getBooleanExtra("isShakeOpen", false);
                boolean vibratorOpen = intent.getBooleanExtra("vibratorOpen", false);
                int isBindBluetooth = intent.getIntExtra("isBindBluetooth", 0);
                String carsig = intent.getStringExtra("carsig");
                int isMyCar = intent.getIntExtra("isMyCar",0);
                String shakeLevel = intent.getStringExtra("shakeLevel");
                BlueLinkControl.getInstance().setData(carId, deviceName, carSign, isUseBlueModel, isShakeOpen, vibratorOpen, isBindBluetooth, carsig,isMyCar,shakeLevel);
            }

//            else if (SERVICE_1_SOUND_CONTROL.equals(intent.getAction())) {
//                String  control     = intent.getStringExtra("control");
//                 if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<SoundKeep??????>>>>>>"+control);
//                SoundPlay.getInstance().setMediaSoundKeep(control);
//            }
        }
    }

    //=====================================================
    private long appTime;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                boolean canDiscover = BlueAdapter.getInstance().gotoDiscoverService();
                if (BuildConfig.DEBUG)
                    Log.e("blue", "onConnectedOK gotoDiscoverService:" + canDiscover);//??????UI??????
            }
        }
    };
    private void handleSerchBlueService(){
        handler.sendEmptyMessage(1);
    }

    public OnBlueStateListenerRoll onBlueStateListener = new OnBlueStateListenerRoll() {
        @Override
        public void needLog(String log) {
            LogMeLinks.e("blue", log);
        }

        @Override
        public void onConnectedFailed(String error, final boolean isNodevice) {
            BlueLinkControl.canCheckDataReturn = false;
            if (BuildConfig.DEBUG) Log.e("bluestate", "onConnectedFailed:" + error);
            Intent broadcast = new Intent();
            broadcast.setAction(ONCONNECTEDFAILED);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            sendBroadcast(broadcast);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
        }

        @Override
        public void onConnecting() {
            BlueLinkControl.preConningTime = System.currentTimeMillis();
            if (BuildConfig.DEBUG) Log.e("bluestate", "onConnecting");
        }

        @Override
        public void onConnectedOK() {
            if (BuildConfig.DEBUG) Log.e("bluestate", "onConnectedOK");
            new TimeDelayTask().runTask(200L, new TimeDelayTask.OnTimeEndListener() {
                @Override
                public void onTimeEnd() {
                    handleSerchBlueService();
//                    boolean canDiscover = BlueAdapter.getInstance().gotoDiscoverService();
//                    if (BuildConfig.DEBUG)
//                        Log.e("blue", "onConnectedOK gotoDiscoverService:" + canDiscover);//??????UI??????
                }
            });
        }

        @Override
        public void onDiscovering() {
            BlueLinkControl.preDiscoveringTime = System.currentTimeMillis();
            if (BuildConfig.DEBUG) Log.e("bluestate", "onDiscovering");
        }

        @Override
        public void onDiscoverOK() {
            BlueLinkControl.canCheckDataReturn = false;
            BlueLinkControl.preDataBackTime = System.currentTimeMillis();
            LogMeLinks.e("blue", "onDiscoverOK");
            Intent broadcast = new Intent();
            broadcast.setAction(ONDISCOVEROK);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            sendBroadcast(broadcast);
            //??????????????????,?????????????????????
            final DataCarBlue dataCar = BlueLinkControl.getInstance().getDataCar();
            if (dataCar != null)
                LogMeLinks.e("blue", "blueSign carId:" + dataCar.carId + " sign:" + dataCar.carSign);
            if (BuildConfig.DEBUG)   Log.e("????????????", "dataCar"+dataCar.deviceName );
            if (dataCar != null && !TextUtils.isEmpty(dataCar.deviceName) && (dataCar.deviceName.startsWith("NFC") ||dataCar.deviceName.startsWith("AKL")||dataCar.deviceName.startsWith("MIN"))) {
                new TimeDelayTask().runTask(800L, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
                        if (dataCar != null && dataCar.carSign != null && dataCar.carSign.length() > 0) {
                            if (BuildConfig.DEBUG) Log.e("????????????", "????????????" + dataCar.carSign);
                            byte[] data16 = BlueVerfyUtils.getEncryptedData(dataCar.carSign);
                            if (BuildConfig.DEBUG) Log.e("????????????", Arrays.toString(data16));
                            if(data16!=null){
                                final byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 0x72, data16);
                                if (BuildConfig.DEBUG) Log.e("????????????", "?????????byte" + Arrays.toString(mess));
                                if (BuildConfig.DEBUG) Log.e("????????????", "?????????hextsr" + bytesToHexString(mess));
                                BlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
                            }
                        }
                    }
                });
            } else {
                new TimeDelayTask().runTask(350L, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
                        if (dataCar != null && dataCar.carSign != null && dataCar.carSign.length() > 0) {
                            byte[] bytes = dataCar.carSign.getBytes();
                            final byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytes);
                            LogMeLinks.e("blue", "onDiscoverOK sendmessage:" + bytesToHexString(mess));//??????UI???????????????????????????
                            BlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
                        }
                    }
                });

            }

        }

        @Override
        public void onDiscoverFailed(String error, final boolean isNoList) {
            LogMeLinks.e("blue", "onDiscoverFailed :" + error);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
        }

        @Override
        public void onMessageSended(byte[] bytes) {
            if (BuildConfig.DEBUG) Log.e("?????????????????????", "123 ");
            if (bytes == null) return;
            String byteStr = bytesToHexString(bytes);
            if (byteStr == null) return;
            if (BuildConfig.DEBUG)
                Log.e("blue", "onMessageSended length:" + bytes.length + " " + byteStr);
            if (BuildConfig.DEBUG)
                Log.e("------------", "onMessageSended length:" + bytes.length + " " + byteStr);
            String s6s = bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(6)));
            if (BuildConfig.DEBUG) Log.e("------------", "onMessageSended length:" + " s6s" + s6s);
            if (bytes.length >= 16) {
                new TimeDelayTask().runTask(200L, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
//                        BlueAdapter.getInstance().sendMessage("AA 02 55 0A F4");
                        if (BuildConfig.DEBUG) Log.e("bluestate", "onMessageSended ????????????????????????!");
                        DataCarBlue dataCar = BlueLinkControl.getInstance().getDataCar();
                        if (BuildConfig.DEBUG)
                            Log.e("------------", "datacar" + dataCar + "dataCar.vibratorOpen" + dataCar.vibratorOpen);
                        if (dataCar != null && dataCar.vibratorOpen) {
                            if (BuildConfig.DEBUG) Log.e("------------", "????????????????????????");
                            showVibrator();
                        }
                    }
                });
            } else if (bytes.length == 5 && bytes[0] == hexStringToBytes("AA")[0]
                    && bytes[1] == 2
                    && bytes[2] == hexStringToBytes("55")[0]
                    && bytes[3] == hexStringToBytes("0A")[0]
                    && bytes[4] == hexStringToBytes("F4")[0]) {
            } else {
                if (BlueLinkControl.preControlCmd == 1 && byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(1))))) {
                    LogMeLinks.e("blueSound", "bytes:" + byteStr + "  " + BlueStaticValue.getControlCmdByID(1));
                    if (BuildConfig.DEBUG) Log.e("SoundPlay", "ServiceA play_start");
                    SoundPlay.getInstance().play_start(KulalaServiceA.this);
                } else if (byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(5))))) {
                    SoundPlay.getInstance().play_backpag(KulalaServiceA.this);
                } else if (byteStr.equals(s6s)) {
                    if (BuildConfig.DEBUG) Log.e("------------", "???????????????");
                    SoundPlay.getInstance().play_findcar(KulalaServiceA.this);
                } else if (byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(3))))
                        || byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(4))))) {
//                    if (System.currentTimeMillis() - OShakeBlue.preShakeTime < 2000L) {
//                        showVibrator();
//                    } else {
//                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
//                    }
//                    if (System.currentTimeMillis() - OShakeBlue.preShakeTime < 2000L) {
//                        showVibrator();
//                    } else {
//                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
//                    }

                    if (System.currentTimeMillis() - OShakeBlueNoScreenOnOrOff.controlSuccessTime < 2000L) {
                        showVibrator();
                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
                    } else {
                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
                    }
//                    if (System.currentTimeMillis() - OShakeBlueNoScreenOnOrOff.controlUnLockSuccessTime < 2000L) {
//                        showVibrator();
//                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
//                    } else {
//                        SoundPlay.getInstance().play_lock(KulalaServiceA.this);
//                    }
                }
            }
            Intent broadcast = new Intent();
            broadcast.setAction(ONMESSAGESENDED);
            broadcast.putExtra("bytes", bytes);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            sendBroadcast(broadcast);
        }

        @Override
        public void onDataBack() {
            //???????????????????????????????????????
            if (BlueAdapter.current_blue_state >= STATE_DISCOVER_OK && BlueLinkControl.canCheckDataReturn) {
                if (System.currentTimeMillis() - BlueAdapter.countMegeTime < 3000L) {
                    BlueLinkControl.canCheckDataReturn = false;
                    if (BuildConfig.DEBUG) Log.e("blue", "?????????????????????");
                    if (BlueLinkControl.getInstance().getDataCar().vibratorOpen) showVibrator();
                }
            }
        }

        @Override
        public void onDataReceived(DataReceive data) {
            BlueLinkControl.preDataBackTime = System.currentTimeMillis();
            if (data == null) return;
            if (data.dataType == 0x03) {
                if (BuildConfig.DEBUG) Log.e("????????????", "??????????????????" + Arrays.toString(data.data));
                if (data.data.length == 17) {
                    byte[] decodeByte = DataReceive.subBytes(data.data, 1, 16);
                    if (BuildConfig.DEBUG)
                        Log.e("????????????", "?????????????????????16???" + Arrays.toString(decodeByte));
                    DataCarBlue dataCar = BlueLinkControl.getInstance().getDataCar();
                    if (dataCar != null && !TextUtils.isEmpty(dataCar.deviceName) &&(dataCar.deviceName.startsWith("NFC")||dataCar.deviceName.startsWith("AKL")||dataCar.deviceName.startsWith("MIN")) ) {
                        if (BuildConfig.DEBUG) Log.e("????????????", "???????????????" + dataCar.carSign);
                        if ( dataCar.carSign != null && dataCar.carSign.length() > 0) {
                            byte[] jiemihoushuzu = AES.decrypt(decodeByte, dataCar.carSign);
                            if (BuildConfig.DEBUG)
                                Log.e("????????????", "??????????????????" + Arrays.toString(jiemihoushuzu));
                            if (BuildConfig.DEBUG)
                                Log.e("????????????", "?????????????????????" + bytesToHexString(jiemihoushuzu));
                            byte[] byteAppTime = DataReceive.subBytes(jiemihoushuzu, 0, 8);
                            byte[] byteBlueTime = DataReceive.subBytes(jiemihoushuzu, 8, 8);
                            String appTime = new String(byteAppTime);
                            if (BuildConfig.DEBUG) Log.e("????????????", "?????????app??????" + appTime);
                            String BlueTime = new String(byteBlueTime);
                            if (BuildConfig.DEBUG) Log.e("????????????", "?????????app????????????" + BlueTime);
                            try{
                                long longAppTime = Long.parseLong(appTime);
                                if (BlueVerfyUtils.appTime != 0 && longAppTime != 0) {
                                    long timeAddOne = Long.parseLong(BlueTime) + 1;
                                    String jiamiTime = String.valueOf(timeAddOne);
                                    if (BuildConfig.DEBUG) Log.e("????????????", "???????????????????????????" + jiamiTime);
                                    byte[] data16 = AES.AESgenerator(jiamiTime, dataCar.carSign);
                                    if (BuildConfig.DEBUG)
                                        Log.e("????????????", "??????????????????????????????" + Arrays.toString(data16));
                                    final byte[] mess ;
                                    if(dataCar.isMyCar==1){
                                        mess = DataReceive.newBlueMessage((byte) 1, (byte) 0x73, data16);
                                    }else{
                                        mess = DataReceive.newBlueMessage((byte) 1, (byte) 0x74, data16);
                                    }
                                    if (BuildConfig.DEBUG)
                                        Log.e("????????????", "?????????byte??????+1" + Arrays.toString(mess));
                                    if (BuildConfig.DEBUG)
                                        Log.e("????????????", "?????????hextsr??????+1" + bytesToHexString(mess));
                                    BlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
//                                new TimeDelayTask().runTask(1000L, new TimeDelayTask.OnTimeEndListener() {
//                                    @Override
//                                    public void onTimeEnd() {
//                                        Log.e("????????????", "?????????????????????" + bytesToHexString(getCalenda()));
//                                        BlueAdapter.getInstance().sendMessage(getCalenda());
//                                    }
//                                });
                                }
                            }catch (NumberFormatException e){
                                Log.e("????????????", "?????????app??????????????????"  );
                            }
                        }
                    }
                }
            }
            if (data.dataType == 0x21) {
                byte[] infos = ConvertHexByte.getBitArray(data.data[1]);//??????????????????
                BlueLinkControl.getInstance().getDataCar().isLock = infos[6];//????????????                0b: ?????? 1b: ??????
                BlueLinkControl.getInstance().getDataCar().isTheft = infos[2] * 2 + infos[1];//??????
                BlueLinkControl.getInstance().getDataCar().isON = infos[0];//ON????????????            0b: OFF 1b: ON
                if (ManagerCurrentCarStatus.getInstance().getCarStatus() != null) {
                    ManagerCurrentCarStatus.getInstance().setIsOn(infos[0]);
                }
                BlueLinkControlLcdKey.getInstance().getDataCar().isLock = infos[6];//????????????                0b: ?????? 1b: ??????
                BlueLinkControlLcdKey.getInstance().getDataCar().isTheft = infos[2] * 2 + infos[1];//??????
                BlueLinkControlLcdKey.getInstance().getDataCar().isON = infos[0];//ON????????????            0b: OFF 1b: ON
            }
//            if (BuildConfig.DEBUG) Log.e("------------", "KulalaServiceA.isInForground"+KulalaServiceA.isInForground );
//            Intent broadcast = new Intent();
//            broadcast.setAction(ONDATARECEIVED);
//            broadcast.putExtra("carId", BlueLinkControl.getInstance().getDataCar().carId);
//            broadcast.putExtra("dataType", data.dataType);
//            broadcast.putExtra("data", data.data);
//            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
//            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
            boolean isForground = isAppForeground(KulalaServiceAThis,"com.client.proj.kusida");
            if (BuildConfig.DEBUG) Log.e("------------", "????????????" + isForground);
            Intent broadcast = new Intent();
            broadcast.setAction(ONDATARECEIVED);
            broadcast.putExtra("carId", BlueLinkControl.getInstance().getDataCar().carId);
            broadcast.putExtra("dataType", data.dataType);
            broadcast.putExtra("data", data.data);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            try{
                sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
            }catch(Exception e){

            }

//            sendBroadcast(broadcast);
            if (!isForground) {
                //                sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
                if (BuildConfig.DEBUG) Log.e("------------", "??????????????????");
                //??????????????????????????????????????????????????????
                if (data.dataType == 0x21) {
                    ManagerCarStatus.setData0x21(data.data);
                } else if (data.dataType == 0x22) {
                    ManagerCarStatus.setData0x22(data.data);
                }
            }
        }

        @Override
        public void onReadRemoteRssi(int rssi, int status) {
            LogMeLinks.e("blue", "?????????rssi:" + rssi + " status:" + status);
        }
    };

    public boolean isAppForeground(Context context,String mPackageName) {
        String packageName=getTopAppInfoPackageName(context);
        if (!TextUtils.isEmpty(packageName) && packageName.equals(mPackageName)) {
            return true;
        }
        return false;
    }
    /**
     * ????????????????????????????????????????????????????????????????????????,<br>
     * ?????????api???????????????,???????????????????????????????????????,???????????????
     *
     * @param context
     *            ???????????????
     * @return ????????????,??????????????????????????????????????????""
     */
    public static String getTopAppInfoPackageName(Context context) {
        if (Build.VERSION.SDK_INT < 21) { // ??????????????????22
            // ?????????activity???????????????
            ActivityManager m = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // ?????????????????????????????????????????????
            List<ActivityManager.RunningTaskInfo> tasks = m.getRunningTasks(1);

            if (tasks != null && tasks.size() > 0) { // ????????????????????????

                // ????????????????????????????????????
                ActivityManager.RunningTaskInfo info = m.getRunningTasks(1).get(0);

                // ????????????????????????
                // String packageName =
                // info.topActivity.getPackageName();
                return info.baseActivity.getPackageName();
            } else {
                return "";
            }
        } else {

            final int PROCESS_STATE_TOP = 2;
            try {
                // ??????????????????????????????????????????????????????????????????,????????????????????????
                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                // ????????????????????????????????????????????????????????????
                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
                // ?????????????????????,??????????????????????????????????????????,??????????????????????????????????????????????????????,??????????????????????????????
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && process.importanceReasonCode == 0) {
                        int state = processStateField.getInt(process);
                        if (state == PROCESS_STATE_TOP) { // ?????????????????????????????????????????????????????????
                            String[] packname = process.pkgList;
                            // ?????????????????????
                            return packname[0];
                        }
                    }
                }
            } catch (Exception e) {
            }
            return "";
        }
    }

    private void showVibrator() {
        LogMeLinks.e("blue>>>", "????????????");
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300L);//?????????????????????pattern ???????????????????????????index??????-1
    }

    // end
    public OnBlueStateListenerRoll onBlueStateListenerLcd = new OnBlueStateListenerRoll() {
        @Override
        public void needLog(String log) {
            LogMeLinks.e("blue", log);
        }

        @Override
        public void onConnectedFailed(String error, final boolean isNodevice) {
            BlueLinkControlLcdKey.canCheckDataReturn = false;
            LogMeLinks.e("blue", "onConnectedFailed:" + error);
            Intent broadcast = new Intent();
            broadcast.setAction(ONCONNECTEDFAILEDLCDKEY);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            sendBroadcast(broadcast);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
        }

        @Override
        public void onConnecting() {
            BlueLinkControlLcdKey.preConningTime = System.currentTimeMillis();
            LogMeLinks.e("blue", "onConnecting");
        }

        @Override
        public void onConnectedOK() {
            LogMeLinks.e("blue", "onConnectedOK");
            new TimeDelayTask().runTask(300L, new TimeDelayTask.OnTimeEndListener() {
                @Override
                public void onTimeEnd() {
                    boolean canDiscover = MyLcdBlueAdapterBack.getInstance().gotoDiscoverService();
                    if (BuildConfig.DEBUG)
                        Log.e("blue", "onConnectedOK gotoDiscoverService:" + canDiscover);//??????UI??????
                }
            });
        }

        @Override
        public void onDiscovering() {
            BlueLinkControlLcdKey.preDiscoveringTime = System.currentTimeMillis();
            LogMeLinks.e("blue", "onDiscovering");
        }

        @Override
        public void onDiscoverOK() {
            BlueLinkControlLcdKey.canCheckDataReturn = false;
            BlueLinkControlLcdKey.preDataBackTime = System.currentTimeMillis();
            LogMeLinks.e("blue", "onDiscoverOK");
            Intent broadcast = new Intent();
            broadcast.setAction(ONDISCOVEROKLCD);
            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
            sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            sendBroadcast(broadcast);
            new TimeDelayTask().runTask(350L, new TimeDelayTask.OnTimeEndListener() {
                @Override
                public void onTimeEnd() {
                    //???????????????????????????,?????????????????????
                    DataCarBlueLcd dataCar = BlueLinkControlLcdKey.getInstance().getDataCar();
                    if (dataCar != null)
//                         if (BuildConfig.DEBUG) Log.e("??????????????????", "blueSign carId:" + dataCar.carId + " sign:" + dataCar.keySig);
                        LogMeLinks.e("blue", "blueSign carId:" + dataCar.carId + " sign:" + dataCar.keySig);
                    if (dataCar != null && dataCar.keySig != null && dataCar.keySig.length() > 0) {
                        byte[] bytes = dataCar.keySig.getBytes();
                        final byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytes);
                        LogMeLinks.e("blue", "onDiscoverOK sendmessage:" + bytesToHexString(mess));//??????UI???????????????????????????
                        LogMeLinks.e("??????????????????", "blueSign carId:" + dataCar.carId + " sign:" + dataCar.keySig);
                        MyLcdBlueAdapterBack.getInstance().sendMessage(bytesToHexString(mess));
                    }
                }
            });
        }

        @Override
        public void onDiscoverFailed(String error, final boolean isNoList) {
            LogMeLinks.e("blue", "onDiscoverFailed :" + error);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
        }

        @Override
        public void onMessageSended(byte[] bytes) {

        }

        @Override
        public void onDataBack() {
            //            if (MyLcdBlueAdapterBack.current_blue_state >= STATE_DISCOVER_OK && BlueLinkControlLcdKey.canCheckDataReturn) {
//                if (System.currentTimeMillis() - MyLcdBlueAdapterBack.countMegeTime < 3000L) {
//                    BlueLinkControlLcdKey.canCheckDataReturn = false;
//                    LogMeLinks.e("blue", "?????????????????????");
//                }
//            }
        }

        @Override
        public void onDataReceived(DataReceive data) {
        }

        @Override
        public void onReadRemoteRssi(int rssi, int status) {
            LogMeLinks.e("blue", "?????????rssi:" + rssi + " status:" + status);
        }
    };

    public static int noSignInt(byte a) {
        return a & 0xff;
    }
    private byte[] getCalenda() {
        Calendar calendar = Calendar.getInstance();
//?????????????????????
//???
        int year = calendar.get(Calendar.YEAR);
        byte myYear = (byte) (year % 100);
//???
        byte month = (byte) (calendar.get(Calendar.MONTH) + 1);
//???
        Log.e("?????????", "month: "+month);
        byte day = (byte) calendar.get(Calendar.DAY_OF_MONTH);
//??????????????????
//??????
        Log.e("?????????", "day: "+day);
        byte hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
//??????
        byte minute = (byte) calendar.get(Calendar.MINUTE);
//???
        byte second = (byte) calendar.get(Calendar.SECOND);
        byte week = (byte) calendar.get(Calendar.DAY_OF_WEEK);
        byte[] times = new byte[12];
        times[0] = (byte)0xE5;
        times[1] = 9;
        times[2] = 0x01;
        times[3] =  (byte)0xA0;
        times[4] = myYear;
        times[5] = month;
        times[6] = day;
        times[7] = hour;
        times[8] = minute;
        times[9] = second;
        times[10] = week;
        for (int i = 0; i < 11; i++) {
            times[11] += times[i];
        }
        times[11] ^= 0xff;
        return times;
    }
}
