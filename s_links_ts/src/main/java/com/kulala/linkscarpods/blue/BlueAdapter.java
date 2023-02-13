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
 * <uses-feature android:name="android.hardware.bluetooth_le" android:required="false"/>PackageManager.hasSystemFeature()方法来判断设备是否支持BLE
 * 1.断线重连，不能直接连，要断成功后，再进行扫描
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
     * 加入蓝牙事件接收器
     * ,需要重连前，才清除，否则留着看距离
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
    //====================启动过程========================
    public void init(Context context) {
        this.mContext = context;
        //1首先获取BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2获取BluetoothAdapter
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
        //1首先获取BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2获取BluetoothAdapter
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
        //1首先获取BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //2获取BluetoothAdapter
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
    //=======================扫描过程=====================
    public void gotoConnDeviceAddress(String deviceAddress) {
            if (bluetoothManager == null) bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null || !bluetoothAdapter.isEnabled()) {
                Log.i("主机蓝牙", "---连接---没有蓝牙服务");
                return;
            }
            if (usedDevice != null && usedDevice.getName() != null&&usedDevice.getAddress()!=null && usedDevice.getAddress().equals(deviceAddress)) {
                if(bluetoothAdapter.isEnabled()) {
                    Log.i("主机蓝牙", "---连接---是旧设备");
                    gotoConnDevice(usedDevice);
                }else{
                    Log.i("主机蓝牙", "---连接---蓝牙没开");
//                closeBlueReal();
                }
            } else {
                Log.i("主机蓝牙", "---连接---是新设备");
                gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
            }
    }
    //1.bluetoothGatt重连每次new出来
    //2.close后要有间隔重启
    //3.先disconnect,再close
    private long preConnTime = 0;
    private void gotoConnDevice(final BluetoothDevice device) {
        if (device == null || device.getAddress() == null || !bluetoothAdapter.isEnabled()) return;
        long now = System.currentTimeMillis();
         if (BuildConfig.DEBUG) Log.e("主机蓝牙", "now"+now+"preConnTime"+preConnTime+ "now - preConnTime "+(now - preConnTime ));
        if (now - preConnTime < 1000L) return;//1秒内重连了
        preConnTime = now;
        //test
         if (BuildConfig.DEBUG) Log.e("主机蓝牙", "current_blue_state"+current_blue_state+ "STATE_CONNECTING "+STATE_CONNECTING);
        if (current_blue_state <STATE_CONNECTING) {//!isConnected
            //重连前，清除缓存
            if (writeCharacteristic != null) writeCharacteristic = null;
//            handleCloseOne();
            long currentTime=System.currentTimeMillis();
            if (BuildConfig.DEBUG) Log.e("主机蓝牙", "currentTime-disConnectTime"+(currentTime-disConnectTime));
            if((currentTime-disConnectTime)<1500)return;
//            if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
            isConnected = false;
//            bluetoothGatt = null;
            current_blue_state = STATE_CONNECTING;
             if (BuildConfig.DEBUG) Log.e("主机蓝牙","---重新连接蓝牙---：" + device.getName());
            usedDevice = device;
             if (BuildConfig.DEBUG) Log.e("主机蓝牙","---onBlueStateListener---：" + onBlueStateListener);
            if (onBlueStateListener != null) {
                onBlueStateListener.onConnecting();
            }

            TimerTask task = new TimerTask() {
                public void run() {
                    connectGatt(device);
//                    if (BuildConfig.DEBUG) Log.e("主机蓝牙","current_blue_state" +current_blue_state+"STATE_CONNECT_OK"+ STATE_CONNECT_OK+"isConnected"+isConnected);
//                    if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
//                    //test
//                    if (BuildConfig.DEBUG) Log.e("主机蓝牙","mContext" +mContext);
//                    if(device!=null && mGattCallback!=null && mContext!=null){
//                        if (BuildConfig.DEBUG) Log.e("主机蓝牙","開始連走回調" );
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (BuildConfig.DEBUG) Log.e("主机蓝牙","device"+device);
//                            bluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE); //
//                            if (BuildConfig.DEBUG) Log.e("主机蓝牙","bluetoothGatt"+bluetoothGatt);
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
             if (BuildConfig.DEBUG) Log.e("主机蓝牙","---重新连接蓝牙,但已经执行重连---：" + device.getName());
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
                if (BuildConfig.DEBUG) Log.e("主机蓝牙","current_blue_state" +current_blue_state+"STATE_CONNECT_OK"+ STATE_CONNECT_OK+"isConnected"+isConnected);
                if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
                //test
                if (BuildConfig.DEBUG) Log.e("主机蓝牙","mContext" +mContext);
                if(device!=null && mGattCallback!=null && mContext!=null){
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙","開始連走回調" );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (BuildConfig.DEBUG) Log.e("主机蓝牙","device"+device);
                        bluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE); //
                        if (BuildConfig.DEBUG) Log.e("主机蓝牙-mygatt","鏈接的bluetoothGatt"+bluetoothGatt);
                    } else {
                        bluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
                    }
                }
//                    bluetoothGatt = usedDevice.connectGatt(mContext, false, mGattCallback);
            }else if(msg.what==2){
                //完全關掉鏈接
                if (BuildConfig.DEBUG) Log.e("主机---------", " bluetoothGatt="+bluetoothGatt);
                if (bluetoothGatt != null)bluetoothGatt.disconnect();
                current_blue_state = STATE_CLOSEED;
                if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
                usedDevice = null;
            }else if(msg.what==3){
                if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
            }else if(msg.what==4){
                        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            current_blue_state = STATE_CLOSEED;
//            if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
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
    //==========================启动扫描==========================

    @SuppressLint("NewApi")
    private final  BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            connectStatus=status;
             if (BuildConfig.DEBUG) Log.e("主机蓝牙","蓝牙连接状态值 status:" + status + " newState:" + newState);
            if (gatt == null) return;
                if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功,status应该是0
                    BlueLinkControl.preDataBackTime = System.currentTimeMillis();//应该从这里开始计时
                    isConnected = true;
                     if (BuildConfig.DEBUG) Log.e("主机蓝牙", "current_blue_state"+current_blue_state+"STATE_CONNECT_OK"+STATE_CONNECT_OK);
                    if (current_blue_state >= STATE_CONNECT_OK) return;//有多次返回，判定
                     if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------STATE_CONNECTED-----");
                    current_blue_state = STATE_CONNECT_OK;
                      if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------ready goto discoverServices-----");
                    if (onBlueStateListener != null) onBlueStateListener.onConnectedOK();
//                bluetoothGatt.discoverServices();//必须UI线程
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                     if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------STATE_DISCONNECTED && onConnectedFailed-----");
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
                        onBlueStateListener.onConnectedFailed("蓝牙断开连接", false);
                }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                if (BuildConfig.DEBUG) Log.e("主机蓝牙-mygatt","回調的的bluetoothGatt"+gatt);
                 if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------已找到服务列表-----bluetoothGatt != null:" + (gatt != null));
                //gatt
                 if (bluetoothGatt != null) {
                    //gatt
                    BluetoothGattService serviceSS = bluetoothGatt.getService(UUID_WRITE_SERVICE.getUuid());
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "BluetoothGattService---------------------------------------" + serviceSS);
                    if (serviceSS != null) {
                        String error = displayGattServices(serviceSS);
                        if(error !=null){
                            current_blue_state = STATE_DISCOVER_FAILED;
                            if (onBlueStateListener != null)onBlueStateListener.onDiscoverFailed("Service无对应通道", true);
                        }
                    } else {
                        current_blue_state = STATE_DISCOVER_FAILED;
                        if (onBlueStateListener != null)
                            onBlueStateListener.onDiscoverFailed("未找到服务列表", true);
                    }
                }
            } else {
//                gatt.close();
//                if (bluetoothGatt != null){
//                    bluetoothGatt.disconnect();
//                    bluetoothGatt.close();
//                    bluetoothGatt=null;
//                }
                 if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------未找到服务列表-----");
                current_blue_state = STATE_DISCOVER_FAILED;
                if (onBlueStateListener != null)
                    onBlueStateListener.onDiscoverFailed("未找到服务列表", true);
            }
        }

        @Override  //读取收到消息
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] bytes = characteristic.getValue();
//            System.out.println("--------收到消息 onCharacteristicRead： " + bytesToHexString(characteristic.getValue()));
             if (BuildConfig.DEBUG) Log.e("主机蓝牙", "--------收到消息 onCharacteristicRead： " + bytesToHexString(characteristic.getValue()));
        }

        @Override //通知收到消息
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            current_blue_state = STATE_MSG_RECEIVED;
             if (BuildConfig.DEBUG) Log.e("加密环节", bytesToHexString(characteristic.getValue())+"主机收到消息");
            findMege(bytes);
        }

        @Override //通知发出消息描述
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            final byte[] result = descriptor.getValue();
             if (BuildConfig.DEBUG) Log.e("主机蓝牙", "描述发出消息成功: onCharacteristicWrite:" + bytesToHexString(result));
//            current_blue_state = STATE_MSG_SENDED;
//            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override //通知发出消息实体
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (BuildConfig.DEBUG) Log.e("主机蓝牙-mygatt","fa的bluetoothGatt"+gatt);
            final byte[] result = characteristic.getValue();
             if (BuildConfig.DEBUG) Log.e("主机蓝牙", "通道发出消息成功: onCharacteristicWrite:" + bytesToHexString(result));
            current_blue_state = STATE_MSG_SENDED;
            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            //rssi返回的是符数
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
            boolean discoverok = bluetoothGatt.discoverServices();//必须UI线程
            if (!discoverok && onBlueStateListener != null) {
                current_blue_state = STATE_DISCOVER_FAILED;
                onBlueStateListener.onDiscoverFailed("discover失败", false);
                return false;
            }
        } else if (!isConnected) {
            current_blue_state = STATE_CONNECT_FAILED;
            if (onBlueStateListener != null)
                onBlueStateListener.onConnectedFailed("扫通道时无连接", false);
            return false;
        } else {
            current_blue_state = STATE_DISCOVER_FAILED;
            if (onBlueStateListener != null) onBlueStateListener.onDiscoverFailed("Gatt不存在", true);
            return false;
        }
        return true;
    }
    /**
     * 7.解析所有服务
     *
     * @return error
     */
    @SuppressLint("NewApi")
    private String displayGattServices(BluetoothGattService gattServices) {
        if (BuildConfig.DEBUG) Log.e("主机蓝牙-mygatt","bluetoothGatt"+bluetoothGatt);
        if (gattServices == null) return "Service列表无数据";
        BluetoothGattCharacteristic gattCharacteristic = gattServices.getCharacteristic(UUID_NOTIFY);
        if (gattCharacteristic == null) return "通知通到获取失败";
        boolean isEnableNotification = bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
         if (BuildConfig.DEBUG) Log.e("主机蓝牙", "通知通道设置:" + isEnableNotification + " " + UUID_NOTIFY);
        if (isEnableNotification) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_NOTIFY_DES);
            if (descriptor != null) {
                boolean descripOK = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);
                 if (BuildConfig.DEBUG) Log.e("主机蓝牙", "通知通道描述设置:" + descripOK + " " + UUID_NOTIFY_DES);
            }
        } else {
            return "通知通到设置开启失败";
        }
        BluetoothGattCharacteristic characteristicWrite = gattServices.getCharacteristic(UUID_WRITE_CHARACTERISTIC);
        if (characteristicWrite != null) {
            writeCharacteristic = characteristicWrite;
             if (BuildConfig.DEBUG) Log.e("主机蓝牙", "写入通道选用:" + writeCharacteristic.getUuid());
            current_blue_state = STATE_DISCOVER_OK;
            if (onBlueStateListener != null) onBlueStateListener.onDiscoverOK();
        } else {
            return "写入通到选取失败";
        }
        return null;
    }

    public void openBlueTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) bluetoothAdapter.enable();
    }

    //主界面销毁后,退出登录后关闭蓝牙
//    public void closeBlueTooth(String fromInfo) {
//        Log.i(">>>Blue>>>", "closeBlueTooth:" + fromInfo);
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter != null) {
//            current_blue_state = STATE_CLOSEING;
//            bluetoothAdapter.disable();
//        }
//    }
    //完全关掉

    public void closeBlueReal() {
//        handleCloseBlueReal();
        if (BuildConfig.DEBUG) Log.e("主机---------", " bluetoothGatt="+bluetoothGatt);
        if (bluetoothGatt != null)bluetoothGatt.disconnect();
        current_blue_state = STATE_CLOSEED;
        if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
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
//            if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
        }
    }
    /**
     * 外面延时判定是否正常关闭 STATE_CLOSEING
     */
    public void closeBlueConnCheck(int currentState) {
//        if (current_blue_state == currentState && currentState == STATE_CLOSEING) {
//            TimerTask task = new TimerTask() {
//                public void run() {
//                    if (current_blue_state != STATE_CLOSEING) return;
//                    if (writeCharacteristic != null) writeCharacteristic = null;
//                    if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
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
     * 发送数据 //AA 02 55 0A F4 确认连接用唤醒指令
     */
    @SuppressLint("NewApi")
    public boolean sendMessage(String hexStr) {
        Log.i("主机蓝牙", " 发送消息");
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("主机蓝牙", " 发包蓝牙失败,未连接");
            return false;
        }
        Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt);
        if(bluetoothGatt!=null){
            Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt.connect());
        }
        Log.e(">>>Blue>>>", "bluetoothGatt" +bluetoothGatt);
        if (BuildConfig.DEBUG) Log.e("主机蓝牙-mygatt","發包的bluetoothGatt"+bluetoothGatt);
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
            Log.i(">>>Blue>>>", " 发包蓝牙：" + hexStr);
            writeCharacteristic.setValue(ConvertHexByte.hexStringToBytes(hexStr));
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }
    public boolean sendMessage( byte[] data) {
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("主机蓝牙", " 发包蓝牙失败,未连接");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
//            Log.i("主机蓝牙", " 发包蓝牙：" + hexStr);
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
     * 收data[1]  2108006A0000000000006C2208F8330100000000
     * 收data[1]  00A92308000040B518000000C7
     */
    private byte[] cachecheck;
    public static long countMegeTime = 0;
    private void findMege(byte[] megebyte) {
        Log.e("uuuuuuu","初始值"+Arrays.toString(megebyte));
        if (onBlueStateListener != null) onBlueStateListener.onDataBack();
        cachecheck = ConvertHexByte.bytesMege(cachecheck, megebyte);
        if (cachecheck.length > 200){
            cachecheck = null;//多出太多数据
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
        //1.检查校验合适
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
            findNext(ConvertHexByte.bytesCut(megebyte, 1));//切掉1byte;
            return;
        }
        if (megebyte.length < data.length + 3){
            cachecheck = megebyte;
            return;//数据未完成，等待下行 Type+length+data+check
        }
        //有够长数据,先把数据读入data
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
                    Log.e("uuuuuuu","data。data"+Arrays.toString(data.data)+"data.type"+data.dataType);
                    if (onBlueStateListener != null) onBlueStateListener.onDataReceived(data);
//             if (BuildConfig.DEBUG) Log.e(">>>Blue>>>", "--------收到消息 字段： "+data.dataType+" : " + bytesToHexString(data.data));//33:21
                }
            }).start();
        }
        byte[] uncheck = ConvertHexByte.bytesCut(megebyte, data.length + 3);//切掉已复制
        findNext(uncheck);
    }
}
