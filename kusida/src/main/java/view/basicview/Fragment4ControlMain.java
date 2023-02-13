package view.basicview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.FragmentBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import common.ResCheck;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import ctrl.OCtrlCheckCarState;
import ctrl.OCtrlCommon;
import model.DataDisPlay;
import model.ManagerCarList;
import model.ManagerDisPlay;
import model.ManagerLoginReg;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import view.view4control.CheckUpGradeUtil;
import view.view4control.ClipPopControlAirConditionNew;
import view.view4control.ImageCarBody;
import view.view4control.ViewCarThreePart;
import view.view4control.ViewControlBottom;
import view.view4control.ViewControlPanelCar;
import view.view4control.ViewControlPanelControl;
import view.view4control.ViewControlPanelOnline;
import view.view4control.ViewControlPanelTitle;
import view.view4control.ViewPerformControl;
import view.view4control.ViewTreasure;
import view.view4me.ResizableImageView;

public class Fragment4ControlMain extends FragmentBase {

    private ViewControlPanelCar       panelCar;
    private ViewControlPanelTitle     panelTitle;
    private ViewControlPanelOnline    panelOnline;
    private ViewControlPanelControl   panelControl;
    private ViewTreasure              treasure;
    private Activity mActivity;
    private ViewPerformControl view_start_protect;
    public  Fragment4ControlMain Fragment4ControlMainThis;
//    private TextView                  txt_blue_use, txt_blue_send, txt_blue_receive,txt_blue_receive_next, txt_blue_state,txt_blue_data;//用来测试蓝牙,0不显示1,2,3,4走到哪一步

    private RelativeLayout mantance_layout;
    private ImageView      img_mantance;
    private RelativeLayout ali_layout;
    private ResizableImageView iv_addver;
    private ImageView chacha;
    private boolean isNowShowAliOrUpgrade;
    @SuppressLint("HandlerLeak")
    private RelativeLayout car_layout;
    private ImageView guanbi;
    private ImageView car_fannn;
    private Animation anmiRotate;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("control生命周期", " Fragment4ControlMain onCreateView: ");
        View view = inflater.inflate(R.layout.view_control_panel, container, false);
        panelCar = (ViewControlPanelCar) view.findViewById(R.id.lin_car);
        panelTitle = (ViewControlPanelTitle) view.findViewById(R.id.lin_title);
        panelOnline = (ViewControlPanelOnline) view.findViewById(R.id.lin_online);
        panelControl = (ViewControlPanelControl) view.findViewById(R.id.lin_control);
        mantance_layout = (RelativeLayout) view.findViewById(R.id.mantance_layout);
        img_mantance = (ImageView) view.findViewById(R.id.img_mantance);
        treasure = (ViewTreasure) view.findViewById(R.id.treasure);
        view_start_protect= (ViewPerformControl) view.findViewById(R.id.view_start_protect);
        ali_layout= view.findViewById(R.id.ali_layout);
        iv_addver= view.findViewById(R.id.iv_addver);
        chacha= view.findViewById(R.id.chacha);
        guanbi= view.findViewById(R.id.guanbi);
        car_layout= view.findViewById(R.id.car_layout);
        car_fannn = view. findViewById(R.id.car_fannn);
        anmiRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation);
        LinearInterpolator lir = new LinearInterpolator();
        anmiRotate.setInterpolator(lir);//必设不然无法均速
        anmiRotate.setFillAfter(true);
        anmiRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

//        txt_blue_use = (TextView) view.findViewById(R.id.txt_blue_use);
//        txt_blue_send = (TextView) view.findViewById(R.id.txt_blue_send);
//        txt_blue_receive = (TextView) view.findViewById(R.id.txt_blue_receive);
//        txt_blue_receive_next = (TextView) view.findViewById(R.id.txt_blue_receive_next);
//        txt_blue_state = (TextView) view.findViewById(R.id.txt_blue_state);
//        txt_blue_data = (TextView) view.findViewById(R.id.txt_blue_data);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        ODispatcher.addEventListener(OEventName.DISPLAY_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.SHOW_PLUE_UPGRADE_VIEW, this);
        ODispatcher.addEventListener(OEventName.SHOW_CAR_BODY, this);
        ODispatcher.addEventListener(OEventName.STOP_ANIM_ROTATE, this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
//            ViewControlBottom.startAnimationCount=0;
        }else{
            car_fannn.clearAnimation();
            car_layout.setVisibility(View.INVISIBLE);
        }
        Log.e("control生命周期", "Fragment4ControlMain hidden"+hidden);
    }
    private void jumpSettingPage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionNet = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);//网络定位
            //网络定位权限
            if (permissionNet != PackageManager.PERMISSION_GRANTED) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("提示")
                        .withInfo("某些机型连接蓝牙需要位置权限，如不设置，蓝牙功能可能无法正常使用")
                        .withButton("取消","去设置")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm) {
                                    toSelfSetting( GlobalContext.getCurrentActivity());
                                }
                            }
                        }).show();
            }
        }
    }
    /**
     * 进入系统设置界面
     */
    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(mIntent);
    }
    @Override
    public void onResume() {
        super.onResume();
//        ViewControlBottom.startAnimationCount=0;//风扇转
        Log.e("control生命周期", "Fragment4ControlMain onResume");
        jumpSettingPage();
        handleSetDisPlay();
        OCtrlCommon.getInstance().cmmd_2301_qurry_Display();
//        MapPosGet.searchCurrentPos(null);
        //全完成后刷新车列表
        OCtrlCar.getInstance().ccmd1203_getcarlist();//刷新车列表
        DataCarStatus carStatus = ManagerCarList.getInstance().getNetCurrentCarStatus();
//        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if(carStatus!=null&&carStatus.isStart==0){
                ClipPopControlAirConditionNew.getInstance().hidePopAirCondition();
        }
        if (FragmentActionBar.currentPos == 1) {
            OCtrlCheckCarState.getInstance().setNeedCheck(true, 10);//轮询检测状态,10秒查一次
        }else {
            OCtrlCheckCarState.getInstance().setNeedCheck(true, 3);//轮询检测状态,3秒查一次
        }
        getCarStatus();
    }

    /*主動自己去拿车辆状态为了中门开关*/
    private void getCarStatus(){
         if (BuildConfig.DEBUG) Log.e("Fragment4ControlMain", "测试是否主動拿状态 " );
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(!BlueLinkReceiver.getInstance().getIsBlueConnOK()){
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

    @Override
    public void onStop() {
        Log.e("control生命周期", "Fragment4ControlMain onStop: ");
        OCtrlCheckCarState.getInstance().setNeedCheck(false, 3);//轮询检测状态,3秒查一次
        super.onStop();
    }


    @Override
    public void onStart() {
        super.onStart();
        Fragment4ControlMainThis = this;
    }

    @Override
    public void onDestroy() {
        Log.e("control生命周期", "onDestroy: ");
        Fragment4ControlMainThis = null;
        super.onDestroy();
    }
    // ================
    public void initViews() {
        handleChangeData();
    }

    public void initEvents() {
        car_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_fannn.clearAnimation();
                car_layout.setVisibility(View.INVISIBLE);
            }
        });
        ali_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNowShowAliOrUpgrade){
                    DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo1;
                    if(disPlay!=null&&disPlay.id!=0){
                        OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay.id);
                    }
                }
                ali_layout.setVisibility(View.INVISIBLE);
            }
        });
        chacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNowShowAliOrUpgrade){
                    DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo1;
                    if(disPlay!=null&&disPlay.id!=0){
                        OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay.id);
                    }
                }
                ali_layout.setVisibility(View.INVISIBLE);
            }
        });
        iv_addver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ali_layout.setVisibility(View.INVISIBLE);
                if(isNowShowAliOrUpgrade){
                    DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo1;
                    if(disPlay!=null&&!TextUtils.isEmpty(disPlay.content)){
                        if (checkAliPayInstalled(GlobalContext.getContext())) {
                            if(disPlay.id!=0){
                                OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay.id);
                            }
                            DataDisPlay disPlay0= ManagerDisPlay.getInstance().displayInfo;
                            if(disPlay0!=null&&disPlay0.id!=0){
                                OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay0.id);
                            }
                            jumpAlipay(GlobalContext.getCurrentActivity(),disPlay.content);
                        } else {
                            new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("请先安装支付宝").quicklyShow();
                        }
                    }
                }else{
//                    Intent intent = new Intent();
//                    intent.setClass(GlobalContext.getCurrentActivity(), ActivityUpGrade.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    GlobalContext.getContext().startActivity(intent);

//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ActivityWeb.TITLE_NAME, "");
//                    bundle.putString(ActivityWeb.HTTP_ADDRESS, "http://manage.kcmoco.com//mekongPlatfrom/html/index/reserve_carPods.html");
//                    intent.putExtras(bundle);
//                    intent.setClass(GlobalContext.getCurrentActivity(), ActivityUpGrade.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    GlobalContext.getContext().startActivity(intent);
                    CheckUpGradeUtil.toTaoBaoOrLiulanQi();
                }
            }
        });
    }
    public static void jumpAlipay(Context context,String uri) {
        try {
//            String uri = "alipayqr://platformapi/startapp?appId=2021002146620507";
////                    + "&page=pages/index/index?userId=123456"//页面参数
////                    + "&query=itemId=005007";//启动参数
            Intent intent = Intent.parseUri(uri, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("支付宝跳转失败").quicklyShow();
            e.printStackTrace();
        }
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    @Override
    protected void invalidateUI() {
        ResCheck.runningLost();
    }

    private long refreshUiTime;
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            if(FragmentActionBar.currentPos!=0)return;
            MyLog.loge("查看位置","刷新UI");
            long currentTime=System.currentTimeMillis();
            boolean isAppForground=   CheckForgroundUtils.isAppForeground();
            if((currentTime-refreshUiTime)>1*1000){
                refreshUiTime=currentTime;
             if(isAppForground){
                 MyLog.loge("查看位置","刷新UI前台");
                 panelTitle.handleChangeData();
                 panelControl.handleChangeData();
                 panelOnline.handleChangeData();
                 panelCar.changeData();
                 handleShowAirCondition();
                 //维修模式
//            int isMaintaince = ManagerCarList.getInstance().getCurrentCar().isMaintaince;
//            if (isMaintaince == 1) {
//                handleShowIsCanNotControl(true);
//            } else {
//                handleShowIsCanNotControl(false);
//            }
                 DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
                 if(currentCar!=null){
                     int washMould = currentCar.washMould;
                     if (washMould == 1) {
                         handleShowIsCanNotControl(true);
                     } else {
                         handleShowIsCanNotControl(false);
                     }
                 }
                 handleShowStartProtect();
             }
            }
        }else  if (eventName.equals(OEventName.DISPLAY_RESULT_BACK)){
            handleSetDisPlay();
        }else if (eventName.equals(OEventName.SHOW_PLUE_UPGRADE_VIEW)){
            handleUpgradePlus();
        }else if (eventName.equals(OEventName.SHOW_CAR_BODY)){
            handleShowCar();
        }
//        else if(OEventName.STOP_ANIM_ROTATE.equals(eventName)){
//            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(car_fannn!=null){
//                        car_fannn.clearAnimation();
//                        if (BuildConfig.DEBUG) Log.e("Fan","STOP Rotate");
//                    }
//                }
//            });
//        }

    }
    /**设置升级plus广告*/
    private void handleUpgradePlus() {
        handler.sendEmptyMessage(2);
    }
    /**显示车门状态*/
    private void handleShowCar() {
        handler.sendEmptyMessage(3);
    }
    /**设置中门开关*/
    private void handleSetDisPlay() {
        handler.sendEmptyMessage(1);
    }
    private final Handler handler=new  Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isNowShowAliOrUpgrade=true;
                    DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo1;
                    if(disPlay==null)return;
                    if(disPlay.isDisplay.equals("1")){
                        ali_layout.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(disPlay.url)){
                            Glide.with(GlobalContext.getContext())
                                    .load(disPlay.url)
                                    .into(iv_addver);
                        }
                    }else{
                        ali_layout.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2:
                    isNowShowAliOrUpgrade=false;
                    ali_layout.setVisibility(View.VISIBLE);
                    iv_addver.setImageResource(R.drawable.img_upgrade_plus);
                    break;
                case 3:
                  car_layout.setVisibility(View.VISIBLE);
                    DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) car_fannn.getLayoutParams();
                    if(car.getCarSkin().ide==33||car.getCarSkin().ide==0){
                       lp.bottomMargin= ODipToPx.dipToPx(GlobalContext.getContext(),536);
                    }else{
                        lp.bottomMargin= ODipToPx.dipToPx(GlobalContext.getContext(),516);
                    }
                    car_fannn.setLayoutParams(lp);
                    DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                    if(car==null||car.isActive==0){
                        status.isStart=0;
                    }
                    if(status.isStart == 1){
                        car_fannn.startAnimation(anmiRotate);
                    }else{
                        car_fannn.clearAnimation();
                    }
                    break;
            }
        }
    };
    private void handleShowStartProtect() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataCarStatus currentStatus=ManagerCarList.getInstance().getCurrentCarStatus();
                if(currentStatus.isWarn==3&&!view_start_protect.isTouch){
                    view_start_protect.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void handleShowIsCanNotControl(final boolean canCtrol) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (canCtrol) {
                    mantance_layout.setVisibility(View.VISIBLE);
                    img_mantance.setVisibility(View.VISIBLE);
                } else if(mantance_layout.getVisibility() == View.VISIBLE){
                    mantance_layout.setVisibility(View.INVISIBLE);
                    img_mantance.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void handleShowAirCondition() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                DataCarStatus carStatus = ManagerCarList.getInstance().getNetCurrentCarStatus();
                DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//                ClipPopControlAirCondition.getInstance().show(carInfo,panelControl);
                if(carStatus!=null){
                    if(carStatus.isStart==1&&carStatus.isTheft==1){
//                        if(BlueLinkReceiver.getIsBlueConnOK()){
//                            if(ClipPopControlAirConditionNew.getInstance().isShowing){
//                                ClipPopControlAirConditionNew.getInstance().handleCloseAirCondition();
//                            }
//                        }else{
                            if(carStatus.ACStatus==-1&&carStatus.windLevel==-1&&carStatus.tempControlStatus==-1){
                                if(carStatus.chairLeftAir==null)return;
                                if(carStatus.chairLeftHeat==null)return;
                                if(carStatus.chairRightAir==null)return;
                                if(carStatus.chairRightHeat==null)return;
                                if(carStatus.chairLeftAir.equals("1")||carStatus.chairLeftHeat.equals("1")||carStatus.chairRightAir.equals("1")||carStatus.chairRightHeat.equals("1")){
                                    String isShowAirPop= ODBHelper.getInstance(mActivity).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"isShowAirPop");
                                    if(isShowAirPop.equals("")){
                                        if(ViewControlPanelControl.isShowAirCondition){
                                            Log.i("abcde", "显示弹框1");
//                                            if(!BlueLinkReceiver.getIsBlueConnOK()){
                                                ClipPopControlAirConditionNew.getInstance().show(carInfo,panelControl);
//                                            }
                                        }
                                        Log.i("abcde", "ClipPopControlAirCondition.getInstance().isTouch"+ClipPopControlAirConditionNew.isTouch+"==="+"ClipPopControlAirCondition.getInstance().isShowing"+ClipPopControlAirConditionNew.getInstance().isShowing);
                                        if(!ClipPopControlAirConditionNew.isTouch&&ClipPopControlAirConditionNew.getInstance().isShowing){
//                                    ClipPopControlAirCondition.getInstance().setAirCondition(carInfo
//                                    );
//                                        Log.i("abcde", "刷新弹框1");
                                            ClipPopControlAirConditionNew.getInstance().handleChangeAirCondition(carInfo);
                                            ClipPopControlAirConditionNew.getInstance().handleChangeSeatMaster(carInfo);
                                            ClipPopControlAirConditionNew.getInstance().handleChangeSeatPolit(carInfo);
                                        }
//                                else{
//                                if(!ClipPopControlAirCondition.getInstance().isTouch&&ClipPopControlAirCondition.getInstance().isShowing){
////                                    ClipPopControlAirCondition.getInstance().setAirCondition(carInfo
////                                    );
//                                    Log.i("abcde", "刷新弹框1");
//                                    ClipPopControlAirCondition.getInstance().handleChangeAirCondition(carInfo);
//                                }
//                                }
                                    }
                                }
                            }else{
                                String isShowAirPop= ODBHelper.getInstance(mActivity).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"isShowAirPop");
                                if(isShowAirPop.equals("")){
                                    if(ViewControlPanelControl.isShowAirCondition){//
                                        Log.i("abcde", "显示弹框1");
//                                        if(!BlueLinkReceiver.getIsBlueConnOK()){
                                            ClipPopControlAirConditionNew.getInstance().show(carInfo,panelControl);
//                                        }
                                    }
                                    Log.i("abcde", "ClipPopControlAirCondition.getInstance().isTouch"+ClipPopControlAirConditionNew.isTouch+"==="+"ClipPopControlAirCondition.getInstance().isShowing"+ClipPopControlAirConditionNew.getInstance().isShowing);
                                    if(!ClipPopControlAirConditionNew.isTouch&&ClipPopControlAirConditionNew.getInstance().isShowing){
//                                    ClipPopControlAirCondition.getInstance().setAirCondition(carInfo
//                                    );
                                        Log.i("abcde", "刷新弹框1");
                                        ClipPopControlAirConditionNew.getInstance().handleChangeAirCondition(carInfo);
//                                    ClipPopControlAirConditionNew.getInstance().handleChangeAirConditionOpen(carInfo);
                                        ClipPopControlAirConditionNew.getInstance().handleChangeSeatMaster(carInfo);
                                        ClipPopControlAirConditionNew.getInstance().handleChangeSeatPolit(carInfo);
                                    }
//                                else{
//                                if(!ClipPopControlAirCondition.getInstance().isTouch&&ClipPopControlAirCondition.getInstance().isShowing){
////                                    ClipPopControlAirCondition.getInstance().setAirCondition(carInfo
////                                    );
//                                    Log.i("abcde", "刷新弹框1");
//                                    ClipPopControlAirCondition.getInstance().handleChangeAirCondition(carInfo);
//                                }
//                                }
                                }
                            }
//                        }
                    }
                    else{
                        if(ClipPopControlAirConditionNew.getInstance().isShowing){
                            ClipPopControlAirConditionNew.getInstance().handleCloseAirCondition();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

}
