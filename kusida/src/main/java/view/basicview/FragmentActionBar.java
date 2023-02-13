package view.basicview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.FragmentBase;


import common.GlobalContext;
import common.LoadPermissions;
import ctrl.OCtrlCheckCarState;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.loginreg.DataUser;
import model.skin.DataTempSetup;
import view.home.activity.ActivityMyHome;

import static model.ManagerSkins.DEFAULT_NAME_TEMP;
import static model.ManagerSkins.TRANSPARENT;

/**
 * 四个主页面
 */
public class FragmentActionBar extends FragmentBase {
    public static int currentPos = 0;
    public        int prePos     = -1;
    private LinearLayout tab_0, tab_1, tab_2, tab_3, tab_4,lin_indicator;//用来设背景
    private ImageView img_red_point;//红点
    private MyHandler handler = new MyHandler();
    private OnTabPosChangeListener onTabPosChangeListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actionbar, container, false);
        tab_0 = (LinearLayout) view.findViewById(R.id.tab_0);
        tab_1 = (LinearLayout) view.findViewById(R.id.tab_1);
        tab_2 = (LinearLayout) view.findViewById(R.id.tab_2);
        tab_3 = (LinearLayout) view.findViewById(R.id.tab_3);
        tab_4= (LinearLayout) view.findViewById(R.id.tab_4);
        lin_indicator = (LinearLayout) view.findViewById(R.id.lin_indicator);
        img_red_point = (ImageView) view.findViewById(R.id.img_red_point);
        initViews();
        initEvents();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ODispatcher.addEventListener(OEventName.CAR_STYLE_CHANGE_ACTIONBAR, this);
        ODispatcher.addEventListener(OEventName.CHANGE_MAIL_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        handleChangeData();
    }

    public interface OnTabPosChangeListener {
        void onPosChange(int pos);
    }
    public void setOnTabPosChangeListener(OnTabPosChangeListener listener) {
        this.onTabPosChangeListener = listener;
    }

    @Override
    protected void initViews() {
//        handleSwitchRedPoint();
    }
    @Override
    protected void initEvents() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tab_0) {
                    currentPos = 0;
                    handleChangeData();
                } else if (v.getId() == R.id.tab_1) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int permissionFineLocation =getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                        int permissionReadPhoneState =getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                        int permissionCoreLocation =getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                        //拍照权限
                        if ( permissionFineLocation != PackageManager.PERMISSION_GRANTED||permissionCoreLocation!= PackageManager.PERMISSION_GRANTED||permissionReadPhoneState!= PackageManager.PERMISSION_GRANTED) {
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                    .withTitle("提示")
                                    .withInfo("使用地图服务需要定位权限和电话权限，请前往设置")
                                    .withButton("取消","去设置")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if (isClickConfirm) {
                                                toSelfSetting( GlobalContext.getCurrentActivity());
                                            }
                                        }
                                    }).show();
                        }else{
                            if (!LoadPermissions.isOpenGps(getActivity())) {
                                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                        .withTitle("使用地图服务前请开启GPS")
                                        .withButton("取消", "开启")
                                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                            @Override
                                            public void onClickConfirm(boolean isClickConfirm) {
                                                if (isClickConfirm) {
                                                    LoadPermissions.openGPS(getActivity());
                                                }
                                            }
                                        }).show();
                            }
                            currentPos = 1;
//                            FullScreenMap.canResetScale = true;//控制是否可以再重定位缩放中心点
                            handleChangeData();
                        }
                    }else{
                        //是否打开gps,点击就提示开
                        if (!LoadPermissions.isOpenGps(getActivity())) {
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                    .withTitle("使用地图服务前请开启GPS")
                                    .withButton("取消", "开启")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if (isClickConfirm) {
                                                LoadPermissions.openGPS(getActivity());
                                            }
                                        }
                                    }).show();
                        }
                        currentPos = 1;
//                        FullScreenMap.canResetScale = true;//控制是否可以再重定位缩放中心点
                        handleChangeData();
                    }
                } else if (v.getId() == R.id.tab_2) {
                    currentPos = 2;
                    handleChangeData();
                } else if (v.getId() == R.id.tab_3) {
                    currentPos = 3;
                    handleChangeData();
                }else if (v.getId() == R.id.tab_4) {
                   Intent intent=new Intent(getActivity(), ActivityMyHome.class);
                   startActivity(intent);
                   getActivity().finish();
                }
                secondChangeNum = 8;
            }
        };
        tab_0.setOnClickListener(listener);
        tab_1.setOnClickListener(listener);
        tab_2.setOnClickListener(listener);
        tab_3.setOnClickListener(listener);
        tab_4.setOnClickListener(listener);
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

    private int secondChangeNum = 8;//重置刷新许可
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CHANGE_MAIL_RESULTBACK)) {
//            handleSwitchRedPoint();
        } else if (eventName.equals(OEventName.SUBMMIT_PASSWORD_PROBLEM)) {
//            handleSwitchRedPoint();
        } else if (eventName.equals(OEventName.CAR_STYLE_CHANGE_ACTIONBAR)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            long currentTime=System.currentTimeMillis();
            if((currentTime-reFreshTime)>1*1000){
                reFreshTime=currentTime;
                if(CheckForgroundUtils.isAppForeground()){

                    if(secondChangeNum<=0)return;
                    handleChangeData();
                    secondChangeNum--;
                }
            }
        }
        super.receiveEvent(eventName, paramObj);
    }
    private long reFreshTime;
    public void handleSwitchRedPoint() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 503;
                handler.sendMessage(message);
            }
        }).start();
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 503:
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                    if (user == null || user.email == null || user.email.equals("") || user.hasSecretQuestion == 0) {
                        img_red_point.setVisibility(View.VISIBLE);
                    } else {
                        img_red_point.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    }
    private Drawable getImage(String url,String name){
        if(ManagerSkins.TRANSPARENT.equals(name)){
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),name));
    }
//    private String[] preStyleUrlArr = new String[]{"-1","-1","-1","-1","-1"};
    @Override
    protected void invalidateUI() {
        if(!FragmentActionBar.this.isAdded())return;
        //先位置变化
        if (prePos != currentPos) {
            if (onTabPosChangeListener != null) onTabPosChangeListener.onPosChange(currentPos);
            if(currentPos == 0){
                //回到页面后，激活重载socket
//                PushManager.getInstance().initialize(getActivity(), MyPushService.class, PHeadSocket.getPHeadSocketAllInit(getActivity()));
//                PushManager.getInstance().registerPushService(getActivity(), MyPushReceiveService.class);
            }
        }
        prePos = currentPos;
        if (currentPos == 0) {
            OCtrlCheckCarState.getInstance().setNeedCheck(true, 1);//轮询检测状态,3秒查一次
        } else if (currentPos == 1) {
            OCtrlCheckCarState.getInstance().setNeedCheck(true, 10);//轮询检测状态,10秒查一次
        }else{
            OCtrlCheckCarState.getInstance().setNeedCheck(false, 3);//轮询检测状态,10秒查一次
        }
        //再设显示
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        String url = "";//使用默认的
        if(car != null && car.skinTemplateInfo!=null){//使用网络的
            url = car.getCarTemplate().url;
        }
        //判定是否需要刷新
//        boolean isNewChange = false;
//        for(String preUrl : preStyleUrlArr){
//            if(!url.equals(preUrl))isNewChange = true;
//        }
//        if(!isNewChange)return;
        //判定是否需要刷新end
        ManagerSkins.getInstance().loadTemp(getActivity(),url,"control_normal_0",null);

        //背景
        Drawable bg = getImage(url,"action_bg");
//        if(bg!=null){
////            preStyleUrlArr[0] = url;
//            lin_indicator.setBackground(bg);
//        }
        //按扭
        for (int i = 0; i <= 4; i++) {
            int  id  = getResources().getIdentifier("tab_" + i, "id", getActivity().getPackageName());
            View tab = (View) getView().findViewById(id);
            DataTempSetup tempSetup = ManagerSkins.getInstance().getTempSetup(ManagerSkins.getTempZipFileName(url));
            if(tempSetup == null)return;
            if(i==4){
                ((TextView) tab.findViewById(R.id.text)).setText("首页");
            }else{
                ((TextView) tab.findViewById(R.id.text)).setText(tempSetup.txt_actions.get(i));
                if (i != currentPos) {
                    ((ImageView) tab.findViewById(R.id.image)).setImageDrawable(
                            ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),"action_off_"+i)));
                    ((TextView) tab.findViewById(R.id.text)).setTextColor(Color.parseColor(tempSetup.color_action_off));
                } else {
                    ((ImageView) tab.findViewById(R.id.image)).setImageDrawable(
                            ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),"action_on_"+i)));
                    ((TextView) tab.findViewById(R.id.text)).setTextColor(Color.parseColor(tempSetup.color_action_on));
                }
            }
//            preStyleUrlArr[i+1] = url;
        }
//         if (BuildConfig.DEBUG) Log.e("秒刷","Action Bar 刷新了");
    }
}
