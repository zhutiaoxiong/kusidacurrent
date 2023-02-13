package view.basicview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.FragmentBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.List;
import common.GlobalContext;
import ctrl.OCtrlCommon;
import model.ManagerCarList;
import model.ManagerCommon;
import model.ManagerLoginReg;
import model.carlist.DataCarInfo;
import model.common.DataPayWay;
import model.loginreg.DataUser;
import view.EquipmentManager;
import view.ViewUserInfoActivity;
import view.find.FindViewItem;
import view.me.MeViewItemTop;

public class Fragment4MeMain extends FragmentBase {
    private MeViewItemTop  head_view;
    private FindViewItem txt_managercar, txt_setup, txt_wallet,txt_share, txt_maintenance,txt_help;
    public static Fragment4MeMain fragment4MeMainThis;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.view_me_main, container, false);
        head_view= view.findViewById(R.id.head_view);
        txt_managercar =  view.findViewById(R.id.txt_managercar);
        txt_setup =  view.findViewById(R.id.txt_setup);
        txt_wallet =  view.findViewById(R.id.txt_wallet);
        txt_share =  view.findViewById(R.id.txt_share);
        txt_maintenance = view.findViewById(R.id.txt_maintenance);
        txt_help = view.findViewById(R.id.txt_help);
        initViews();
        initEvents();
        return view;
    }

    @Override
    public void initViews() {
        handleSwitchRedPoint();
    }
    @Override
    public void onStop() {
        fragment4MeMainThis = null;
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment4MeMainThis = this;
        ODispatcher.addEventListener(OEventName.LOGIN_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.REGISTER_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        ODispatcher.addEventListener(OEventName.MESSAGE_USER_NEW_BACK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_MAIL_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE, this);
        ODispatcher.addEventListener(OEventName.SWITCH_ALL_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        ODispatcher.addEventListener(OEventName.SHOW_CHONGZHI, this);
//        ODispatcher.addEventListener(OEventName.KULALA_SHAKE, this);
        handleChangeData();
        setQianbaoUI();
    }
    private void setQianbaoUI(){
        if(EquipmentManager.isMini()){
            txt_wallet.setVisibility(View.GONE);
        }else{
            txt_wallet.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        Log.e("Fragment4MeMain","onHiddenChanged"+hidden);
        // hidden为true时当前fragment显示
        if (!hidden) {
            setQianbaoUI();
        }
    }

    @Override
    public void initEvents() {
        OnClickListenerMy clickUser = new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ViewUserInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        head_view.setOnClickListener(clickUser);
        OnClickListenerMy clickGotoView = new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (v == txt_managercar) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
                } else if (v == txt_setup) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
                } else if (v == txt_wallet) {
                    if(EquipmentManager.isMini()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备不支持").quicklyShow();
                        return;
                    }
                    DataCarInfo      selectCar  = ManagerCarList.getInstance().getCurrentCar();
                    List<DataPayWay> payWayList = ManagerCommon.getInstance().payWayList;
                    if (selectCar == null) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,
                                getResources().getString(R.string.currently_there_is_no_available_vehicles));

                    } else if (payWayList.size() == 0) {
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.please_wait_a_moment_data_is_not_loaded)).quicklyShow();
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_wait_a_moment_data_is_not_loaded));

                        OCtrlCommon.getInstance().ccmd1303_getCommonInfo();
                    } else {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_pay);
                    }
                }  else if (v == txt_share) {
                    // ViewMichelle.CODE =
                    // "不想混了，敢动动我试试，我脚踢北海养老院，拳打南山幼儿园，横批：就是无敌";

                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_share);
                } else if (v == txt_maintenance) {
                    if(EquipmentManager.isMini()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备不支持").quicklyShow();
                        return;
                    }
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_mantance);
                }else if (v == txt_help) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_help);
//                    new ToastTxt(getActivity(),null).withInfo("此功能暂未开放").show();
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), JiecaoVideoPlayerActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
        };
        txt_managercar.setOnClickListener(clickGotoView);
        txt_setup.setOnClickListener(clickGotoView);
        txt_wallet.setOnClickListener(clickGotoView);
        txt_share.setOnClickListener(clickGotoView);
        txt_maintenance.setOnClickListener(clickGotoView);
        txt_help.setOnClickListener(clickGotoView);
    }
    private long refreshTime;

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.LOGIN_SUCCESS)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.REGISTER_SUCCESS)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CHANGE_PHONENUM_RESULTBACK)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CHANGE_USER_INFO_OK)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CHANGE_MAIL_RESULTBACK)) {
            handleSwitchRedPoint();
        } else if (eventName.equals(OEventName.SUBMMIT_PASSWORD_PROBLEM)) {
            handleSwitchRedPoint();
        } else if (eventName.equals(OEventName.LANGUAGE_CHANGE)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.SWITCH_ALL_RESULTBACK)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            long currentTime=System.currentTimeMillis();
            if((currentTime-refreshTime)>1*1000){
                refreshTime=currentTime;
                if(CheckForgroundUtils.isAppForeground()){
                    handleChangeData();
                }
            }
        }else if (eventName.equals(OEventName.SHOW_CHONGZHI)) {
            Log.e("Fragment4MeMain","從車輛管理出來");
            handleChongZhi();
        }
//        else if(eventName.equals(OEventName.KULALA_SHAKE)){
////           handleshake();
//        }
    }

    // =====================================================
    @Override
    public void callback(String key, Object value) {
//        if(key.equals("nfccheck")){
//            //点击验证密码确定框
//            JsonObject obj  = (JsonObject) value;
//            String     pass = OJsonGet.getString(obj, OToastInput.PASS);
//            BasicParamCheckPassWord.isFindMain=1;
//            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
//        }
    }


    @Override
    public void invalidateUI() {
        //设用户信息
        final DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null) return;
        if (user.name.equals("")) {
            head_view.txt_center.setText("昵称");
            head_view.txt_center.setTextColor(Color.parseColor("#ff666666"));
        } else {
            head_view.txt_center.setText(user.name);
            head_view.txt_center.setTextColor(Color.parseColor("#000000"));
        }
        if(user!=null&&user.avatarUrl!=null&&user.avatarUrl.length()>0){
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.head_no).placeholder(R.drawable.head_no);
            Glide.with(GlobalContext.getContext())
                    .load(user.avatarUrl)
                    .apply(options)
                    .into(head_view.img_left);
        }else{
            head_view.img_left.setImageResource(R.drawable.head_no);
        }
        if(EquipmentManager.isMini()){
            txt_wallet.setVisibility(View.GONE);
        }else{
            txt_wallet.setVisibility(View.VISIBLE);
        }

//        if (user.avatarUrl == null || user.avatarUrl.length() <= 1) {
//            head_view.img_left.setTag(null);
//            head_view.img_left.setImageResource(R.drawable.head_no);
//        } else {
//            String tag = (String)(head_view.img_left.getTag());
//            boolean needRefresh = true;
//            if(tag!=null){
//                if(tag.equals(user.avatarUrl))needRefresh = false;
//            }
//            if(needRefresh) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Bitmap bitmap = DataUser.getBitmap(user.avatarUrl);
//                            Message message = Message.obtain();
//                            message.what = 1;
//                            message.obj = bitmap;
//                            handler1.sendMessage(message);
//                        } catch (IOException e) {
//                            handleHeadNo();
//                        }
//                    }
//                }).start();
//            }
//        }

        //庶一弈是否显示
//        List<DataCarInfo> listCar = ManagerCarList.getInstance().getCarInfoList();
//        boolean haveBlue = false;
//        if(listCar != null && listCar.size() > 0){
//            for(DataCarInfo carInfo : listCar){
//                if(carInfo.bluetoothName!=null && carInfo.bluetoothName.length()>0 && carInfo.isBindBluetooth == 1)
//                    haveBlue = true;
//            }
//        }



//        if (BlueLinkNetSwitch.getSwitchShakeInfo().isShow == 1){
//            txt_shake.setVisibility(View.VISIBLE);
//        }else {
//            txt_shake.setVisibility(View.GONE);
//        }
//        //无庶是否显示
//        if(ManagerSwitchs.getInstance().getSwitchNoKeys() == null){
//            txt_nokey.setVisibility(View.GONE);
//        }else{
//            txt_nokey.setVisibility(View.VISIBLE);
//        }


//        txt_nokey.setVisibility(View.VISIBLE);
//        DataCarInfo curCar = ManagerCarList.getInstance().getCurrentCar();
//        DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
//        if (shakeinfo != null && shakeinfo.isOpen == 1
//                && haveBlue && curCar!=null
//                && curCar.bluetoothName!=null && curCar.bluetoothName.contains("BLE#42#")){
//            txt_nokey.setVisibility(View.VISIBLE);
//        }else {
//            txt_nokey.setVisibility(View.GONE);
//        }
//        txt_wear.setVisibility(View.GONE);
    }

    private final Handler handler1 = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                    head_view.img_left.setImageBitmap(bitmap);
                    head_view.img_left.setTag(user.avatarUrl);
                }
            }
        }
    };

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

    public void handleHeadNo() {
        Message message = new Message();
        message.what = 504;
        handler.sendMessage(message);
    }
    public void handleChongZhi() {
        handler.sendEmptyMessage(505);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    private final Handler handler=new  Handler (Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 503:
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                    if(user == null){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo("无用户信息").show();
                    }
                    break;
                case 504:
                    head_view.img_left.setTag(null);
                    head_view.img_left.setImageResource(R.drawable.head_no);
                    break;
                case 505:
                    setQianbaoUI();
                    break;
            }
        }
    };
    // ===================================================
}
