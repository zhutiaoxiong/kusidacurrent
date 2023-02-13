package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;


import com.kulala.linkspods.BuildConfig;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 1.扫描全部
 * 2.扫描单个
 */

public class BlueReConnScanner {
    private String  matchBlueName = null;
    private long preScanMatchTime = 0;
    private int preScanRssi = 0;

    private boolean isScanning    = false;
    private        BluetoothManager  bluetoothManager;
    private        BluetoothAdapter  bluetoothAdapter;
    //==================================================
    private static BlueReConnScanner _instance;
    protected BlueReConnScanner() {
    }
    public static BlueReConnScanner getInstance() {
        if (_instance == null)
            _instance = new BlueReConnScanner();
        return _instance;
    }
    public boolean getIsScanning(){
        return isScanning;
    }
    public boolean getIsNearDevice(String deviceName){
        if(matchBlueName == null)return false;
        if(deviceName == null)return false;
        if(preScanMatchTime == 0)return false;
        if(preScanRssi == 0)return false;
        if(preScanRssi<100 && System.currentTimeMillis() - preScanMatchTime < 5000L && matchBlueName.equals(deviceName))
            return true;
        return false;
    }
    //=========================初始化=========================
    public boolean initializeOK(Context context) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le"))
            return false;//"没有硬件蓝牙"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return false;//"android版本过低，请升级至5.0以上"
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) return false;//"没有蓝牙服务"
        }
        bluetoothAdapter = bluetoothManager.getAdapter();//Build.VERSION.SDK_INT >= 19
        if (bluetoothAdapter == null) return false;//"不能开启蓝牙连接"
        else if (!bluetoothAdapter.isEnabled()) return false;//"没有打开蓝牙"
        else return true;
    }
    //=======================扫描过程===blueName null就返回全列表==================
    private Timer timerScanStop;
    /**
     * @param blueName null means scanAll Device,else scan one
     */
    public void scanLeDevice(boolean enable, Context context, String blueName) {

        if (enable) {
            if (!isScanning) {
                if (context == null)return;
                if (blueName == null)return;
                if (!initializeOK(context)) return;//无蓝牙，未开
                 if (BuildConfig.DEBUG) Log.e("blue", "start scanning!");
                isScanning = true;
                matchBlueName = blueName;
                bluetoothAdapter.startLeScan(mLeScanCallback);
                TimerTask task = new TimerTask() {
                    public void run() {
                        scanLeDevice(false, null, null);
                    }
                };
                timerScanStop = new Timer();
                timerScanStop.schedule(task, 60 * 1000L);//60秒后停了,扫单个烂牙
            }
        } else {
            if (isScanning) {
                 if (BuildConfig.DEBUG) Log.e("blue", "stop scanning!");
                if (timerScanStop != null) timerScanStop.cancel();
                timerScanStop = null;
                bluetoothAdapter.stopLeScan(mLeScanCallback);
                matchBlueName = null;
                isScanning = false;
            }
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (matchBlueName == null){
                scanLeDevice(false, null, null);
                return;
            }
            if (matchBlueName.equals(device.getName())) {//判断远程设备是否与用户目标设备相同
                preScanMatchTime = System.currentTimeMillis();
                preScanRssi = rssi;
            }
        }
    };
}
