package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkspods.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY;
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

public class BlueLinkControlLcdKey {
    private Context context;
    private DataCarBlueLcd dataCar;

    public DataCarBlueLcd getDataCar() {
        return dataCar;
    }

    public static int preControlCmd;
    private List<BluetoothDevice> blueDeviceList = new ArrayList<>();//blueName,address
    public static long preDataBackTime = 0;
    public static long preDiscoveringTime = 0;
    public static long preConningTime = 0;
    public static boolean canCheckDataReturn = false;
    //=================================================================
    private static BlueLinkControlLcdKey _instance;

    public static BlueLinkControlLcdKey getInstance() {
        if (_instance == null)
            _instance = new BlueLinkControlLcdKey();
        return _instance;
    }

    public boolean init(Context context) {
        boolean isInited = (this.context == null) ? false : true;
        this.context = context;
        dataCar = DataCarBlueLcd.loadLocal(context);
        if (dataCar == null) dataCar = new DataCarBlueLcd();
        MyLcdBlueAdapterBack.getInstance().initializeOK(context);
        MyLcdBlueAdapterBack.getInstance().setOnBlueStateListener(KulalaServiceA.KulalaServiceAThis.onBlueStateListenerLcd);
        if (!isTimeRunning) new TimeCheckThread1().start();
        //test
//        changeCar(7468,"BLE#0x0C61CF370606","07f680a6b67237ca",false,true,true);
        return isInited;
    }

    public boolean getIsBlueConnOK() {
        if (MyLcdBlueAdapterBack.current_blue_state >= STATE_DISCOVER_OK) return true;
        return false;
    }

    public void closeBlueConnReal(final String info) {
        dataCar = new DataCarBlueLcd();
//        if (MyLcdBlueAdapterBack.current_blue_state < STATE_CONNECT_OK) return;
        MyLcdBlueAdapterBack.getInstance().closeBlueReal();
         if (BuildConfig.DEBUG) Log.e("????????????", "closeBlueConnClearName????????????from:" + info);
    }

    public void changeLcdKey(long carId, String keyBlueName, String keySig, int isKeyBind, int isKeyOpen, long userId
    ) {
         if (BuildConfig.DEBUG) Log.e("????????????", "carID=" + carId + "keyBlueName=" + keyBlueName + "isKeyBind" + isKeyBind + "isKeyOpen" + isKeyOpen);
        if (carId == 0 || keyBlueName == null || keyBlueName.length() == 0) {
            closeBlueConnReal("????????????????????????");
        } else if (getIsBlueConnOK() && (dataCar.keyBlueName == null || keyBlueName == null || !dataCar.keyBlueName.equals(keyBlueName))) {//????????????????????????
            closeBlueConnReal("?????????----->?????????");
        } else if (getIsBlueConnOK() && dataCar != null && isKeyBind != 1) {
            closeBlueConnReal("???????????????????????????----->?????????");
        } else if (getIsBlueConnOK() && dataCar != null && isKeyOpen != 1) {
            closeBlueConnReal("????????????????????????????????????----->?????????");
        }
        dataCar = new DataCarBlueLcd();
        dataCar.carId = carId;
        dataCar.keyBlueName = keyBlueName;
        dataCar.keySig = keySig;
        dataCar.isKeyBind = isKeyBind;
        dataCar.isKeyOpen = isKeyOpen;
        dataCar.userId = userId;
//        dataCar.isON = isKeyOpen;
//        dataCar.isTheft = isTheft;
//        dataCar.isKeyOpen = isKeyOpen;
         if (BuildConfig.DEBUG) Log.e("????????????", "dataCar=" + dataCar + "deviceName=" + keyBlueName + "blueDeviceList=" + blueDeviceList + "blueDeviceList.Size()" + blueDeviceList.size());
        String reealMacAderess = StringToMacUtilCopy.collapseString(dataCar.keyBlueName, 2, ":");
        final BluetoothDevice device1 = BlueGet.getDeviceAderessFromList(reealMacAderess, blueDeviceList);
//
////         if (BuildConfig.DEBUG) Log.e("????????????", "device1="+device1+"device1.getName()="+device1.getName());
        if ((device1 != null && device1.getAddress() != null)) {
            dataCar.deviceAddressLcd = device1.getAddress();
        }
        DataCarBlueLcd.saveLocal(context, dataCar);
        //????????????????????????????????????
        if (dataCar != null && dataCar.deviceAddressLcd != null && dataCar.deviceAddressLcd.length() > 0)
            gotoConnDevice(dataCar.deviceAddressLcd, " ?????????,?????????:" + reealMacAderess);
    }

    //===============================timeRoll==================================
    private long timeNum1 = 0;
    private static boolean isTimeRunning = false;
    private long sleepTime = 1500L;

    class TimeCheckThread1 extends Thread {
        public TimeCheckThread1() {
        }

        public void run() {
            isTimeRunning = true;
//             if (BuildConfig.DEBUG) Log.e("BlueLinkControl","TimeCheckThread start");
            while (isTimeRunning) {
                try {
                    Thread.sleep(sleepTime);
                    sleepTime += 100;
                    if (sleepTime >= 3000L) sleepTime = 1500L;

//                    if(System.currentTimeMillis() - preActiveTime > 60*1000L)continue;//test
                    /**2???????????????8????????????*/
                    timeNum1++;
                     if (BuildConfig.DEBUG) Log.e("????????????", "??????????????????:" );
                    if (timeNum1 % 7 == 0)
                        LogMeLinks.e("????????????", "timeNum1:" + timeNum1 + " blue_state:" + MyLcdBlueAdapterBack.current_blue_state);
                    if (dataCar == null || dataCar.carId == 0) {
                        Intent broadcast = new Intent(BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY);
                        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast);
                    }
                    if (dataCar.deviceAddressLcd != null && dataCar.deviceAddressLcd.length() > 0) {//?????????????????????
//                         if (BuildConfig.DEBUG) Log.e("blue", "??????????????????readRssi:" + dataCar.deviceName);
//                        String reealMacAderess= StringToMacUtilCopy.collapseString(dataCar.keyBlueName, 2, ":");
                        if(MyLcdBlueAdapterBack.current_blue_state<STATE_DISCOVER_OK){
                            gotoConnDevice(dataCar.deviceAddressLcd, " address:" + dataCar.deviceAddressLcd);
                             if (BuildConfig.DEBUG) Log.e("????????????", "?????????????????????????????????" );
                        }
                    }else if (dataCar.keyBlueName != null && dataCar.keyBlueName.length() > 0) {
                        String reealMacAderess= StringToMacUtilCopy.collapseString(dataCar.keyBlueName, 2, ":");
                        if(MyLcdBlueAdapterBack.current_blue_state<STATE_DISCOVER_OK){
                            gotoConnDevice(reealMacAderess, " address:" + "??????????????????????????????????????????");
                        }
//                        String reealMacAderess = StringToMacUtilCopy.collapseString(dataCar.keyBlueName, 2, ":");
//                        final BluetoothDevice device1 = BlueGet.getDeviceAderessFromList(reealMacAderess, blueDeviceList);
//                        //timeNum1 % 3 == 0 &&
//                        if ((device1 == null || device1.getAddress() == null)) {
//                             if (BuildConfig.DEBUG) Log.e("????????????", "?????????????????????" + "??????????????????" + BlueScannerAllways.getInstance().getIsScanning());
//                            if (!BlueScannerAllways.getInstance().getIsScanning()) {
//                                BlueScannerAllways.getInstance().scanLeDevice(context, new BlueScannerAllways.OnScanBlueListener() {
//                                    @Override
//                                    public void onScanedDevice(BluetoothDevice device) {
//                                        if (device != null && !TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress()))
//                                             if (BuildConfig.DEBUG) Log.e("????????????", "?????????" + device.getName());
//                                        if (!blueDeviceList.contains(device)) {
//                                            blueDeviceList.add(device);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onScanStop() {
//
//                                    }
//                                });
//                            }
//                        } else  {
//                            dataCar.deviceAddressLcd = device1.getAddress();
//                            if(MyLcdBlueAdapterBack.current_blue_state<STATE_DISCOVER_OK){
//                                gotoConnDevice(dataCar.deviceAddressLcd, " address:" + device1.getAddress()+"??????????????????????????????");
//                            }
//                        }
                    }
                } catch (Exception e) {
                     if (BuildConfig.DEBUG) Log.e("????????????", "???????????????:" + e.toString());
                }
            }
            isTimeRunning = false;
             if (BuildConfig.DEBUG) Log.e("????????????", "TimeCheckThread stop:");
        }
    }

    // ========================public======================
    public void sendMessage(String hexStr, boolean needMessageSendedInfo) {
        MyLcdBlueAdapterBack.getInstance().sendMessage(hexStr);//????????????
    }

    //================================conn===============================
    private boolean getCanReconn() {
         if (BuildConfig.DEBUG) Log.e("????????????", "dataCar=" + dataCar + "dataCar.keyBlueName=" + dataCar.keyBlueName +
                "dataCar.isKeyBind=" + dataCar.isKeyBind);
        if (dataCar == null) return false;
        if (dataCar.keyBlueName == null) return false;
        if (dataCar.isKeyBind != 1) return false;
        return true;
    }

    public void gotoConnDevice(final String address, String info) {
         if (BuildConfig.DEBUG) Log.e("????????????", "gotoConnDevice:" + info);
        new ConnDeviceThread(address).start();
    }

    private static boolean ConnDeviceThreadIsStoped = true;//1.5?????????????????????????????????????????????

    private class ConnDeviceThread extends Thread {
        private String address = "";

        ConnDeviceThread(final String useAddress) {
            address = useAddress;
        }

        public void run() {
            if (!ConnDeviceThreadIsStoped) return;
            //1.??????????????????
//             if (BuildConfig.DEBUG) Log.e("blue", "t:" + MyLcdBlueAdapterBack.current_blue_state + " ????????????");
            //3.??????????????????????????????
            if (!getCanReconn()) {
                 if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "??????????????????");
                ConnDeviceThreadIsStoped = true;
                return;
            }
            //4.???????????????????????????????????????????????????????????????????????????????????????????????????
            //5.?????????????????????????????????????????????????????????????????????
            long now = System.currentTimeMillis();
            BluetoothDevice useDevice = MyLcdBlueAdapterBack.getInstance().getUsedDevice();
            /**??????????????????????????????**/

            if (timeNum1 % 4 == 0) {
                 if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "state:" + MyLcdBlueAdapterBack.current_blue_state);
            }
            if (MyLcdBlueAdapterBack.current_blue_state == STATE_CLOSEED || MyLcdBlueAdapterBack.current_blue_state < STATE_CLOSEING) {
                 if (BuildConfig.DEBUG) Log.e("????????????", "STATE_CLOSEED ???????????? ??????" + address);
                MyLcdBlueAdapterBack.getInstance().gotoConnDeviceAddress(address);
            } else if (MyLcdBlueAdapterBack.current_blue_state == STATE_CONNECT_FAILED) {
                if (now - preConningTime > 3500L) {
                     if (BuildConfig.DEBUG) Log.e("????????????", "STATE_CONNECT_FAILED>3500L ???????????? ??????");
                    MyLcdBlueAdapterBack.getInstance().gotoConnDeviceAddress(address);
                }
            } else if (MyLcdBlueAdapterBack.current_blue_state == STATE_CONNECTING) {
                 if (BuildConfig.DEBUG) Log.e("????????????", "STATE_CONNECTING");
                if (now - preConningTime > 7000L) {
                     if (BuildConfig.DEBUG) Log.e("????????????", "STATE_CONNECTING 7s?????????????????????");
                    MyLcdBlueAdapterBack.getInstance().gotoConnDeviceAddress(address);
                }
            } else if (MyLcdBlueAdapterBack.current_blue_state == STATE_CONNECT_OK) {
                 if (BuildConfig.DEBUG) Log.e("????????????", "STATE_CONNECT_OK ?????????");
            } else if (MyLcdBlueAdapterBack.current_blue_state == STATE_DISCOVER_FAILED) {
                if (now - preDiscoveringTime > 3500L) {
                     if (BuildConfig.DEBUG) Log.e("????????????", "STATE_DISCOVER_FAILED>3500L ??????????????? ??????");
                    MyLcdBlueAdapterBack.getInstance().gotoDiscoverService();
                }
            } else if (MyLcdBlueAdapterBack.current_blue_state == STATE_DISCOVERING) {
                 if (BuildConfig.DEBUG) Log.e("????????????", "STATE_DISCOVERING ????????? readRssi");
                MyLcdBlueAdapterBack.getInstance().readRssiRemote();
            } else if (MyLcdBlueAdapterBack.current_blue_state >= STATE_DISCOVER_OK) {//???????????????????????????
                MyLcdBlueAdapterBack.getInstance().readRssiRemote();
                String reealMacAderess = StringToMacUtilCopy.collapseString(dataCar.keyBlueName, 2, ":");
                 if (BuildConfig.DEBUG) Log.e("????????????", "dataCar.keyBlueName" + dataCar.keyBlueName + "------useDevice.getAddress()" + useDevice.getAddress());
                if (useDevice != null && useDevice.getName() != null && !useDevice.getAddress().equals(reealMacAderess)) {
                    //?????????????????????
                    canCheckDataReturn = false;
                     if (BuildConfig.DEBUG) Log.e("????????????", "STATE_DISCOVER_OK ?????????????????????");
                    MyLcdBlueAdapterBack.getInstance().closeBlueReal();
                } else if (now - preDataBackTime > 1006500L && now - MyLcdBlueAdapterBack.countMegeTime > 1006500L) {//???????????????????????????
                    canCheckDataReturn = true;
//                 if (BuildConfig.DEBUG) Log.e("blue","????????????????????????????????????,6500L?????????");
                     if (BuildConfig.DEBUG) Log.e("????????????", "1006500L?????????,???????????????,??????????????????????????? readRssi");
                    MyLcdBlueAdapterBack.getInstance().closeBlueWait();
                } else {
                    canCheckDataReturn = false;
                    ConnDeviceThreadIsStoped = true;
                    return;
                }
                /**????????????????????????**/
            }
            ConnDeviceThreadIsStoped = true;
//        return false;
        }
    }
//================================conn===============================
}
