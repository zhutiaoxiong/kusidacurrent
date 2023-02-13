package view.view4me.myblue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 仅用于扫秒列表，启动后列表加项就返回一次
 */

public class MyBlueScanList {
    private static final long SCAN_DEVICE_TIME_DISTANCE =15000L;//5000毫秒太短可能搜不到15
    private boolean isScanning = false;
    private BluetoothManager   bluetoothManager;
    private BluetoothAdapter   bluetoothAdapter;
    private Context context;
    private  int canTouch;
    //==================================================
    private OnScanBlueListener onScanBlueListener;
    private boolean isHaveBooth;
    public interface OnScanBlueListener {
        void onScanedDeviceList(List<BluetoothDevice> deviceList);//已找到蓝牙全返回
        void onScanStop(boolean ishavebOOTH);
    }
    //==================================================
    private static MyBlueScanList _instance;
    protected MyBlueScanList() {
    }
    public static MyBlueScanList getInstance() {
        if (_instance == null)
            _instance = new MyBlueScanList();
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
    private List<BluetoothDevice> cacheDeviceList;
    private Timer timerScanStop;
    private  TimerTask task;
    public void scanDeviceList(Context context1,boolean enable,OnScanBlueListener listener) {
        this.context = context1;
        this.onScanBlueListener = listener;
        if(!initializeOK(context))return;
        if(bluetoothAdapter ==null)return;
        if (enable) {
            if (!isScanning) {//启动扫描
                 if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "启动扫描 ");
                 if (BuildConfig.DEBUG) Log.e("scan","scanLeDevice Func:启动扫描");
                isScanning = true;
                cacheDeviceList = new ArrayList<>();
                        if(bluetoothAdapter!=null){
                          bluetoothAdapter.startLeScan(mLeScanCallback);
                            isHaveBooth=false;
                            canTouch=0;
                    }
                        if(task==null){
                             task = new TimerTask() {
                                public void run() {
                                     if (BuildConfig.DEBUG) Log.e("查看綫程", "run: "+Thread.currentThread() );
                                    scanDeviceList(context,false,null);
                                }
                            };
                        }
                        if(timerScanStop==null){
                            timerScanStop = new Timer();
                        }
                        timerScanStop.schedule(task, SCAN_DEVICE_TIME_DISTANCE);//5秒后停了
            }
        } else {//停止扫描
            if (isScanning) {
                if(task!=null){
                    task.cancel();
                    task=null;
                }
                if (timerScanStop != null){
                    timerScanStop.cancel();
                    timerScanStop.purge();
                    timerScanStop = null;
                }
                bluetoothAdapter.stopLeScan(mLeScanCallback);
                 if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "停止扫描 ");
                isScanning = false;
                cacheDeviceList = null;
                if (onScanBlueListener != null){
                    onScanBlueListener.onScanStop(isHaveBooth);
                }
            }
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    isHaveBooth=true;
                    data(device);
                }
            });
//            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    };
    private synchronized void data (BluetoothDevice  device){
        if(device == null || device.getName() == null || device.getAddress() == null)return;
        //扫到的结果会重复
        if(cacheDeviceList==null)return;//差错控制
        boolean isRepeat = false;
        if(cacheDeviceList!=null&&cacheDeviceList.size()>0){
            for(BluetoothDevice de : cacheDeviceList){
                if(de!=null&&de.getName()!=null&&device!=null&&device.getName()!=null){
                    if(de.getName().equals(device.getName()))isRepeat = true;
                }
            }
        }
        if(!isRepeat){
            if(cacheDeviceList!=null&&device!=null){
                cacheDeviceList.add(device);
            }
            if (onScanBlueListener != null){
                 if (BuildConfig.DEBUG) Log.e("scan","scanLeDevice Func:掃描到列表");
                onScanBlueListener.onScanedDeviceList(cacheDeviceList);
            }
        }
    }
}
