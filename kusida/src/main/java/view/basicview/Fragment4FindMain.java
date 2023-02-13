package view.basicview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.FragmentBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlRegLogin;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.ManagerSwitchs;
import model.carlist.DataCarInfo;
import view.EquipmentManager;
import view.find.BasicParamCheckPassWord;
import view.find.FindViewItem;
import view.find.FindViewItemDress;
import view.view4me.lcdkey.ActivityLCDkey;

/**
 * Created by qq522414074 on 2016/9/2.发现主页面
 */
public class Fragment4FindMain extends FragmentBase {
    private FindViewItemDress dressup;
    private FindViewItem txt_lcdkay, txt_nfc, txt_tem_control, txt_shake, txt_nokey,txt_trust,txt_project_set,txt_status_record,txt_touch_in;
    private ImageView fuchezhu_view;
    private LinearLayout master_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_find, container, false);
        dressup =view.findViewById(R.id.view_find_dressup);
        txt_shake = view.findViewById(R.id.txt_shake);
        txt_nokey = view.findViewById(R.id.txt_nokey);//无药进入需要检测 BLE#42#头才开启
        txt_lcdkay = view.findViewById(R.id.txt_lcdkay);
        txt_nfc = view.findViewById(R.id.txt_nfc);
        txt_tem_control = view.findViewById(R.id.txt_tem_control);
        txt_trust = view.findViewById(R.id.txt_trust);
        txt_project_set = view.findViewById(R.id.txt_project_set);
        fuchezhu_view= view.findViewById(R.id.fuchezhu_view);
        master_layout= view.findViewById(R.id.master_layout);
        txt_status_record= view.findViewById(R.id.txt_status_record);
        txt_touch_in= view.findViewById(R.id.txt_touch_in);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE, this);
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CAR_SELECT_CHANGE, this);
        ODispatcher.addEventListener(OEventName.SHOW_CHONGZHI, this);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Fragmentadasd", "onDetach: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Fragmentadasd", "onDestroyView: ");
    }

    @Override
    protected void initViews() {
        handleChangeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setYaoKongQiUI();
        Log.e("Fragmentadasd", "onResume: ");
    }

    @Override
    public void onStop() {
        Log.e("Fragmentadasd", "onStop: ");
        super.onStop();

    }

    @Override
    public void onStart() {
        Log.e("Fragmentadasd", "onStart: ");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.e("Fragmentadasd", "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragmentadasd", "onPause: ");
    }
    private void setYaoKongQiUI(){
        if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
            txt_tem_control.setVisibility(View.GONE);
        }else{
            txt_tem_control.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        Log.e("Fragment4MeMain","onHiddenChanged"+hidden);
        // hidden为true时当前fragment显示
        if (!hidden) {
            setYaoKongQiUI();
        }
    }

    @Override
    protected void initEvents() {

        dressup.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_car_dressup);
            }
        });

        txt_shake.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_shake);
            }
        });
        txt_nokey.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (BluePermission.checkPermission(GlobalContext.getCurrentActivity()) != 1) {
                    BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
                } else {
                    if (!BlueLinkReceiver.getIsBlueConnOK()) {
                        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                        if (carInfo.isBindBluetooth == 1) {
                            BlueLinkNetSwitch.setSwitchBlueModel(true, carInfo.ide, "NOKEY");
                            BlueLinkReceiver.needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig, carInfo.isBindBluetooth, carInfo.carsig,carInfo.isMyCar);
                            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!请稍候!").show();
                        } else {
                            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先绑定蓝牙!").show();
                        }
                    } else {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nokey_new);
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nokey_new);
                    }
                }
            }
        });

        txt_touch_in.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (BluePermission.checkPermission(GlobalContext.getCurrentActivity()) != 1) {
                    BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
                } else {
                    if (!BlueLinkReceiver.getIsBlueConnOK()) {
                        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                        if (carInfo.isBindBluetooth == 1) {
                            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!请稍候!").show();
                        } else {
                            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先绑定蓝牙!").show();
                        }
                    } else {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_touch_in);
                    }
                }
            }
        });

        txt_lcdkay.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备不支持").quicklyShow();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(GlobalContext.getContext(), ActivityLCDkey.class);
                intent.putExtra("我來了", "");
                GlobalContext.getCurrentActivity().startActivity(intent);
            }
        });
        txt_nfc.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                DataCarInfo dataCarInfo = ManagerCarList.getInstance().getCurrentCar();
                if (dataCarInfo != null) {
                    if (dataCarInfo.isActive == 0) {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("当前车辆未激活").show();
                        return;
                    }
                    if (dataCarInfo.isMyCar == 0) {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("此功能暂未向副车主开放").show();
                        return;
                    }
                    if(EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()){
                        if(BlueLinkReceiver.getIsBlueConnOK()){
                            //跳转nfc界面
                            OToastInput.getInstance().showInput(txt_nfc, "", "请输入登录密码:", new String[]{OToastInput.PASS}, "nfccheck", Fragment4FindMain.this);
                        }else{
                            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接请稍后").show();
                        }
                    }else{
                        //跳转nfc界面
                        OToastInput.getInstance().showInput(txt_nfc, "", "请输入登录密码:", new String[]{OToastInput.PASS}, "nfccheck", Fragment4FindMain.this);
                    }
                } else {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("当前无车辆").show();
                }
            }
        });

        txt_tem_control.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备不支持").quicklyShow();
                    return;
                }
                DataCarInfo dataCarInfo = ManagerCarList.getInstance().getCurrentCar();
                if (dataCarInfo != null) {
                    if (dataCarInfo.isActive == 0) {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("当前车辆未激活").show();
                        return;
                    }
                    if (dataCarInfo.isMyCar == 0) {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("此功能暂未向副车主开放").show();
                        return;
                    }
                    //跳转nfc界面
                    OToastInput.getInstance().showInput(txt_nfc, "", "请输入登录密码:", new String[]{OToastInput.PASS}, "temcontrol", Fragment4FindMain.this);
                } else {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("当前无车辆").show();
                }
            }
        });
        txt_trust.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
            }
        });
        txt_project_set.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                //状态记录
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if( EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){//MINI版本
                    if(BlueLinkReceiver.getIsBlueConnOK()){
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_project_set);
                    }else{
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接请稍后再试").quicklyShow();
                    }
                }else{
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_project_set);
                }
            }
        });
        txt_status_record.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                //状态记录
                if( EquipmentManager.isMini()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("此设备不支持").quicklyShow();
                    return;
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_warnings);
            }
        });


    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.LANGUAGE_CHANGE)) {
            handleChangeData();
        } else if (s.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            boolean check = (Boolean) o;
            if (check) {
                if(BasicParamCheckPassWord.isFindMain ==0){
                    //跳转到nfc页面
                    DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                    if( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()){
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.askCardInfo()),false);
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nfc_blue);
                    }else{
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nfc);
                    }
                }else   if(BasicParamCheckPassWord.isFindMain ==7){
                    //跳转到nfc页面
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_tem_contorl);
                }
            }
        }else if(s.equals(OEventName.CAR_SELECT_CHANGE)){
            setPage();
        }else if (s.equals(OEventName.SHOW_CHONGZHI)) {
            Log.e("Fragment4MeMain","從車輛管理出來改变显示");
            handleYaokongQi();
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("nfccheck")) {
            //点击验证密码确定框
            JsonObject obj = (JsonObject) value;
            String pass = OJsonGet.getString(obj, OToastInput.PASS);
            BasicParamCheckPassWord.isFindMain=0;
            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
        }else  if (key.equals("temcontrol")) {
            //点击验证密码确定框
            JsonObject obj = (JsonObject) value;
            String pass = OJsonGet.getString(obj, OToastInput.PASS);
            BasicParamCheckPassWord.isFindMain=7;
            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
        }
    }
    private void setPage(){
        Message message=Message.obtain();
        message.what=147;
        handler.sendMessage(message);
    }
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==147){
                showPage();
            }else  if(msg.what==505){
                setYaoKongQiUI();
            }
        }
    };
    public void handleYaokongQi() {
        handler.sendEmptyMessage(505);
    }
    private void showPage(){
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            if(car.isMyCar==0){
                master_layout.setVisibility(View.INVISIBLE);
                fuchezhu_view.setVisibility(View.VISIBLE);
            }else{
                master_layout.setVisibility(View.VISIBLE);
                fuchezhu_view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void invalidateUI() {
        if (BlueLinkNetSwitch.getSwitchShakeInfo().isShow == 1) {
            txt_shake.setVisibility(View.VISIBLE);
        } else {
            txt_shake.setVisibility(View.GONE);
        }
        //无庶是否显示
        if (ManagerSwitchs.getInstance().getSwitchNoKeys() == null) {
            txt_nokey.setVisibility(View.GONE);
        } else {
            txt_nokey.setVisibility(View.VISIBLE);
        }
        showPage();
    }
}
