package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.status.DataSwitch;
import view.EquipmentManager;

/**
 * 弹出四警告
 */
public class ClipPopCarSet implements OEventObject {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;

    private RelativeLayout thisView;
    private View view_background;
    private ImageView sound_Switch, img_blue_switch, img_xihu_switch, img_lock_switch, img_window_switch, img_start_protect_switch;
    private LinearLayout car_set_blue_containers_1, car_set_xihu_containers_2, car_set_sound_containers_3, car_set_lock_containers_4, car_set_window_containers_5, car_set_start_protect_6;

    // ========================out======================
    private static ClipPopCarSet _instance;

    public static ClipPopCarSet getInstance() {
        if (_instance == null)
            _instance = new ClipPopCarSet();
        return _instance;
    }

    //===================================================
    public void show(View parentView) {
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_car_set, null);
        view_background = (View) thisView.findViewById(R.id.view_background);
        sound_Switch = (ImageView) thisView.findViewById(R.id.img_sound_switch);
        img_blue_switch = (ImageView) thisView.findViewById(R.id.img_blue_switch);
        img_xihu_switch = (ImageView) thisView.findViewById(R.id.img_xihu_switch);
        img_lock_switch = (ImageView) thisView.findViewById(R.id.img_lock_switch);
        img_window_switch = (ImageView) thisView.findViewById(R.id.img_window_switch);
        img_start_protect_switch = (ImageView) thisView.findViewById(R.id.img_start_protect_switch);
        car_set_blue_containers_1 = (LinearLayout) thisView.findViewById(R.id.car_set_blue_containers_1);
        car_set_xihu_containers_2 = (LinearLayout) thisView.findViewById(R.id.car_set_xihu_containers_2);
        car_set_sound_containers_3 = (LinearLayout) thisView.findViewById(R.id.car_set_sound_containers_3);
        car_set_lock_containers_4 = (LinearLayout) thisView.findViewById(R.id.car_set_lock_containers_4);
        car_set_window_containers_5 = (LinearLayout) thisView.findViewById(R.id.car_set_window_containers_5);
        car_set_start_protect_6 = (LinearLayout) thisView.findViewById(R.id.car_set_start_protect_6);
        //设置UI
        ODispatcher.addEventListener(OEventName.CAR_SET_CONTROL_RESULT, this);
//		ODispatcher.addEventListener(OEventName.CAR_SHAKE_SET_RESULT,this);
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE, this);
        ODispatcher.addEventListener(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK, this);


        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if (car != null) {
            if ( EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
                car_set_xihu_containers_2.setVisibility(View.GONE);
//                car_set_start_protect_6.setVisibility(View.GONE);
            }else{
                car_set_xihu_containers_2.setVisibility(View.VISIBLE);
//                car_set_start_protect_6.setVisibility(View.VISIBLE);
            }
            if ( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()) {
                int isOpenSound = BlueInstructionCollection.getInstance().getIsOpenSound();
                if (isOpenSound == 0) {
                    sound_Switch.setImageResource(R.drawable.car_set_on);
                } else {
                    sound_Switch.setImageResource(R.drawable.car_set_off);
                }
                int isOpenStartProtect = BlueInstructionCollection.getInstance().getIsOpenStartProtect();
                if (isOpenStartProtect == 0) {
                    img_start_protect_switch.setImageResource(R.drawable.car_set_off);
                } else {
                    img_start_protect_switch.setImageResource(R.drawable.car_set_on);
                }
            } else {
                if (car.isMute == 0) {
                    sound_Switch.setImageResource(R.drawable.car_set_on);
                } else {
                    sound_Switch.setImageResource(R.drawable.car_set_off);
                }
                if (car.startDefend == 0) {
                    img_start_protect_switch.setImageResource(R.drawable.car_set_off);
                } else {
                    img_start_protect_switch.setImageResource(R.drawable.car_set_on);
                }
            }
            if (car.isQuitLock == 0) {
                img_lock_switch.setImageResource(R.drawable.car_set_off);
            } else {
                img_lock_switch.setImageResource(R.drawable.car_set_on);
            }
            if (car.lockCloseWin == 0) {
                img_window_switch.setImageResource(R.drawable.car_set_off);
            } else {
                img_window_switch.setImageResource(R.drawable.car_set_on);
            }
            if (car.washMould == 0) {
                img_xihu_switch.setImageResource(R.drawable.car_set_off);
            } else {
                img_xihu_switch.setImageResource(R.drawable.car_set_on);
            }
            DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
            if (shakeinfo.isOpen == 0) {
                img_blue_switch.setImageResource(R.drawable.car_set_off);
            } else {
                img_blue_switch.setImageResource(R.drawable.car_set_on);
            }
        }
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
//		popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    exitThis();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void initEvents() {
        view_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitThis();
            }
        });
        car_set_sound_containers_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if ( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()) {
                    int isOpenSound = BlueInstructionCollection.getInstance().getIsOpenSound();
                    if (isOpenSound == 0) {
                        sound_Switch.setImageResource(R.drawable.car_set_off);
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.openSound()),false);
                    } else {
                        sound_Switch.setImageResource(R.drawable.car_set_on);
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.closeSound()),false);
                    }
                } else {
                    if (car != null && car.isActive == 1) {
                        if(car.isMute==0){
                            sound_Switch.setImageResource(R.drawable.car_set_off);
                        }else{
                            sound_Switch.setImageResource(R.drawable.car_set_on);
                        }
                        int[] types = new int[6];
                        types[3] = car.washMould;
                        types[0] = car.isMute == 0 ? 1 : 0;
                        types[1] = car.isQuitLock;
                        types[2] = car.lockCloseWin;
                        types[4] = 0;
                        types[5] = car.startDefend;
                        OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide, types, 0);
                    }
                }
            }
        });
        car_set_blue_containers_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
                if (shakeinfo.isOpen == 1) {//无或选中
                    OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(), false);
                } else {
                    if (BluePermission.checkPermission(GlobalContext.getCurrentActivity()) != 1) {
                        BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
                    } else {
                        OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(), true);
                    }
                }
            }
        });
        car_set_xihu_containers_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();

                if (car != null && car.isActive == 1) {
                    if(car.washMould==0){
                        img_xihu_switch.setImageResource(R.drawable.car_set_on);
                    }else{
                        img_xihu_switch.setImageResource(R.drawable.car_set_off);
                    }
                    int[] types = new int[6];
                    types[3] = car.washMould == 0 ? 1 : 0;
                    types[0] = car.isMute;
                    types[1] = car.isQuitLock;
                    types[2] = car.lockCloseWin;
                    types[4] = 0;
                    types[5] = car.startDefend;
                    OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide, types, 3);
                }
            }
        });
        car_set_lock_containers_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car != null && car.isActive == 1) {
                    int[] types = new int[6];
                    types[3] = car.washMould;
                    types[0] = car.isMute;
                    types[1] = car.isQuitLock == 0 ? 1 : 0;
                    types[2] = car.lockCloseWin;
                    types[4] = 0;
                    types[5] = car.startDefend;
                    OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide, types, 1);
                }
            }
        });
        car_set_window_containers_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car != null && car.isActive == 1) {
                    int[] types = new int[6];
                    types[3] = car.washMould;
                    types[0] = car.isMute;
                    types[1] = car.isQuitLock;
                    types[2] = car.lockCloseWin == 0 ? 1 : 0;
                    types[4] = 0;
                    types[5] = car.startDefend;
                    OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide, types, 2);
                }
            }
        });
        car_set_start_protect_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if ( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()) {
                    if(!BlueLinkReceiver.getIsBlueConnOK()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接").quicklyShow();
                        return;
                    }
                    int isOpenStartProtect = BlueInstructionCollection.getInstance().getIsOpenStartProtect();
                    if (isOpenStartProtect == 0) {
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.openStartProtect()),false);
                    } else {
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.closeStartProtect()),false);
                    }
                } else {
                    if (car != null && car.isActive == 1) {
                        if(car.startDefend==0){
                            img_start_protect_switch.setImageResource(R.drawable.car_set_on);
                        }else{
                            img_start_protect_switch.setImageResource(R.drawable.car_set_off);
                        }
                        int[] types = new int[6];
                        types[3] = car.washMould;
                        types[0] = car.isMute;
                        types[1] = car.isQuitLock;
                        types[2] = car.lockCloseWin;
                        types[4] = 0;
                        types[5] = car.startDefend == 0 ? 1 : 0;
                        OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide, types, 5);
                    }
                }
            }
        });
    }

    private void exitThis() {
        context = null;
        ODispatcher.removeEventListener(OEventName.CAR_SET_CONTROL_RESULT, this);
//		ODispatcher.removeEventListener(OEventName.CAR_SHAKE_SET_RESULT,this);
        ODispatcher.removeEventListener(OEventName.CAR_LIST_CHANGE, this);
        ODispatcher.removeEventListener(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK, this);
        popContain.dismiss();
    }

    public void chageUI() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendMessage(message);
    }

    public void chageBlueUI() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                JsonObject obj = DataCarInfo.toJsonObject(car);
                if (BuildConfig.DEBUG) Log.e("车辆禁声", "car " + obj);
                if (car != null) {
                    if ( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()) {
                        int isOpenSound = BlueInstructionCollection.getInstance().getIsOpenSound();
                        if (isOpenSound == 0) {
                            sound_Switch.setImageResource(R.drawable.car_set_on);
                        } else {
                            sound_Switch.setImageResource(R.drawable.car_set_off);
                        }
                        int isOpenStartProtect = BlueInstructionCollection.getInstance().getIsOpenStartProtect();
                        if (isOpenStartProtect == 0) {
                            img_start_protect_switch.setImageResource(R.drawable.car_set_off);
                        } else {
                            img_start_protect_switch.setImageResource(R.drawable.car_set_on);
                        }
                    } else {
                        if (car.isMute == 0) {
                            sound_Switch.setImageResource(R.drawable.car_set_on);
                        } else {
                            sound_Switch.setImageResource(R.drawable.car_set_off);
                        }
                        if (car.startDefend == 0) {
                            img_start_protect_switch.setImageResource(R.drawable.car_set_off);
                        } else {
                            img_start_protect_switch.setImageResource(R.drawable.car_set_on);
                        }
                    }
                    if (car.isQuitLock == 0) {
                        img_lock_switch.setImageResource(R.drawable.car_set_off);
                    } else {
                        img_lock_switch.setImageResource(R.drawable.car_set_on);
                    }
                    if (car.lockCloseWin == 0) {
                        img_window_switch.setImageResource(R.drawable.car_set_off);
                    } else {
                        img_window_switch.setImageResource(R.drawable.car_set_on);
                    }
                    if (car.washMould == 0) {
                        img_xihu_switch.setImageResource(R.drawable.car_set_off);
                    } else {
                        img_xihu_switch.setImageResource(R.drawable.car_set_on);
                    }
                }
            }
//			else if(msg.what==111){
//				DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
//				DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//				if(shakeinfo.isOpen==0){
//					img_blue_switch.setImageResource(R.drawable.car_set_off);
//					BlueLinkNetSwitch.setSwitchNetAll();
//					BlueLinkReceiver.needChangeCar(0,"","");
//				}else{
//					BlueLinkReceiver.needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig);
//					img_blue_switch.setImageResource(R.drawable.car_set_on);
//				}
//			}
        }
    };

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CAR_SET_CONTROL_RESULT)) {
            chageUI();
        }
//		else if(s.equals(OEventName.CAR_SHAKE_SET_RESULT)){
//			chageBlueUI();
//		}
        else if (s.equals(OEventName.CAR_LIST_CHANGE)) {
            chageUI();
        } else if (s.equals(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK)) {
            chageUI();
        }
    }
}

