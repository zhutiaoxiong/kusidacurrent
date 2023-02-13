package view.view4me.myblue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

/**
 * 仅用于扫秒列表，启动后列表加项就返回一次
 */

public class MyNewBlueScanList {
    private static final long SCAN_DEVICE_TIME_DISTANCE = 2100L;//5000毫秒太短可能搜不到
    private boolean isScanning = false;
    private BluetoothManager   bluetoothManager;
    private BluetoothAdapter   bluetoothAdapter;
    private Context context;
    private BluetoothLeScanner scanner;
    //==================================================
    private OnScanBlueListener onScanBlueListener;
    private  ScanSettings.Builder builder;
    private ScanSettings settings;
    private  List<ScanFilter> bleScanFilters;
    private Handler handler;
    public interface OnScanBlueListener {
        void onScanedDeviceList(List<BluetoothDevice> deviceList);//已找到蓝牙全返回
        void onScanStop();
    }
    //==================================================
    private static MyNewBlueScanList _instance;
    protected MyNewBlueScanList() {
    }
    public static MyNewBlueScanList getInstance() {
        if (_instance == null)
            _instance = new MyNewBlueScanList();
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
        //创建ScanSettings的build对象用于设置参数
        if(builder==null){
             builder = new ScanSettings.Builder()
                    //设置高功耗模式
                    .setScanMode(SCAN_MODE_LOW_LATENCY);
            //android 6.0添加设置回调类型、匹配模式等
            if(android.os.Build.VERSION.SDK_INT >= 23) {
                //定义回调类型
                builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
                //设置蓝牙LE扫描滤波器硬件匹配的匹配模式
                builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
            }
//芯片组支持批处理芯片上的扫描
            if (bluetoothAdapter.isOffloadedScanBatchingSupported()) {
                //设置蓝牙LE扫描的报告延迟的时间（以毫秒为单位）
                //设置为0以立即通知结果
                builder.setReportDelay(0L);
            }
            builder.build();
        }
        if(bleScanFilters==null){
            bleScanFilters = new ArrayList<>();
            bleScanFilters.add(new ScanFilter.Builder().setDeviceName("").build());
        }
        return true;
    }
    //=======================扫描过程===blueName null就返回全列表==================
    private List<BluetoothDevice> cacheDeviceList;
    private Timer timerScanStop;
    private TimerTask task;
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
                        if(bluetoothAdapter!=null){
//                          bluetoothAdapter.startLeScan(mLeScanCallback);
                                 scanner=bluetoothAdapter.getBluetoothLeScanner();
                                if(android.os.Build.VERSION.SDK_INT >= 21) {
                                    //标记当前的为扫描状态
                                    isScanning = true;
                                    //获取5.0新添的扫描类
                                    //开始扫描
                                    //mScanSettings是ScanSettings实例，mScanCallback是ScanCallback实例，后面进行讲解。
                                    if(settings==null){
                                        settings=builder.build();
                                    }
                                     if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "扫描开始");
                                    scanner.startScan(null,settings,mScanCallback);
                                }
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
                if (timerScanStop != null) {
                    timerScanStop.cancel();
                    timerScanStop.purge();
                    timerScanStop = null;
                }
                if(android.os.Build.VERSION.SDK_INT >= 21) {
                    //标记当前的为未扫描状态
                    isScanning = false;
                    if(scanner!=null){
                        scanner.stopScan(mScanCallback);
                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "扫描结束");
                    }
                } else {
                    //标记当前的为未扫描状态
                    isScanning = false;
                    //5.0以下  停止扫描
                    if(scanner!=null){
                        scanner.stopScan(mScanCallback);
                    }
                }
                scanner=null;
                isScanning = false;
                cacheDeviceList = null;
                if (onScanBlueListener != null){
                    onScanBlueListener.onScanStop();
                }
            }
        }
    }
    private ScanCallback mScanCallback=new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
          final  BluetoothDevice device=result.getDevice();
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    data(device);
                }
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "扫描到了列表里面");
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "扫描失败"+errorCode);
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
