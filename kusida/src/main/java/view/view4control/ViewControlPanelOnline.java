                                                                                                                                                                                                                       package view.view4control;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnClickListnerZhu;

import com.kulala.staticsview.toast.ToastTxt;
import com.kulala.staticsview.tools.ActivityUtils;

import java.text.DecimalFormat;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import model.DataDisPlay;
import model.ManagerCarList;
import model.ManagerDisPlay;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import view.EquipmentManager;
import view.clip.ClipPopLoading;
import view.view4app.lendcartemporary.ActivityLendCarTemporary;
import view.ActivityWebDisPla;


public class ViewControlPanelOnline extends RelativeLayoutBase {
    private final ImageView img_refresh;
//    private final ImageView img_switch_bluetooth;
    private final ImageView img_switch_air_conditioning;
//    private final ImageView img_battery;
//    private final ImageView img_wifi;
//    private final ImageView img_gps;
//    private final ImageView img_temperature;
//    private final ImageView img_net_model;
//    private final TextView txt_battery;
    private final ImageView img_meter;
    private final TextView txt_meter;
    private final TextView txt_meter_km;
    private final ImageView img_oil;
    private final TextView txt_oil;
    private final TextView txt_oil_l;
    private final ImageView img_temporary_borrow_car;



//    private final TextView txt_wifi;
//    private final TextView txt_gps;
//    private final TextView txt_temperature;
//    private final TextView txt_net_model;
    public static int currentPos = -1;
    private CountDownTimerMy countControlTimer;
    private final ViewClipSeekBar seekBarLeftDoor;
    private final ViewClipSeekBar seekBarRightDoor;
    private long optionTime;
    private ImageView iv_alibaba;
    private RelativeLayout progress_layout;
    private TextView load_txt;
    private LinearLayout left_seek_layout,right_seek_layout;

    public ViewControlPanelOnline(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_control_panel_online, this, true);
//        img_battery = findViewById(R.id.img_battery);
//        img_wifi = findViewById(R.id.img_wifi);
//        img_gps = findViewById(R.id.img_gps);
        img_refresh = findViewById(R.id.img_refresh);
//        img_switch_bluetooth = findViewById(R.id.img_switch_bluetooth);
//        img_switch_bluetooth.setVisibility(View.INVISIBLE);
//        img_temperature = findViewById(R.id.img_temperature);
//        txt_battery = findViewById(R.id.txt_battery);
        img_meter = findViewById(R.id.img_meter);
        txt_meter = findViewById(R.id.txt_meter);
        txt_meter_km = findViewById(R.id.txt_meter_km);
        img_oil = findViewById(R.id.img_oil);
        txt_oil = findViewById(R.id.txt_oil);
        txt_oil_l = findViewById(R.id.txt_oil_l);
        img_temporary_borrow_car= findViewById(R.id.img_temporary_borrow_car);
        iv_alibaba= findViewById(R.id.iv_alibaba);
        //        txt_wifi = findViewById(R.id.txt_wifi);
//        txt_gps = findViewById(R.id.txt_gps);
//        txt_temperature = findViewById(R.id.txt_temperature);
//        img_net_model = findViewById(R.id.img_net_model);
//        txt_net_model = findViewById(R.id.txt_net_model);
        img_switch_air_conditioning = findViewById(R.id.img_switch_air_conditioning);//空调开关
        seekBarLeftDoor= findViewById(R.id.seekbar_leftdoor);
        seekBarRightDoor= findViewById(R.id.seekbar_right_door);
        progress_layout= findViewById(R.id.progress_layout);
        load_txt= findViewById(R.id.load_txt);
        left_seek_layout= findViewById(R.id.left_seek_layout);
        right_seek_layout= findViewById(R.id.right_seek_layout);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE, this);//车辆增删时变化
        ODispatcher.addEventListener(OEventName.PRESS_REFRESH_RESULTOK, this);
        ODispatcher.addEventListener(OEventName.CAR_SELF_REFRESH_STATUS, this);
        ODispatcher.addEventListener(OEventName.DISPLAY_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.SHOW_PROGRESS_DIOLOG, this);
        ODispatcher.addEventListener(OEventName.HIDE_PROGRESS_DIOLOG, this);
    }


    public void initViews() {
        handleChangeData();
    }

    private long preRefreshTime = 0;
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
    public static void jumpAlipay(Context context) {
        try {
            String uri = "alipayqr://platformapi/startapp?appId=2021002146620507";
//                    + "&page=pages/index/index?userId=123456"//页面参数
//                    + "&query=itemId=005007";//启动参数
            Intent intent = Intent.parseUri(uri, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("支付宝跳转失败").quicklyShow();
            e.printStackTrace();
        }
    }

    public void initEvents() {
        iv_alibaba.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo;
                if(!TextUtils.isEmpty(disPlay.url)){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(ActivityWebDisPla.TITLE_NAME, "去支付宝认证");
                    String address=disPlay.url;
                    bundle.putString(ActivityWebDisPla.HTTP_ADDRESS, address);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), ActivityWebDisPla.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });
        seekBarLeftDoor.setOnThumbChangeListner(new ViewClipSeekBar.OnThumbChangeListner() {
            @Override
            public void getThumbChange(int location) {
                if ( EquipmentManager.isMini()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
                    return;
                }
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car!=null&&car.isActive==0) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前车辆未激活").quicklyShow();
                    return;
                }
                DataCarStatus currentStatus=ManagerCarList.getInstance().getCurrentCarStatus();
                if(car!=null&&currentStatus!=null&&car.isActive==1){
//                    if(currentStatus.isTheft==1){
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "当前操作无效，请先解防后再操作");
//                         if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "location"+location);
//                        seekBarLeftDoor.setThumbLocation(location==0?1:0);
//                        return ;
//                    }
                    if(location==0){
                        OCtrlCar.getInstance().ccmd1252_middle_door_control(car.ide,1,1);
                    }else{
                        OCtrlCar.getInstance().ccmd1252_middle_door_control(car.ide,0,1);
                    }
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "指令已发送");
                    optionTime=System.currentTimeMillis();
                    Toast toast= Toast.makeText(getContext(),"指令已发送",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, ODipToPx.dipToPx(getContext(), 175));
                    toast.show();
                     if (BuildConfig.DEBUG) Log.e("查看指令", "指令已发送");
                    new TimeDelayTask().runTask(10000, new TimeDelayTask.OnTimeEndListener() {
                        @Override
                        public void onTimeEnd() {
                            //5秒获取车状态
                             if (BuildConfig.DEBUG) Log.e("查看发送时间", ""+System.currentTimeMillis() );
                            getCarStatus();
                        }
                    });
                }
            }
        });
        seekBarRightDoor.setOnThumbChangeListner(new ViewClipSeekBar.OnThumbChangeListner() {
            @Override
            public void getThumbChange(int location) {
                if ( EquipmentManager.isMini()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
                    return;
                }
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car!=null&&car.isActive==0) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前车辆未激活").quicklyShow();
                    return;
                }
                DataCarStatus currentStatus=ManagerCarList.getInstance().getCurrentCarStatus();
                if(car!=null&&currentStatus!=null&&car.isActive==1){
                    if(currentStatus.isTheft==1){
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "当前操作无效，请先解防后再操作");
                        seekBarRightDoor.setThumbLocation(location==0?0:1);
                        return ;
                    }
                    if(location==0){
                        OCtrlCar.getInstance().ccmd1252_middle_door_control(car.ide,1,2);
                    }else{
                        OCtrlCar.getInstance().ccmd1252_middle_door_control(car.ide,0,2);
                    }
                    optionTime=System.currentTimeMillis();//操作中门开关发指令的时间
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "指令已发送");
                    Toast toast= Toast.makeText(getContext(),"指令已发送",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, ODipToPx.dipToPx(getContext(), 175));
                    toast.show();
                     if (BuildConfig.DEBUG) Log.e("查看发送时间", "发协议时间"+System.currentTimeMillis() );
                    new TimeDelayTask().runTask(10000, new TimeDelayTask.OnTimeEndListener() {
                        @Override
                        public void onTimeEnd() {
                            //5秒获取车状态
                             if (BuildConfig.DEBUG) Log.e("查看发送时间", "获取车辆状态时间"+System.currentTimeMillis() );
                            getCarStatus();
                        }
                    });
                }
            }
        });

        img_switch_air_conditioning.setOnClickListener(new OnClickListnerZhu() {
            @Override
            public void onClickNoFast(View v) {
                long nowTime=System.currentTimeMillis();
                if(ClipPopControlAirConditionNew.time==0||nowTime-ClipPopControlAirConditionNew.time>7000L){
                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                    if (currentCar == null) return;
                    Log.i("abcde", "显示弹框2");
                    ClipPopControlAirConditionNew.getInstance().show(currentCar, img_switch_air_conditioning);
                }else{
                    new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo("请勿频繁控制空调").show();
                }
            }
        });
        img_temporary_borrow_car.setOnClickListener(new OnClickListnerZhu() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLendCarTemporary.class);
            }
        });

        //2018/08/02 去掉车辆页皮肤入口
//        txt_go_dressup.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_car_dressup);
//            }
//        });
        img_refresh.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(EquipmentManager.isMini()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo("当前设备不支持").show();
                    return;
                }
                long now = System.currentTimeMillis();
                if(now - preRefreshTime<1500)return;
                preRefreshTime = now;
                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if (currentCar == null) {
                    return;
                }
                if (currentCar.isActive == 0) {//无车,未激活,跳转激活
                    boolean isDemoMode = DemoMode.getIsDemoMode();
                    if (isDemoMode) {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_TOAST_SCALE, getResources().getString(R.string.is_to_refresh_the_vehicle_state));
                        if(!BlueLinkReceiver.getIsBlueConnOK()) {
                            handleStartLoading();
                            OCtrlCar.getInstance().ccmd1219_getCarState(currentCar.ide, 1, true);
                        }
                        return;
                    } else {
                        return;
                    }
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_TOAST_SCALE, getResources().getString(R.string.is_to_refresh_the_vehicle_state));
                if(!BlueLinkReceiver.getIsBlueConnOK()) {
                    handleStartLoading();
                    OCtrlCar.getInstance().ccmd1219_getCarState(currentCar.ide, 0, true);
                }
            }

        });
//        img_switch_bluetooth.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                 if (BuildConfig.DEBUG) Log.e("blueCNOK",""+BlueLinkReceiver.getIsBlueConnOK());
//                OToastSelectBlueOrNet.getInstance().show(img_switch_bluetooth);
//                OToastSelectBlueOrNet.getInstance().setonChooseListener(new OToastSelectBlueOrNet.OnChooseListener() {
//                    @Override
//                    public void OnChoose(boolean isBluetooth) {
//                        final DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//                        if (isBluetooth) {//选择蓝牙方式
//                            if (carInfo.isBindBluetooth == 0) {//无蓝牙的车
//                                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.blue_tooth_this_car_unbind));
//                            } else {//有绑蓝牙的车
//                                //未开蓝牙去打开
//                                if (BluePermission.checkPermission(GlobalContext.getCurrentActivity()) != 1) {
//                                    //弹出打开蓝牙面板
//                                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//                                            .withTitle("蓝牙模式")
//                                            .withInfo("请打开蓝牙，否则无法切换到蓝牙模式。")
//                                            .withButton("网络模式", "去打开")
//                                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                                                @Override
//                                                public void onClickConfirm(boolean isClickConfirm) {
//                                                    if (isClickConfirm) {//点确认
//                                                        BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
//                                                        BlueLinkNetSwitch.setSwitchBlueModel(true, carInfo.ide,"img_switch_bluetooth.setOnClickListener confirm");//handleChangeBlueState();
//                                                    }
//                                                }
//                                            })
//                                            .show();
//                                } else {
//                                    BlueLinkNetSwitch.setSwitchBlueModel(true, carInfo.ide,"img_switch_bluetooth.setOnClick confrim");//handleChangeBlueState();
//                                    // 打开蓝牙并允许重连
//                                    BlueLinkReceiver.needChangeCar(carInfo.ide,carInfo.bluetoothName,carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig);
//                                }
//                            }
//                        } else {//选择网络方式
//                            if (SystemMe.isNetworkConnected(getContext())) {//网络已开
//                                BlueLinkNetSwitch.setSwitchBlueModel(false, carInfo.ide,"img_switch_bluetooth.setOnClick net");
//                            } else {//网络未开
//                                //弹出打开网络面板
//                                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//                                        .withTitle("网络连接失败")
//                                        .withInfo("无法连接网络,请检查网络后再试,您也可以使用蓝牙连接。")
//                                        .withButton("蓝牙模式", "重试")
//                                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                                            @Override
//                                            public void onClickConfirm(boolean isClickConfirm) {
//                                                if (isClickConfirm) {//点确认
//                                                    BlueLinkNetSwitch.setSwitchBlueModel(false, carInfo.ide,"img_switch_bluetooth.setOnClick net");
//                                                } else {//点取消
//                                                    BlueLinkNetSwitch.setSwitchBlueModel(true, carInfo.ide,"img_switch_bluetooth.setOnClick net");
//                                                }
//                                            }
//                                        })
//                                        .show();
//                            }
//                        }
//                    }
//                });
//            }
//        });
    }


    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.PRESS_REFRESH_RESULTOK)) {
            handleStopLoading();
            boolean resultOK = (Boolean) paramObj;
            if (resultOK) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "刷新成功！");
            } else {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "刷新失败!");
            }
        }else  if (eventName.equals(OEventName.CAR_SELF_REFRESH_STATUS)){
             if (BuildConfig.DEBUG) Log.e("查看发送时间", "车辆状态回包时间"+System.currentTimeMillis() );
            long getDataTime=System.currentTimeMillis();
            if((getDataTime-optionTime)>10*1000||optionTime==0){
                 if (BuildConfig.DEBUG) Log.e("查看发送时间", "刷新UI" );
                handleSetDoorUI();
            }
        }else  if (eventName.equals(OEventName.DISPLAY_RESULT_BACK)){
            handleSetDisPlay();
        }else  if (eventName.equals(OEventName.SHOW_PROGRESS_DIOLOG)){
            int arg=(Integer)paramObj;
            handleShowProdilog(arg);
        }else  if (eventName.equals(OEventName.HIDE_PROGRESS_DIOLOG)){
            handleHideProdilog();
        }
    }
    /**设置中门开关*/
    private void handleSetDisPlay() {
        Message message = new Message();
        message.what = 18659;
        handler.sendMessage(message);
    }
    /**设置中门开关*/
    public void handleSetDoorUI() {
        Message message = new Message();
        message.what = 18652;
        handler.sendMessage(message);
    }
    /**显示dialog*/
    public void handleShowProdilog(int arg) {
        Message message = new Message();
        message.what = 11111;
        message.arg1=arg;
        handler.sendMessage(message);
    }
    /**隐藏dialog*/
    public void handleHideProdilog() {
        Message message = new Message();
        message.what = 11112;
        handler.sendMessage(message);
    }


    @Override
    public void callback(String key, Object value) {
        if (key.equals("activecar")) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
        }
    }
    /*主動自己去拿车辆状态为了中门开关*/
    private void getCarStatus(){
         if (BuildConfig.DEBUG) Log.e("ViewControlPanelOnline", "测试是否主動拿状态 " );
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(!BlueLinkReceiver.getIsBlueConnOK()){
            if (car == null) return;
            if (car.ide == 0) return;//未激活车，不请求
            if (car.isActive == 1) {
//                             if (BuildConfig.DEBUG) Log.e("Online","ccmd1219_getCarState:"+car.ide);
                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0,true,0
                );
            }else{
                if (DemoMode.getIsDemoMode()) {
                    OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 1,true,1);
                }
            }
        }
    }




    //打开Loading计时,6秒停止
    private void handleStartLoading() {
        if (countControlTimer == null) countControlTimer = new CountDownTimerMy(6000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (ClipPopLoading.getInstance().getIsShowing()) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.send_the_timeout));
                    handleStopLoading();
                }
            }
        };
        countControlTimer.cancel();
        countControlTimer.start();
        Message message = new Message();
        message.what = 18650;
        handler.sendMessage(message);
    }

    public void handleStopLoading() {
        Message message = new Message();
        message.what = 18651;
        handler.sendMessage(message);
    }

  private final  Handler handler=new  Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 18650:
                    ClipPopLoading.getInstance().show(img_refresh);
                    break;
                case 18651:
                    ClipPopLoading.getInstance().stopLoading();
                    break;
                case 18652:
                    DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                    if(!ViewClipSeekBar.isScroll){
                        if(status.leftBehindOpen==0){
                             if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "刷新设置的 ");
                            seekBarLeftDoor.setThumbLocation(1);

                        }else{
                            seekBarLeftDoor.setThumbLocation(0);
                        }
                        if(status.rightBehindOpen==0){
                            seekBarRightDoor.setThumbLocation(1);
                        }else{
                            seekBarRightDoor.setThumbLocation(0);
                        }
                    }
                    break;
                case 18659:
                  DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo;
                  if(disPlay==null)return;
                  if(disPlay.isDisplay.equals("1")){
                      iv_alibaba.setVisibility(View.VISIBLE);
                  }else{
                      iv_alibaba.setVisibility(View.INVISIBLE);
                  }
                    break;
                case 11111:
                    int arg=msg.arg1;
                    progress_layout.setVisibility(View.VISIBLE);
                    if(arg==1){
                        load_txt.setText("正在升窗");
                    }else   if(arg==2){
                        load_txt.setText("正在降窗");
                    }else   if(arg==3){
                        load_txt.setText("正在升降后尾箱");
                    }
                    break;
                case 11112:
                    progress_layout.setVisibility(View.INVISIBLE);
                    break;

            }
        }

    };
    private void setMvpDoor( DataCarStatus status){
        if(!ViewClipSeekBar.isScroll){
            if(status.leftBehindOpen==0){
                if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "刷新设置的 ");
                seekBarLeftDoor.setThumbLocation(1);

            }else{
                seekBarLeftDoor.setThumbLocation(0);
            }
            if(status.rightBehindOpen==0){
                seekBarRightDoor.setThumbLocation(1);
            }else{
                seekBarRightDoor.setThumbLocation(0);
            }
        }
    }

    private void imageSet(ImageView image, int resid) {
        if (image.getTag() == null || ((Integer) image.getTag()) != resid) {
            image.setTag(resid);
            image.setImageResource(resid);
        }
    }
    private void textSet(TextView view, String text) {
        if(text.equals(view.getText().toString()))return;
        view.setText(text);
    }
    private void setMeterUI(DataCarStatus custatus){
        if(custatus==null||custatus.miles==0){
//            img_meter.setVisibility(View.INVISIBLE);
            txt_meter.setVisibility(View.INVISIBLE);
            txt_meter_km.setVisibility(View.INVISIBLE);
        }else{
//            img_meter.setVisibility(View.VISIBLE);
            txt_meter.setVisibility(View.VISIBLE);
            txt_meter_km.setVisibility(View.VISIBLE);
            txt_meter_km.setText(String.valueOf(custatus.miles)+"km");
        }
    }
    private void setOilUI(DataCarStatus custatus){
        if(custatus==null||custatus.remainOil==0){
//            img_oil.setVisibility(View.INVISIBLE);
            txt_oil.setVisibility(View.INVISIBLE);
            txt_oil_l.setVisibility(View.INVISIBLE);
        }else{
//            img_oil.setVisibility(View.VISIBLE);
            txt_oil.setVisibility(View.VISIBLE);
            txt_oil_l.setVisibility(View.VISIBLE);
            txt_oil.setText(custatus.remainOil +"L");
        }
    }
    private void setTempoBorrowCarUI(DataCarInfo info){
        if(info==null||info.isActive==0){
            img_temporary_borrow_car.setVisibility(View.INVISIBLE);
        }else{
            img_temporary_borrow_car.setVisibility(View.VISIBLE);
           if(info.authorityEndTime1 > System.currentTimeMillis()) {
               img_temporary_borrow_car.setImageResource(R.drawable.img_tempor_borrow_car_yes);
           }else{
               img_temporary_borrow_car.setImageResource(R.drawable.img_tempor_borrow_car_no);
           }
        }

    }
    //刷新次数太多，要控制
    protected void invalidateUI() {
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car == null||car.isActive==0){//car null
             if (BuildConfig.DEBUG) Log.e("Online","Car null show");
//            textSet(txt_battery,getResources().getString(R.string.voltage));
//            txt_battery.setTextColor(getResources().getColor(R.color.red_dark));
//            img_battery.setImageResource(R.drawable.battery_off);
            setMeterUI(null);
            setOilUI(null);
            setTempoBorrowCarUI(null);
            if(car!=null&&car.carType==3){
                left_seek_layout.setVisibility(View.VISIBLE);
                right_seek_layout.setVisibility(View.VISIBLE);
            }else{
                left_seek_layout.setVisibility(View.INVISIBLE);
                right_seek_layout.setVisibility(View.INVISIBLE);
            }
            if(img_switch_air_conditioning.getVisibility() != INVISIBLE)img_switch_air_conditioning.setVisibility(View.INVISIBLE);
        }else{
            if(car.carType==3){
                left_seek_layout.setVisibility(View.VISIBLE);
                right_seek_layout.setVisibility(View.VISIBLE);
            }else{
                left_seek_layout.setVisibility(View.INVISIBLE);
                right_seek_layout.setVisibility(View.INVISIBLE);
            }
            setTempoBorrowCarUI(car);
            DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
//            long carStatusBackTime=System.currentTimeMillis();
//            if(carStatusBackTime-optionTime>10*1000){
//                setMvpDoor(status);
//            }
            //空调开关
            DataCarStatus netStastus=ManagerCarList.getInstance().getNetCurrentCarStatus();
            //空调开关
            if (netStastus != null) {
                if (netStastus.isStart == 1 && netStastus.isTheft == 1) {
                    if (netStastus.ACStatus == -1 && netStastus.windLevel == -1 && netStastus.tempControlStatus == -1) {
                        if(netStastus.chairLeftAir==null)return;
                        if(netStastus.chairLeftHeat==null)return;
                        if(netStastus.chairRightAir==null)return;
                        if(netStastus.chairRightHeat==null)return;
                        if(netStastus.chairLeftAir.equals("1")||netStastus.chairLeftHeat.equals("1")||netStastus.chairRightAir.equals("1")||netStastus.chairRightHeat.equals("1")){
                            img_switch_air_conditioning.setVisibility(View.VISIBLE);
                        }else{
                            img_switch_air_conditioning.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        img_switch_air_conditioning.setVisibility(View.VISIBLE);
//                        if(!BlueLinkReceiver.getIsBlueConnOK()){
//                           img_switch_air_conditioning.setVisibility(View.VISIBLE);
//                        }else{
//                           img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//                        }
                    }
                }else{
                    img_switch_air_conditioning.setVisibility(View.INVISIBLE);
                }
            } else {
                Log.i("abcde", "没空调状态");
                img_switch_air_conditioning.setVisibility(View.INVISIBLE);
            }
//            if (status != null) {
//                if (status.isStart == 1 && status.isTheft == 1) {
//                    if (status.ACStatus == -1 && status.windLevel == -1 && status.tempControlStatus == -1) {
//                        if(BlueLinkReceiver.getIsBlueConnOK()){
//                            img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//                        }
//                        if(status.chairLeftAir==null)return;
//                        if(status.chairLeftHeat==null)return;
//                        if(status.chairRightAir==null)return;
//                        if(status.chairRightHeat==null)return;
//                        if(status.chairLeftAir.equals("1")||status.chairLeftHeat.equals("1")||status.chairRightAir.equals("1")||status.chairRightHeat.equals("1")){
//                            img_switch_air_conditioning.setVisibility(View.VISIBLE);
//                        }else{
//                            img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//                        }
//                    } else {
//                        if(!BlueLinkReceiver.getIsBlueConnOK()){
//                            img_switch_air_conditioning.setVisibility(View.VISIBLE);
//                        }else{
//                            img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                }else{
//                    img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//                }
//            } else {
//                Log.i("abcde", "没空调状态");
//                img_switch_air_conditioning.setVisibility(View.INVISIBLE);
//            }
            setMeterUI(status);
            if(EquipmentManager.isShouweiSix()){
                setOilUI(null);
            }else{
                setOilUI(status);
            }
            if(status!=null&&status.carId==car.ide){
//                imageSet(img_battery, (status.voltage <=11.5) ? R.drawable.battery_off :R.drawable.battery_on);
                DecimalFormat df = new DecimalFormat("##.0");
//                textSet(txt_battery,(status.voltage == 0) ? "电压:" :"电压:" + df.format(status.voltage) + "V");
//                if (status.voltageStatus == 1) {//电压低
//                    if(txt_battery.getCurrentTextColor() != getResources().getColor(R.color.white))txt_battery.setTextColor(getResources().getColor(R.color.white));
//                } else {
//                    if(txt_battery.getCurrentTextColor() != getResources().getColor(R.color.red_dark))txt_battery.setTextColor(getResources().getColor(R.color.red_dark));
//                }
                if (status.voltage <= 11.5) {//电压低
//                    if(txt_battery.getCurrentTextColor() != getResources().getColor(R.color.red_dark))txt_battery.setTextColor(getResources().getColor(R.color.red_dark));
                } else {
//                    if(txt_battery.getCurrentTextColor() != getResources().getColor(R.color.white))txt_battery.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
    }
}
