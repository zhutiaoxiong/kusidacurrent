package view.view4me.shake;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;


import com.kulala.linkscarpods.blue.MyBluetoothDevice;
import com.kulala.linkspods.BuildConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;

/**
 * Created by Administrator on 2017/11/16.
 if (BlueScanner.getInstance().initialize(GlobalContext.getContext()) == null) {
 BlueScanner.getInstance().scanLeDevice(true);
 }
 */

public class BlueScannerAllways {
    private static final long SCAN_DEVICE_TIME_DISTANCE = 10000L;//5000毫秒太短可能搜不到
//    private boolean isScanning = false;
    private BluetoothManager   bluetoothManager;
    private BluetoothAdapter   bluetoothAdapter;
    private Context context;
    private BluetoothLeScanner scanner;
    private  ScanSettings.Builder builder;
    private ScanSettings settings;
    private List<ScanFilter> bleScanFilters;

    //==================================================
    private OnScanBlueListener onScanBlueListener;
    public interface OnScanBlueListener {
        void onScanedDevice(MyBluetoothDevice device,BluetoothDevice device1);//已找到蓝牙
        void onScanStop();
    }
    //==================================================
    private static BlueScannerAllways _instance;
    protected BlueScannerAllways() {
    }
    public static BlueScannerAllways getInstance() {
        if (_instance == null)
            _instance = new BlueScannerAllways();
        return _instance;
    }
    PendingIntent callbackIntent;
    //=========================初始化=========================
//    public boolean getIsScanning(){
//        return isScanning;
//    }
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
        //创建ScanSettings的build对象用于设置参数
        if(builder==null){
            builder = new ScanSettings.Builder()
                    //低功耗       设置高功耗模式
//            SCAN_MODE_LOW_POWER SCAN_MODE_LOW_LATENCY SCAN_MODE_BALANCED
                    .setScanMode(SCAN_MODE_LOW_POWER);
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
//        if(bleScanFilters==null){
//            bleScanFilters = new ArrayList<>();
//            ScanFilter.Builder builder = new ScanFilter.Builder();
//            builder.setServiceUuid(ParcelUuid.fromString("00001000-0000-1000-8000-00805f9b34fb"));
//            ScanFilter scanFilter = builder.build();
//            bleScanFilters.add(scanFilter);
//        }
//        com.client.proj.kulala com.kulala.linksankula
//        if(android.os.Build.VERSION.SDK_INT >= 26){
//             callbackIntent = PendingIntent.getForegroundService(
//                    context,
//                     1,
//                    new Intent("com.hungrytree.receiver.BleService").setPackage("com.client.proj.kulala"),
//                    PendingIntent.FLAG_UPDATE_CURRENT );
//        }


        //指定扫描到蓝牙后是以什么方式通知到app端，这里将以可见服务的形式进行启动



        return true;
    }
    //=======================扫描过程===blueName null就返回全列表==================
    private Timer timerScanStop;
    private TimerTask timerTask;
    public  void scanLeDevice(Context context1, OnScanBlueListener listener) {
        if (BuildConfig.DEBUG) Log.e("扫描蓝牙","1");
        if(context1==null) {
            return;
        }
        this.context = context1;
        this.onScanBlueListener = listener;
        if (BuildConfig.DEBUG) Log.e("扫描蓝牙","2");
        if(!initializeOK(context)){
            return;
        }
        if (BuildConfig.DEBUG) Log.e("扫描蓝牙","4");
//        if (enable) {
//            if (!isScanning) {
                if (BuildConfig.DEBUG) Log.e("扫描蓝牙","5");
                 if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:启动扫描");
//                isScanning = true;
                scanner=bluetoothAdapter.getBluetoothLeScanner();
                if(settings==null){
                    settings=builder.build();
                }
                if (BuildConfig.DEBUG) Log.e("扫描蓝牙","6");
                scanner.startScan(bleScanFilters,settings,mLeScanCallback);
                if(timerScanStop==null){
                    timerScanStop=new Timer();
                }
        timerTask=new TimerTask() {
            @Override
            public void run() {
                stopTimerAndStopScan();
            }
        };
        timerScanStop.schedule(timerTask,5000L);
//                timerScanStop.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                      stopTimerAndStopScan();
//                        if (BuildConfig.DEBUG) Log.e("扫描蓝牙","7");
//                    }
//                },5000L);
//                try {
//                    Thread.sleep(5000L);
//                    stopScanBlueLeScan();
////                    Thread.sleep(500);
////                    if(android.os.Build.VERSION.SDK_INT >= 26){
////                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "26");
////                        scanner.startScan(bleScanFilters,settings,callbackIntent);
////                    }
////                    Thread.sleep(3000L);
////                    stopScanBluePendIntent();
//                    isScanning=false;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
    }
    private void stopTimerAndStopScan(){
        if(timerScanStop!=null){
            timerTask.cancel();
            timerTask=null;
            timerScanStop.cancel();
        }
        stopScanBlueLeScan();
//        isScanning=false;
        timerScanStop=null;
    }
    //rssi:-90以外的认为信号太差
    private  ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device=result.getDevice();
            ScanRecord record= result.getScanRecord();
            MyBluetoothDevice device1=new MyBluetoothDevice();
            device1.address=device.getAddress();
            if(record.getDeviceName()!=null){
//                if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:扫到设备名称不为空时:"+device.getAddress());
//                if (onScanBlueListener != null) onScanBlueListener.onScanedDevice(device);
                device1.name=device.getName();
            }else{
                byte[] bytes=    record.getBytes();
                if(bytes!=null){
                    byte[] newbytes=  Arrays.copyOfRange(bytes,31,62);
                    String devicename=new String(newbytes);
                    device1.name=devicename;
                    Log.e("blue","scanLeDevice Func:扫到设备设备名称asdsadasdad:"+ devicename);
                }
            }
            int rssi=result.getRssi();
            if(device.getAddress() == null)return;
//            if(rssi > -90 ){//判断远程设备是否与用户目标设备相同
//                scanLeDevice(context,false,null,onScanBlueListener);//关闭搜索
            if (BuildConfig.DEBUG) Log.e("blue","scanLeDevice Func:扫到设备:"+device.getName());
            if (onScanBlueListener != null) onScanBlueListener.onScanedDevice(device1,device);
//            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            if (BuildConfig.DEBUG) Log.e("扫描蓝牙","8");
            stopTimerAndStopScan();
            if (BuildConfig.DEBUG) Log.e("一直掃描", "掃描失敗錯誤碼"+errorCode );
        }
    };
    private void stopScanBlueLeScan(){
        if(scanner!=null){
             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "两秒后停止");
            if(mLeScanCallback!=null){
                if(bluetoothAdapter!=null&&bluetoothAdapter.isEnabled()){
                    scanner.stopScan(mLeScanCallback);
                }
            }
        }
    }
}
