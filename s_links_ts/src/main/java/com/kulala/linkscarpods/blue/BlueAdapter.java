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
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import androidx.annotation.NonNull;
import android.util.Log;

import com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll;
import com.kulala.linkspods.BuildConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;
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
public class BlueAdapter {
    private static final ParcelUuid UUID_WRITE_SERVICE        = ParcelUuid.fromString("00001000-0000-1000-8000-00805f9b34fb");
    private static final UUID       UUID_WRITE_CHARACTERISTIC = UUID.fromString("00001001-0000-1000-8000-00805f9b34fb");
    private static final UUID       UUID_NOTIFY               = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");
    private static final UUID       UUID_NOTIFY_DES           = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private Context                     mContext;
    private BluetoothManager            bluetoothManager;
    private BluetoothAdapter            bluetoothAdapter;
    private BluetoothGatt               bluetoothGatt;
    private BluetoothGattCharacteristic writeCharacteristic;

    private BluetoothDevice usedDevice;

    private boolean isConnected = false;
    private int connectStatus;
    private long disConnectTime;

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
    public static BlueAdapter _instance;
    protected BlueAdapter() {
    }
    public static BlueAdapter getInstance() {
        if (_instance == null)
            _instance = new BlueAdapter();
        return _instance;
    }
    //====================????????????========================
    public void init(Context context) {
        this.mContext = context;
        //1????????????BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2??????BluetoothAdapter
        if (bluetoothAdapter == null) {
            if (Build.VERSION.SDK_INT >= 19) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            } else {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
        }
    }

    public List<BluetoothDevice> getConnectDevice(Context context) {
        this.mContext = context;
        //1????????????BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2??????BluetoothAdapter
        if (bluetoothAdapter == null) {
            if (Build.VERSION.SDK_INT >= 19) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            } else {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
        }
        if (bluetoothAdapter.isEnabled()) {
            List<BluetoothDevice> list = bluetoothManager.getConnectedDevices(7);
            return list;
        }
        return null;
    }
    public  Set<BluetoothDevice> getBondedDevice(Context context) {
        this.mContext = context;
        //1????????????BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2??????BluetoothAdapter
        if (bluetoothAdapter == null) {
            if (Build.VERSION.SDK_INT >= 19) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            } else {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
        }
        if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            return devices;
        }
        return null;
    }
    //=======================????????????=====================
    public void gotoConnDeviceAddress(String deviceAddress) {
            if (bluetoothManager == null) bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null || !bluetoothAdapter.isEnabled()) {
                Log.i("????????????", "---??????---??????????????????");
                return;
            }
            if (usedDevice != null && usedDevice.getName() != null&&usedDevice.getAddress()!=null && usedDevice.getAddress().equals(deviceAddress)) {
                if(bluetoothAdapter.isEnabled()) {
                    Log.i("????????????", "---??????---????????????");
                    gotoConnDevice(usedDevice);
                }else{
                    Log.i("????????????", "---??????---????????????");
//                closeBlueReal();
                }
            } else {
                Log.i("????????????", "---??????---????????????");
                gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
            }
    }
    //1.bluetoothGatt????????????new??????
    //2.close?????????????????????
    //3.???disconnect,???close
    private long preConnTime = 0;
    private void gotoConnDevice(final BluetoothDevice device) {
        if (device == null || device.getAddress() == null || !bluetoothAdapter.isEnabled()) return;
        long now = System.currentTimeMillis();
         if (BuildConfig.DEBUG) Log.e("????????????", "now"+now+"preConnTime"+preConnTime+ "now - preConnTime "+(now - preConnTime ));
        if (now - preConnTime < 1000L) return;//1???????????????
        preConnTime = now;
        //test
         if (BuildConfig.DEBUG) Log.e("????????????", "current_blue_state"+current_blue_state+ "STATE_CONNECTING "+STATE_CONNECTING);
        if (current_blue_state <STATE_CONNECTING) {//!isConnected
            //????????????????????????
            if (writeCharacteristic != null) writeCharacteristic = null;
//            handleCloseOne();
            long currentTime=System.currentTimeMillis();
            if (BuildConfig.DEBUG) Log.e("????????????", "currentTime-disConnectTime"+(currentTime-disConnectTime));
            if((currentTime-disConnectTime)<1500)return;
//            if (bluetoothGatt != null) bluetoothGatt.close();//????????????
            isConnected = false;
//            bluetoothGatt = null;
            current_blue_state = STATE_CONNECTING;
             if (BuildConfig.DEBUG) Log.e("????????????","---??????????????????---???" + device.getName());
            usedDevice = device;
             if (BuildConfig.DEBUG) Log.e("????????????","---onBlueStateListener---???" + onBlueStateListener);
            if (onBlueStateListener != null) {
                onBlueStateListener.onConnecting();
            }

            TimerTask task = new TimerTask() {
                public void run() {
                    connectGatt(device);
//                    if (BuildConfig.DEBUG) Log.e("????????????","current_blue_state" +current_blue_state+"STATE_CONNECT_OK"+ STATE_CONNECT_OK+"isConnected"+isConnected);
//                    if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
//                    //test
//                    if (BuildConfig.DEBUG) Log.e("????????????","mContext" +mContext);
//                    if(device!=null && mGattCallback!=null && mContext!=null){
//                        if (BuildConfig.DEBUG) Log.e("????????????","??????????????????" );
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (BuildConfig.DEBUG) Log.e("????????????","device"+device);
//                            bluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE); //
//                            if (BuildConfig.DEBUG) Log.e("????????????","bluetoothGatt"+bluetoothGatt);
//                        } else {
//                            bluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
//                        }
//                    }
////                    bluetoothGatt = usedDevice.connectGatt(mContext, false, mGattCallback);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 500L);
        } else {
             if (BuildConfig.DEBUG) Log.e("????????????","---??????????????????,?????????????????????---???" + device.getName());
        }
    }
    private void handleCloseOne(){
        Message message=Message.obtain();
        message.what=3;
        handler.sendMessage(message);
    }
    private void handleCloseBlueReal(){
        Message message=Message.obtain();
        message.what=2;
        handler.sendMessage(message);
    }
    private void connectGatt(BluetoothDevice device){
        Message message=Message.obtain();
        message.what=1;
        message.obj=device;
        handler.sendMessage(message);
    }
    private void handleCloseWait(){
        Message message=Message.obtain();
        message.what=4;
        handler.sendMessage(message);
    }
    private void handleCloseWhenDisconnect(){
        Message message=Message.obtain();
        message.what=5;
        handler.sendMessage(message);
    }
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                BluetoothDevice device=(BluetoothDevice) msg.obj;
                if (BuildConfig.DEBUG) Log.e("????????????","current_blue_state" +current_blue_state+"STATE_CONNECT_OK"+ STATE_CONNECT_OK+"isConnected"+isConnected);
                if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
                //test
                if (BuildConfig.DEBUG) Log.e("????????????","mContext" +mContext);
                if(device!=null && mGattCallback!=null && mContext!=null){
                    if (BuildConfig.DEBUG) Log.e("????????????","??????????????????" );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (BuildConfig.DEBUG) Log.e("????????????","device"+device);
                        bluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE); //
                        if (BuildConfig.DEBUG) Log.e("????????????-mygatt","?????????bluetoothGatt"+bluetoothGatt);
                    } else {
                        bluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
                    }
                }
//                    bluetoothGatt = usedDevice.connectGatt(mContext, false, mGattCallback);
            }else if(msg.what==2){
                //??????????????????
                if (BuildConfig.DEBUG) Log.e("??????---------", " bluetoothGatt="+bluetoothGatt);
                if (bluetoothGatt != null)bluetoothGatt.disconnect();
                current_blue_state = STATE_CLOSEED;
                if (bluetoothGatt != null) bluetoothGatt.close();//????????????
                usedDevice = null;
            }else if(msg.what==3){
                if (bluetoothGatt != null) bluetoothGatt.close();//????????????
            }else if(msg.what==4){
                        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            current_blue_state = STATE_CLOSEED;
//            if (bluetoothGatt != null) bluetoothGatt.close();//????????????
        }
            }else if(msg.what==5){
                if (writeCharacteristic != null) writeCharacteristic = null;
                if (bluetoothGatt != null){
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    bluetoothGatt=null;
                }
            }
        }
    };
    //==========================????????????==========================

    @SuppressLint("NewApi")
    private final  BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //?????????????????????????????????????????????????????????
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            connectStatus=status;
             if (BuildConfig.DEBUG) Log.e("????????????","????????????????????? status:" + status + " newState:" + newState);
            if (gatt == null) return;
                if (newState == BluetoothProfile.STATE_CONNECTED) { //????????????,status?????????0
                    BlueLinkControl.preDataBackTime = System.currentTimeMillis();//???????????????????????????
                    isConnected = true;
                     if (BuildConfig.DEBUG) Log.e("????????????", "current_blue_state"+current_blue_state+"STATE_CONNECT_OK"+STATE_CONNECT_OK);
                    if (current_blue_state >= STATE_CONNECT_OK) return;//????????????????????????
                     if (BuildConfig.DEBUG) Log.e("????????????", "--------STATE_CONNECTED-----");
                    current_blue_state = STATE_CONNECT_OK;
                      if (BuildConfig.DEBUG) Log.e("????????????", "--------ready goto discoverServices-----");
                    if (onBlueStateListener != null) onBlueStateListener.onConnectedOK();
//                bluetoothGatt.discoverServices();//??????UI??????
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //????????????
                     if (BuildConfig.DEBUG) Log.e("????????????", "--------STATE_DISCONNECTED && onConnectedFailed-----");
                   gatt.close();
//                   handleCloseWhenDisconnect();
                    if (writeCharacteristic != null) writeCharacteristic = null;
                    if (bluetoothGatt != null){
                        bluetoothGatt.disconnect();
                        bluetoothGatt.close();
                        bluetoothGatt=null;
                        disConnectTime=System.currentTimeMillis();
                    }
                    isConnected = false;
                    current_blue_state = STATE_CONNECT_FAILED;
                    if (onBlueStateListener != null)
                        onBlueStateListener.onConnectedFailed("??????????????????", false);
                }
        }

        @Override  //???????????????????????????????????????????????????
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {   //???????????????
                //??????????????????????????????????????????????????????????????????
                if (BuildConfig.DEBUG) Log.e("????????????-mygatt","????????????bluetoothGatt"+gatt);
                 if (BuildConfig.DEBUG) Log.e("????????????", "--------?????????????????????-----bluetoothGatt != null:" + (gatt != null));
                //gatt
                 if (bluetoothGatt != null) {
                    //gatt
                    BluetoothGattService serviceSS = bluetoothGatt.getService(UUID_WRITE_SERVICE.getUuid());
                    if (BuildConfig.DEBUG) Log.e("????????????", "BluetoothGattService---------------------------------------" + serviceSS);
                    if (serviceSS != null) {
                        String error = displayGattServices(serviceSS);
                        if(error !=null){
                            current_blue_state = STATE_DISCOVER_FAILED;
                            if (onBlueStateListener != null)onBlueStateListener.onDiscoverFailed("Service???????????????", true);
                        }
                    } else {
                        current_blue_state = STATE_DISCOVER_FAILED;
                        if (onBlueStateListener != null)
                            onBlueStateListener.onDiscoverFailed("?????????????????????", true);
                    }
                }
            } else {
//                gatt.close();
//                if (bluetoothGatt != null){
//                    bluetoothGatt.disconnect();
//                    bluetoothGatt.close();
//                    bluetoothGatt=null;
//                }
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
             if (BuildConfig.DEBUG) Log.e("????????????", "--------???????????? onCharacteristicRead??? " + bytesToHexString(characteristic.getValue()));
        }

        @Override //??????????????????
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            current_blue_state = STATE_MSG_RECEIVED;
             if (BuildConfig.DEBUG) Log.e("????????????", bytesToHexString(characteristic.getValue())+"??????????????????");
            findMege(bytes);
        }

        @Override //????????????????????????
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            final byte[] result = descriptor.getValue();
             if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????????: onCharacteristicWrite:" + bytesToHexString(result));
//            current_blue_state = STATE_MSG_SENDED;
//            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override //????????????????????????
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (BuildConfig.DEBUG) Log.e("????????????-mygatt","fa???bluetoothGatt"+gatt);
            final byte[] result = characteristic.getValue();
             if (BuildConfig.DEBUG) Log.e("????????????", "????????????????????????: onCharacteristicWrite:" + bytesToHexString(result));
            current_blue_state = STATE_MSG_SENDED;
            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            //rssi??????????????????
            if (onBlueStateListener != null)onBlueStateListener.onReadRemoteRssi(Math.abs(rssi),status);
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
                onBlueStateListener.onDiscoverFailed("discover??????", false);
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
        if (BuildConfig.DEBUG) Log.e("????????????-mygatt","bluetoothGatt"+bluetoothGatt);
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
//        handleCloseBlueReal();
        if (BuildConfig.DEBUG) Log.e("??????---------", " bluetoothGatt="+bluetoothGatt);
        if (bluetoothGatt != null)bluetoothGatt.disconnect();
        current_blue_state = STATE_CLOSEED;
        if (bluetoothGatt != null) bluetoothGatt.close();//????????????
        bluetoothGatt=null;
        usedDevice = null;
        disConnectTime=System.currentTimeMillis();
    }
    @SuppressLint("NewApi")
    public void closeBlueWait() {
//        handleCloseWait();
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
        Log.i("????????????", " ????????????");
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("????????????", " ??????????????????,?????????");
            return false;
        }
        Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt);
        if(bluetoothGatt!=null){
            Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt.connect());
        }
        Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt);
        if (BuildConfig.DEBUG) Log.e("????????????-mygatt","?????????bluetoothGatt"+bluetoothGatt);
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
            Log.i(">>>Blue>>>", " ???????????????" + hexStr);
            writeCharacteristic.setValue(ConvertHexByte.hexStringToBytes(hexStr));
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }
    public boolean sendMessage( byte[] data) {
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("????????????", " ??????????????????,?????????");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
//            Log.i("????????????", " ???????????????" + hexStr);
            writeCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }
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
        Log.e("uuuuuuu","?????????"+Arrays.toString(megebyte));
        if (onBlueStateListener != null) onBlueStateListener.onDataBack();
        cachecheck = ConvertHexByte.bytesMege(cachecheck, megebyte);
        if (cachecheck.length > 200){
            cachecheck = null;//??????????????????
            return;
        }
        long now = System.currentTimeMillis();
//        if (now - countMegeTime >= 50L) {
            countMegeTime = now;
//             if (BuildConfig.DEBUG) Log.e("blue", "2108 cachecheck:"+ConvertHexByte.bytesToHexString(cachecheck));
            Log.e("uuuuuuu","cachecheck"+Arrays.toString(cachecheck));
            findNext(cachecheck);
            cachecheck = null;
//        }
    }
    public void findNext(final byte[] megebyte) {
        //1.??????????????????
        final DataReceive data = new DataReceive();
        if (megebyte == null || megebyte.length<4) {
            cachecheck = megebyte;
            return;
        }
        //1 DataType
        data.dataType = megebyte[0];
        //2 Length
        data.length = megebyte[1];
        //3 data
        if (data.length == 0 || data.length > 32 || data.length < 0) {
            findNext(ConvertHexByte.bytesCut(megebyte, 1));//??????1byte;
            return;
        }
        if (megebyte.length < data.length + 3){
            cachecheck = megebyte;
            return;//?????????????????????????????? Type+length+data+check
        }
        //???????????????,??????????????????data
        data.data = new byte[data.length];
        byte[] newMergeByte=new byte[megebyte.length];
        for (int i=0;i<megebyte.length;i++){
            newMergeByte[i]+= megebyte[i]&0xff;
        }
        System.arraycopy(newMergeByte, 2, data.data, 0, data.length);
        //4.check
        data.check = megebyte[data.length + 2];
        if (data.matchCheck()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("uuuuuuu","data???data"+Arrays.toString(data.data)+"data.type"+data.dataType);
                    if (onBlueStateListener != null) onBlueStateListener.onDataReceived(data);
//             if (BuildConfig.DEBUG) Log.e(">>>Blue>>>", "--------???????????? ????????? "+data.dataType+" : " + bytesToHexString(data.data));//33:21
                }
            }).start();
        }
        byte[] uncheck = ConvertHexByte.bytesCut(megebyte, data.length + 3);//???????????????
        findNext(uncheck);
    }
}
