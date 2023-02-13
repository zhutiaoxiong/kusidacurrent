package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.kulala.linkspods.BuildConfig;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Administrator on 2017/11/16.
 if (BlueScanner.getInstance().initialize(GlobalContext.getContext()) == null) {
 BlueScanner.getInstance().scanLeDevice(true);
 }
 */

public class BlueScanner {
    private static final long SCAN_DEVICE_TIME_DISTANCE = 10000L;//5000毫秒太短可能搜不到
    private boolean isScanning = false;
    private BluetoothManager   bluetoothManager;
    private BluetoothAdapter   bluetoothAdapter;
    private Context context;
    private String matchBlueName = null;
    //==================================================
    private OnScanBlueListener onScanBlueListener;
    public interface OnScanBlueListener {
        void onScanedDevice(MyBluetoothDevice device);//已找到蓝牙
        void onScanStop();
    }
    //==================================================
    private static BlueScanner _instance;
    protected BlueScanner() {
    }
    public static BlueScanner getInstance() {
        if (_instance == null)
            _instance = new BlueScanner();
        return _instance;
    }
    //=========================初始化=========================
    public boolean getIsScanning(){
        return isScanning;
    }
    /**
     * 需要先BluePermission
     * @return 1初始化成功 其它error
     */
    public boolean initializeOK(Context context) {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) return false;//"没有蓝牙服务";
        }
        bluetoothAdapter = bluetoothManager.getAdapter();//Build.VERSION.SDK_INT >= 19
        if (bluetoothAdapter == null) {
            return false;//"不能开启蓝牙连接";
        } else if (!bluetoothAdapter.isEnabled()) {
            return false;//"没有打开蓝牙";
        }
        return true;
    }
    //=======================扫描过程===blueName null就返回全列表==================
    private Timer timerScanStop;
    public void scanLeDevice(Context context1, boolean enable, String blueName, OnScanBlueListener listener) {
        this.context = context1;
        this.onScanBlueListener = listener;
        if(!initializeOK(context))return;
        if(bluetoothAdapter ==null)return;
        if (enable) {
            if (!isScanning) {//启动扫描
                 if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:启动扫描");
                isScanning = true;
                matchBlueName = blueName;
                bluetoothAdapter.startLeScan(mLeScanCallback);
                TimerTask task = new TimerTask() {
                    public void run() {
                        scanLeDevice(context,false,null,null);
                    }
                };
                timerScanStop = new Timer();
                timerScanStop.schedule(task, SCAN_DEVICE_TIME_DISTANCE);//5秒后停了
            }
        } else {//停止扫描
            if (isScanning) {
                if (timerScanStop != null) timerScanStop.cancel();
                timerScanStop = null;
                bluetoothAdapter.stopLeScan(mLeScanCallback);
                matchBlueName = null;
                isScanning = false;
                if(onScanBlueListener!=null)onScanBlueListener.onScanStop();
            }
        }
    }
    //rssi:-90以外的认为信号太差
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            MyBluetoothDevice device1=new MyBluetoothDevice();
            device1.address=device.getAddress();
            if(device.getName()!=null){
//                if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:扫到设备名称不为空时:"+device.getAddress());
//                if (onScanBlueListener != null) onScanBlueListener.onScanedDevice(device);
                device1.name=device.getName();
            }else{
                if(scanRecord!=null){
                    byte[] newbytes=  Arrays.copyOfRange(scanRecord,31,62);
                    String devicename=new String(newbytes);
                    device1.name=devicename;
                    Log.e("blue","scanLeDevice Func:扫到设备设备名称asdsadasdad:"+ devicename);
                }
            }
            if( device.getAddress() == null)return;
            if(!TextUtils.isEmpty(matchBlueName)){
                if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:"+matchBlueName+" 扫出设备 other1:"+device.getName());
            } else {
                if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:扫出设备 other1:"+device.getName());
            }
            if(TextUtils.isEmpty(matchBlueName))return;
            if(rssi > -90 && matchBlueName.equals(device.getName())){//判断远程设备是否与用户目标设备相同
                scanLeDevice(context,false,null,onScanBlueListener);//关闭搜索
                if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:扫到设备:"+device.getName());
                if (onScanBlueListener != null) onScanBlueListener.onScanedDevice(device1);
            }
        }
    };
}
