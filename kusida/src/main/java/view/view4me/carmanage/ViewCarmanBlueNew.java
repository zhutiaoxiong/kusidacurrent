package view.view4me.carmanage;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BlueAdapter;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.linkscarpods.blue.MyBluetoothDevice;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.zxing.activity.CaptureActivity;

import java.util.List;
import java.util.Set;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueScanList;
import ctrl.OCtrlBlueTooth;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

import static common.GlobalContext.getCurrentActivity;

/**
 * 车辆蓝牙，进入此页，先设data
 * 请注意data 不是当前车
 * 只有已激活车，才进的了此页
 */
public class ViewCarmanBlueNew extends RelativeLayoutBase {
    private static final String TAG = ViewCarmanBlueNew.class.getSimpleName();
    public static ViewCarmanBlueNew viewCarmanBlueThis;
    private static DataCarInfo data;

    private final ClipTitleMeSet title_head;
    private final TextView txt_blue_state;
    private final TextView txt_intro;
    private final TextView input_code;
    private final Button btn_confirm;
    private final ImageView img_scan;
    private List<MyBluetoothDevice> scanedDevices;

    public ViewCarmanBlueNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_blue_set_new, this, true);
        title_head = findViewById(R.id.title_head);
        txt_blue_state = findViewById(R.id.txt_blue_state);
        txt_intro = findViewById(R.id.txt_intro);
        input_code = findViewById(R.id.input_code);
        btn_confirm = findViewById(R.id.btn_confirm);
        img_scan = findViewById(R.id.img_scan);
        getAderess();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MINI_BIND_RESULT, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewCarmanBlueThis = this;
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
        viewCarmanBlueThis = null;
        BlueScanList.getInstance().scanDeviceList(null, false, null);
        super.onDetachedFromWindow();
    }

    public static void setData(DataCarInfo datacar) {
        data = datacar;
        ManagerCarList.getInstance().setCurrentCar(datacar.ide);
    }

    @Override
    public void initViews() {
        data = ManagerCarList.getInstance().getCarByID(data.ide);
        invalidateUI();
        if (data == null) return;
        scanList();
    }

    private void scanList() {
        if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "正在扫描蓝牙");
            return;//未开蓝牙不扫秒
        }
        long now = System.currentTimeMillis();
        if (now - preScanTime < 5000L) return;//未到时间不扫
        preScanTime = now;
        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "正在扫描蓝牙");
        BlueScanList.getInstance().scanDeviceList(getContext(), true, listenerList);
    }

    BlueScanList.OnScanBlueListener listenerList = new BlueScanList.OnScanBlueListener() {
//        @Override
//        public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
//            if (BuildConfig.DEBUG) Log.e(TAG, "onScanedDeviceList:" + deviceList.toString());
//            if (deviceList != null && deviceList.size() > 0) {
//                scanedDevices = deviceList;
//                //显示
////                final List<BluetoothDevice> newDeviceList = new ArrayList<>();
////                if (scanedDevices.size() > 0) {
////                    for (int i = 0; i < scanedDevices.size(); i++) {
////                        BluetoothDevice device = scanedDevices.get(i);
////                        if (device != null) {
////                            if (device.getName().startsWith("BLE") || device.getName().startsWith("0")) {
////                                newDeviceList.add(scanedDevices.get(i));
////                            }
////                        }
////                    }
////                }
////
////                final String[] arr = new String[newDeviceList.size()];
////                for (int i = 0; i < newDeviceList.size(); i++) {
////                    BluetoothDevice devices = newDeviceList.get(i);
////                    if (devices != null) {
////                        String blueName = devices.getName();
////                        if (!TextUtils.isEmpty(blueName)) {
////                            if (blueName.startsWith("BLE") || blueName.startsWith("0")) {
////                                arr[i] = blueName;
////                            }
////                        }
////                    }
////                }
//            }
//        }

        @Override
        public void onScanedDeviceList(List<MyBluetoothDevice> deviceList) {
            if (BuildConfig.DEBUG) Log.e(TAG, "onScanedDeviceList:" + deviceList.toString());
            if (deviceList != null && deviceList.size() > 0) {
                scanedDevices = deviceList;
                //显示
//                final List<BluetoothDevice> newDeviceList = new ArrayList<>();
//                if (scanedDevices.size() > 0) {
//                    for (int i = 0; i < scanedDevices.size(); i++) {
//                        BluetoothDevice device = scanedDevices.get(i);
//                        if (device != null) {
//                            if (device.getName().startsWith("BLE") || device.getName().startsWith("0")) {
//                                newDeviceList.add(scanedDevices.get(i));
//                            }
//                        }
//                    }
//                }
//
//                final String[] arr = new String[newDeviceList.size()];
//                for (int i = 0; i < newDeviceList.size(); i++) {
//                    BluetoothDevice devices = newDeviceList.get(i);
//                    if (devices != null) {
//                        String blueName = devices.getName();
//                        if (!TextUtils.isEmpty(blueName)) {
//                            if (blueName.startsWith("BLE") || blueName.startsWith("0")) {
//                                arr[i] = blueName;
//                            }
//                        }
//                    }
//                }
            }
        }

        @Override
        public void onScanStop() {
        }
    };

    private long preScanTime = 0;
    /**
     * 检查是否在蓝牙列表里
     * */
    private boolean checkBlueIsInList(String blueName){
        if(scanedDevices==null||scanedDevices.size()==0){
          if(BuildConfig.DEBUG) Log.e(TAG, "扫描的蓝牙列表为空");
        }
        if(TextUtils.isEmpty(blueName)){
            if(BuildConfig.DEBUG) Log.e(TAG, "扫描框没扫描到蓝牙名字");
        }
        if(scanedDevices!=null&&scanedDevices.size()>0){
            for (int i = 0; i < scanedDevices.size(); i++) {
                if(scanedDevices.get(i)!=null&&scanedDevices.get(i).getName()!=null&&scanedDevices.get(i).getName().contains(blueName)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查是否在蓝牙列表里
     * */
    private boolean checkBlueIsInBondList(String blueName){
        Set<BluetoothDevice> deviceList1= BlueAdapter.getInstance().getBondedDevice(GlobalContext.getContext());
        if(deviceList1!=null&&deviceList1.size()>0){
            for (BluetoothDevice device : deviceList1) {
                if(!TextUtils.isEmpty( blueName)&&!TextUtils.isEmpty(device.getName())&&blueName.equals(device.getName())){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void initEvents() {
        img_scan.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                //拍照权限
                boolean needPermission = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        needPermission = true;
                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }
                if (!needPermission) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), CaptureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("scantype", "oned");
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(mLocationClient!=null){
                    mLocationClient.stop();
                    mLocationClient=null;
                }
                data = ManagerCarList.getInstance().getCarByID(data.ide);
                BlueLinkNetSwitch.setSwitchBlueModel(false, data.ide, "CarmanBlue back net");//切回网络模式
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            }
        });
        input_code.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                    BluePermission.openBlueTooth(getCurrentActivity());
                    return;
                }
                scanList();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                OCtrlBlueTooth.getInstance().ccmd_10002_bind_mini("NFC#123456",data.ide, MyLocationListener.myCity);//蓝牙名字，ide,所在城市
                String blueDeviceName = input_code.getText().toString();//名称有空格
                if (blueDeviceName.length() == 0) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "蓝牙未输入名称");
                    return;
                }
                if(!checkBlueIsInList(blueDeviceName)&&!checkBlueIsInBondList(blueDeviceName)){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未识别到蓝牙名称或者蓝牙名称不在附近蓝牙列表中");
                    BlueScanList.getInstance().scanDeviceList(getContext(), true, listenerList);
                    return;
                }
                if(TextUtils.isEmpty(MyLocationListener.myCity)){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未定位成功");
                    return;
                }
                OCtrlBlueTooth.getInstance().ccmd_10002_bind_mini(blueDeviceName,data.ide, MyLocationListener.myCity);//蓝牙名字，ide,所在城市
//                OCtrlBlueTooth.getInstance().ccmd_10002_bind_mini(blueDeviceName,data.ide, "广东-东莞");//蓝牙名字，ide,所在城市
            }
        });

    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    public void invalidateUI() {
        if (data == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无车辆数据");
            return;
        }
        data = ManagerCarList.getInstance().getCarByID(data.ide);
        if (data == null) return;
        txt_blue_state.setText("蓝牙未绑定，请绑定");
        txt_intro.setText("请打开手机蓝牙，绑定蓝牙需在车辆旁边（由安装工程师操作），部分硬件不支持蓝牙设备。");
        input_code.setVisibility(VISIBLE);
        btn_confirm.setBackground(getContext().getResources().getDrawable(R.drawable.bgst_round8_467d9f));
        handleCheckConfirmShow();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObject) {
        data = ManagerCarList.getInstance().getCarByID(data.ide);
        if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {//扫描激活码
            String tst = (String) paramObject;
            handleSetText(tst);
        }else if(eventName.equals(OEventName.MINI_BIND_RESULT)){
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆激活成功!");
        }
    }

    //=============================================================
    public void handleCheckConfirmShow() {
        Message message = new Message();
        message.what = 326;
        handler.sendMessage(message);
    }

    public void handleSetText(String result) {
        Message message = new Message();
        message.what = 327;
        message.obj = result;
        handler.sendMessage(message);
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 326) {
                if (input_code.getText().toString().length() > 0) {
                    btn_confirm.setText("绑定");
                    btn_confirm.setEnabled(true);
                    btn_confirm.setAlpha(1f);
                } else {
                    btn_confirm.setText("绑定");
                    btn_confirm.setEnabled(false);
                    btn_confirm.setAlpha(0.5f);
                }
            } else if (msg.what == 327) {
                String tst = (String) msg.obj;
                if (!TextUtils.isEmpty(tst)) {
                    input_code.setText(tst);
                    btn_confirm.setText("绑定");
                    btn_confirm.setEnabled(true);
                    btn_confirm.setAlpha(1f);
                }
            }
        }
    };
    LocationClient  mLocationClient;
    private MyLocationListener myListener = new MyLocationListener();
    private void getAderess(){
        LocationClient.setAgreePrivacy(true);
        try {
            if(mLocationClient==null){
                mLocationClient = new LocationClient(GlobalContext.getContext());
                //声明LocationClient类
                mLocationClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                option.setScanSpan(1500);
                option.setOpenGps(true);
                mLocationClient.setLocOption(option);
                mLocationClient.start();
            }
        } catch (Exception e) {

        }
    }

    // ===================================================
}
