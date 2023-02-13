package common.blue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.blue.BlueStaticValue;
import com.kulala.linkscarpods.blue.KulalaServiceA;

import common.GlobalContext;
import ctrl.OCtrlCar;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.status.DataSwitch;
import view.view4control.MiniDataIsShowLock;
import view.view4control.ViewControlPanelControl;
import view.view4me.myblue.LcdManagerCarStatus;
import view.view4me.shake.ViewNoKey;

import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_CHANGE_LCDKEY;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_GET_CARINFO;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED_SEND_CARINFO_TO_LCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.BLUETOOTH_NEED__LCDKEY_CONTROL_CAR;
//import static com.kulala.linksankula.blue.BlueStaticValue.IS_ACTIVITYMAIN_FORGROUND;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONBLUECONNCHANGE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONCONNECTEDFAILED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONCONNECTEDFAILEDLCDKEY;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDATARECEIVED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDISCOVEROK;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONDISCOVEROKLCD;
import static com.kulala.linkscarpods.blue.BlueStaticValue.ONMESSAGESENDED;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SEND_CAR_STATUS_BLUETOOTH_PROGRESS;
import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;
import static com.kulala.linkscarpods.interfaces.OnBlueStateListenerRoll.STATE_DISCOVER_OK;

/**
 * 用于主activity监控蓝牙状态,自定版
 */

public class BlueLinkReceiver extends BroadcastReceiver {
    private static boolean isBlueConnOK = false;//需要每2秒刷一次
    private static boolean isBlueLCDConnOK = false;//需要每2秒刷一次
    private static long usedCarId = 0;
    private Context mContext;
    private static boolean alreadyRegCanDestory = false;
    //    /**8.0取消了静态注删，只能动态*/
    public static BlueLinkReceiver blueLinkReceiverThis;

    // ========================from service======================
    public static boolean getIsBlueConnOK() {
        return isBlueConnOK;
    }

    public static long getUsedCarId() {
        return usedCarId;
    }

    //=================================================================
    private static BlueLinkReceiver _instance;

    public static BlueLinkReceiver getInstance() {
        if (_instance == null) {
            _instance = new BlueLinkReceiver();
        }
        return _instance;
    }

    //=================================================================
    public void initReceiver(Context context) {
        if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            blueLinkReceiverThis = new BlueLinkReceiver();
            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ONCONNECTEDFAILED);
                filter.addAction(ONDISCOVEROK);
                filter.addAction(ONMESSAGESENDED);
                filter.addAction(ONDATARECEIVED);
                filter.addAction(ONBLUECONNCHANGE);
                filter.addAction(BLUETOOTH_NEED_GET_CARINFO);
                filter.addAction(BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY);
                filter.addAction(ONCONNECTEDFAILEDLCDKEY);
                filter.addAction(ONDISCOVEROKLCD);
                filter.addAction(BLUETOOTH_NEED_SEND_CARINFO_TO_LCD);
                filter.addAction(BLUETOOTH_NEED__LCDKEY_CONTROL_CAR);

                mContext = context;
                context.registerReceiver(blueLinkReceiverThis, filter,MytoolsGetPackageName.getBroadCastPermision(),null);
//                context.registerReceiver(blueLinkReceiverThis, filter);
                alreadyRegCanDestory = true;
                 if (BuildConfig.DEBUG) Log.e("BlueLinkReceiver", "initReceiver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Intent intentA = new Intent(GlobalContext.getContext(), KulalaServiceA.class);
//        intentA.setPackage(GlobalContext.getContext().getPackageName());
//        GlobalContext.getContext().bindService(intentA,conn,BIND_AUTO_CREATE);
    }

    public void unRegReceiver() {
        if (alreadyRegCanDestory && blueLinkReceiverThis != null && mContext != null) {
            mContext.unregisterReceiver(blueLinkReceiverThis);
            blueLinkReceiverThis = null;
            alreadyRegCanDestory = false;
            mContext = null;
             if (BuildConfig.DEBUG) Log.e("BlueLinkReceiver", "unRegReceiver");
        }
    }


//    private static ServiceConnection conn = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            KulalaServiceA.MyBinder binder = (KulalaServiceA.MyBinder) service;
//            KulalaServiceA bindService = binder.getService();
//            boolean connected = bindService.getBlueConnected();
//             if (BuildConfig.DEBUG) Log.e("BlueLink","connected:"+connected);
//        }
//        //client 和service连接意外丢失时，会调用该方法
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.v("hjz","onServiceDisconnected  A");
//        }
//    };

    public static void startA() {
//         if (BuildConfig.DEBUG) Log.e("BlueLinkReceiver","startA");
        try {
            Intent intentA = new Intent(GlobalContext.getContext(), KulalaServiceA.class);
            intentA.setPackage(GlobalContext.getContext().getPackageName());
            GlobalContext.getContext().startService(intentA);
//             if (BuildConfig.DEBUG) Log.e("BlueReceiver", "startA");
        } catch (Exception e) {
//             if (BuildConfig.DEBUG) Log.e("BlueReceiver", "startA:" + e.toString());
        }
    }

    //==================================================================
//    public void SoundControl(String info) {
//        Intent broadcast = new Intent();
//        broadcast.setAction(BlueStaticValue.SERVICE_1_SOUND_CONTROL);
//        broadcast.putExtra("control", info);
//        GlobalContext.getContext().sendBroadcast(broadcast);
//    }
    public void closeBlueConnClearName(String info) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_STOPCONN_CLEARDATA);
        broadcast.putExtra("info", info);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

    public void closeBlueConnClearNameLcdKey(String info) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_STOPCONN_CLEARDATA_LCD);
        broadcast.putExtra("info", info);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

    public void sendMessage(String hexStr, boolean needMessageSendedInfo) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_SENDMESSAGE);
        broadcast.putExtra("hexStr", hexStr);
        broadcast.putExtra("needMessageSendedInfo", needMessageSendedInfo);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }
    public void sendMessagelcd(String hexStr, boolean needMessageSendedInfo) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_SENDMESSAGE_LCD);
        broadcast.putExtra("hexStr", hexStr);
        broadcast.putExtra("needMessageSendedInfo", needMessageSendedInfo);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

    public static void needChangeCar(long carId, String deviceName, String carSign,int isMyCar ) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR);
        usedCarId = carId;
        broadcast.putExtra("carId", carId);
        broadcast.putExtra("deviceName", deviceName);
        broadcast.putExtra("carSign", carSign);
        broadcast.putExtra("isUseBlueModel", !BlueLinkNetSwitch.getIsNetModel(carId));
        DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
        boolean shakeOpen = (shakeinfo != null && shakeinfo.isOpen == 1) ? true : false;
        broadcast.putExtra("isShakeOpen", shakeOpen);
        broadcast.putExtra("vibratorOpen", BlueLinkNetSwitch.getIsShakeOpenVibrate());
        broadcast.putExtra("isMyCar", isMyCar);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }
    public static void needChangeCar(long carId, String deviceName, String carSign,int isBindBluetooth,String carsig,int isMyCar ) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR);
        usedCarId = carId;
        broadcast.putExtra("carId", carId);
        broadcast.putExtra("deviceName", deviceName);
        broadcast.putExtra("carSign", carSign);
        broadcast.putExtra("isUseBlueModel", !BlueLinkNetSwitch.getIsNetModel(carId));
        DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
        boolean shakeOpen = (shakeinfo != null && shakeinfo.isOpen == 1) ? true : false;
        broadcast.putExtra("isShakeOpen", shakeOpen);
        broadcast.putExtra("vibratorOpen", BlueLinkNetSwitch.getIsShakeOpenVibrate());
        broadcast.putExtra("isBindBluetooth", isBindBluetooth);
        broadcast.putExtra("carsig", carsig);
        broadcast.putExtra("isMyCar", isMyCar);
        broadcast.putExtra("shakeLevel", BlueLinkNetSwitch.getShakeLevel());
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
        Log.e("切换车辆", "广播发出1");
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

    public static void needChangeCarData(long carId, String deviceName, String carSign,int isBindBluetooth,String carsig,int isMyCar ) {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR_DATA);
        usedCarId = carId;
        broadcast.putExtra("carId", carId);
        broadcast.putExtra("deviceName", deviceName);
        broadcast.putExtra("carSign", carSign);
        broadcast.putExtra("isUseBlueModel", !BlueLinkNetSwitch.getIsNetModel(carId));
        DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
        boolean shakeOpen = (shakeinfo != null && shakeinfo.isOpen == 1) ? true : false;
        broadcast.putExtra("isShakeOpen", shakeOpen);
        broadcast.putExtra("vibratorOpen", BlueLinkNetSwitch.getIsShakeOpenVibrate());
        broadcast.putExtra("isBindBluetooth", isBindBluetooth);
        broadcast.putExtra("carsig", carsig);
        broadcast.putExtra("isMyCar", isMyCar);
        broadcast.putExtra("shakeLevel", BlueLinkNetSwitch.getShakeLevel());
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
        Log.e("切换车辆", "广播发出2");
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

//    public static void needChangeCar(long carId, String deviceName, String carSign,int isBind ,int isBindBluetooth) {
//        Intent broadcast = new Intent();
//        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_CHANGE_CAR);
//        usedCarId = carId;
//        broadcast.putExtra("carId", carId);
//        broadcast.putExtra("deviceName", deviceName);
//        broadcast.putExtra("carSign", carSign);
//        broadcast.putExtra("isUseBlueModel", !BlueLinkNetSwitch.getIsNetModel(carId));
//        DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
//        boolean shakeOpen = (shakeinfo != null && shakeinfo.isOpen == 1) ? true : false;
//        broadcast.putExtra("isShakeOpen", shakeOpen);
//        broadcast.putExtra("vibratorOpen", BlueLinkNetSwitch.getIsShakeOpenVibrate());
//        broadcast.putExtra("isBindBluetooth", isBindBluetooth);
//        GlobalContext.getContext().sendBroadcast(broadcast);
//    }

    public static void needChangeLcdKey(long carId, String keyBlueName, String keySig,int keyBind,int isKeyOpen,long userId) {
        Intent broadcast = new Intent();
        broadcast.setAction(BLUETOOTH_NEED_CHANGE_LCDKEY);
        usedCarId = carId;
        broadcast.putExtra("carId", carId);
        broadcast.putExtra("keyBlueName", keyBlueName);
        broadcast.putExtra("keySig", keySig);
        broadcast.putExtra("isKeyBind", keyBind);
        broadcast.putExtra("isKeyOpen", isKeyOpen);
        broadcast.putExtra("userId", userId);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }
//    public static void notyfyActivityMainIsForground(boolean isForgound) {
//         if (BuildConfig.DEBUG) Log.e("------------", "發廣播給後臺 "+isForgound);
//        Intent broadcast = new Intent();
//        broadcast.setAction(IS_ACTIVITYMAIN_FORGROUND);
//        broadcast.putExtra("isForground", isForgound);
//        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
////        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
//    }
    public static void sendCarStatusBlueToothProgress (String carStatus) {
        Intent broadcast = new Intent();
        broadcast.setAction(SEND_CAR_STATUS_BLUETOOTH_PROGRESS);
        broadcast.putExtra("carStatus", carStatus);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        try{
            GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
        }catch(Exception e){

        }
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }
    public static void clearBluetooth() {
        Intent broadcast = new Intent();
        broadcast.setAction(BlueStaticValue.BLUETOOTH_NEED_CLEAR_BLUETOOTH);
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
        Log.e("清空蓝牙", "广播发出");
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }


    //==================================================================
    @Override
    public void onReceive(Context content, Intent intent) {
        if (blueLinkReceiverThis == null) {
            blueLinkReceiverThis = this;
        }
        if (ONMESSAGESENDED.equals(intent.getAction())) {
            byte[] bytes = intent.getByteArrayExtra("bytes");
            onMessageSended(bytes);
        } else if (ONDATARECEIVED.equals(intent.getAction())) {
            long carId = intent.getLongExtra("carId", 0);
            int dataType = intent.getIntExtra("dataType", 0);
            byte[] data = intent.getByteArrayExtra("data");
            onDataReceived(carId, dataType, data);
        } else if (ONBLUECONNCHANGE.equals(intent.getAction())) {
//            isBlueConnOK = intent.getBooleanExtra("blueConnOK", false);
            int isOk=intent.getIntExtra("blueConnOK", 0);
            Log.e("txtxtxtxt", "1: "+isOk);
            if(isOk>= STATE_DISCOVER_OK){
                isBlueConnOK=true;
                Log.e("txtxtxtxt", "1: "+isBlueConnOK);
            }else{
                if(isOk!=0){
                    isBlueConnOK=false;
                    MiniDataIsShowLock.isLockChange=-1;
                }
                Log.e("txtxtxtxt", "1: "+isBlueConnOK);
            }
        } else if (ONDISCOVEROK.equals(intent.getAction())) {
            isBlueConnOK = true;
            Log.e("txtxtxtxt", "2: "+isBlueConnOK);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
            ODispatcher.dispatchEvent(OEventName.BLUETOOTH_CONNECTED_STATUS,"1");
        } else if (ONCONNECTEDFAILED.equals(intent.getAction())) {
            isBlueConnOK = false;
            Log.e("txtxtxtxt", "3: "+isBlueConnOK);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
            ODispatcher.dispatchEvent(OEventName.BLUETOOTH_CONNECTED_STATUS,"0");
        } else if (BLUETOOTH_NEED_GET_CARINFO.equals(intent.getAction())) {
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            if (carInfo != null) {
                needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
                 if (BuildConfig.DEBUG) Log.e("查看是否拿到數據", "carInfo.ide" +carInfo.ide+"carInfo.bluetoothName"+carInfo.bluetoothName+"carInfo.blueCarsig"+carInfo.blueCarsig+"carInfo.isBindBluetooth"+carInfo.isBindBluetooth);
            }
        } else if (ONCONNECTEDFAILEDLCDKEY.equals(intent.getAction())) {
            isBlueLCDConnOK = false;
          //液晶鑰匙鏈接失敗
        }else if (BLUETOOTH_NEED_SEND_CARINFO_TO_LCD.equals(intent.getAction())) {
            //需要發送車狀態給液晶鑰匙
            //发送车辆状态
            DataCarStatus status=ManagerCarList.getInstance().getCurrentCarStatus();
            if(status!=null){
//               LcdManagerCarStatus.sendCarStatus(status);
                sendMessagelcd(bytesToHexString( LcdManagerCarStatus.ObjectToByteBlue(status)),true);
            }

        }else if (BLUETOOTH_NEED__LCDKEY_CONTROL_CAR.equals(intent.getAction())) {
            //需要通过液晶钥匙控制车辆
            //
             if (BuildConfig.DEBUG) Log.e("MyLcdBlueAdapterBack", "前台的reciver收到控制指令" );
            long carId = intent.getLongExtra("carId", 0);
            int dataType = intent.getIntExtra("dataType", 0);
            byte[] cmdByte = intent.getByteArrayExtra("data");
            int length = intent.getIntExtra("length",0);
            int cmd=0;
            if( cmdByte[0]==0x01){
                cmd=4;//开锁
            }else  if( cmdByte[0]==0x02){
                cmd=3;//关索
            }else  if( cmdByte[0]==0x03){
                cmd=5;//尾箱
            }else  if( cmdByte[0]==0x04){
                cmd=1;//启动
            }else  if( cmdByte[0]==0x05){
                cmd=6;//寻车
            }
            int time=0;
            if(length==2){
                if(cmdByte[1]>=0&&cmdByte[1]<=7){
                    time=cmdByte[1];
                }
            }
            DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
            DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
            ViewControlPanelControl.preControlCmd=cmd;
            if(carStatus!=null&&currentCar!=null){
                if(cmd==1){
                    if(carStatus.isON==1){
                        ViewControlPanelControl.preControlCmd=2;
                    }else{
                        ViewControlPanelControl.preControlCmd=1;
                    }
                }
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, ViewControlPanelControl.preControlCmd, time);
            }
        }  else if (BLUETOOTH_NEED_GET_CARINFO_FOR_LCDKEY.equals(intent.getAction())) {
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            if (carInfo != null) {
                long userId= ManagerLoginReg.getInstance().getCurrentUser().userId;
                needChangeLcdKey(carInfo.ide, carInfo.keyBlueName, carInfo.keySig,carInfo.isKeyBind,carInfo.isKeyOpen,userId);
            }
        } else {
            startA();
             if (BuildConfig.DEBUG) Log.e("Receiver", "onReceive" + intent.getAction());
        }
    }

    public void onMessageSended(byte[] bytes) {
        if (bytes == null) return;
        String byteStr = bytesToHexString(bytes);
//        boolean control3 = byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(3))));
//        boolean control4 = byteStr.equals(bytesToHexString(hexStringToBytes(BlueStaticValue.getControlCmdByID(4))));
//        long now = System.currentTimeMillis();
//        if ((control3|| control4) && now - OShake.preShakeTime < 2500L
//                && ManagerSwitchs.getInstance().getVibratorOpen())showVibrator();
        if(ViewControlPanelControl.ViewControlPanelControlThis!=null)ViewControlPanelControl.ViewControlPanelControlThis.handleBlueCmdSended(bytes);
        OCtrlCar.getInstance().ifNeedBlueSendNext(bytes);
    }

    public void onDataReceived(long carId, int dataType, byte[] data) {
        if (data == null) return;
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        if (dataType == 0x21) {
            ManagerCarStatus.setData0x21(data, carInfo.ide);
        } else if (dataType == 0x22) {
            ManagerCarStatus.setData0x22(data, carInfo.ide);
        }
        BlueInstructionCollection.getInstance().saveAllSwitch( dataType, data);
        if (ViewNoKey.viewNoKeyThis != null)
            ViewNoKey.viewNoKeyThis.onDataReceived(carId, dataType, data);
    }

}
