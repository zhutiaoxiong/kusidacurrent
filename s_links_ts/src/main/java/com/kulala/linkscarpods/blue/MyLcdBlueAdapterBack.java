package com.kulala.linkscarpods.blue;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.time.TimeDelayTask;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_SEND_CARINFO_TO_LCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED__LCDKEY_CONTROL_CAR;
import static com.kulala.linkscarpods.blue.KulalaServiceA.KulalaServiceAThis;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CLOSEED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECTING;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECT_FAILED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECT_OK;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVERING;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_FAILED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_OK;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_MSG_RECEIVED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_MSG_SENDED;


/**
 * kc002 0001 654321
 * Created by Administrator on 2016/8/27.
 * <uses-feature android:name="android.hardware.bluetooth_le" android:required="false"/>PackageManager.hasSystemFeature()?????????????????????????????????BLE
 * 1.??????????????????????????????????????????????????????????????????
 */
public class MyLcdBlueAdapterBack {
    private static final ParcelUuid UUID_WRITE_SERVICE = ParcelUuid.fromString("00001000-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_WRITE_CHARACTERISTIC = UUID.fromString("00001001-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_NOTIFY = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_NOTIFY_DES = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private Context mContext;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private  BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic writeCharacteristic;

    private BluetoothDevice usedDevice;

    private boolean isConnected = false;
    private int connectState;

    //============================================
    /**
     * ???????????????????????????
     * ,???????????????????????????????????????????????????
     */
    public static int current_blue_state = 0;
    private OnBlueStateListenerRoll onBlueStateListener;

    public void setOnBlueStateListener(OnBlueStateListenerRoll listener) {
        this.onBlueStateListener = listener;
    }

    //============================================
    public BluetoothDevice getUsedDevice() {
        return usedDevice;
    }

    //============================================
    private static MyLcdBlueAdapterBack _instance;

    protected MyLcdBlueAdapterBack() {
    }

    public static MyLcdBlueAdapterBack getInstance() {
        if (_instance == null)
            _instance = new MyLcdBlueAdapterBack();
        return _instance;
    }

    //====================????????????========================
    public boolean initializeOK(Context context) {
        if (context == null) return false;
        this.mContext = context;
        //1????????????BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) return false;
        //2??????BluetoothAdapter
        if (bluetoothAdapter == null) {
            if (Build.VERSION.SDK_INT >= 19) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            } else {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
        }
        if (bluetoothAdapter == null) return false;
        return true;
    }


    public boolean getIsConnectted() {
        return isConnected;
    }

    //=======================????????????=====================
    public  void gotoConnDeviceAddress(String deviceAddress) {
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null || !bluetoothAdapter.isEnabled()) {
            Log.i("????????????", "---??????---??????????????????");
            return;
        }
        if(connectState==133){
            gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
        }
        if (usedDevice != null && usedDevice.getName() != null && usedDevice.getAddress().equals(deviceAddress)) {
            if (bluetoothAdapter.isEnabled()) {
                Log.i("????????????", "---??????---????????????");
                gotoConnDevice(usedDevice);
            } else {
                Log.i("????????????", "---??????---????????????");
            }
        } else {
            Log.i("????????????", "---??????---????????????" + deviceAddress);
            gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
        }
    }

    //1.bluetoothGatt????????????new??????
    //2.close?????????????????????
    //3.???disconnect,???close
    private long preConnTime = 0;

    public  void gotoConnDevice(final BluetoothDevice device) {
        if (device == null || device.getAddress() == null || !bluetoothAdapter.isEnabled()) return;
        long now = System.currentTimeMillis();
        if (now - preConnTime < 1000L) return;//1???????????????
        preConnTime = now;
        //test
        if (current_blue_state <STATE_CONNECTING) {//!isConnected
            //????????????????????????
            if (writeCharacteristic != null) writeCharacteristic = null;
            if (bluetoothGatt != null) bluetoothGatt.close();//????????????
            isConnected = false;
            bluetoothGatt = null;
            current_blue_state = STATE_CONNECTING;

            usedDevice = device;
             if (BuildConfig.DEBUG) Log.e("????????????", "---??????????????????---???" + device + device.getName());
            if (onBlueStateListener != null) onBlueStateListener.onConnecting();
            TimerTask task = new TimerTask() {
                public  void run() {
                    if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
                    //test
                    if ( mContext != null&&device!=null&&mGattCallback!=null){
                         if (BuildConfig.DEBUG) Log.e("????????????", "---??????????????????---???" + device + device.getName());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            bluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE); //
                        } else {
                            bluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
                        }
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 500L);
        } else {
             if (BuildConfig.DEBUG) Log.e("????????????", "---??????????????????,?????????????????????---???" + device + device.getName());
        }
    }
    //==========================????????????==========================

    @SuppressLint("NewApi")
    private final  BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //?????????????????????????????????????????????????????????
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            connectState=status;
             if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????? status:" + status + " newState:" + newState);
            if (gatt == null) return;

                if (newState == BluetoothProfile.STATE_CONNECTED) { //????????????,status?????????0
                    BlueLinkControlLcdKey.preDataBackTime = System.currentTimeMillis();//???????????????????????????
                    isConnected = true;
                    if (current_blue_state >= STATE_CONNECT_OK) return;//????????????????????????
                     if (BuildConfig.DEBUG) Log.e("????????????", "--------STATE_CONNECTED-----");
                    current_blue_state = STATE_CONNECT_OK;
                    if (onBlueStateListener != null) onBlueStateListener.onConnectedOK();
//                bluetoothGatt.discoverServices();//??????UI??????
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //????????????
                    gatt.close();
                     if (BuildConfig.DEBUG) Log.e("????????????", "--------STATE_DISCONNECTED && onConnectedFailed-----");
                    if (writeCharacteristic != null) writeCharacteristic = null;
                    if (bluetoothGatt != null){
                        bluetoothGatt.disconnect();
                        bluetoothGatt.close();//????????????
                        bluetoothGatt=null;
                    }
                    isConnected = false;
                    current_blue_state = STATE_CONNECT_FAILED;
//                    if(status==133&&bluetoothAdapter!=null&&bluetoothAdapter.isEnabled()){
//                        bluetoothAdapter.disable();
//                        try {
//                            Thread.sleep(2000);
//                            bluetoothAdapter.enable();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (onBlueStateListener != null)
                        onBlueStateListener.onConnectedFailed("??????????????????", false);
                }
        }

        @Override  //???????????????????????????????????????????????????
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //???????????????
                //??????????????????????????????????????????????????????????????????
                 if (BuildConfig.DEBUG) Log.e("????????????", "--------?????????????????????-----bluetoothGatt != null:" + (gatt != null));
                if (gatt != null) {
                    BluetoothGattService serviceSS = gatt.getService(UUID_WRITE_SERVICE.getUuid());
                    if (serviceSS != null) {
                        String error = displayGattServices(serviceSS);
                        if (error != null) {
                            current_blue_state = STATE_DISCOVER_FAILED;
                            if (onBlueStateListener != null)
                                onBlueStateListener.onDiscoverFailed("Service???????????????", true);
                        }
                    } else {
                        current_blue_state = STATE_DISCOVER_FAILED;
                        if (onBlueStateListener != null)
                            onBlueStateListener.onDiscoverFailed("?????????????????????", true);
                    }
                }
            } else {
                 if (BuildConfig.DEBUG) Log.e("????????????", "--------?????????????????????-----");
                current_blue_state = STATE_DISCOVER_FAILED;
                if (onBlueStateListener != null)
                    onBlueStateListener.onDiscoverFailed("?????????????????????", true);
            }
        }

        @Override  //??????????????????
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] bytes = characteristic.getValue();

//            System.out.println("--------???????????? onCharacteristicRead??? " + bytesToHexString(characteristic.getValue()));
             if (BuildConfig.DEBUG) Log.e("????????????", "--------???????????? onCharacteristicRead??? " + ByteHelper.bytesToHexString(characteristic.getValue()));
        }

        @Override //??????????????????
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            current_blue_state = STATE_MSG_RECEIVED;
            if (BuildConfig.DEBUG)
                 if (BuildConfig.DEBUG) Log.e("????????????", ByteHelper.bytesToHexString(characteristic.getValue()) + "????????????????????????");
            findMege(bytes);
        }

        @Override //????????????????????????
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            final byte[] result = descriptor.getValue();
             if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????????: onCharacteristicWrite:" + ByteHelper.bytesToHexString(result));
//            current_blue_state = STATE_MSG_SENDED;
        }

        @Override //????????????????????????
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            final byte[] result = characteristic.getValue();
             if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????????: onCharacteristicWrite:" + ByteHelper.bytesToHexString(result) + "????????????????????????");
            current_blue_state = STATE_MSG_SENDED;
            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            //rssi??????????????????
            if (onBlueStateListener != null)
                onBlueStateListener.onReadRemoteRssi(Math.abs(rssi), status);
        }

    };

    public void readRssiRemote() {
        if (bluetoothGatt != null) {
            bluetoothGatt.readRemoteRssi();
        }
    }

    //    public static long                  preDiscoverTime = 0;
    public boolean gotoDiscoverService() {
//        long now = System.currentTimeMillis();
//        if (now - preDiscoverTime < 1000L) return;
//        preDiscoverTime = now;
        if (bluetoothGatt != null && isConnected) {
            current_blue_state = STATE_DISCOVERING;
            if (onBlueStateListener != null) onBlueStateListener.onDiscovering();
            boolean discoverok = bluetoothGatt.discoverServices();//??????UI??????
            if (!discoverok && onBlueStateListener != null) {
                current_blue_state = STATE_DISCOVER_FAILED;
                onBlueStateListener.onDiscoverFailed("discover??????"
                        , false);
                return false;
            }
        } else if (!isConnected) {
            current_blue_state = STATE_CONNECT_FAILED;
            if (onBlueStateListener != null)
                onBlueStateListener.onConnectedFailed("?????????????????????", false);
            return false;
        } else {
            current_blue_state = STATE_DISCOVER_FAILED;
            if (onBlueStateListener != null) onBlueStateListener.onDiscoverFailed("Gatt?????????", true);
            return false;
        }
        return true;
    }

    /**
     * 7.??????????????????
     *
     * @return error
     */
    @SuppressLint("NewApi")
    private String displayGattServices(BluetoothGattService gattServices) {
        if (gattServices == null) return "Service???????????????";
        BluetoothGattCharacteristic gattCharacteristic = gattServices.getCharacteristic(UUID_NOTIFY);
        if (gattCharacteristic == null) return "????????????????????????";
        boolean isEnableNotification = bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
         if (BuildConfig.DEBUG) Log.e("????????????", "??????????????????:" + isEnableNotification + " " + UUID_NOTIFY);
        if (isEnableNotification) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_NOTIFY_DES);
            if (descriptor != null) {
                boolean descripOK = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);
                 if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????????:" + descripOK + " " + UUID_NOTIFY_DES);
            }
//            for(BluetoothGattDescriptor dp:gattCharacteristic.getDescriptors()){
//                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                bluetoothGatt.writeDescriptor(dp);
//            }
        } else {
            return "??????????????????????????????";
        }
        BluetoothGattCharacteristic characteristicWrite = gattServices.getCharacteristic(UUID_WRITE_CHARACTERISTIC);
        if (characteristicWrite != null) {
            writeCharacteristic = characteristicWrite;
             if (BuildConfig.DEBUG) Log.e("????????????", "??????????????????:" + writeCharacteristic.getUuid());
            current_blue_state = STATE_DISCOVER_OK;
            if (onBlueStateListener != null) onBlueStateListener.onDiscoverOK();
        } else {
            return "????????????????????????";
        }
        return null;
    }

    public void openBlueTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) bluetoothAdapter.enable();
    }

    //??????????????????,???????????????????????????
//    public void closeBlueTooth(String fromInfo) {
//        Log.i(">>>Blue>>>", "closeBlueTooth:" + fromInfo);
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter != null) {
//            current_blue_state = STATE_CLOSEING;
//            bluetoothAdapter.disable();
//        }
//    }
    //????????????
    public void closeBlueReal() {
        if (bluetoothGatt != null) bluetoothGatt.disconnect();
        current_blue_state = STATE_CLOSEED;
        if (bluetoothGatt != null) bluetoothGatt.close();//????????????
        usedDevice = null;
        isConnected = false;
    }

    @SuppressLint("NewApi")
    public void closeBlueWait() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            current_blue_state = STATE_CLOSEED;
//            if (bluetoothGatt != null) bluetoothGatt.close();//????????????
        }
    }

    /**
     * ???????????????????????????????????? STATE_CLOSEING
     */
    public void closeBlueConnCheck(int currentState) {
//        if (current_blue_state == currentState && currentState == STATE_CLOSEING) {
//            TimerTask task = new TimerTask() {
//                public void run() {
//                    if (current_blue_state != STATE_CLOSEING) return;
//                    if (writeCharacteristic != null) writeCharacteristic = null;
//                    if (bluetoothGatt != null) bluetoothGatt.close();//????????????
//                    isConnected = false;
//                    bluetoothGatt = null;
//                    current_blue_state = STATE_CLOSEED;
//                }
//            };
//            Timer timer = new Timer();
//            timer.schedule(task, 300L);
//        }
    }
    //==================================get=========================================

    /**
     * ???????????? //AA 02 55 0A F4 ???????????????????????????
     */
    @SuppressLint("NewApi")
    public boolean sendMessage(String hexStr) {
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("????????????", " ??????????????????,?????????");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
            Log.i("????????????", " ??????????????????hexstr" + hexStr);
            writeCharacteristic.setValue(ByteHelper.hexStringToBytes(hexStr));
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }

    public boolean sendMessage(byte[] data) {
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("????????????", " ??????????????????,?????????");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
//            Log.i(">>>Blue>>>", " ???????????????" + hexStr);
            writeCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }
    /**
     * ????????????
     */

//    public byte[] findNextAllMatch(byte[] cache) {
//        if (cache == null) return new byte[0];
//        if (cache.length < 4) return new byte[0];
//        DataReceive data = new DataReceive();
//        //1 DataType
//        data.dataType = cache[0];
//        //2 Length
//        data.length = cache[1];
//        //3 data
//        if (cache.length != data.length + 3) return new byte[0];
//        data.check = cache[cache.length - 1];
//        byte[] results = new byte[data.length];
//        System.arraycopy(cache, 2, results, 0, data.length);
//        data.data = results;
//        if (onBlueStateListener != null) onBlueStateListener.onDataReceived(data);
//        return results;
//    }
    /**
     * ???data[1]  2108006A0000000000006C2208F8330100000000
     * ???data[1]  00A92308000040B518000000C7
     */
    private byte[] cachecheck;
    public static long countMegeTime = 0;

    private void findMege(byte[] megebyte) {
        if (onBlueStateListener != null) onBlueStateListener.onDataBack();
        cachecheck = ByteHelper.bytesMege(cachecheck, megebyte);
        if (cachecheck.length > 200) {
            cachecheck = null;//??????????????????
            return;
        }
        long now = System.currentTimeMillis();
        countMegeTime = now;
//        if (now - countMegeTime >= 300L) {
//            countMegeTime = now;
////             if (BuildConfig.DEBUG) Log.e("blue", "2108 cachecheck:"+ConvertHexByte.bytesToHexString(cachecheck));
//            findNext(cachecheck);
//            cachecheck = null;
//        }

//        if (now - countMegeTime >= 300L) {
//            countMegeTime = now;
////             if (BuildConfig.DEBUG) Log.e("blue", "2108 cachecheck:"+ConvertHexByte.bytesToHexString(cachecheck));
        findNext(cachecheck);
        cachecheck = null;
//        }
    }

    public void findNext(final byte[] megebyte) {
        //1.??????????????????
        final DataReceive data = new DataReceive();
        if (megebyte == null || megebyte.length < 4) {
            cachecheck = megebyte;
            return;
        }
        //1 DataType
        data.dataType = megebyte[0];
        //2 Length
        data.length = megebyte[1];
        //3 data
        if (data.length == 0 || data.length > 32 || data.length < 0) {
            findNext(ByteHelper.bytesCut(megebyte, 1));//??????1byte;
            return;
        }
        if (megebyte.length < data.length + 3) {
            cachecheck = megebyte;
            return;//?????????????????????????????? Type+length+data+check
        }
        //???????????????,??????????????????data
        data.data = new byte[data.length];
        System.arraycopy(megebyte, 2, data.data, 0, data.length);
        //4.check
        data.check = megebyte[data.length + 2];
        if (data.matchCheck()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if (onBlueStateListener != null){
//                        onBlueStateListener.onDataReceived(data);
//                    }
//                    //??????????????????
//                            cmdControlCommand(data);
//                            //??????????????????
//                            getSystemTime(data);
//                            sendCarStatus(data);
//
////             if (BuildConfig.DEBUG) Log.e(">>>Blue>>>", "--------???????????? ????????? "+data.dataType+" : " + bytesToHexString(data.data));//33:21
//                }
//            }).start();

            ThreadManagerLink.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (onBlueStateListener != null) {
                        onBlueStateListener.onDataReceived(data);
                    }
                    BlueLinkControlLcdKey.preDataBackTime = System.currentTimeMillis();
                    //??????????????????
                    cmdControlCommand(data);
                    //??????????????????
                    getSystemTime(data);
                    sendCarStatus(data);
                }
            });
        }
        byte[] uncheck = ByteHelper.bytesCut(megebyte, data.length + 3);//???????????????
        findNext(uncheck);
    }

    private void sendCarStatus(DataReceive data) {
        if (data.dataType == 0x43 && data.length == 1) {
            byte[] requst = data.data;
            if (requst[0] == 0x00) {
                boolean isForground=true;
//                boolean isForground=getForground();
                //KulalaServiceA.isInForground
                if(isForground){
                    Intent broadcast = new Intent(BLUETOOTH_NEED_SEND_CARINFO_TO_LCD);
                   broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
                    KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//                    KulalaServiceAThis.sendBroadcast(broadcast);
                }else{
                    final    DataCarStatus currentStatus=ManagerCurrentCarStatus.getInstance().getCarStatus();
//                     if (BuildConfig.DEBUG) Log.e("???????????????????????????", "sendCarStatus:"+DataCarStatus.toJsonObject(currentStatus) .toString());
                    if(currentStatus==null)return;
                    new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
                        @Override
                        public void onTimeEnd() {
                            BlueLinkControlLcdKey.getInstance().sendMessage(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(currentStatus)),true);
                        }
                    });
                }
                //???????????????????????????
            }
        }
    }

    private void sendCarStatusCopy() {
        //??????????????????
//                DataCarStatus status=ManagerCarList.getInstance().getCurrentCarStatus();
//                if(status!=null){
//                    LcdManagerCarStatus.sendCarStatus(status);
//                }
    }

    private void cmdControlCommand(DataReceive data) {
        if (data.dataType == 0x42) {
//            Intent broadcast = new Intent();
//            broadcast.setAction(BLUETOOTH_NEED__LCDKEY_CONTROL_CAR);
//            broadcast.putExtra("carId", BlueLinkControlLcdKey.getInstance().getDataCar().carId);
//            broadcast.putExtra("dataType", data.dataType);
//            broadcast.putExtra("data", data.data);
//            broadcast.putExtra("length", data.length);
//            KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast);
             if (BuildConfig.DEBUG) Log.e("MyLcdBlueAdapterBack", "????????????????????????reciver");
            //????????????????????????????????????
//                DataCarBlueLcd lcd=DataCarBlueLcd.loadLocal(mContext);
//                long carId=lcd.carId;
//                byte[] cmdByte = data.data;
//                int length = data.length;
//                int cmd=0;
//                if( cmdByte[0]==0x01){
//                    cmd=4;//??????
//                }else  if( cmdByte[0]==0x02){
//                    cmd=3;//??????
//                }else  if( cmdByte[0]==0x03){
//                    cmd=5;//??????
//                }else  if( cmdByte[0]==0x04){
//                    cmd=1;//??????
//                }else  if( cmdByte[0]==0x05){
//                    cmd=6;//??????
//                }
//                int time=0;
//                if(length==2){
//                    if(cmdByte[1]>=0&&cmdByte[1]<=7){
//                        time=cmdByte[1];
//                    }
//                }

//                DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
//                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//                ViewControlPanelControl.preControlCmd=cmd;
//                if(cmd==1){
//                    if(carStatus.isON==1){
//                        ViewControlPanelControl.preControlCmd=2;
//                    }else{
//                        ViewControlPanelControl.preControlCmd=1;
//                    }
//                }
//                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, ViewControlPanelControl.preControlCmd, time);
//                long carId = intent.getLongExtra("carId", 0);
//                int dataType = intent.getIntExtra("dataType", 0);

//                byte[] cmdByte = intent.getByteArrayExtra("data");
//                int length = intent.getIntExtra("length",0);


//                byte[] cmdByte = data.data;
//                int length = data.length;
//                int cmd=0;
//                if( cmdByte[0]==0x01){
//                    cmd=4;//??????
//                }else  if( cmdByte[0]==0x02){
//                    cmd=3;//??????
//                }else  if( cmdByte[0]==0x03){
//                    cmd=5;//??????
//                }else  if( cmdByte[0]==0x04){
//                    cmd=1;//??????
//                }else  if( cmdByte[0]==0x05){
//                    cmd=6;//??????
//                }
//                int time=0;
//                if(length==2){
//                    if(cmdByte[1]>=0&&cmdByte[1]<=7){
//                        time=cmdByte[1];
//                    }
//                }
//                DataCarBlue dataCarBlue=DataCarBlue.loadLocal(mContext);
//                if(dataCarBlue==null)return;
//                long carId=dataCarBlue.carId;
//                int isOn= dataCarBlue.isON ;
//             if (BuildConfig.DEBUG) Log.e("????????????", "cmdControlCommand: ");
//                String blueToothname=dataCarBlue.deviceName;
//                int  isBindBluetooth=dataCarBlue.isBindBluetooth;
//                if(cmd==1){
//                    if(isOn==1){
//                        cmd=2;
//                    }else{
//                        cmd=1;
//                    }
//                }
//                DataCarInfo carInfo =new DataCarInfo();
//                carInfo.bluetoothName=blueToothname;
//                carInfo.ide=carId;
//                carInfo.carsig=dataCarBlue.carSign;
//                carInfo.isBindBluetooth=isBindBluetooth;
//                ???????????????????????????
//                if(carInfo.isBindBluetooth==1&&BlueAdapter.current_blue_state>=STATE_DISCOVER_OK){
//                     if (BuildConfig.DEBUG) Log.e("????????????", "??????????????????" );
//                    final    String cacheCmd=BlueStaticValue.getControlCmdByID(cmd);
//                    if(cmd==1){
//                        String setTime = BlueStaticValue.getBlueTimeCmd(time);//0-7
//                        BlueAdapter.getInstance().sendMessage(setTime);
//                        new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
//                            @Override
//                            public void onTimeEnd() {
//                                BlueLinkControl.getInstance().sendMessage(cacheCmd,false);
//                            }
//                        });
//                    }else{
//                        BlueLinkControl.getInstance().sendMessage(cacheCmd,false);
//                    }
//                }
//                else{
////
////
////                    //?????????????????????
             if (BuildConfig.DEBUG) Log.e("????????????", "??????socket??????");
//                    BlueLcdNet.getInstance().ccmd1233_controlCar(carInfo, cmd, time);
//                    OCtrlSocketMsgBackground.getInstance().ccmd_controlCar(carInfo,cmd,time,mContext);

//           }
//             if (BuildConfig.DEBUG) Log.e("?????????", "KulalaServiceA.isInForground" +KulalaServiceA.isInForground);
//          boolean  isInForground= KulalaServiceA.isInForground;
//             if (BuildConfig.DEBUG) Log.e("------------", "?????????" );
//            Intent broadcast = new Intent();
//            broadcast.setAction(BLUETOOTH_NEED__LCDKEY_CONTROL_CAR);
//            broadcast.putExtra("carId", BlueLinkControlLcdKey.getInstance().getDataCar().carId);
//            broadcast.putExtra("dataType", data.dataType);
//            broadcast.putExtra("data", data.data);
//            broadcast.putExtra("length", data.length);
//            broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
////                KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//            KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast);
//            isInForground=false;
            boolean isForground=true;
//            boolean isForground= getForground();
            if (isForground) {
                 if (BuildConfig.DEBUG) Log.e("------------", "?????????" );
                Intent broadcast = new Intent();
                broadcast.setAction(BLUETOOTH_NEED__LCDKEY_CONTROL_CAR);
                broadcast.putExtra("carId", BlueLinkControlLcdKey.getInstance().getDataCar().carId);
                broadcast.putExtra("dataType", data.dataType);
                broadcast.putExtra("data", data.data);
                broadcast.putExtra("length", data.length);
                broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
                KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//                KulalaServiceAThis.sendBroadcast(broadcast);
            } else {
                byte[] cmdByte = data.data;
                int length = data.length;
                int cmd = 0;
                if (cmdByte[0] == 0x01) {
                    cmd = 4;//??????
                } else if (cmdByte[0] == 0x02) {
                    cmd = 3;//??????
                } else if (cmdByte[0] == 0x03) {
                    cmd = 5;//??????
                } else if (cmdByte[0] == 0x04) {
                    cmd = 1;//??????
                } else if (cmdByte[0] == 0x05) {
                    cmd = 6;//??????
                }
                int time = 0;
                if (length == 2) {
                    if (cmdByte[1] >= 0 && cmdByte[1] <= 7) {
                        time = cmdByte[1];
                    }
                }
                DataCarBlue dataCarBlue=DataCarBlue.loadLocal(mContext);
                if(dataCarBlue==null)return;
                long carId=dataCarBlue.carId;
//                int isOn= BlueLinkControl.getInstance().getDataCar().isON;
                int isOn= ManagerCurrentCarStatus.getInstance().getCarStatus().isON;
                 if (BuildConfig.DEBUG) Log.e("??????????????????", "??????on??????"+isOn);
                String blueToothname=dataCarBlue.deviceName;
                int  isBindBluetooth=dataCarBlue.isBindBluetooth;
                if(cmd==1){
                    if(isOn==1){
                        cmd=2;
                    }
                }
                BlueLinkControl.preControlCmd=cmd;
                 if (BuildConfig.DEBUG) Log.e("??????????????????", " BlueLinkControl.preControlCmd "+  BlueLinkControl.preControlCmd);
                DataCarInfo carInfo =new DataCarInfo();
                carInfo.bluetoothName=blueToothname;
                carInfo.ide=carId;
                carInfo.carsig=dataCarBlue.carsig;//??????????????????
                 if (BuildConfig.DEBUG) Log.e("------------", "carInfo.carsig"+carInfo.carsig);
                carInfo.isBindBluetooth=isBindBluetooth;
                 DataCarBlueLcd dataCarBlueLcd=DataCarBlueLcd.loadLocal(mContext);
                 long userId=dataCarBlueLcd.userId;
//                ???????????????????????????
                boolean isBlutooth;
                if(carInfo.isBindBluetooth == 1 && BlueAdapter.current_blue_state >= STATE_DISCOVER_OK){
                    isBlutooth=true;
                }else{
                    isBlutooth=false;
                }
                if (isBlutooth) {
                     if (BuildConfig.DEBUG) Log.e("------------", "???????????????");
                    final String cacheCmd = BlueStaticValue.getControlCmdByID(cmd);
                    if (cmd == 1) {
                        String setTime = BlueStaticValue.getBlueTimeCmd(time);//0-7
                        BlueAdapter.getInstance().sendMessage(setTime);
                        new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
                            @Override
                            public void onTimeEnd() {
                                 if (BuildConfig.DEBUG) Log.e("------------", "???????????????"+"??????"+cacheCmd+"cmd"+BlueLinkControl.preControlCmd);
                                BlueLinkControl.getInstance().sendMessage(cacheCmd, false);
                            }
                        });
                    } else {
                         if (BuildConfig.DEBUG) Log.e("------------", "???????????????"+"??????"+cacheCmd);
                         if (BuildConfig.DEBUG) Log.e("------------", "???????????????"+"??????"+cacheCmd+"cmd"+BlueLinkControl.preControlCmd);
                        BlueLinkControl.getInstance().sendMessage(cacheCmd, false);
                    }
                } else {
                     if (BuildConfig.DEBUG) Log.e("------------", "???????????????");
                    BlueLinkReciverForSocket.getInstance().sendSocket(carInfo.ide,carInfo.bluetoothName,carInfo.carsig,carInfo.isBindBluetooth,cmd,time,userId);
//                    OCtrlSocketMsgBackground.getInstance().ccmd_controlCar(carInfo,cmd,time,mContext);
                }
            }
        }
    }
    private boolean getForground(){
        if(KulalaServiceAThis==null){
            return true;
        }else {
            return KulalaServiceAThis.isAppForeground("com.client.proj.kusida");
        }
    }

    private void getSystemTime(DataReceive data) {
        if (data.dataType == 0x45) {
            if (data.data[0] == 0x01) {
                MyLcdBlueAdapterBack.getInstance().sendMessage(getCalenda());
            }
        }
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
        byte day = (byte) calendar.get(Calendar.DAY_OF_MONTH);
//??????????????????
//??????
        byte hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
//??????
        byte minute = (byte) calendar.get(Calendar.MINUTE);
//???
        byte second = (byte) calendar.get(Calendar.SECOND);
        byte week = (byte) calendar.get(Calendar.DAY_OF_WEEK);
        byte[] times = new byte[10];
        times[0] = 0x04;
        times[1] = 7;
        times[2] = myYear;
        times[3] = month;
        times[4] = day;
        times[5] = hour;
        times[6] = minute;
        times[7] = second;
        times[8] = week;
        times[9] = 0;
        for (int i = 0; i < 9; i++) {
            times[9] += times[i];
        }
        times[9] ^= 0xff;
        return times;
    }
}
