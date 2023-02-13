package view;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;
import com.wearkulala.www.wearfunc.WearLinkServicePhone;
import com.wearkulala.www.wearfunc.WearReg;

import java.util.List;

import common.GlobalContext;
import common.PopViewManager;
import common.ResCheck;
import common.blue.BlueLinkReceiver;
import common.linkbg.BootBroadcastReceiver;
import common.pinyinzhuanhuan.KeyBoard;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlAuthorization;
import ctrl.OCtrlBaseHttp;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import ctrl.OCtrlGps;
import model.ManagerAuthorization;
import model.ManagerCarList;
import model.ManagerCommon;
import model.ManagerLoginReg;
import model.carcontrol.DataWarnings;
import model.carlist.DataCarInfo;
import model.demomode.DemoMode;
import model.loginreg.DataUser;
import model.maintain.ManagerMaintainList;
import view.basicview.CheckForgroundUtils;
import view.basicview.Fragment4AppMain;
import view.basicview.Fragment4ControlMain;
import view.basicview.Fragment4FindMain;
import view.basicview.Fragment4MeMain;
import view.basicview.FragmentActionBar;
import view.clip.ClipPopConfirmAuthor;
import view.clip.child.ClipTextScaleAnimation;
import view.clip.gesture.GestureVerityPage;
import view.view4app.maintain.MaintainPromeBox;
import view.view4app.maintain.MaintainPromeBoxData;
import view.view4info.card.CardSynthesisSuccess;

public class ActivityKulalaMain extends ActivityBase {
    public static boolean IS_ON_RESUME = false;
    //    private ToastConfirm confirmView;
    private FragmentActionBar actionBar;
    private Fragment4ControlMain main0;
    private Fragment4AppMain main1;
    private Fragment4FindMain main2;
    private Fragment4MeMain main3;
    private FrameLayout layer_inner;
    private TextView Log_txt;
    private RelativeLayout layer_outer;
    private RelativeLayout prompt_layout, exit_prompt_box_layout, exitdemo;
    //    private RelativeLayout re_shake_click_me;
    private TextView scale_text;
    private ViewPromeBox box;
    private View rootView;
    private TextView textView;
    private ViewGroup view;
//    private MyHandler handler = new MyHandler();
    public static DataWarnings areaWar;

    public static int POP_VIEW_NORMAL = 0, CURRENT_VIEW_ID = 0;
    public static boolean GestureNeed = true;
    public static ActivityKulalaMain ActivityKulalaMainThis;

    public static int getPAGEPOS() {
        return PAGEPOS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (BuildConfig.DEBUG) Log.e("<ActivityKulalaMain>", "<<<<<onCreate>>>>>>");
        setContentView(R.layout.activity_kulala_main);
        actionBar = (FragmentActionBar) getFragmentManager().findFragmentById(R.id.fragment_actionbar);
        layer_inner = (FrameLayout) findViewById(R.id.layer_inner);//fragment
        layer_outer = (RelativeLayout) findViewById(R.id.layer_outer);//popview
        prompt_layout = (RelativeLayout) findViewById(R.id.prompt_box_layout);
        scale_text = (TextView) findViewById(R.id.scale_text);
        Log_txt = (TextView) findViewById(R.id.Log_txt);
        exit_prompt_box_layout = (RelativeLayout) findViewById(R.id.exit_prompt_box_layout);
        exitdemo = (RelativeLayout) findViewById(R.id.exitdemo);
//        re_shake_click_me = (RelativeLayout) findViewById(R.id.re_shake_click_me);
        OCtrlCommon.getInstance().cmmd_2303_qurryTaoBaoInfo();
        initViews();
        initEvents();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                opoenBlueTooth();
//            }
//        }, 1000);
        OTimeSchedule.getInstance().init();
        ODispatcher.addEventListener(OEventName.GLOBAL_SHOW_LOG, this);
        ODispatcher.addEventListener(OEventName.ACTIVITY_KULALA_GOTOVIEW, this);
        ODispatcher.addEventListener(OEventName.ACTIVITY_KULALA_TOAST_SCALE, this);

        ODispatcher.addEventListener(OEventName.ACTIVITY_KULALA_DESTORY, this);
        ODispatcher.addEventListener(OEventName.CALL_MY_PHONE, this);
        ODispatcher.addEventListener(OEventName.GLOBAL_NEED_REFRESH_CAR, this);
        ODispatcher.addEventListener(OEventName.GLOBAL_NEED_CANCEL_AREA, this);
        ODispatcher.addEventListener(OEventName.TAB_CLICK_TRUE, this);
        ODispatcher.addEventListener(OEventName.POP_UP_BOX, this);
        ODispatcher.addEventListener(OEventName.DEMO_MODE_START, this);
        ODispatcher.addEventListener(OEventName.EXIT_DEMOMODE_WINDOW_SHOW, this);
        ODispatcher.addEventListener(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS, this);
        ODispatcher.addEventListener(OEventName.CAR_ACTIVATE_SUCESS, this);
        ODispatcher.addEventListener(OEventName.CARD_RECIVIE, this);
        ODispatcher.addEventListener(OEventName.MAINTAIN_MESSAGEBACK, this);
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE, this);
        ODispatcher.addEventListener(OEventName.SEND_CAR_STATUS_NET_OR_SOCKET, this);
        ODispatcher.addEventListener(OEventName.SEND_CAR_STATUS_BLUETOOTH, this);

        BlueLinkReceiver.getInstance().initReceiver(this);
        BootBroadcastReceiver.getInstance().initReceiver(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(getPackageName(), MyJobService.class.getName()))
//                    .setPeriodic(2000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .build();
//            jobScheduler.schedule(jobInfo);
//        }
//        BluetoothReceiver.getInstance().initReceiver(this);

//        LcdBlueOptionUtils.getInstance().scanAndLinkBlue(ActivityKulalaMain.this);
    }

//    public void notyfyActivityMainIsForgroundA() {
//        boolean canSendActivityStatus = true;
//        int count = 0;
//        while (canSendActivityStatus) {
//            try {
//                Thread.sleep(200);
//                count++;
//                 if (BuildConfig.DEBUG) Log.e("------------", "第" + count + "次发送" + IS_ON_RESUME);
//                BlueLinkReceiver.notyfyActivityMainIsForground(IS_ON_RESUME);
//                if (count == 3) {
//                    canSendActivityStatus = false;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    protected void onStop() {
        IS_ON_RESUME = false;
        ActivityKulalaMain.GestureNeed = false;
         if (BuildConfig.DEBUG) Log.e("前后台", "false");
//        BlueLinkReceiver.notyfyActivityMainIsForground(false);
//        BootBroadcastReceiver.notyfyActivityMainIsForground(this, false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        BlueLinkReceiver.notyfyActivityMainIsForground(false);
        BlueLinkReceiver.getInstance().unRegReceiver();
         if (BuildConfig.DEBUG) Log.e("前后台", "false");
//        BootBroadcastReceiver.notyfyActivityMainIsForground(this, false);
        BootBroadcastReceiver.getInstance().unRegReceiver();
//        BluetoothReceiver.getInstance().unRegReceiver();
        ActivityKulalaMainThis = null;
        Glide.with(getApplicationContext()).pauseRequests();
        super.onDestroy();
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//         if (BuildConfig.DEBUG) Log.e("ActivityKulalaMain","onStart");
//    }
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//         if (BuildConfig.DEBUG) Log.e("ActivityKulalaMain","onAttachedToWindow");
//    }

    private boolean isFirstCreate = true;//是否初进主页面

    @Override
    protected void onResume() {
        super.onResume();
        CheckForgroundUtils.isForground=true;
        if (BuildConfig.DEBUG) Log.e("<ActivityKulalaMain>", "<<<<<onResume>>>>>>");
        ActivityKulalaMainThis = this;
        IS_ON_RESUME = true;
        //激活重载service
         if (BuildConfig.DEBUG) Log.e("前后台", "true");
        BlueLinkReceiver.startA();
        BootBroadcastReceiver.startC();
//        BlueLinkReceiver.notyfyActivityMainIsForground(true);
//        notyfyActivityMainIsForgroundA();//这句代替上面的
//        BootBroadcastReceiver.notyfyActivityMainIsForground(this, true);
//        KulalaServiceC.isInForground = true;
        BootBroadcastReceiver.sendMessage(GlobalContext.getContext(), 2, "");//发心跳
        WearReg.setIsCanGetLoginState(true);
//        if(WearReg.isGooglePlayServiceAvailable(this)) {
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                Intent intent1 = new Intent(ActivityKulalaMain.this, WearLinkServicePhone.class);
//                startService(intent1);
        if (WearReg.isGooglePlayServiceAvailable(this)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Intent intent1 = new Intent(ActivityKulalaMain.this, WearLinkServicePhone.class);
                startService(intent1);
            }
        }
//            }
//        }
//        BootBroadcastReceiver.socketMsgOnOffUIopen(this, true);//9100 UI open
        OCtrlBaseHttp.getInstance();
        OCtrlCar.getInstance();


        //提示零时授权
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        if (GlobalContext.getIsBackApp() && carInfo.isMyCar == 1 && carInfo.authorityEndTime1 > System.currentTimeMillis()) {
            new ToastTxt(this, null).withInfo("您已把车临时借出去，请注意时间。").show();
        }
//        long now = System.currentTimeMillis();
//        if(preResumeTime!=0 && now - preResumeTime>10*1000L){//2小时不重启就重进,不然界面卡顿,消息不收 2*60*60*1000L
//            Intent intent = new Intent();
//            intent.setClass(GlobalContext.getContext(), ActivityFlash.class);//未登录
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            GlobalContext.getContext().startActivity(intent);
//            if(ActivityKulalaMain.ActivityKulalaMainThis!=null)ActivityKulalaMain.ActivityKulalaMainThis.finish();
//        }
//        preResumeTime = now;
        //初始化取必要信息
        if (isFirstCreate) {
            isFirstCreate = false;
            //初始化
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
//                    SDKInitializer.initialize(getApplicationContext());//百度地图初始化
                    Looper.loop();
                }
            }).start();
        }
//        restartService();//启动了二次

        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null || user.userId == 0) {
           ManagerCommon.getInstance().exitToLogin("");
        } else {
            //判断是否显示登录页
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("finishInfo");
            boolean finishInfo = ODBHelper.queryResult2boolean(result);
            if (!finishInfo) {//还没完善信息,只需要再次登录
               ManagerCommon.getInstance().exitToLogin("");
            } else {
                ResCheck.runningCheck();//取通用信息,已有判定
                String result111 = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
                int isOpenGesture = ODBHelper.queryResult2Integer(result111, 0);
                if (isOpenGesture == 1 && GestureNeed) {
                    GestureVerityPage.isNeedNextToEditGesture = false;
                    GestureVerityPage.fromPage = "kulalaMain";
                    handlePopView(R.layout.clip_gesture_verify);
                } else {
                    if (layer_outer.getChildCount() == 0) {//在进入其它页面时不要跳回主页面
                        handlePopView(POP_VIEW_NORMAL);
                    }
                }
            }
        }
    }



    private static int PAGEPOS = 0;

    private void switchView(int pos) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (main0 != null) transaction.hide(main0);
        if (main1 != null) transaction.hide(main1);
        if (main2 != null) transaction.hide(main2);
        if (main3 != null) transaction.hide(main3);
        PAGEPOS = pos;
        getWindow().setStatusBarColor(Color.parseColor("#eeeeee"));
        switch (pos) {
            case 0:
                getWindow().setStatusBarColor(Color.parseColor("#9bacb7"));
                if (main0 == null) {
                    main0 = new Fragment4ControlMain();
                    transaction.add(R.id.layer_inner, main0, "main0");
                }
                transaction.show(main0);
                break;
            case 1:
                if (main1 == null) {
                    main1 = new Fragment4AppMain();
                    transaction.add(R.id.layer_inner, main1, "main1");
                }
                transaction.show(main1);
                break;
            case 2:
                if (main2 == null) {
                    main2 = new Fragment4FindMain();
                    transaction.add(R.id.layer_inner, main2, "main2");
                }
                transaction.show(main2);
                break;
            case 3:
                if (main3 == null) {
                    main3 = new Fragment4MeMain();
                    transaction.add(R.id.layer_inner, main3, "main3");
                }
                transaction.show(main3);
                break;
        }
//        transaction.commit();
        transaction.commitAllowingStateLoss();
//        if(ActivityKulalaMain.this.isDestroyed())return;
//        transaction.commitAllowingStateLoss();//onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后再给它添加Fragment就会出错
    }

    protected void initViews() {
        boolean isDemoMode = DemoMode.getIsDemoMode();
        if (isDemoMode) {
            handleShowButton(true);
        }
//        boolean isClickPicMe=  DataShakeTips.loadIsClickPicMe(ActivityKulalaMain.this);
//        if(isClickPicMe){
//            re_shake_click_me.setVisibility(View.INVISIBLE);
//        }else{
//            re_shake_click_me.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void initEvents() {
        actionBar.setOnTabPosChangeListener(new FragmentActionBar.OnTabPosChangeListener() {
            @Override
            public void onPosChange(int pos) {
                switchView(pos);
            }
        });
//        re_shake_click_me.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//                DataShakeTips.savePicMe(ActivityKulalaMain.this,true);
//                re_shake_click_me.setVisibility(View.INVISIBLE);
//            }
//        });
    }

    @Override
    protected void onPause() {
//         if (BuildConfig.DEBUG) Log.e("<ActivityKulalaMain>", "<<<<<onPause>>>>>>");
        super.onPause();
        CheckForgroundUtils.isForground=false;
    }


    @Override
    public void invalidateUI() {

    }

    long preBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (layer_outer.getChildCount() > 0) {//当前是登录页
                ODispatcher.dispatchEvent(OEventName.MAIN_CLICK_BACK);
            } else {
                long now = System.currentTimeMillis();
                if (now - preBackTime < 2000L) {
                    ManagerCommon.getInstance().ExitAndFinish();
                    finish();
                    System.exit(0);
                } else {
                    Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                    preBackTime = now;
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.ACTIVITY_KULALA_GOTOVIEW)) {
            KeyBoard.hintKb();
            int page = (Integer) paramObj;
            handlePopView(page);
        } else if (eventName.equals(OEventName.ACTIVITY_KULALA_DESTORY)) {
            ActivityKulalaMain.this.finish();
        } else if (eventName.equals(OEventName.ACTIVITY_KULALA_TOAST_SCALE)) {//显示文字动画
            String str = (String) paramObj;
            handleShowScaleText(str);
        } else if (eventName.equals(OEventName.CALL_MY_PHONE)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionCall = checkSelfPermission(Manifest.permission.CALL_PHONE);
                //拔打电话权限
                if (permissionCall != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    String phonenum = (String) paramObj;
                    Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum));
                    ActivityKulalaMain.this.startActivity(intent1);
                }
            } else {
                String phonenum = (String) paramObj;
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum));
                ActivityKulalaMain.this.startActivity(intent1);
            }

        } else if (eventName.equals(OEventName.GLOBAL_NEED_REFRESH_CAR)) {
            String msg = (String) paramObj;
            if(TextUtils.isEmpty(msg))return;
            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                    .withTitle("提示")
                    .withInfo(msg)
                    .withButton("", "确定")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (isClickConfirm) OCtrlCar.getInstance().ccmd1203_getcarlist();
                        }
                    }).show();
        } else if (eventName.equals(OEventName.GLOBAL_NEED_CANCEL_AREA)) {
            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                    .withTitle("电子围栏")
                    .withInfo("电子围栏警报，车辆已超围栏范围，现在就取消围栏吗?")
                    .withButton("否", "是")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (isClickConfirm) {
                                if (areaWar != null)
                                    OCtrlGps.getInstance().ccmd1214_setArea(areaWar.carId, 0, 0);
                                areaWar = null;
                            }
                        }
                    }).show();
        } else if (eventName.equals(OEventName.TAB_CLICK_TRUE)) {
            handleShowPopUpBox(true);
        } else if (eventName.equals(OEventName.POP_UP_BOX)) {
            handleShowPopUpBox(false);
        } else if (eventName.equals(OEventName.DEMO_MODE_START)) {
            handleShowButton(true);
        } else if (eventName.equals(OEventName.EXIT_DEMOMODE_WINDOW_SHOW)) {
            handleShowExitDemoWindow(true);
        } else if (eventName.equals(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS)) {
            handleShowExitDemoWindow(false);
        } else if (eventName.equals(OEventName.CAR_ACTIVATE_SUCESS)) {
            DemoMode.saveIsDemoMode(false);
//            DemoMode.isDemoMode = "";
            handleShowButton(false);
        } else if (eventName.equals(OEventName.CARD_RECIVIE)) {
            handleShowRecivieCard();
        } else if (eventName.equals(OEventName.MAINTAIN_MESSAGEBACK)) {
            handleShowMaintainPromeBox();
        } else if (eventName.equals(OEventName.LANGUAGE_CHANGE)) {
            if (box != null) {
                box.handleChangeData();
            }
        } else if (eventName.equals(OEventName.GLOBAL_SHOW_LOG)) {
            final String logValue = (String) paramObj;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log_txt.setText(logValue);
                }
            });
        } else if (eventName.equals(OEventName.SEND_CAR_STATUS_NET_OR_SOCKET)) {
            String jsonStringCarStatus = (String) paramObj;
             if (BuildConfig.DEBUG) Log.e("传输车辆状态", "activity收到網絡发给蓝牙状态字符串 " + jsonStringCarStatus);
//            BlueLinkReceiver.sendCarStatusBlueToothProgress(jsonStringCarStatus);
        } else if (eventName.equals(OEventName.SEND_CAR_STATUS_BLUETOOTH)) {
            String jsonStringCarStatus = (String) paramObj;
             if (BuildConfig.DEBUG) Log.e("传输车辆状态", "activity收到藍牙发给蓝牙状态字符串 " + jsonStringCarStatus);
            if (!BlueLinkReceiver.getIsBlueConnOK()) return;
            if(!TextUtils.isEmpty(jsonStringCarStatus)){
                JsonObject carStatusInfo = new JsonParser().parse(jsonStringCarStatus).getAsJsonObject();
                long carId=ManagerCarList.getInstance().getCurrentCarID();
                carStatusInfo.addProperty("carId", carId);
                ManagerCarList.getInstance().saveCarStatus(carStatusInfo,"blutooth");//socket有部分数据是没有，比如启动时长
                long currentTime=System.currentTimeMillis();
                if(currentTime-blueMessageBackTime>2000L){
                    blueMessageBackTime=currentTime;
                    ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
                }
            }
//             BlueLinkReceiver.sendCarStatusBlueToothProgress(jsonStringCarStatus);
        }
        super.receiveEvent(eventName, paramObj);
    }

private long blueMessageBackTime;
    private void handleShowMaintainPromeBox() {
        Message message = Message.obtain();
        message.what = 1001;
        handler.sendMessage(message);
    }
//    private void restartService() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(20L);
//                    Intent intent1 = new Intent(ActivityKulalaMain.this, DaKulalaServiceA.class);
//                    String pkName  = getPackageName();
//                    pkName = (pkName == null) ? "com.client.proj.kulala" : pkName;
//                    intent1.setPackage(pkName);
//                    startService(intent1);
//                    Thread.sleep(100L);
//                    LinkControl.getInstance().initNotification(GlobalContext.getContext(), R.drawable.kulala_icon, getResources().getString(R.string.app_name), ManagerSwitchs.getInstance().getSoundOpen(), ManagerSwitchs.getInstance().getVibratorOpen());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    //popView -1 open,-2close scoll
    @Override
    public void popView(int resId) {
        if (resId == R.layout.activity_kulala_main || resId == POP_VIEW_NORMAL) {
//             if (BuildConfig.DEBUG) Log.e("ActivityKulalaMain", "POP_VIEW_NORMAL  " + System.currentTimeMillis());
            DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
            if (car != null && car.ide != 0) {
                boolean isDemoMode = DemoMode.getIsDemoMode();
//                        if (!DemoMode.isDemoMode.equals("演示开始")) {
                if (isDemoMode) {
//                if (DemoMode.isDemoMode.equals("演示开始")) {
                    OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 1);
                } else {
                    OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0);
                }
            }
            ///===================
            CURRENT_VIEW_ID = 0;
            ResCheck.runningCheck();//取通用信息,已有判定
            layer_outer.removeAllViews();
//            if (layer_inner.getChildCount() == 0) {
//                 if (BuildConfig.DEBUG) Log.e("ActivityKulalaMain", "addView ViewBasic4Page  " + System.currentTimeMillis());
//                layer_inner.addView(new ViewBasic4Page(getApplicationContext(), null));
//            }
            layer_inner.setVisibility(View.VISIBLE);
            layer_outer.setVisibility(View.INVISIBLE);//显示隐藏1.防点击多层 2.防无底色
            switchView(FragmentActionBar.currentPos);
        } else {
            ViewGroup view = PopViewManager.findViewByResId(this, resId);
//            ViewGroup view = PopViewManager.findViewByResId(getApplicationContext(), resId);
            if (view != null) {
                CURRENT_VIEW_ID = resId;
                layer_outer.removeAllViews();
                layer_outer.setVisibility(View.VISIBLE);
                layer_outer.addView(view);
                layer_inner.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("coauthorConfirm")) {
            String va = (String) value;
            if (va.equals("confirm")) {
                if (ClipPopConfirmAuthor.author.type == 1) {
                    OCtrlAuthorization.getInstance().ccmd1222_codriver_confirmauthor(ClipPopConfirmAuthor.author.authorityId, 1);
                } else if (ClipPopConfirmAuthor.author.type == 2) {
                    OCtrlAuthorization.getInstance().ccmd1222_codriver_confirmauthor(ClipPopConfirmAuthor.author.authorityId, 3);
                }
            } else if (va.equals("cancel")) {
                if (ClipPopConfirmAuthor.author.type == 1) {
                    OCtrlAuthorization.getInstance().ccmd1222_codriver_confirmauthor(ClipPopConfirmAuthor.author.authorityId, 2);
                } else if (ClipPopConfirmAuthor.author.type == 2) {
                    OCtrlAuthorization.getInstance().ccmd1222_codriver_confirmauthor(ClipPopConfirmAuthor.author.authorityId, 4);
                }
            }
            ManagerAuthorization.getInstance().authorInfoSocket.remove(0);
//            handleShowSocketPopAuthorNext();//弹出确认框
        }
        super.callback(key, value);
    }

    public void handleShowRecivieCard() {
        Message message = new Message();
        message.what = 1000;
        this.handler.sendMessage(message);
    }

    public void handleShowScaleText(String str) {
        Message message = new Message();
        message.what = 109;
        message.obj = str;
        this.handler.sendMessage(message);
    }

    public void handleShowPopUpBox(boolean isShow) {
        Message message = new Message();
        message.what = 110;
        message.obj = isShow;
        this.handler.sendMessage(message);
    }

    public void handleShowButton(boolean isShow) {
        Message message = new Message();
        message.what = 111;
        message.obj = isShow;
        this.handler.sendMessage(message);
    }

    public void handleShowExitDemoWindow(boolean isShow) {
        Message message = new Message();
        message.what = 112;
        message.obj = isShow;
        this.handler.sendMessage(message);
    }

    // ===================================================

    private final Handler handler= new  Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 109:
                    String str = (String) msg.obj;
                    scale_text.setText(str);
                    ClipTextScaleAnimation.show(scale_text, ActivityKulalaMain.this);
                    break;
                case 110:
                    boolean isShow = (boolean) msg.obj;
                    if (isShow) {
                        boolean isDemoMode = DemoMode.getIsDemoMode();
//                        if (!DemoMode.isDemoMode.equals("演示开始")) {
                        if (!isDemoMode) {
                            List<DataCarInfo> carInfoList;
                            carInfoList = ManagerCarList.getInstance().getCarInfoList();
                            if (carInfoList == null || carInfoList.size() == 0) {
//                                DemoMode.jumpToWhere = "车辆管理";
                                DemoMode.jumpToWhere = getResources().getString(R.string.direct_demonstration);
                                prompt_layout.setVisibility(View.VISIBLE);
                                if (prompt_layout.getChildCount() == 0) {

                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                    box = new ViewPromeBox(ActivityKulalaMain.this, null);
                                    box.setLayoutParams(params);
                                    prompt_layout.addView(box);
                                }
                            } else {
                                int count = 0;
                                for (int i = 0; i < carInfoList.size(); i++) {
                                    if (carInfoList.get(i).isActive == 1) {
                                        count++;
                                    }
                                }
                                if (count == 0) {
                                    DemoMode.jumpToWhere = getResources().getString(R.string.direct_demonstration);

                                    prompt_layout.setVisibility(View.VISIBLE);
                                    if (prompt_layout.getChildCount() == 0) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                        ViewPromeBox box = new ViewPromeBox(ActivityKulalaMain.this, null);
                                        box.setLayoutParams(params);
                                        prompt_layout.addView(box);
                                    }
                                }
                            }
                        }
                    } else {
                        prompt_layout.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 111:
                    boolean isShow1 = (boolean) msg.obj;
                    if (isShow1) {
                        exitdemo.setVisibility(View.VISIBLE);
                        if (exitdemo.getChildCount() == 0) {
                            boolean isDemoMode = DemoMode.getIsDemoMode();
//                        if (!DemoMode.isDemoMode.equals("演示开始")) {
                            if (isDemoMode) {
//                            if (DemoMode.isDemoMode.equals("演示开始")) {
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                ViewExitButton exit = new ViewExitButton(ActivityKulalaMain.this, null);
                                exit.setLayoutParams(params);
                                exitdemo.addView(exit);
                            }
                        }
                    } else {
                        exitdemo.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 112:
                    boolean canShow = (boolean) msg.obj;
                    if (canShow) {
                        exit_prompt_box_layout.setVisibility(View.VISIBLE);
                        if (exit_prompt_box_layout.getChildCount() == 0) {
                            ViewPromeBoxExit exit = new ViewPromeBoxExit(ActivityKulalaMain.this, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            exit.setLayoutParams(params);
                            exit_prompt_box_layout.addView(exit);
                        }
                    } else {
                        boolean isDemoMode = DemoMode.getIsDemoMode();
//                        if (!DemoMode.isDemoMode.equals("演示开始")) {
                        exit_prompt_box_layout.setVisibility(View.INVISIBLE);
                        if (!isDemoMode) {
//                        if (DemoMode.isDemoMode.equals("演示结束")) {
                            exitdemo.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                case 1000:
                    String pic = CardSynthesisSuccess.recivieCardInfo.pic;
                    String title = CardSynthesisSuccess.recivieCardInfo.typeStr;
                    String name = getResources().getString(R.string.you_receive) + CardSynthesisSuccess.recicieUserName + getResources().getString(R.string.gift_card_please_check);
                    CardSynthesisSuccess.getInstance().show(layer_outer, pic, title, false, name);
                    break;
                case 1001:
                    int type = ManagerMaintainList.getInstance().maintain.msgType;
                    MaintainPromeBox.getInstance().show(layer_outer, new MaintainPromeBoxData(getResources().getString(R.string.the_tip), ManagerMaintainList.getInstance().maintain.message, type));
                    break;
            }
        }
    };
}
