package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import com.kulala.linkscarpods.blue.BlueStaticValue;
import com.kulala.linkscarpods.service.NetManager;
import com.kulala.linkscarpods.service.SoundPlay;
import com.kulala.staticsfunc.LogMe;
import com.kulala.staticsfunc.static_assistant.ButtonBgStyle;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.CarControlResult;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.ManagerSkins;
import model.ManagerSwitchs;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import model.skin.DataTempSetup;
import model.status.DataSwitch;
import view.EquipmentManager;
import view.clip.ClipPopChooseMinutes;
import view.clip.ClipPopLoading;
import view.find.BasicParamCheckPassWord;

import static ctrl.CarControlResult.CARCONTROL_SENDED;
import static ctrl.CarControlResult.CARCONTROL_SUCESS;
import static model.ManagerSkins.DEFAULT_NAME_TEMP;
import static model.ManagerSkins.TRANSPARENT;

public class ViewControlPanelControl extends RelativeLayoutBase {
    private final Button img_findcar;
//    private final Button img_unlock;
//    private final Button img_backbag;
//    private final Button img_lock;
    private final MyLongClickButton img_lock;
    private final MyLongClickButton img_unlock;
    private final MyLongClickButton10Seconds img_backbag;
//    private final Button img_lock;

    private final ImageView img_start;
    private final ImageView img_bg;
    private final ImageView img_start_state;
    private DataCarInfo currentCar;
    public static int       preControlCmd    = 0;                                    // 前次控制指令
    public static CountDownTimerMy countControlTimer;                                 // 前次控制指令
    public static  boolean isShowAirCondition=false;
    @SuppressLint("StaticFieldLeak")
    public static ViewControlPanelControl ViewControlPanelControlThis;
    private final View img_can_click_bg;
    private final ImageView img_car_start_state;
    private LinearLayout left_seek_layout,right_seek_layout;
    private final ViewClipSeekBar seekBarLeftDoor;
    private final ViewClipSeekBar seekBarRightDoor;
    private long optionTime;
    private View view1,view2;

    private final Handler handler          = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 18650:
                    ClipPopLoading.getInstance().show(img_lock);
                    break;
                case 18651:
                    ClipPopLoading.getInstance().stopLoading();
                    break;
                case 18652:
                    ChangeStyle();
                    break;
                case 18653:
//                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(4).isOpen == 1) {
//                        //熄火
                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_car_off), getResources().getString(R.string.info_control_toast_car_off), "", getResources().getString(R.string.name_car_off), "stopcar", ViewControlPanelControl.this, false);
//                    } else {
//                        if(System.currentTimeMillis() - preStartClickTime < 4000L)return;
//                        preStartClickTime = System.currentTimeMillis();
//                        checkCarIsOnLine();
//                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 2, 0);
////                                OCtrlControl.getInstance().ccmd_controlCar(currentCar, 2, 0);
//                    }
                    break;

            }
        }
    };
    public ViewControlPanelControl(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        //        if(SystemMe.isPad(getContext())){
//            LayoutInflater.from(context).inflate(R.layout.view_control_panel_control_pad, this, true);
//        }else{
        LayoutInflater.from(context).inflate(R.layout.view_control_panel_control, this, true);
//        }
        img_findcar = findViewById(R.id.img_findcar);
        img_unlock = findViewById(R.id.img_unlock);
        img_backbag = findViewById(R.id.img_backbag);
        img_lock = findViewById(R.id.img_lock);
        img_start = findViewById(R.id.img_start);
        img_bg = findViewById(R.id.img_bg);
        img_start_state= findViewById(R.id.img_start_state);
        img_can_click_bg= findViewById(R.id.img_can_click_bg);
        img_car_start_state= findViewById(R.id.img_car_start_state);
        seekBarLeftDoor= findViewById(R.id.seekbar_leftdoor);
        seekBarRightDoor= findViewById(R.id.seekbar_right_door);
        left_seek_layout= findViewById(R.id.left_seek_layout);
        right_seek_layout= findViewById(R.id.right_seek_layout);
        view1= findViewById(R.id.view1);
        view2= findViewById(R.id.view2);
        NetManager.instance().init(GlobalContext.getContext());
        initViews();
        initEvents();
        //private use
        ODispatcher.addEventListener(OEventName.CAR_CONTROL_RESULT, this);
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
        SoundPlay.getInstance().init(getContext());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewControlPanelControlThis = this;
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewControlPanelControlThis = null;
    }

    public void initViews() {
        ManagerSwitchs.getInstance();// 初始化声音
    }
    //只要没有激活车，就弹演示模式框,无车时服务端会动态加车
    private void showDemoModeChoose() {
        ODispatcher.dispatchEvent(OEventName.TAB_CLICK_TRUE);
    }
    //有激活车，当前车是没激活，就提示去激活
    private void showActiveCar() {
        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                .withTitle("激活车辆")
                .withInfo("未激活车辆，现在就去激活么?")
                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                    @Override
                    public void onClickConfirm(boolean isClickConfirm) {
                        if (isClickConfirm) {
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
                        }
                    }
                })
                .show();
    }
    //检查按下行为
    private void checkPressAction() throws Exception{
        boolean           isDemoMode = DemoMode.getIsDemoMode();
        List<DataCarInfo> activeList = ManagerCarList.getInstance().getCarListActive();//已激活车列表
        if (activeList == null || activeList.size() == 0) {//只要没有激活车，就弹演示模式框,无车时服务端会动态加车
            if (isDemoMode){
                int carListSize = ManagerCarList.getInstance().getCarInfoList().size();//所有车列表
                if(carListSize>0) {
                    throw new Exception();
                }else showActiveCar();
                return;
            }
            showDemoModeChoose();
            return;
        }
        currentCar = ManagerCarList.getInstance().getCurrentCar();
        if (currentCar == null || currentCar.isActive == 0) {//有激活车，当前车是没激活，就提示去激活
            showActiveCar();
        }else{
            throw new Exception();
        }
    }

    private boolean checkSwitchesList(){
        List<DataSwitch> confirms = ManagerSwitchs.getInstance().getSwitchConfirms();
        return confirms == null || confirms.size() == 0;
    }
    private static long preStartClickTime = 0;
    public void initEvents() {
        view1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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
        img_can_click_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 1：开启2：熄火3：开锁4：解锁5：开启尾箱6：寻车
        img_start.setOnClickListener(new OnClickListenerControl() {
            @Override
            public void onClickNoFast(View v) {
                if(checkSwitchesList())return;
                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                try {
                    checkPressAction();
                } catch (Exception e) {
                    if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
//                        ODispatcher.dispatchEvent(OEventName.SHOW_PLUE_UPGRADE_VIEW);
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备不支持").quicklyShow();
                        return;
                    }
                    if(currentCar == null || carStatus == null)return;
                    if (carStatus.isStart == 1) {
                        preControlCmd = 2;
//                        if (carStatus.isStart == 0) {//手动关电
//                            ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_battery_protected), getResources().getString(R.string.info_control_toast_battery_protected), "", getResources().getString(R.string.i_know), "noOperate", ViewControlPanelControl.this, true);
//                            return;
//                        }
                        if(EquipmentManager.isMinJiaQiang()){
                            //跳转nfc界面
                            OToastInput.getInstance().showInput(img_start, "请输入登录密码", "请输入登录密码:", new String[]{OToastInput.PASS}, "stopcheckpassword", ViewControlPanelControl.this);
                        }else{
//                            if (ManagerSwitchs.getInstance().getSwitchConfirms().get(4).isOpen == 1) {
//                                //熄火
                                ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_car_off), getResources().getString(R.string.info_control_toast_car_off), "", getResources().getString(R.string.name_car_off), "stopcar", ViewControlPanelControl.this, false);
//                            } else {
//                                if(System.currentTimeMillis() - preStartClickTime < 4000L)return;
//                                preStartClickTime = System.currentTimeMillis();
//                                checkCarIsOnLine();
//                                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 2, 0);
////                                OCtrlControl.getInstance().ccmd_controlCar(currentCar, 2, 0);
//                            }
                        }
                    } else {
                        isShowAirCondition=true;
                        preControlCmd = 1;
                        if(EquipmentManager.isMinJiaQiang()){
                            ClipPopChooseMinutes.getInstance().show(img_start, "chooseTime", ViewControlPanelControl.this);
                        }else{
                            if (carStatus.isTheft == 1) {
                                ClipPopChooseMinutes.getInstance().show(img_start, "chooseTime", ViewControlPanelControl.this);
                            } else {
                                //撤防状态下启动
                                ClipPopConfirmControl.getInstance().show(165, img_start, getResources().getString(R.string.title_control_toast_car_on_unlock), "⚠警告：此次启动无预约自动熄火，\n请注意剩余油量和防盗。", "", getResources().getString(R.string.name_car_on), "startcar", ViewControlPanelControl.this, false);
                                ClipPopConfirmControl.getInstance().resetInfoWithWarningImg("警告：此次启动无预约自动熄火，","请注意剩余油量和防盗。");
                            }
                        }
                    }
                }
            }
        });
//        img_lock.setOnClickListener(new OnClickListenerControl() {
//            @Override
//            public void onClickNoFast(View v) {
//                if(EquipmentManager.isMini()){
//                    if(!BlueLinkReceiver.getIsBlueConnOK()){
//                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
//                        return;
//                    }
//                }
//                if(checkSwitchesList())return;
//                preControlCmd = 3;
//                try {
//                    checkPressAction();
//                }catch (Exception e){
//                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(0).isOpen == 1) {
//                        //设防
//                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_lock), getResources().getString(R.string.info_control_toast_lock), "", getResources().getString(R.string.name_lock), "lockcar", ViewControlPanelControl.this, false);
//                    } else {
//                        checkCarIsOnLine();
//                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 3, 0);
////                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 3, 0);
//                    }
//                }
//            }
//        });
        img_lock.setMyClickListner(new MyLongClickButton.MyClickListner() {
            @Override
            public void onClick() {
                if(EquipmentManager.isMini()&&!EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                }
                if(checkSwitchesList())return;
                preControlCmd = 3;
                try {
                    checkPressAction();
                }catch (Exception e){
                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(0).isOpen == 1) {
                        //设防
                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_lock), getResources().getString(R.string.info_control_toast_lock), "", getResources().getString(R.string.name_lock), "lockcar", ViewControlPanelControl.this, false);
                    } else {
                        checkCarIsOnLine();
                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 3, 0);
//                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 3, 0);
                    }
                }
            }

            @Override
            public void onLongClick() {
//                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
//                }else{
//                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
//                    return;
//                }
                preControlCmd = 3;
                ODispatcher.dispatchEvent(OEventName.SHOW_PROGRESS_DIOLOG,1);
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.sendLock()), false);
            }

            @Override
            public void onStop() {
                ODispatcher.dispatchEvent(OEventName.HIDE_PROGRESS_DIOLOG);
            }
        });
        img_unlock.setMyClickListner(new MyLongClickButton.MyClickListner() {
            @Override
            public void onClick() {
                if(EquipmentManager.isMini()&&!EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                }
                if(checkSwitchesList())return;
                preControlCmd = 4;
                try {
                    checkPressAction();
                }catch (Exception e){
                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(1).isOpen == 1) {
                        //撤防
                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_unlock), getResources().getString(R.string.info_control_toast_unlock), "", getResources().getString(R.string.name_unlock), "unlockcar", ViewControlPanelControl.this, false);
                    } else {
                        checkCarIsOnLine();
                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 4, 0);
//                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 4, 0);
                    }
                }
            }

            @Override
            public void onLongClick() {
//                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
//                }else{
//                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
//                    return;
//                }
                preControlCmd = 4;
                ODispatcher.dispatchEvent(OEventName.SHOW_PROGRESS_DIOLOG,2);
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.sendUnLock()), false);
            }

            @Override
            public void onStop() {
                ODispatcher.dispatchEvent(OEventName.HIDE_PROGRESS_DIOLOG);
            }
        });
        img_backbag.setMyClickListner(new MyLongClickButton10Seconds.MyClickListner() {
            @Override
            public void onClick() {
                if(EquipmentManager.isMini()&&!EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                }
                if(checkSwitchesList())return;
                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                preControlCmd = 5;
                try {
                    checkPressAction();
                }catch (Exception e){
                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(2).isOpen == 1) {
                        if (carStatus.afterBehindOpen == 0) {//未开尾箱
                            ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_open_trunk), getResources().getString(R.string.info_control_toast_open_trunk), "", getResources().getString(R.string.name_open), "openbackbag", ViewControlPanelControl.this, false);
                        } else {//尾箱已开
                            ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_close_trunk), getResources().getString(R.string.info_control_toast_close_trunk), "", getResources().getString(R.string.name_close), "openbackbag", ViewControlPanelControl.this, false);
                        }
                    } else {
                        checkCarIsOnLine();
                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 5, 0);
//                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 5, 0);
                    }
                }
            }

            @Override
            public void onLongClick() {
                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                }else{
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
                    return;
                }
                preControlCmd = 5;
                ODispatcher.dispatchEvent(OEventName.SHOW_PROGRESS_DIOLOG,3);
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.sendWeixiang()), false);
            }

            @Override
            public void onStop() {
                ODispatcher.dispatchEvent(OEventName.HIDE_PROGRESS_DIOLOG);
            }
        });
//        img_unlock.setOnClickListener(new OnClickListenerControl() {
//            @Override
//            public void onClickNoFast(View v) {
//                if(EquipmentManager.isMini()){
//                    if(!BlueLinkReceiver.getIsBlueConnOK()){
//                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
//                        return;
//                    }
//                }
//                if(checkSwitchesList())return;
//                preControlCmd = 4;
//                try {
//                    checkPressAction();
//                }catch (Exception e){
//                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(1).isOpen == 1) {
//                        //撤防
//                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_unlock), getResources().getString(R.string.info_control_toast_unlock), "", getResources().getString(R.string.name_unlock), "unlockcar", ViewControlPanelControl.this, false);
//                    } else {
//                        checkCarIsOnLine();
//                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 4, 0);
////                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 4, 0);
//                    }
//                }
//            }
//        });
//        img_backbag.setOnClickListener(new OnClickListenerControl() {
//            @Override
//            public void onClickNoFast(View v) {
//                if(EquipmentManager.isMini()){
//                    if(!BlueLinkReceiver.getIsBlueConnOK()){
//                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
//                        return;
//                    }
//                }
//                if(checkSwitchesList())return;
//                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//                preControlCmd = 5;
//                try {
//                    checkPressAction();
//                }catch (Exception e){
//                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(2).isOpen == 1) {
//                        if (carStatus.afterBehindOpen == 0) {//未开尾箱
//                            ClipPopConfirmControl.getInstance().show(145, img_start, getResources().getString(R.string.title_control_toast_open_trunk), getResources().getString(R.string.info_control_toast_open_trunk), "", getResources().getString(R.string.name_open), "openbackbag", ViewControlPanelControl.this, false);
//                        } else {//尾箱已开
//                            ClipPopConfirmControl.getInstance().show(145, img_start, getResources().getString(R.string.title_control_toast_close_trunk), getResources().getString(R.string.info_control_toast_close_trunk), "", getResources().getString(R.string.name_close), "openbackbag", ViewControlPanelControl.this, false);
//                        }
//                    } else {
//                        checkCarIsOnLine();
//                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 5, 0);
////                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 5, 0);
//                    }
//                }
//            }
//        });
        img_findcar.setOnClickListener(new OnClickListenerControl() {
            @Override
            public void onClickNoFast(View v) {
                if(EquipmentManager.isMini()&&!EquipmentManager.isShouweiSix()){
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                }
                if(checkSwitchesList())return;
                preControlCmd = 6;
                try {
                    checkPressAction();
                }catch (Exception e){
                    if (ManagerSwitchs.getInstance().getSwitchConfirms().get(3).isOpen == 1) {
                        //寻车
                        ClipPopConfirmControl.getInstance().show(img_start, getResources().getString(R.string.title_control_toast_normal), getResources().getString(R.string.info_control_toast_find_car), "", getResources().getString(R.string.name_find_car), "findcar", ViewControlPanelControl.this, false);
                    } else {
                        checkCarIsOnLine();
                        OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 6, 0);
//                            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 6, 0);
                    }
                }
            }
        });
    }
    private void checkCarIsOnLine(){
        DataCarStatus currentCarstatus=ManagerCarList.getInstance().getCurrentCarStatus();
        if(currentCarstatus!=null&&currentCarstatus.isOnline==1){
            handleStartLoading();
        }else{
            handleStartLoadingNotCount();
        }
    }
    private String getControlString(int cmd) {
        switch (cmd) {
            case 1:
                return getResources().getString(R.string.open_vehicle);
            case 2:
                return getResources().getString(R.string.vehicle_flameout);
            case 3:
                return getResources().getString(R.string.title_control_toast_lock);
            case 4:
                return getResources().getString(R.string.title_control_toast_unlock);
            case 5:
                return getResources().getString(R.string.open_the_trunk);
            case 6:
                return getResources().getString(R.string.vehicle_for_car);
        }
        return "";
    }
    /**
     * 发送通讯请求
     通讯成功
     请求连接车辆
     连接成功
     启动指令下发成功
     引擎启动中(无效)
     状态返回中...
     */
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
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_CONTROL_RESULT)) {
            CarControlResult result = (CarControlResult)paramObj;
            if(result == null)return;//回包数据为空
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            if(carInfo == null)return;//车ID不正确
            if(result.carId!=carInfo.ide){
                 if (BuildConfig.DEBUG) Log.e("modelStep","ERROR>>>>>result.carId:"+result.carId+" carInfo.ide:"+carInfo.ide);
                return;
            }
            String showStr = getControlString(result.instruction);
            if(result.currentProcess == CARCONTROL_SENDED){
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_SENDED");
                handleStopLoading();
                if(result.status == 1){
//                    if(result.instruction == 1)LoadingCarStart.getInstance().gotoStepSendOK();/**2**/
                    showStr += "指令发送成功!";
                    if (preControlCmd == 1) {
                         if (BuildConfig.DEBUG) Log.e("SoundPlay","control play_start");
                        SoundPlay.getInstance().play_start(getContext());
                    } else if (preControlCmd == 5) {
                        SoundPlay.getInstance().play_backpag(getContext());
                    } else if (preControlCmd == 6) {
                        SoundPlay.getInstance().play_findcar(getContext());
                        ODispatcher.dispatchEvent(OEventName.CONTROL_FINDCAR);
                    }
                }else{
                    showStr += "指令发送失败!";
                }
            }else if(result.currentProcess == CARCONTROL_SUCESS){
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_SUCESS");
                if(result.status == 1){
//                    if(result.instruction == 1)LoadingCarStart.getInstance().gotoStepRunOK();/**5**/
                    showStr += "指令执行成功!";
//                    if (result.instruction == 1) {
////						OSoundPlay.getInstance().play_start();
//                    } else if (result.instruction == 3 || result.instruction == 4) {
////                        SoundPlay.getInstance().play_lock(getContext());
//                    }
                    if(result.instruction==3){
                        showStr="设防成功";
                    }else   if(result.instruction==4){
                        showStr="撤防成功";
                    }
                } else {
                    showStr += "指令执行失败!";
                }
            }else{
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_ARRIVE_MODEL");
                showStr += "指令已发送模组!";
            }
            if(result.instruction >= 1)ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_TOAST_SCALE, showStr);
        } else if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            boolean check = (Boolean) paramObj;
            if (check) {
                if(BasicParamCheckPassWord.isFindMain ==8){
                    xihuo();
                }
            }
        }else  if (eventName.equals(OEventName.CAR_SELF_REFRESH_STATUS)){
            if (BuildConfig.DEBUG) Log.e("查看发送时间", "车辆状态回包时间"+System.currentTimeMillis() );
            long getDataTime=System.currentTimeMillis();
            if((getDataTime-optionTime)>10*1000||optionTime==0){
                if (BuildConfig.DEBUG) Log.e("查看发送时间", "刷新UI" );
                handleSetDoorUI();
            }
        }
    }

    private long lastStartCarTime = 0;

    @Override
    public void callback(String key, Object value) {
        long now = System.currentTimeMillis();
        switch (key) {
            case "chooseTime":
                //控制车启动未成功，要间隔十秒才能再点
                if (now - lastStartCarTime > 10000) {
                    lastStartCarTime = now;
                    // time只有0到7档，每档5分钟，0档位5分钟
                    int time = (Integer) value;
                    if (now - preStartClickTime < 4000L) return;
                    preStartClickTime = now;
                    checkCarIsOnLine();
//                LoadingCarStart.getInstance().show(img_start);/**1**/
//                LoadingCarStart.getInstance().gotoStepReadySend();
                    OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 1, time);
//                OCtrlControl.getInstance().ccmd_controlCar(currentCar, 1, time);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_do_not_click_frequently));
                }
                break;
            case "startcar":
                if (now - preStartClickTime < 4000L) return;
                preStartClickTime = now;
                checkCarIsOnLine();
//            LoadingCarStart.getInstance().show(img_start);/**1**/
//            LoadingCarStart.getInstance().gotoStepReadySend();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 1, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 1, 0);//任意时间，立即启动
                break;
            case "lockcar":
                checkCarIsOnLine();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 3, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 3, 0);
                break;
            case "unlockcar":
                checkCarIsOnLine();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 4, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 4, 0);
                break;
            case "openbackbag":
                checkCarIsOnLine();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 5, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 5, 0);
                break;
            case "findcar":
                checkCarIsOnLine();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 6, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 6, 0);
                break;
            case "stopcar":
                if (now - preStartClickTime < 4000L) return;
                preStartClickTime = now;
                checkCarIsOnLine();
                OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 2, 0);
//            OCtrlControl.getInstance().ccmd_controlCar(currentCar, 2, 0);
                break;
            case "stopcheckpassword":
                //点击验证密码确定框
                JsonObject obj = (JsonObject) value;
                String pass = OJsonGet.getString(obj, OToastInput.PASS);
                BasicParamCheckPassWord.isFindMain=8;
                OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
                break;
        }
        super.callback(key, value);
    }

    @Override
    public void invalidateUI() {
        Log.e("TAG", "invalidateUI: ");
        if(!ViewControlPanelControl.this.isAttachedToWindow())return;
//         if (BuildConfig.DEBUG) Log.e("PanelCon","invalidateUI");
        currentCar = ManagerCarList.getInstance().getCurrentCar();
        DataCarStatus carStatus  = ManagerCarList.getInstance().getCurrentCarStatus();
        if (currentCar == null||currentCar.isActive==0) {
            img_start.setSelected(false);
            img_start_state.setSelected(false);
            img_lock.setSelected(true);
            img_unlock.setSelected(false);
            img_backbag.setSelected(false);
            img_car_start_state.setImageResource(R.drawable.img_car_state_off);

//            view1.setVisibility(View.INVISIBLE);
//            view2.setVisibility(View.INVISIBLE);
//            left_seek_layout.setVisibility(View.INVISIBLE);
//            right_seek_layout.setVisibility(View.INVISIBLE);
            return;
        } else {
            if(!NetManager.instance().isNetworkConnected()&&!BlueLinkReceiver.getIsBlueConnOK()){
                img_start.setSelected(false);
                img_start_state.setSelected(false);
                img_lock.setSelected(true);
                img_unlock.setSelected(false);
                img_backbag.setSelected(false);
                img_car_start_state.setImageResource(R.drawable.img_car_state_off);
            }else{
                if (carStatus.isON == 1) {
                    img_car_start_state.setImageResource(R.drawable.img_car_state_on);
                    if(!img_start.isSelected())img_start.setSelected(true);
                    if(!img_start_state.isSelected())img_start_state.setSelected(true);
                } else {
                    img_car_start_state.setImageResource(R.drawable.img_car_state_off);
                    if(img_start.isSelected())img_start.setSelected(false);
                    if(img_start_state.isSelected())img_start_state.setSelected(false);
                }
                if((EquipmentManager.isMini()&&!MiniDataIsShowLock.isShowLock)||(EquipmentManager.isShouweiSix()&&!MiniDataIsShowLock.isShowLock)){
                    img_lock.setSelected(false);
                    img_unlock.setSelected(false);
                }else{
                    if (carStatus.isLock == 1) {
                        if(!img_lock.isSelected())img_lock.setSelected(true);
                        if(img_unlock.isSelected())img_unlock.setSelected(false);
                    } else {
                        if(img_lock.isSelected())img_lock.setSelected(false);
                        if(!img_unlock.isSelected())img_unlock.setSelected(true);
                    }
                }
                if (carStatus.afterBehindOpen == 1) {
                    if(!img_backbag.isSelected())img_backbag.setSelected(true);
                } else if (img_backbag.isSelected()) img_backbag.setSelected(false);
            }
//            if(currentCar.carType==3){
//                left_seek_layout.setVisibility(View.VISIBLE);
//                right_seek_layout.setVisibility(View.VISIBLE);
//                view1.setVisibility(View.VISIBLE);
//                view2.setVisibility(View.VISIBLE);
//            }else{
//                view1.setVisibility(View.INVISIBLE);
//                view2.setVisibility(View.INVISIBLE);
//                left_seek_layout.setVisibility(View.INVISIBLE);
//                right_seek_layout.setVisibility(View.INVISIBLE);
//            }
            long carStatusBackTime=System.currentTimeMillis();
            if(carStatusBackTime-optionTime>10*1000){
                setMvpDoor(carStatus);
            }

        }
//        handleChangeStyle();
    }
    /**设置中门开关*/
    public void handleSetDoorUI() {
        Message message = new Message();
        message.what = 18652;
        handler.sendMessage(message);
    }
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

    //打开Loading计时,6秒停止
    private void handleStartLoading() {
        if (countControlTimer == null) countControlTimer = new CountDownTimerMy(6000, 1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                if (ClipPopLoading.getInstance().getIsShowing()) {
                    handleStopLoading();
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string. send_the_timeout));
                    }
                }
            }
        };
        countControlTimer.cancel();
        countControlTimer.start();
        Message message = new Message();
        message.what = 18650;
        handler.sendMessage(message);
    }
    //只计时不谈
    private void handleStartLoadingNotCount(){
        if (countControlTimer == null) countControlTimer = new CountDownTimerMy(6000, 1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
//                if(!BlueLinkReceiver.getIsBlueConnOK()){
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string. send_the_timeout));
//                }
                if (ClipPopLoading.getInstance().getIsShowing()) {
                    handleStopLoading();
                }
            }
        };
        countControlTimer.cancel();
        countControlTimer.start();
    }
    public void handleStopLoading() {
        Message message = new Message();
        message.what = 18651;
        handler.sendMessage(message);
    }
    /**蓝牙指令发送成功*/
    //接收在主控制更改
    public void handleBlueCmdSended(byte[] bytes) {
        if (bytes == null || bytes.length != 5) return;
        handleStopLoading();
        if(!BlueStaticValue.isControlCmd(bytes,BlueStaticValue.getControlCmdByID(preControlCmd)))return;
        if (preControlCmd == 1) {
             if (BuildConfig.DEBUG) Log.e("SoundPlay","control cmd play_start:"+ ByteHelper.bytesToHexString(bytes));
            SoundPlay.getInstance().play_start(getContext());
        } else if (preControlCmd == 5) {
            SoundPlay.getInstance().play_backpag(getContext());
        } else if (preControlCmd == 6) {
            SoundPlay.getInstance().play_findcar(getContext());
            ODispatcher.dispatchEvent(OEventName.CONTROL_FINDCAR);
        }
    }
    //接收在主控制更改
    public void handleChangeStyle() {
        Message message = new Message();
        message.what = 18652;
        handler.sendMessage(message);
    }
    //接收在主控制更改
    public void xihuo() {
        Message message = new Message();
        message.what = 18653;
        handler.sendMessage(message);
    }
    // ===================================================
    private Drawable getImage(String url,String name){
        if(ManagerSkins.TRANSPARENT.equals(name)){
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),name));
    }
    private final String[] preStyleUrlArr = new String[]{"-1","-1","-1","-1","-1","-1"};
    private void ChangeStyle(){
        //再看用什么Action
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        String url = "";//使用默认的
        if(car != null && car.skinTemplateInfo!=null){//使用网络的
            url = car.getCarTemplate().url;
        }
        ManagerSkins.getInstance().loadTemp(getContext(),url,"control_normal_0",null);
        //判定是否需要刷新
        boolean isNewChange = false;
        for(String preUrl : preStyleUrlArr){
            if (!url.equals(preUrl)) {
                isNewChange = true;
                break;
            }
        }
        if(!isNewChange)return;
        //判定是否需要刷新end
         if (BuildConfig.DEBUG) Log.e("控制面板背景", "url: "+url );
        StateListDrawable lockBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"control_normal_0"),getImage(url,"control_press_0"),getImage(url,"control_select_0"));
        if(lockBg != null){
            img_lock.setBackground(lockBg);
            preStyleUrlArr[0] = url;
        }else return;
        StateListDrawable unlockBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"control_normal_1"),getImage(url,"control_press_1"),getImage(url,"control_select_1"));
        if(unlockBg != null){
            img_unlock.setBackground(unlockBg);
            preStyleUrlArr[1] = url;
        }else return;
        StateListDrawable bagBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"control_normal_2"),getImage(url,"control_press_2"),getImage(url,"control_select_2"));
        if(bagBg != null){
            preStyleUrlArr[2] = url;
            img_backbag.setBackground(bagBg);
        }else return;
        StateListDrawable findBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"control_normal_3"),getImage(url,"control_press_3"),getImage(url,"control_select_3"));
        if(findBg != null){
            img_findcar.setBackground(findBg);
            preStyleUrlArr[3] = url;
        }else return;
        StateListDrawable startBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"control_normal_4"),getImage(url,"control_press_4"),getImage(url,"control_select_4"));
        if(startBg != null){
            img_start.setBackground(startBg);
            preStyleUrlArr[4] = url;
        }else return;
        //设值分辩,以下不做确认
        //背景
            Drawable imgbg = getImage(url,"control_bg");
        if(imgbg!=null){
            img_bg.setBackground(imgbg);
            preStyleUrlArr[5] = url;
        }else return;
//         if (BuildConfig.DEBUG) Log.e("秒刷","Panel Contrl 刷新了");
        //以下不分变，因为旧款无 img_start_state
        //标志,旧款皮肤无
        StateListDrawable startstateBg = ButtonBgStyle.createDrawableSelector(getContext(), getImage(url,"img_start_state_no"),getImage(url,"img_start_state_yes"),getImage(url,"img_start_state_yes"));
        if(startstateBg != null) img_start_state.setBackground(startstateBg);
        //======================start size & position====================
        DataTempSetup tempSetup = ManagerSkins.getInstance().getTempSetup(ManagerSkins.getTempZipFileName(url));
        if(tempSetup!=null){
            setStartButtonLocation(tempSetup);
        }
    }
    /**
     * 此方法用于动态设置启动按钮的位置以及下方的安酷拉文字图片是否显示
     */
    public void setStartButtonLocation(DataTempSetup panelSkin){
        if(panelSkin == null)return;
        if(img_start_state == null)return;//奇怪的报错601 img_start_state == null
        if(panelSkin.control_center_location == null||panelSkin.control_center_location.x==2){//旧款
            LogMe.e("设置中间按钮", "location.isIncenter=="+"放原来的固定中间");
            RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)img_start.getLayoutParams();
            layout.height=ODipToPx.dipToPx(getContext(),88);
            layout.width=ODipToPx.dipToPx(getContext(),88);
            layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout.topMargin=ODipToPx.dipToPx(getContext(),47);
            img_start.setLayoutParams(layout);
            img_start_state.setVisibility(View.INVISIBLE);
        }else {//新款
            img_start_state.setVisibility(View.VISIBLE);
            LogMe.e("设置中间按钮", "location.isIncenter=="+"放启动按钮和下面状态按钮");
            RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)img_start.getLayoutParams();
            layout.height=ODipToPx.dipToPx(getContext(),panelSkin.control_center_size.w);
            layout.width=ODipToPx.dipToPx(getContext(),panelSkin.control_center_size.h);
            layout.removeRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout.topMargin=ODipToPx.dipToPx(getContext(),panelSkin.control_center_location.y);
            img_start.setLayoutParams(layout);
            //设置启动下方的按钮大小以及状态
            RelativeLayout.LayoutParams secondLayout=(RelativeLayout.LayoutParams)img_start_state.getLayoutParams();
            secondLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            LogMe.e("设置中间按钮 ","secondLayout.topMargin"+secondLayout);
            secondLayout.topMargin=ODipToPx.dipToPx(getContext(),panelSkin.control_second_button_location);
            img_start_state.setLayoutParams(secondLayout);
            ViewGroup.LayoutParams secondParams = img_start_state.getLayoutParams();
            secondParams.height=ODipToPx.dipToPx(getContext(),panelSkin.control_second_button_size.h);
            secondParams.width= ODipToPx.dipToPx(getContext(),panelSkin.control_second_button_size.w);
            LogMe.e("设置中间按钮 "," secondParams.height"+ secondParams.height+" secondParams.width" +secondParams.width);
            img_start_state.setLayoutParams(secondParams);
        }
    }
}
