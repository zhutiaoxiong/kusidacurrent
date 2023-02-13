package view.view4me.lcdkey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.linkscarpods.blue.BluePermission;

import java.util.List;

import common.GlobalContext;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.myblue.DataReceive;
import view.view4me.myblue.MyBlueScanList;
import view.view4me.myblue.MyLcdBlueAdapter;

import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;

public class LcdBlueOptionUtils {
    private static  LcdBlueOptionUtils instance;
    public static LcdBlueOptionUtils getInstance() {
        if (instance == null)
            instance = new LcdBlueOptionUtils();
        return instance;
    }
    private long preScanTime=0;
    private List<BluetoothDevice> caCheDeviceList;
    public void scanAndLinkBlue(final Activity context){
   final  DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
        if(carInfo.isKeyBind==0)return;
        if(carInfo.isKeyOpen==0)return;
        if(TextUtils.isEmpty(carInfo.keySig)||TextUtils.isEmpty(carInfo.keyBlueName))return;
        if (BluePermission.checkPermission(context) != 1) {
            BluePermission.openBlueTooth(context);
            return;
        }
        long now = System.currentTimeMillis();
        if (now - preScanTime < 3000L) return;//未到时间不扫
        preScanTime = now;

        MyBlueScanList.getInstance().scanDeviceList(GlobalContext.getContext(), true, new MyBlueScanList.OnScanBlueListener() {
            @Override
            public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
                caCheDeviceList=deviceList;
            }

            @Override
            public void onScanStop(boolean ishavebOOTH) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    boolean isInBlueToothList=false;
                    if(caCheDeviceList==null||caCheDeviceList.size()==0)return;
                    for (int i = 0; i <caCheDeviceList.size() ; i++) {
                        if(carInfo.keyBlueName.equals(caCheDeviceList.get(i).getAddress())){
                            isInBlueToothList=true;
                            break;
                        }
                    }
                    if(isInBlueToothList){
                        //如果附近有这个设备则开始连接蓝牙
                        MyLcdBlueAdapter.getInstance().initializeOK(GlobalContext.getContext());
                        MyLcdBlueAdapter.getInstance().setOnBlueStateListener(new MyOnBlueStateListenerRoll(new MyOnBlueStateListenerRoll.OnonDescriptorWriteLister() {
                            @Override
                            public void onDescriptorWrite() {
//                    DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
                                //蓝牙验证串
                                DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
                                String keySig=carInfo.keySig;
                                 if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "验证串"+keySig );
                                if(!TextUtils.isEmpty(keySig)){
                                    byte[] bytesig = keySig.getBytes();
                                    byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytesig);
                                    String datasend=bytesToHexString(mess);
                                    MyLcdBlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
//                             if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "发绑定数据"+mess.toString());
                                     if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "发绑定数据"+datasend);
                                }
//                        MyLcdBlueAdapter.getInstance().sendMessage("01 02 01 03 F8");
                            }

                            @Override
                            public void onConnectedFailed() {
                                 if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "连接失败");
                                scanAndLinkBlue(context);
                            }
                        }));
                        if(BluetoothAdapter.checkBluetoothAddress(carInfo.keyBlueName)){
                            MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(carInfo.keyBlueName);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
