package view.view4me.carmanage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.zxing.activity.CaptureActivity;

import java.util.List;
import java.util.Set;

import common.GlobalContext;
import common.blue.BlueScanList;
import ctrl.OCtrlBlueTooth;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.ActivityWeb;
import view.clip.ClipPopLoadingActive;
import view.view4me.set.ClipTitleMeSet;

import static common.GlobalContext.getCurrentActivity;

/**
 * 车辆模组，进入此页，先设data
 */
public class ViewCarmanModelBind extends RelativeLayoutBase {
    public static DataCarInfo    data;
    public static String    equipmentNunber;
    private ClipTitleMeSet title_head;
    private       RelativeLayout lin_input;
    private       LinearLayout   lin_agreement;
    private       TextView       txt_model_state, txt_agreement, txt_intro;
    private EditText  input_code;
    private ImageView img_scan, img_check;
    private Button btn_confirm;
    private TextView txt_text,btn_cancel,btn_sure;
    protected MyHandler handler = new MyHandler();
    private List<MyBluetoothDevice> scanedDevices;
    private LinearLayout lin_shows;
    private int switchState=-1;
    public ViewCarmanModelBind(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_model_set_bind, this, true);
        title_head =  findViewById(R.id.title_head);
        lin_input = (RelativeLayout) findViewById(R.id.lin_input);
        lin_agreement = (LinearLayout) findViewById(R.id.lin_agreement);
        txt_model_state = (TextView) findViewById(R.id.txt_model_state);
        txt_agreement = (TextView) findViewById(R.id.txt_agreement);
        txt_intro = (TextView) findViewById(R.id.txt_intro);
        input_code = (EditText) findViewById(R.id.input_code);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        img_check = (ImageView) findViewById(R.id.img_check);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        lin_shows = findViewById(R.id.lin_shows);
        txt_text = findViewById(R.id.txt_text);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_sure = findViewById(R.id.btn_sure);
        img_check.setSelected(true);
        if(!TextUtils.isEmpty(equipmentNunber)){
            if(equipmentNunber.startsWith("MIN")||equipmentNunber.startsWith("NFC")){
                checkBlueToothAndGpsMINI();
            }
        }else{
            checkBlueToothAndGpsMINIDontSure();
        }
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_ACTIVATE_SUCESS, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.MINI_BIND_RESULT, this);
    }

    private boolean checkBlueToothAndGpsMINI(){
        if(GpsUtil.isOPen(getContext())){
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","绑定MINI蓝牙gps已打开，蓝牙未打开");
                txt_text.setText("绑定MINI版需要打开蓝牙");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=0;
                getAderess();
            }else{
                Log.e("蓝牙gps","绑定MINI蓝牙gps蓝牙都已打开");
                getAderess();
                scanList();
                return true;
            }
        }else{
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","绑定MINI蓝牙gps均未打开");
                txt_text.setText("绑定MINI版需要打开蓝牙并且开启gps开关");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=1;
            }else{
                txt_text.setText("绑定MINI版需要开启gps开关");
                Log.e("蓝牙gps","绑定MINI蓝牙gps未打开,蓝牙已打开");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=2;
                scanList();
            }
        }
        return false;
    }
    private boolean canScan=true;
    private boolean checkBlueToothAndGpsMINIDONTScan(){
        if(GpsUtil.isOPen(getContext())){
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","绑定MINI蓝牙gps已打开，蓝牙未打开");
                txt_text.setText("绑定MINI版需要打开蓝牙");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=0;
            }else{
                if(canScan){
                    getAderess();
                    scanList();
                    canScan=false;
                }
                Log.e("蓝牙gps","绑定MINI蓝牙gps蓝牙都已打开");
                return true;
            }
        }else{
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","绑定MINI蓝牙gps均未打开");
                txt_text.setText("绑定MINI版需要打开蓝牙并且开启gps开关");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=1;
            }else{
                txt_text.setText("绑定MINI版需要开启gps开关");
                Log.e("蓝牙gps","绑定MINI蓝牙gps未打开,蓝牙已打开");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=2;
            }
        }
        return false;
    }
    private void checkBlueToothAndGpsMINIDontSure(){
        if(GpsUtil.isOPen(getContext())){
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","手动输入蓝牙gps已打开，蓝牙未打开");
                txt_text.setText("如需绑定MINI版需要打开蓝牙");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=0;
                getAderess();
            }else{
                Log.e("蓝牙gps","手动输入蓝牙gps蓝牙都已打开");
                getAderess();
                scanList();
            }
        }else{
            if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
                Log.e("蓝牙gps","手动输入蓝牙gps均未打开");
                txt_text.setText("如需绑定MINI版需要打开蓝牙并且开启gps开关");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=1;
            }else{
                txt_text.setText("如需绑定MINI版需要开启gps开关");
                Log.e("蓝牙gps","手动输入蓝牙gps未打开,蓝牙已打开");
                lin_shows.setVisibility(View.VISIBLE);
                switchState=2;
                scanList();
            }
        }
    }

    @Override
    public void initViews() {
        invalidateUI();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BlueScanList.getInstance().scanDeviceList(getContext(), false, null);
        if(mLocationClient!=null){
            mLocationClient.stop();
        }
    }

    private    LocationClient  mLocationClient;
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
    private long preScanTime = 0;
    private void scanList() {
        if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
            return;//未开蓝牙不扫秒
        }
        long now = System.currentTimeMillis();
        if (now - preScanTime < 5000L) return;//未到时间不扫
        preScanTime = now;
//        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "正在扫描蓝牙");
        BlueScanList.getInstance().scanDeviceList(getContext(), true, listenerList);
    }

    BlueScanList.OnScanBlueListener listenerList = new BlueScanList.OnScanBlueListener() {
//        @Override
//        public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
//            if (BuildConfig.DEBUG) Log.e("TAG", "onScanedDeviceList:" + deviceList.toString());
//            if (deviceList != null && deviceList.size() > 0) {
//                scanedDevices = deviceList;
//            }
//        }

        @Override
        public void onScanedDeviceList(List<MyBluetoothDevice> deviceList) {
            if (BuildConfig.DEBUG) Log.e("TAG", "onScanedDeviceList:" + deviceList.toString());
            if (deviceList != null && deviceList.size() > 0) {
                scanedDevices = deviceList;
            }
        }

        @Override
        public void onScanStop() {
        }
    };

    /**
     * 检查是否在蓝牙列表里
     * */
    private boolean checkBlueIsInList(String blueName){
        if(scanedDevices==null||scanedDevices.size()==0){
            if(BuildConfig.DEBUG) Log.e("TAG", "扫描的蓝牙列表为空");
        }
        if(TextUtils.isEmpty(blueName)){
            if(BuildConfig.DEBUG) Log.e("TAG", "扫描框没扫描到蓝牙名字");
        }
        if(scanedDevices!=null&&scanedDevices.size()>0){
            for (int i = 0; i < scanedDevices.size(); i++) {
                if(BuildConfig.DEBUG) Log.e("TAG", "是否在"+scanedDevices.get(i).getName());
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
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
               lin_shows.setVisibility(View.GONE);
            }
        });
        btn_sure.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(switchState==0){
                    BluePermission.openBlueTooth(getCurrentActivity());
                }else if(switchState==1){
                    BluePermission.openBlueTooth(getCurrentActivity());
                    GpsUtil.openGPS(getContext());
                }else if(switchState==2){
                    GpsUtil.openGPS(getContext());
                }
                lin_shows.setVisibility(View.GONE);
            }
        });


        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(btn_confirm.getText().toString().equals("激活")) {
                    if(!TextUtils.isEmpty(input_code.getText().toString())){
                        if(input_code.getText().toString().startsWith("MIN")||input_code.getText().toString().startsWith("NFC")){
                            String blueDeviceName = input_code.getText().toString();
                            if (blueDeviceName.length() == 0) {
                                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "蓝牙未输入名称");
                                return;
                            }
                            if(!checkBlueToothAndGpsMINIDONTScan()){
                                return;
                            }
                            if(!checkBlueIsInList(blueDeviceName)&&!checkBlueIsInBondList(blueDeviceName)){
                                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未识别到蓝牙名称或者蓝牙名称不在附近蓝牙列表中");
                                BlueScanList.getInstance().scanDeviceList(getContext(), true, listenerList);
                                return;
                            }
//                            if(TextUtils.isEmpty(MyLocationListener.myCity)){
//                                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未定位成功");
//                                return;
//                            }
                            OCtrlBlueTooth.getInstance().ccmd_10002_bind_mini(blueDeviceName,data.ide, "广东-东莞");//蓝牙名字，ide,所在城市
//                            OCtrlBlueTooth.getInstance().ccmd_10002_bind_mini(blueDeviceName,data.ide, MyLocationListener.myCity);//蓝牙名字，ide,所在城市
                        }else{
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(),null,false)
                                    .withTitle("激活需知")
                                    .withInfo("设备需接收到GPS以及GSM信号后才能被" +
                                            "激活，点击激活前请按以下要求操作：\n" +
                                            "1.确认安全的前提下，将车辆启动一次；\n" +
                                            "2.将车辆移至能接收GPS信号的户外；\n" +
                                            "3.接收信号将耗时几分钟，请耐心等待；")
                                    .withButton("先去操作","继续激活")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if(isClickConfirm){
                                                OCtrlCar.getInstance().ccmd1204_activatecar(data.ide, input_code.getText().toString());
//                                        ToastConfirmData dataa = new ToastConfirmData("激活车辆", "确定要使用激活码: "+input_code.getText()+" 激活车辆: "+data.num+" 吗?",new ToastConfirmData.OnConfirmClickListener() {
//                                            @Override
//                                            public void onConfirm(boolean isClickConfirm) {
//                                                if (isClickConfirm ) {
//                                                    OCtrlCar.getInstance().ccmd1204_activatecar(data.ide, input_code.getText().toString());
//                                                }
//                                            }
//                                        });
//                                        dataa.setOther("", "",false, R.color.red_dark);
//                                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_CONFIRM, dataa);
                                            }
                                        }
                                    }).show();
                        }
                    }
                }
            }
        });
        img_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_check.setSelected(!img_check.isSelected());
                if(img_check.isSelected()){
                    img_check.setImageResource(R.drawable.check_ok_white);
                }else{
                    img_check.setImageResource(R.drawable.check_fail_white);
                }
                handleCheckConfirmShow();
            }
        });
        //打开用户协义
        txt_agreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address;
                try {
                    ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                } catch (PackageManager.NameNotFoundException e) {
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                }
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                handleCheckConfirmShow();
            }
        });
        //扫描
        img_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照权限
                boolean needPermission = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        needPermission = true;
                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }
                if(!needPermission) {
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
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_ACTIVATE_SUCESS)) {
            handleShowLoading();
//            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
//
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆激活成功!");
//            ManagerCarList.getInstance().setCurrentCar(data.ide);
//            data = ManagerCarList.getInstance().getCarByID(data.ide);
//            handleChangeData();
        }else if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {//扫描激活码
            String tst = (String) paramObj;
            handleScanBack(tst);
        }else if(eventName.equals(OEventName.MINI_BIND_RESULT)){
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆激活成功!");
        }
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
            txt_model_state.setText("模组未激活，请先激活");
            txt_intro.setText("激活码是产品模组上的硬件码，用户购买产品后，由安装工程师操作激活。");
            lin_input.setVisibility(VISIBLE);
            input_code.setFocusable(true);
            input_code.setVisibility(View.VISIBLE);
            lin_agreement.setVisibility(VISIBLE);
            if(TextUtils.isEmpty(equipmentNunber)){
                input_code.setHint("请输入激活码");
            }else{
                input_code.setText(equipmentNunber);
            }
            handleCheckConfirmShow();
    }
    // ===================================================
    private void handleScanBack(String result) {
        Message message = new Message();
        message.what = 325;
        message.obj = result;
        handler.sendMessage(message);
    }
    public void handleCheckConfirmShow() {
        Message message = new Message();
        message.what = 326;
        handler.sendMessage(message);
    }
    public void handleShowLoading() {
        Message message = new Message();
        message.what = 327;
        handler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 325 :
                    String tst = (String) msg.obj;
                    input_code.setText(tst);
                    break;
                case 326:
                    if(data.isMyCar == 0)return;
                    if(data.isActive == 1)return;
                    if(img_check.isSelected() && input_code.getText().toString().length()>0){
                        btn_confirm.setText("激活");
                        btn_confirm.setEnabled(true);
                        btn_confirm.setAlpha(1f);
                    }else {
                        btn_confirm.setText("激活");
                        btn_confirm.setEnabled(false);
                        btn_confirm.setAlpha(0.5f);
                    }
                    break;
                case 327 :
                    ClipPopLoadingActive.getInstance().show(title_head);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ClipPopLoadingActive.getInstance().stopLoading();
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆激活成功!");
                            ManagerCarList.getInstance().setCurrentCar(data.ide);
                        }
                    },1500);
                    break;
            }
        }
    }
    // ===================================================
}
