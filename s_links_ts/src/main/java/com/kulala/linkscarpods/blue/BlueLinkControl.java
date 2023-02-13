package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkspods.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_GET_CARINFO;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CLOSEED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CLOSEING;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECTING;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECT_FAILED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_CONNECT_OK;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVERING;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_FAILED;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_OK;


/**
 * Created by Administrator on 2018/5/11.
 */

public class BlueLinkControl {
    private Context context;
    private DataCarBlue dataCar;

    public DataCarBlue getDataCar() {
        if (BuildConfig.DEBUG)
            Log.e("bluestate", "BlueAdapter.current_blue_state" + BlueAdapter.current_blue_state);
        return dataCar;
    }

    public static int preControlCmd;
    public static List<MyBluetoothDevice> blueDeviceList = new ArrayList<>();//blueName,address
    public static long preDataBackTime = 0;
    public static long preDiscoveringTime = 0;
    public static long preConningTime = 0;
    public static boolean canCheckDataReturn = false;
    //=================================================================
    private static BlueLinkControl _instance;

    public static BlueLinkControl getInstance() {
        if (_instance == null)
            _instance = new BlueLinkControl();
        return _instance;
    }

    public boolean init(Context context) {
        boolean isInited = (this.context == null) ? false : true;
        this.context = context;
        dataCar = DataCarBlue.loadLocal(context);
        if (dataCar == null) dataCar = new DataCarBlue();
        BlueAdapter.getInstance().init(context);
        BlueAdapter.getInstance().setOnBlueStateListener(KulalaServiceA.KulalaServiceAThis.onBlueStateListener);
        if (!isTimeRunning) new TimeCheckThread1().start();
        //test
//        changeCar(7468,"BLE#0x0C61CF370606","07f680a6b67237ca",false,true,true);
        return isInited;
    }
    public boolean init() {
        boolean isInited = (this.context == null) ? false : true;
        if(context==null)return false;
        dataCar = DataCarBlue.loadLocal(context);
        if (dataCar == null) dataCar = new DataCarBlue();
        BlueAdapter.getInstance().init(context);
        BlueAdapter.getInstance().setOnBlueStateListener(KulalaServiceA.KulalaServiceAThis.onBlueStateListener);
        if (!isTimeRunning) new TimeCheckThread1().start();
        //test
//        changeCar(7468,"BLE#0x0C61CF370606","07f680a6b67237ca",false,true,true);
        return isInited;
    }

    public boolean getIsBlueConnOK() {
        if (BuildConfig.DEBUG)
            Log.e("bluestate", "BlueAdapter.current_blue_state" + BlueAdapter.current_blue_state);
        if(BlueAdapter.current_blue_state >= STATE_DISCOVER_OK){
            Log.e("bluestate", "BlueAdapter.current_blue_state" + true);
        }else{
            Log.e("bluestate", "BlueAdapter.current_blue_state" + false);
        }
        if (BlueAdapter.current_blue_state >= STATE_DISCOVER_OK) return true;
        return false;
    }
    public int getIsBlueConnOKk() {
        if (BuildConfig.DEBUG)
            Log.e("bluestate", "BlueAdapter.current_blue_state" + BlueAdapter.current_blue_state);
       return BlueAdapter.current_blue_state;
    }

    public void closeBlueConnReal(final String info) {
        dataCar = new DataCarBlue();
//        if (BlueAdapter.current_blue_state < STATE_CONNECT_OK) return;
        BlueAdapter.getInstance().closeBlueReal();
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "closeBlueConnClearName清除设备from:" + info);
    }

    //摇一摇，或模式开一个就重连回数据
    public void changeCar(long carId, String deviceName, String carSign
            , boolean isUseBlueModel, boolean isShakeOpen
            , boolean vibratorOpen) {
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "carID=" + carId + "deviceName=" + deviceName + "deviceName.length()=" + deviceName.length());
        if (carId == 0 || deviceName == null || deviceName.length() == 0) {
            closeBlueConnReal("连另外无车的蓝牙");
        } else if (getIsBlueConnOK() && (dataCar.deviceName == null || !dataCar.deviceName.equals(deviceName))) {//可以使用的车连接
            closeBlueConnReal("切换车----->关连接");
        } else if (getIsBlueConnOK() && dataCar != null && (dataCar.isShakeOpen != isShakeOpen)) {
            closeBlueConnReal("切换摇一摇----->关连接");
        }
        if(dataCar==null){
            dataCar = new DataCarBlue();
        }
        dataCar.carId = carId;
        dataCar.deviceName = deviceName;
        dataCar.carSign = carSign;
        dataCar.isUseBlueModel = isUseBlueModel;
        dataCar.isShakeOpen = isShakeOpen;
        dataCar.vibratorOpen = vibratorOpen;
        if(deviceName==null||deviceName.equals("")){
            dataCar.deviceAddress="";
        }
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "dataCar=" + dataCar + "deviceName=" + deviceName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
        if(deviceList1!=null&&deviceList1.size()>0){
        for (BluetoothDevice device : deviceList1) {
            if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
            if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                if(dataCar.deviceName.startsWith("*")){
                    dataCar.deviceAddress="";
                }else{
                    dataCar.deviceAddress=device.getAddress();
                }
            }
        }
        }
        final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddress = device1.getAddress();
        }
        DataCarBlue.saveLocal(context, dataCar);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "切换车,蓝牙名：" + carId + deviceName + isUseBlueModel + isShakeOpen + vibratorOpen + carSign);
        //如果是有地址的，直接去连
        if (dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0)
            gotoConnDevice(dataCar.deviceAddress, " 切换车,直接连1:" );
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "11111111111");
    }

    //摇一摇，或模式开一个就重连回数据
    public void changeCar(long carId, String deviceName, String carSign
            , boolean isUseBlueModel, boolean isShakeOpen
            , boolean vibratorOpen, int isBindBluetooth) {
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "carID=" + carId + "deviceName=" + deviceName + "deviceName.length()=" + deviceName.length());
        if (carId == 0 || deviceName == null || deviceName.length() == 0||TextUtils.isEmpty(carSign)||carSign.equals("-1")) {
            closeBlueConnReal("连另外无车的蓝牙");
        } else if (getIsBlueConnOK() && (dataCar.deviceName == null || !dataCar.deviceName.equals(deviceName))) {//可以使用的车连接
            closeBlueConnReal("切换车----->关连接");
        } else if (getIsBlueConnOK() && dataCar != null && (dataCar.isShakeOpen != isShakeOpen)) {
            closeBlueConnReal("切换摇一摇----->关连接");
        }
        if(dataCar==null){
            dataCar = new DataCarBlue();
        }
        dataCar.carId = carId;
        dataCar.deviceName = deviceName;
        dataCar.carSign = carSign;
        dataCar.isUseBlueModel = isUseBlueModel;
        dataCar.isShakeOpen = isShakeOpen;
        dataCar.vibratorOpen = vibratorOpen;
        dataCar.isBindBluetooth = isBindBluetooth;
        if(deviceName==null||deviceName.equals("")){
            dataCar.deviceAddress="";
        }
        if (BuildConfig.DEBUG) Log.e("車驗證全", "dataCar.carSign" + dataCar.carSign);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "dataCar=" + dataCar + "deviceName=" + deviceName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
        if(deviceList1!=null&&deviceList1.size()>0){
            for (BluetoothDevice device : deviceList1) {
                if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
                if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                    if(dataCar.deviceName.startsWith("*")){
                        dataCar.deviceAddress="";
                    }else{
                        dataCar.deviceAddress=device.getAddress();
                    }
                }
            }
        }

        final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddress = device1.getAddress();
        }
        DataCarBlue.saveLocal(context, dataCar);

        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "切换车,蓝牙名：" + carId + deviceName + isUseBlueModel + isShakeOpen + vibratorOpen + carSign);
        //如果是有地址的，直接去连
        if (dataCar != null && dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0)
            gotoConnDevice(dataCar.deviceAddress, " 切换车,直接连2:" );
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "22222222222");
    }

    //摇一摇，或模式开一个就重连回数据
    public void changeCar(long carId, String deviceName, String carSign
            , boolean isUseBlueModel, boolean isShakeOpen
            , boolean vibratorOpen, int isBindBluetooth, String carsig,int isMyCar) {
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "carID=" + carId + "deviceName=" + deviceName + "deviceName.length()=" + deviceName.length());
        if (carId == 0 || deviceName == null || deviceName.length() == 0||TextUtils.isEmpty(carSign)||carSign.equals("-1")) {
            closeBlueConnReal("连另外无车的蓝牙");
        } else if (getIsBlueConnOK() && (dataCar.deviceName == null || deviceName == null || !dataCar.deviceName.equals(deviceName))) {//可以使用的车连接
            closeBlueConnReal("切换车----->关连接");
        } else if (getIsBlueConnOK() && dataCar != null && (dataCar.isShakeOpen != isShakeOpen)) {
            closeBlueConnReal("切换摇一摇----->关连接");
        }
        if(dataCar==null){
            dataCar = new DataCarBlue();
        }
        dataCar.carId = carId;
        dataCar.deviceName = deviceName;
        dataCar.carSign = carSign;
        dataCar.isUseBlueModel = isUseBlueModel;
        dataCar.isShakeOpen = isShakeOpen;
        dataCar.vibratorOpen = vibratorOpen;
        dataCar.isBindBluetooth = isBindBluetooth;
        dataCar.carsig = carsig;
        dataCar.isMyCar = isMyCar;
        if(deviceName==null||deviceName.equals("")){
            dataCar.deviceAddress="";
        }
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "dataCar.carSign" + dataCar.carSign);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "dataCar=" + dataCar + "deviceName=" + deviceName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
        if(deviceList1!=null&&deviceList1.size()>0){
        for (BluetoothDevice device : deviceList1) {
            if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
            if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                if(dataCar.deviceName.startsWith("*")){
                    dataCar.deviceAddress="";
                }else{
                    dataCar.deviceAddress=device.getAddress();
                }
            }
         }
        }
        final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddress = device1.getAddress();
        }
        DataCarBlue.saveLocal(context, dataCar);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "切换车,蓝牙名：" + carId + deviceName + isUseBlueModel + isShakeOpen + vibratorOpen + carSign);
        //如果是有地址的，直接去连
        if (dataCar != null && dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0)
            gotoConnDevice(dataCar.deviceAddress, " 切换车,直接连3:" );
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "3333333333");
    }

    //摇一摇，或模式开一个就重连回数据
    public void changeCar(long carId, String deviceName, String carSign
            , boolean isUseBlueModel, boolean isShakeOpen
            , boolean vibratorOpen, int isBindBluetooth, String carsig,int isMyCar,String shakeLevel) {
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "carID=" + carId + "deviceName=" + deviceName + "deviceName.length()=" + deviceName.length());
        if (carId == 0 || deviceName == null || deviceName.length() == 0||TextUtils.isEmpty(carSign)||carSign.equals("-1")) {
            closeBlueConnReal("连另外无车的蓝牙");
        } else if (getIsBlueConnOK() && (dataCar.deviceName == null || deviceName == null || !dataCar.deviceName.equals(deviceName))) {//可以使用的车连接
            closeBlueConnReal("切换车----->关连接");
        } else if (getIsBlueConnOK() && dataCar != null && (dataCar.isShakeOpen != isShakeOpen)) {
            closeBlueConnReal("切换摇一摇----->关连接");
        }
        if(dataCar==null){
            dataCar = new DataCarBlue();
        }
        dataCar.carId = carId;
        dataCar.deviceName = deviceName;
        dataCar.carSign = carSign;
        dataCar.isUseBlueModel = isUseBlueModel;
        dataCar.isShakeOpen = isShakeOpen;
        dataCar.vibratorOpen = vibratorOpen;
        dataCar.isBindBluetooth = isBindBluetooth;
        dataCar.carsig = carsig;
        dataCar.isMyCar = isMyCar;
        dataCar.shakeLevel = shakeLevel;
        if(deviceName==null||deviceName.equals("")){
            dataCar.deviceAddress="";
        }
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "dataCar.carSign" + dataCar.carSign);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "dataCar=" + dataCar + "deviceName=" + deviceName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
        if(deviceList1!=null&&deviceList1.size()>0){
            for (BluetoothDevice device : deviceList1) {
                if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
                if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                    if(dataCar.deviceName.startsWith("*")){
                        dataCar.deviceAddress="";
                    }else{
                        dataCar.deviceAddress=device.getAddress();
                    }
                }
            }
        }
        final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddress = device1.getAddress();
        }
        DataCarBlue.saveLocal(context, dataCar);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "切换车,蓝牙名：" + carId + deviceName + isUseBlueModel + isShakeOpen + vibratorOpen + carSign);
        //如果是有地址的，直接去连
        if (dataCar != null && dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0)
            gotoConnDevice(dataCar.deviceAddress, " 切换车,直接连4" );
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "3333333333");
    }

    //摇一摇，或模式开一个就重连回数据
    public void setData(long carId, String deviceName, String carSign
            , boolean isUseBlueModel, boolean isShakeOpen
            , boolean vibratorOpen, int isBindBluetooth, String carsig,int isMyCar,String shakeLevel) {
        if(dataCar==null){
            dataCar = new DataCarBlue();
        }
        dataCar.carId = carId;
        dataCar.deviceName = deviceName;
        dataCar.carSign = carSign;
        dataCar.isUseBlueModel = isUseBlueModel;
        dataCar.isShakeOpen = isShakeOpen;
        dataCar.vibratorOpen = vibratorOpen;
        dataCar.isBindBluetooth = isBindBluetooth;
        dataCar.carsig = carsig;
        dataCar.isMyCar = isMyCar;
        dataCar.shakeLevel = shakeLevel;
        if(deviceName==null||deviceName.equals("")){
            dataCar.deviceAddress="";
        }
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "dataCar.carSign" + dataCar.carSign);
        if (BuildConfig.DEBUG)
            Log.e("主机蓝牙", "dataCar=" + dataCar + "deviceName=" + deviceName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
        if(deviceList1!=null&&deviceList1.size()>0){
            for (BluetoothDevice device : deviceList1) {
                if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
                if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                    if(dataCar.deviceName.startsWith("*")){
                        dataCar.deviceAddress="";
                    }else{
                        dataCar.deviceAddress=device.getAddress();
                    }
                }
            }
        }
        final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddress = device1.getAddress();
        }
        DataCarBlue.saveLocal(context, dataCar);
    }
    public void clearCacheBlue() {
        dataCar = new DataCarBlue();
    }


    //===============================timeRoll==================================
    private long timeNum1 = 0;
    private static boolean isTimeRunning = false;
    private long sleepTime = 1500L;

//    class TimeCheckThread1 extends Thread {
//        public TimeCheckThread1() {
//        }
//
//        public void run() {
//            isTimeRunning = true;
////             if (BuildConfig.DEBUG) Log.e("BlueLinkControl","TimeCheckThread start");
//            while (isTimeRunning) {
//                try {
//                    Thread.sleep(sleepTime);
//                    sleepTime += 100;
//                    if (sleepTime >= 3000L) sleepTime = 1500L;
////                    if(System.currentTimeMillis() - preActiveTime > 60*1000L)continue;//test
//                    /**2秒连一次，8秒扫一次*/
//                    timeNum1++;
//                    if (timeNum1 % 7 == 0)
//                        LogMeLinks.e("Blue", "timeNum1:" + timeNum1 + " blue_state:" + BlueAdapter.current_blue_state);
//                    if (dataCar == null || dataCar.carId == 0) {
//                        if (BuildConfig.DEBUG) Log.e("藍牙監控", "5");
//                        Intent broadcast = new Intent(BLUETOOTH_NEED_GET_CARINFO);
//                        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
////                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast);
//                    } else if (dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0) {//不用搜，直接连
////                         if (BuildConfig.DEBUG) Log.e("blue", "蓝线心跳启动readRssi:" + dataCar.deviceName);
//                        if (BuildConfig.DEBUG) Log.e("藍牙監控", "1");
//                        gotoConnDevice(dataCar.deviceAddress, " address:" + dataCar.deviceAddress);
//                    } else if (dataCar.deviceName != null && dataCar.deviceName.length() > 0) {
//                        final BluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
//                        //timeNum1 % 3 == 0 &&
//                        if ((device1 == null || device1.getAddress() == null)) {
////                             if (BuildConfig.DEBUG) Log.e("blue", "蓝线心跳启动扫描:" + dataCar.deviceName + " device1:"+device1);
//                            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                                if (!BlueScannerAllways.getInstance().getIsScanning()) {
//                                    BlueScannerAllways.getInstance().scanLeDevice(context, new BlueScannerAllways.OnScanBlueListener() {
//                                        @Override
//                                        public void onScanedDevice(BluetoothDevice device) {
//                                            if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
//                                                if (BuildConfig.DEBUG)
//                                                    Log.e("主机蓝牙", "扫描到" + device.getName());
//                                            if (!blueDeviceList.contains(device)) {
//                                                blueDeviceList.add(device);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onScanStop() {
//
//                                        }
//                                    });
////                            }
//
//                                }
//                            } else {
//                                if (!BlueScanner.getInstance().getIsScanning()) {
//                                    BlueScanner.getInstance().scanLeDevice(context, true, dataCar.deviceName, new BlueScanner.OnScanBlueListener() {
//                                        @Override
//                                        public void onScanedDevice(BluetoothDevice device) {
//                                            if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
//                                                if (BuildConfig.DEBUG)
//                                                    Log.e("主机蓝牙", "扫描到" + device.getName());
//                                            blueDeviceList.add(device);
//                                        }
//
//                                        @Override
//                                        public void onScanStop() {
//
//                                        }//--------------------------------
//                                    });
//                                }
//                            }
//                            if (BuildConfig.DEBUG) Log.e("藍牙監控", "4");
//
////                            BlueScannerAllways.getInstance().scanLeDevice(context);
//                            if (BuildConfig.DEBUG)
//                                Log.e("主机蓝牙", "未连接准备扫描" + "是否正在扫描" + BlueScannerAllways.getInstance().getIsScanning());
//                        } else if (device1 != null && device1.getAddress() != null) {
//                            if (BuildConfig.DEBUG) Log.e("藍牙監控", "3");
////                                 if (BuildConfig.DEBUG) Log.e("BlueLinkControl", "已经扫到设备了停止扫描");
////                                BlueScannerAllways.getInstance().stopScanBlue();
////                             if (BuildConfig.DEBUG) Log.e("blue", "蓝线心跳已扫描到设备readRssi:" + dataCar.deviceName);
//                            dataCar.deviceAddress = device1.getAddress();
//                            gotoConnDevice(dataCar.deviceAddress, " address:" + device1.getAddress());
//                        } else {
////                             if (BuildConfig.DEBUG) Log.e("blue", "TimeCheckThread:另外的情况1");
//                        }
//                    } else if (timeNum1 % 10 == 0) {
////                         if (BuildConfig.DEBUG) Log.e("blue", "TimeCheckThread:另外的情况2:"+dataCar.deviceName);
//                    }
//                } catch (Exception e) {
//                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "重连线程错:" + e.toString());
//                }
//            }
//            isTimeRunning = false;
//            if (BuildConfig.DEBUG) Log.e("主机蓝牙", "TimeCheckThread stop:");
//        }
//    }


    private long scanTime;
    class TimeCheckThread1 extends Thread {
        public TimeCheckThread1() {
        }

        public void run() {
            isTimeRunning = true;
            while (isTimeRunning) {
                try {
                    Thread.sleep(sleepTime);
                    sleepTime += 100;
                    if (sleepTime >= 3000L) sleepTime = 1500L;
                    /**2秒连一次，8秒扫一次*/
                    timeNum1++;
                    if (timeNum1 % 7 == 0)
                        LogMeLinks.e("Blue", "timeNum1:" + timeNum1 + " blue_state:" + BlueAdapter.current_blue_state);
                    if (dataCar == null || dataCar.carId == 0) {
                        if (BuildConfig.DEBUG) Log.e("藍牙監控", "5");
                        Intent broadcast = new Intent(BLUETOOTH_NEED_GET_CARINFO);
                        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());

                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
                    } else{
                        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "44444444444");
                        if (dataCar.deviceAddress != null && dataCar.deviceAddress.length() > 0) {//不用搜，直接连
                            if (BuildConfig.DEBUG) Log.e("藍牙監控", "1");
                            gotoConnDevice(dataCar.deviceAddress, " address:" + dataCar.deviceAddress);
                        }else{
                            if(!TextUtils.isEmpty(dataCar.deviceAddress)){
                                if (BuildConfig.DEBUG) Log.e("藍牙監控", "2");
                                gotoConnDevice(dataCar.deviceAddress, " address:" + dataCar.deviceAddress);
                            }else{
                                if (dataCar.deviceName != null && dataCar.deviceName.length() > 0&&!dataCar.deviceName.equals("0")) {
                                    final MyBluetoothDevice device1 = BlueGet.getDeviceFromList(dataCar.deviceName, blueDeviceList);
                                    if ((device1 == null || device1.getAddress() == null)) {
                                        boolean haveList=false;
                                        Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
                                        if(deviceList1!=null&&deviceList1.size()>0){
                                            for (BluetoothDevice device : deviceList1) {
                                                if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +device.getName()+"已配对的蓝牙地址"+device.getAddress());
                                                if(!TextUtils.isEmpty( dataCar.deviceName)&&!TextUtils.isEmpty(device.getName())&&dataCar.deviceName.equals(device.getName())){
                                                    dataCar.deviceAddress=device.getAddress();
                                                    haveList=true;
                                                    break;
                                                }
                                            }
                                        }
                                        if(haveList){
                                            gotoConnDevice(dataCar.deviceAddress, " address:" + dataCar.deviceAddress);
                                        }else{
                                            long currentTime=System.currentTimeMillis();
                                            if(scanTime==0||currentTime-scanTime>6*1000L){
                                                scanTime=currentTime;
                                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                                                if (!BlueScannerAllways.getInstance().getIsScanning()) {
                                                    BlueScannerAllways.getInstance().scanLeDevice(context, new BlueScannerAllways.OnScanBlueListener() {
//                                                        @Override
//                                                        public void onScanedDevice(BluetoothDevice device) {
//                                                            if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
//                                                                if (BuildConfig.DEBUG)
//                                                                    Log.e("主机蓝牙", "扫描到" + device.getName());
//                                                            if (!blueDeviceList.contains(device)) {
//                                                                blueDeviceList.add(device);
//                                                            }
//                                                        }

                                                        @Override
                                                        public void onScanedDevice(MyBluetoothDevice device) {
                                                            if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
                                                                if (BuildConfig.DEBUG)
                                                                    Log.e("主机蓝牙", "扫描到" + device.getName());
                                                            if (!blueDeviceList.contains(device)) {
                                                                blueDeviceList.add(device);
                                                            }
                                                        }

                                                        @Override
                                                        public void onScanStop() {

                                                        }
                                                    });
//                                                }
                                                } else {
                                                    if (!BlueScanner.getInstance().getIsScanning()) {
                                                        BlueScanner.getInstance().scanLeDevice(context, true, dataCar.deviceName, new BlueScanner.OnScanBlueListener() {
//                                                            @Override
//                                                            public void onScanedDevice(BluetoothDevice device) {
//                                                                if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
//                                                                    if (BuildConfig.DEBUG)
//                                                                        Log.e("主机蓝牙", "扫描到" + device.getName());
//                                                                blueDeviceList.add(device);
//                                                            }

                                                            @Override
                                                            public void onScanedDevice(MyBluetoothDevice device) {
                                                                if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
                                                                    if (BuildConfig.DEBUG)
                                                                        Log.e("主机蓝牙", "扫描到" + device.getName());
                                                                blueDeviceList.add(device);
                                                            }

                                                            @Override
                                                            public void onScanStop() {

                                                            }//--------------------------------
                                                        });
                                                    }
                                                }
                                                if (BuildConfig.DEBUG) Log.e("藍牙監控", "4");
                                                if (BuildConfig.DEBUG)
                                                    Log.e("主机蓝牙", "未连接准备扫描"  );
                                            }
                                        }
                                    } else if (device1 != null && device1.getAddress() != null) {
                                        if (BuildConfig.DEBUG) Log.e("藍牙監控", "3");
                                        dataCar.deviceAddress = device1.getAddress();
                                        DataCarBlue.saveLocal(context, dataCar);
                                        gotoConnDevice(dataCar.deviceAddress, " address:" + device1.getAddress());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "重连线程错:" + e.toString());
                }
            }
            isTimeRunning = false;
            if (BuildConfig.DEBUG) Log.e("主机蓝牙", "TimeCheckThread stop:");
        }
    }


    // ========================public======================
    public void sendMessage(String hexStr, boolean needMessageSendedInfo) {
        BlueAdapter.getInstance().sendMessage(hexStr);//发送指令
    }

    //================================conn===============================
    private boolean getCanReconn() {
//         if (BuildConfig.DEBUG) Log.e("一直扫描", "dataCar="+dataCar+"dataCar.deviceName="+dataCar.deviceName+
//                "dataCar.deviceAddress="+dataCar.deviceAddress+"dataCar.isShakeOpen="+dataCar.isShakeOpen+"dataCar.isUseBlueModel="+dataCar.isUseBlueModel);
        if (dataCar == null) return false;
        if (dataCar.deviceName == null) return false;
        if (dataCar.deviceAddress == null) return false;
//        if (!dataCar.isShakeOpen && !dataCar.isUseBlueModel) return false;
        return true;
    }

    public void gotoConnDevice(final String address, String info) {
//        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            BlueScannerAllways.getInstance().stopTimerAndStopScan();
//        }
        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "gotoConnDevice:" + info);
        new ConnDeviceThread(address).start();
    }


    private static boolean ConnDeviceThreadIsStoped = true;//1.5秒刷一次可能卡死，控制不刷太快

    private class ConnDeviceThread extends Thread {
        private String address = "";
        private long descovingTime;
        ConnDeviceThread(final String useAddress) {
            address = useAddress;
        }

        public void run() {
            if (!ConnDeviceThreadIsStoped) return;
            //1.权限不作检查
//             if (BuildConfig.DEBUG) Log.e("blue", "t:" + BlueAdapter.current_blue_state + " 重连准备");
            //3.是否是可以重连的状态
            if (!getCanReconn()) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "当前不能重连");
                ConnDeviceThreadIsStoped = true;
                return;
            }
            //4.不要主动中断系统进程，不然出错，连接，扫通道，都等，只判断有没数据
            //5.有数据时提示连接成功，没数据时提示蓝牙断连接了
            long now = System.currentTimeMillis();
            BluetoothDevice useDevice = BlueAdapter.getInstance().getUsedDevice();

            if (timeNum1 % 4 == 0) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "state:" + BlueAdapter.current_blue_state);
            }
            if (BlueAdapter.current_blue_state == STATE_CLOSEED || BlueAdapter.current_blue_state < STATE_CLOSEING) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CLOSEED 连接关闭 重连" + address);
                if(BluetoothAdapter.checkBluetoothAddress(address)){
                    BlueAdapter.getInstance().gotoConnDeviceAddress(address);
                }
            } else if (BlueAdapter.current_blue_state == STATE_CONNECT_FAILED) {
                if (now - preConningTime >2000L) {
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CONNECT_FAILED>3500L 连接断开 重连");
                    if(BluetoothAdapter.checkBluetoothAddress(address)){
                        BlueAdapter.getInstance().gotoConnDeviceAddress(address);
                    }
                }
            } else if (BlueAdapter.current_blue_state == STATE_CONNECTING) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CONNECTING");
                if (now - preConningTime > 5000L) {
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CONNECTING 7s后重连，但不断");
//                    BlueAdapter.getInstance().closeBlueReal();
//                    BlueAdapter._instance=null;
//                    BlueLinkControl.getInstance().init();
                    if(BluetoothAdapter.checkBluetoothAddress(address)){
                        BlueAdapter.getInstance().gotoConnDeviceAddress(address);
                    }
                }
//                else if(now - preConningTime > 15000L){
//                     if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CONNECTING 15s后断了重连");
//                    BlueAdapter.getInstance().closeBlueReal();
//                    BlueAdapter.getInstance().gotoConnDeviceAddress(address);
//                }
            } else if (BlueAdapter.current_blue_state == STATE_CONNECT_OK) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_CONNECT_OK 不处理");
            }
            else if (BlueAdapter.current_blue_state == STATE_DISCOVER_FAILED) {
                if (now - preDiscoveringTime > 3500L) {
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVER_FAILED>3500L 搜通道失败 重搜");
                    BlueAdapter.getInstance().gotoDiscoverService();
                }
            }
            else if (BlueAdapter.current_blue_state == STATE_DISCOVERING) {
                if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVERING+descovingTime"+descovingTime+"now-descovingTime"+(now-descovingTime));
//                    if(descovingTime!=0&&(now-descovingTime)>7*1000){
//                        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVERING 超过7秒");
//                        descovingTime=now;
                        BlueAdapter.getInstance().readRssiRemote();
//                        BlueAdapter.getInstance().closeBlueReal();
//                    }
                if(now - preDiscoveringTime > 7000L){
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVERING 关连接");
                    BlueAdapter.getInstance().closeBlueReal();
                }
//                    if(descovingTime==0){
//                        BlueAdapter.getInstance().readRssiRemote();
//                        descovingTime=now;
//                        if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVERING 不处理 readRssi");
//                    }else{
//                        if((now-descovingTime)>7*1000){
//                            descovingTime=now;
//                            BlueAdapter.getInstance().closeBlueReal();
//                            if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVERING 关连接");
//                        }
//                    }
            }
            else if (BlueAdapter.current_blue_state >= STATE_DISCOVER_OK) {//判断掉线和重连设备
                BlueAdapter.getInstance().readRssiRemote();
                if (useDevice != null && useDevice.getName() != null &&dataCar.deviceName!=null&& !useDevice.getName().equals(dataCar.deviceName)) {//改变设备，重连
                    canCheckDataReturn = false;
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "STATE_DISCOVER_OK 切换设备关连接");
                    dataCar.deviceAddress="";
                    BlueAdapter.getInstance().closeBlueReal();
                }else if (now - preDataBackTime > 1006500L && now - BlueAdapter.countMegeTime > 1006500L) {//无数据回包掉线判断
                    canCheckDataReturn = true;
//                 if (BuildConfig.DEBUG) Log.e("blue","无数据回包掉线关连接重连,6500L无数据");
                    if (BuildConfig.DEBUG) Log.e("主机蓝牙", "1006500L无数据,但不关连接,应该显示为蓝牙断开 readRssi");
                    BlueAdapter.getInstance().closeBlueWait();
                } else {
                    canCheckDataReturn = false;
                    ConnDeviceThreadIsStoped = true;
                    return;
                }
            }
            ConnDeviceThreadIsStoped = true;
//        return false;
        }
    }
//================================conn===============================
}
