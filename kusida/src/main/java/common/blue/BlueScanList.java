package common.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.linkscarpods.blue.MyBluetoothDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import view.view4me.myblue.ThreadManager;

/**
 * 仅用于扫秒列表，启动后列表加项就返回一次
 */

public class BlueScanList {
    private static final long SCAN_DEVICE_TIME_DISTANCE = 3000L;//5000毫秒太短可能搜不到
    private boolean isScanning = false;
    private BluetoothManager   bluetoothManager;
    private BluetoothAdapter   bluetoothAdapter;
    private Context context;
    //==================================================
    private OnScanBlueListener onScanBlueListener;
    public interface OnScanBlueListener {
        void onScanedDeviceList(List<MyBluetoothDevice> deviceList);//已找到蓝牙全返回
        void onScanStop();
    }
    //==================================================
    private static BlueScanList _instance;
    protected BlueScanList() {
    }
    public static BlueScanList getInstance() {
        if (_instance == null)
            _instance = new BlueScanList();
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
    private boolean initializeOK(Context context) {
        if(context == null)return false;
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
    private List<MyBluetoothDevice> cacheDeviceList;
    private Timer timerScanStop;
    public void scanDeviceList(Context context1,boolean enable,OnScanBlueListener listener) {
        this.context = context1;
        this.onScanBlueListener = listener;
        if(!initializeOK(context))return;
        if(bluetoothAdapter ==null)return;
        if (enable) {
            if (!isScanning) {//启动扫描
                 if (BuildConfig.DEBUG) Log.e("scan","scanLeDevice Func:启动扫描");
                isScanning = true;
                cacheDeviceList = new ArrayList<>();
                bluetoothAdapter.startLeScan(mLeScanCallback);
                TimerTask task = new TimerTask() {
                    public void run() {
                        scanDeviceList(context,false,null);
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
                Log.e("scan","scanLeDevice Func:停止扫描");
                isScanning = false;
                cacheDeviceList = null;
                if (onScanBlueListener != null)onScanBlueListener.onScanStop();
            }
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    data(device,scanRecord);
                }
            });
//            if(device == null || device.getName() == null || device.getAddress() == null)return;
//            //扫到的结果会重复
//            if(cacheDeviceList==null)return;//差错控制
//            boolean isRepeat = false;
//            for(BluetoothDevice de : cacheDeviceList){
//               if(de!=null&&de.getName()!=null&&device.getName()!=null){
//                   if(de.getName().equals(device.getName()))isRepeat = true;
//               }
//            }
//            if(!isRepeat){
//                cacheDeviceList.add(device);
//                if (onScanBlueListener != null)
//                     if (BuildConfig.DEBUG) Log.e("scan","scanLeDevice Func:掃描到列表");
//                    onScanBlueListener.onScanedDeviceList(cacheDeviceList);
//            }
        }
    };
    private synchronized void data (BluetoothDevice  device,byte[] scanRecord){
        if(device == null || device.getAddress() == null)return;
        //扫到的结果会重复
        if(cacheDeviceList==null)return;//差错控制
        boolean isRepeat = false;
        if(cacheDeviceList!=null&&cacheDeviceList.size()>0){
            for(MyBluetoothDevice de : cacheDeviceList){
                if(de != null && de.getName() != null){
                    if(de.getAddress().equals(device.getAddress())){
                        isRepeat = true;
                    }
                }
            }
        }
        if(!isRepeat){
            if(cacheDeviceList != null){
                MyBluetoothDevice device1=new MyBluetoothDevice();
                device1.address=device.getAddress();
                if(!TextUtils.isEmpty(device.getName())){
                    device1.name=device.getName();
                }else{
                    if(scanRecord!=null){
                        byte[] newbytes=  Arrays.copyOfRange(scanRecord,31,62);
                        String devicename=new String(newbytes);
                        device1.name=devicename;
                        Log.e("blue","scanLeDevice Func:扫到设备设备名称asdsadasdad:"+ devicename);
                    }
                }
                cacheDeviceList.add(device1);
            }
            if (onScanBlueListener != null){
                if (BuildConfig.DEBUG) Log.e("scan","scanLeDevice Func:掃描到列表");
                onScanBlueListener.onScanedDeviceList(cacheDeviceList);
            }
        }
    }
}
