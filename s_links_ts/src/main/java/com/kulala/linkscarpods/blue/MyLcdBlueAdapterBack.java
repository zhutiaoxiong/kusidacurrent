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
 * <uses-feature android:name="android.hardware.bluetooth_le" android:required="false"/>PackageManager.hasSystemFeature()方法来判断设备是否支持BLE
 * 1.断线重连，不能直接连，要断成功后，再进行扫描
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
    private static MyLcdBlueAdapterBack _instance;

    protected MyLcdBlueAdapterBack() {
    }

    public static MyLcdBlueAdapterBack getInstance() {
        if (_instance == null)
            _instance = new MyLcdBlueAdapterBack();
        return _instance;
    }

    //====================启动过程========================
    public boolean initializeOK(Context context) {
        if (context == null) return false;
        this.mContext = context;
        //1首先获取BluetoothManager
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) return false;
        //2获取BluetoothAdapter
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

    //=======================扫描过程=====================
    public  void gotoConnDeviceAddress(String deviceAddress) {
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null || !bluetoothAdapter.isEnabled()) {
            Log.i("液晶钥匙", "---连接---没有蓝牙服务");
            return;
        }
        if(connectState==133){
            gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
        }
        if (usedDevice != null && usedDevice.getName() != null && usedDevice.getAddress().equals(deviceAddress)) {
            if (bluetoothAdapter.isEnabled()) {
                Log.i("液晶钥匙", "---连接---是旧设备");
                gotoConnDevice(usedDevice);
            } else {
                Log.i("液晶钥匙", "---连接---蓝牙没开");
            }
        } else {
            Log.i("液晶钥匙", "---连接---是新设备" + deviceAddress);
            gotoConnDevice(bluetoothAdapter.getRemoteDevice(deviceAddress));
        }
    }

    //1.bluetoothGatt重连每次new出来
    //2.close后要有间隔重启
    //3.先disconnect,再close
    private long preConnTime = 0;

    public  void gotoConnDevice(final BluetoothDevice device) {
        if (device == null || device.getAddress() == null || !bluetoothAdapter.isEnabled()) return;
        long now = System.currentTimeMillis();
        if (now - preConnTime < 1000L) return;//1秒内重连了
        preConnTime = now;
        //test
        if (current_blue_state <STATE_CONNECTING) {//!isConnected
            //重连前，清除缓存
            if (writeCharacteristic != null) writeCharacteristic = null;
            if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
            isConnected = false;
            bluetoothGatt = null;
            current_blue_state = STATE_CONNECTING;

            usedDevice = device;
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "---准备执行重连---：" + device + device.getName());
            if (onBlueStateListener != null) onBlueStateListener.onConnecting();
            TimerTask task = new TimerTask() {
                public  void run() {
                    if (current_blue_state >= STATE_CONNECT_OK || isConnected) return;
                    //test
                    if ( mContext != null&&device!=null&&mGattCallback!=null){
                         if (BuildConfig.DEBUG) Log.e("液晶钥匙", "---开始执行重连---：" + device + device.getName());

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
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "---重新连接蓝牙,但已经执行重连---：" + device + device.getName());
        }
    }
    //==========================启动扫描==========================

    @SuppressLint("NewApi")
    private final  BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            connectState=status;
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "蓝牙连接状态值 status:" + status + " newState:" + newState);
            if (gatt == null) return;

                if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功,status应该是0
                    BlueLinkControlLcdKey.preDataBackTime = System.currentTimeMillis();//应该从这里开始计时
                    isConnected = true;
                    if (current_blue_state >= STATE_CONNECT_OK) return;//有多次返回，判定
                     if (BuildConfig.DEBUG) Log.e("液晶钥匙", "--------STATE_CONNECTED-----");
                    current_blue_state = STATE_CONNECT_OK;
                    if (onBlueStateListener != null) onBlueStateListener.onConnectedOK();
//                bluetoothGatt.discoverServices();//必须UI线程
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                    gatt.close();
                     if (BuildConfig.DEBUG) Log.e("液晶钥匙", "--------STATE_DISCONNECTED && onConnectedFailed-----");
                    if (writeCharacteristic != null) writeCharacteristic = null;
                    if (bluetoothGatt != null){
                        bluetoothGatt.disconnect();
                        bluetoothGatt.close();//每次重连
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
                        onBlueStateListener.onConnectedFailed("蓝牙断开连接", false);
                }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                 if (BuildConfig.DEBUG) Log.e("液晶钥匙", "--------已找到服务列表-----bluetoothGatt != null:" + (gatt != null));
                if (gatt != null) {
                    BluetoothGattService serviceSS = gatt.getService(UUID_WRITE_SERVICE.getUuid());
                    if (serviceSS != null) {
                        String error = displayGattServices(serviceSS);
                        if (error != null) {
                            current_blue_state = STATE_DISCOVER_FAILED;
                            if (onBlueStateListener != null)
                                onBlueStateListener.onDiscoverFailed("Service无对应通道", true);
                        }
                    } else {
                        current_blue_state = STATE_DISCOVER_FAILED;
                        if (onBlueStateListener != null)
                            onBlueStateListener.onDiscoverFailed("未找到服务列表", true);
                    }
                }
            } else {
                 if (BuildConfig.DEBUG) Log.e("液晶钥匙", "--------未找到服务列表-----");
                current_blue_state = STATE_DISCOVER_FAILED;
                if (onBlueStateListener != null)
                    onBlueStateListener.onDiscoverFailed("未找到服务列表", true);
            }
        }

        @Override  //读取收到消息
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] bytes = characteristic.getValue();

//            System.out.println("--------收到消息 onCharacteristicRead： " + bytesToHexString(characteristic.getValue()));
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "--------收到消息 onCharacteristicRead： " + ByteHelper.bytesToHexString(characteristic.getValue()));
        }

        @Override //通知收到消息
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            current_blue_state = STATE_MSG_RECEIVED;
            if (BuildConfig.DEBUG)
                 if (BuildConfig.DEBUG) Log.e("液晶钥匙", ByteHelper.bytesToHexString(characteristic.getValue()) + "液晶钥匙收到消息");
            findMege(bytes);
        }

        @Override //通知发出消息描述
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            final byte[] result = descriptor.getValue();
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "描述发出消息成功: onCharacteristicWrite:" + ByteHelper.bytesToHexString(result));
//            current_blue_state = STATE_MSG_SENDED;
        }

        @Override //通知发出消息实体
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            final byte[] result = characteristic.getValue();
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "通道发出消息成功: onCharacteristicWrite:" + ByteHelper.bytesToHexString(result) + "液晶钥匙发送消息");
            current_blue_state = STATE_MSG_SENDED;
            if (onBlueStateListener != null) onBlueStateListener.onMessageSended(result);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            //rssi返回的是符数
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
            boolean discoverok = bluetoothGatt.discoverServices();//必须UI线程
            if (!discoverok && onBlueStateListener != null) {
                current_blue_state = STATE_DISCOVER_FAILED;
                onBlueStateListener.onDiscoverFailed("discover失败"
                        , false);
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
        if (gattServices == null) return "Service列表无数据";
        BluetoothGattCharacteristic gattCharacteristic = gattServices.getCharacteristic(UUID_NOTIFY);
        if (gattCharacteristic == null) return "通知通到获取失败";
        boolean isEnableNotification = bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
         if (BuildConfig.DEBUG) Log.e("液晶钥匙", "通知通道设置:" + isEnableNotification + " " + UUID_NOTIFY);
        if (isEnableNotification) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_NOTIFY_DES);
            if (descriptor != null) {
                boolean descripOK = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);
                 if (BuildConfig.DEBUG) Log.e("液晶钥匙", "通知通道描述设置:" + descripOK + " " + UUID_NOTIFY_DES);
            }
//            for(BluetoothGattDescriptor dp:gattCharacteristic.getDescriptors()){
//                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                bluetoothGatt.writeDescriptor(dp);
//            }
        } else {
            return "通知通到设置开启失败";
        }
        BluetoothGattCharacteristic characteristicWrite = gattServices.getCharacteristic(UUID_WRITE_CHARACTERISTIC);
        if (characteristicWrite != null) {
            writeCharacteristic = characteristicWrite;
             if (BuildConfig.DEBUG) Log.e("液晶钥匙", "写入通道选用:" + writeCharacteristic.getUuid());
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
        if (bluetoothGatt != null) bluetoothGatt.disconnect();
        current_blue_state = STATE_CLOSEED;
        if (bluetoothGatt != null) bluetoothGatt.close();//每次重连
        usedDevice = null;
        isConnected = false;
    }

    @SuppressLint("NewApi")
    public void closeBlueWait() {
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
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("液晶钥匙", " 发包蓝牙失败,未连接");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
            Log.i("液晶钥匙", " 发给液晶钥匙hexstr" + hexStr);
            writeCharacteristic.setValue(ByteHelper.hexStringToBytes(hexStr));
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }

    public boolean sendMessage(byte[] data) {
        if (current_blue_state < STATE_DISCOVER_OK) {
            Log.i("液晶钥匙", " 发包蓝牙失败,未连接");
            return false;
        }
        if (bluetoothGatt != null && bluetoothGatt.connect() && writeCharacteristic != null) {
//            Log.i(">>>Blue>>>", " 发包蓝牙：" + hexStr);
            writeCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        } else {
            return false;
        }
    }
    /**
     * 发送文件
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
     * 收data[1]  2108006A0000000000006C2208F8330100000000
     * 收data[1]  00A92308000040B518000000C7
     */
    private byte[] cachecheck;
    public static long countMegeTime = 0;

    private void findMege(byte[] megebyte) {
        if (onBlueStateListener != null) onBlueStateListener.onDataBack();
        cachecheck = ByteHelper.bytesMege(cachecheck, megebyte);
        if (cachecheck.length > 200) {
            cachecheck = null;//多出太多数据
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
        //1.检查校验合适
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
            findNext(ByteHelper.bytesCut(megebyte, 1));//切掉1byte;
            return;
        }
        if (megebyte.length < data.length + 3) {
            cachecheck = megebyte;
            return;//数据未完成，等待下行 Type+length+data+check
        }
        //有够长数据,先把数据读入data
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
//                    //介入控制命令
//                            cmdControlCommand(data);
//                            //获取系统时间
//                            getSystemTime(data);
//                            sendCarStatus(data);
//
////             if (BuildConfig.DEBUG) Log.e(">>>Blue>>>", "--------收到消息 字段： "+data.dataType+" : " + bytesToHexString(data.data));//33:21
//                }
//            }).start();

            ThreadManagerLink.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (onBlueStateListener != null) {
                        onBlueStateListener.onDataReceived(data);
                    }
                    BlueLinkControlLcdKey.preDataBackTime = System.currentTimeMillis();
                    //介入控制命令
                    cmdControlCommand(data);
                    //获取系统时间
                    getSystemTime(data);
                    sendCarStatus(data);
                }
            });
        }
        byte[] uncheck = ByteHelper.bytesCut(megebyte, data.length + 3);//切掉已复制
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
//                     if (BuildConfig.DEBUG) Log.e("发送状态给液晶钥匙", "sendCarStatus:"+DataCarStatus.toJsonObject(currentStatus) .toString());
                    if(currentStatus==null)return;
                    new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
                        @Override
                        public void onTimeEnd() {
                            BlueLinkControlLcdKey.getInstance().sendMessage(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(currentStatus)),true);
                        }
                    });
                }
                //发送状态给液晶钥匙
            }
        }
    }

    private void sendCarStatusCopy() {
        //发送车辆状态
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
             if (BuildConfig.DEBUG) Log.e("MyLcdBlueAdapterBack", "发送指令给前台的reciver");
            //如果连着蓝牙就发蓝牙消息
//                DataCarBlueLcd lcd=DataCarBlueLcd.loadLocal(mContext);
//                long carId=lcd.carId;
//                byte[] cmdByte = data.data;
//                int length = data.length;
//                int cmd=0;
//                if( cmdByte[0]==0x01){
//                    cmd=4;//开锁
//                }else  if( cmdByte[0]==0x02){
//                    cmd=3;//关索
//                }else  if( cmdByte[0]==0x03){
//                    cmd=5;//尾箱
//                }else  if( cmdByte[0]==0x04){
//                    cmd=1;//启动
//                }else  if( cmdByte[0]==0x05){
//                    cmd=6;//寻车
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
//                    cmd=4;//开锁
//                }else  if( cmdByte[0]==0x02){
//                    cmd=3;//关索
//                }else  if( cmdByte[0]==0x03){
//                    cmd=5;//尾箱
//                }else  if( cmdByte[0]==0x04){
//                    cmd=1;//启动
//                }else  if( cmdByte[0]==0x05){
//                    cmd=6;//寻车
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
//             if (BuildConfig.DEBUG) Log.e("查看动向", "cmdControlCommand: ");
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
//                蓝牙模式直接发指令
//                if(carInfo.isBindBluetooth==1&&BlueAdapter.current_blue_state>=STATE_DISCOVER_OK){
//                     if (BuildConfig.DEBUG) Log.e("查看动向", "通过蓝牙去发" );
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
////                    //这里用网络去发
             if (BuildConfig.DEBUG) Log.e("查看动向", "通过socket去发");
//                    BlueLcdNet.getInstance().ccmd1233_controlCar(carInfo, cmd, time);
//                    OCtrlSocketMsgBackground.getInstance().ccmd_controlCar(carInfo,cmd,time,mContext);

//           }
//             if (BuildConfig.DEBUG) Log.e("前后台", "KulalaServiceA.isInForground" +KulalaServiceA.isInForground);
//          boolean  isInForground= KulalaServiceA.isInForground;
//             if (BuildConfig.DEBUG) Log.e("------------", "在前台" );
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
                 if (BuildConfig.DEBUG) Log.e("------------", "在前台" );
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
                    cmd = 4;//开锁
                } else if (cmdByte[0] == 0x02) {
                    cmd = 3;//关索
                } else if (cmdByte[0] == 0x03) {
                    cmd = 5;//尾箱
                } else if (cmdByte[0] == 0x04) {
                    cmd = 1;//启动
                } else if (cmdByte[0] == 0x05) {
                    cmd = 6;//寻车
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
                 if (BuildConfig.DEBUG) Log.e("传输车辆状态", "查看on狀態"+isOn);
                String blueToothname=dataCarBlue.deviceName;
                int  isBindBluetooth=dataCarBlue.isBindBluetooth;
                if(cmd==1){
                    if(isOn==1){
                        cmd=2;
                    }
                }
                BlueLinkControl.preControlCmd=cmd;
                 if (BuildConfig.DEBUG) Log.e("传输车辆状态", " BlueLinkControl.preControlCmd "+  BlueLinkControl.preControlCmd);
                DataCarInfo carInfo =new DataCarInfo();
                carInfo.bluetoothName=blueToothname;
                carInfo.ide=carId;
                carInfo.carsig=dataCarBlue.carsig;//车辆控制密钥
                 if (BuildConfig.DEBUG) Log.e("------------", "carInfo.carsig"+carInfo.carsig);
                carInfo.isBindBluetooth=isBindBluetooth;
                 DataCarBlueLcd dataCarBlueLcd=DataCarBlueLcd.loadLocal(mContext);
                 long userId=dataCarBlueLcd.userId;
//                蓝牙模式直接发指令
                boolean isBlutooth;
                if(carInfo.isBindBluetooth == 1 && BlueAdapter.current_blue_state >= STATE_DISCOVER_OK){
                    isBlutooth=true;
                }else{
                    isBlutooth=false;
                }
                if (isBlutooth) {
                     if (BuildConfig.DEBUG) Log.e("------------", "後臺藍牙發");
                    final String cacheCmd = BlueStaticValue.getControlCmdByID(cmd);
                    if (cmd == 1) {
                        String setTime = BlueStaticValue.getBlueTimeCmd(time);//0-7
                        BlueAdapter.getInstance().sendMessage(setTime);
                        new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
                            @Override
                            public void onTimeEnd() {
                                 if (BuildConfig.DEBUG) Log.e("------------", "後臺藍牙發"+"命令"+cacheCmd+"cmd"+BlueLinkControl.preControlCmd);
                                BlueLinkControl.getInstance().sendMessage(cacheCmd, false);
                            }
                        });
                    } else {
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺藍牙發"+"命令"+cacheCmd);
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺藍牙發"+"命令"+cacheCmd+"cmd"+BlueLinkControl.preControlCmd);
                        BlueLinkControl.getInstance().sendMessage(cacheCmd, false);
                    }
                } else {
                     if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡發");
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
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
        byte myYear = (byte) (year % 100);
//月
        byte month = (byte) (calendar.get(Calendar.MONTH) + 1);
//日
        byte day = (byte) calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        byte hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        byte minute = (byte) calendar.get(Calendar.MINUTE);
//秒
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
