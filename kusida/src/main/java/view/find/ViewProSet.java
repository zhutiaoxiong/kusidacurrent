package view.find;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.Timer;
import java.util.TimerTask;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import model.BlueInstructionCollection;
import model.GetCarInfoUtils;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.EquipmentManager;
import view.view4me.set.ClipTitleMeSet;

public class ViewProSet extends RelativeLayoutBase {

    private final ProItem lock_down;
    private final ProItem lock_up_window;
    private final ClipTitleMeSet title;
    private final ProItem trumpet_switch_level;
    private final ProItem open_trunk_way;
    private final ProItem item_upwindow_time;
    private final ProItem item_evasive_pass_time;
    private final ProItem item_evasive_close_time;
    private final ProItem item_evasive_control_time;
    private final ProItem item_evasive_control_way;
    private final ProItem up_window_delay_time;
    private final ProItem lock_trigger_way;
    private final ProItem unlock_trigger_way;
    private final ProItem findcar_way;
    private final ProItem findcar_jiange_time;
    private Timer timer;
    private TimerTask timerTask;
    private final ProItem recover_factory;
    private final ProItem lock_unlock_settings;
    private final ProItem mini_evasive_device;
    private int isFirstIn;
    private ProSetData  cacheProsetData=new ProSetData();
    private ProSetData  firstProsetData;
    private boolean isRecivePush;

    public ViewProSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_project_set, this, true);
        title = findViewById(R.id.title);
        lock_up_window = findViewById(R.id.lock_up_window);
        lock_down = findViewById(R.id.lock_down);
        trumpet_switch_level = findViewById(R.id.trumpet_switch);
        open_trunk_way = findViewById(R.id.open_trunk_way);
        item_upwindow_time = findViewById(R.id.item_upwindow_time);
        item_evasive_pass_time = findViewById(R.id.item_evasive_pass_time);
        item_evasive_close_time = findViewById(R.id.item_evasive_close_time);
        item_evasive_control_time = findViewById(R.id.item_evasive_control_time);
        item_evasive_control_way = findViewById(R.id.item_evasive_control_way);
        up_window_delay_time = findViewById(R.id.up_window_delay_time);
        lock_trigger_way = findViewById(R.id.lock_trigger_way);
        unlock_trigger_way = findViewById(R.id.unlock_trigger_way);
        recover_factory= findViewById(R.id.recover_factory);
        lock_unlock_settings= findViewById(R.id.lock_unlock_settings);
        mini_evasive_device= findViewById(R.id.mini_evasive_device);
        findcar_way= findViewById(R.id.findcar_way);
        findcar_jiange_time= findViewById(R.id.findcar_jiange_time);
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_SET_CONTROL_RESULT, this);
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE, this);
        ODispatcher.addEventListener(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.MINI_SET_FACTORY_RESULT, this);
        ODispatcher.addEventListener(OEventName.LOCK_UNLOCK_LEVEL_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.EVASIVE_DEVICE_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.SOCKET_PRO_SET, this);
        ODispatcher.addEventListener(OEventName.MINI_CONTROL_FINDCAR_RESULT_BACK, this);
        if(!EquipmentManager.isMini()&&!EquipmentManager.isShouweiSix()){
            OCtrlCar.getInstance().ccmd1203_getcarlist();//刷新车列表
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("ViewProSet", "onAttachedToWindow: +");
        if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
            sendQueryData();
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ODispatcher.removeEventListener(OEventName.CAR_SET_CONTROL_RESULT, this);
        ODispatcher.removeEventListener(OEventName.CAR_LIST_CHANGE, this);
        ODispatcher.removeEventListener(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK, this);
        ODispatcher.removeEventListener(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK, this);
        ODispatcher.removeEventListener(OEventName.MINI_SET_FACTORY_RESULT, this);
        ODispatcher.removeEventListener(OEventName.LOCK_UNLOCK_LEVEL_RESULT_BACK, this);
        ODispatcher.removeEventListener(OEventName.EVASIVE_DEVICE_RESULT_BACK, this);
        ODispatcher.removeEventListener(OEventName.SOCKET_PRO_SET, this);
        ODispatcher.removeEventListener(OEventName.MINI_CONTROL_FINDCAR_RESULT_BACK, this);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int num = msg.what;
            if (BuildConfig.DEBUG) Log.e("查看定时器", "------- " + num);
            if (num == 1) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurrySwitch()), false);
            } else if (num == 2) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTrumpetsTime()), false);
            } else if (num == 3) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryUpWindowTime()), false);
            } else if (num == 4) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryEvasivePassTime()), false);
            } else if (num == 5) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryEvasiveCloseTime()), false);
            } else if (num == 6) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryEvasiveControlTime()), false);
            } else if (num == 7) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryEvasiveControlWay()), false);
            } else if (num == 8) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryLockTrigerWay()), false);
            } else if (num == 9) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryUnLockTrigerWay()), false);
            } else if (num == 10) {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryLockUpWindowIntervalTime()), false);
            } else if (num == 11) {
                if(EquipmentManager.isMinJiaQiang()){
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurrylockunlocksetting()), false);
                }
            } else if (num == 12) {
                if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryEvasiveDeviceMini()), false);
                }
            } else if (num == 13) {
                if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTruckOpenTime()), false);
                }
            }  else if (num == 14) {
                if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTruckFindCarWay()), false);
                }
            } else if (num == 15) {
                if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTruckFindCarJianGeTime()), false);
                }
            } else if (num == 16) {
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                messageNum = 0;
            } else if (num == 1100) {
                int arg1 = msg.arg1;
                setLockOrUnLockUI(arg1);
            }else if (num == 1200) {
                setLockOrUnLockLevelUI();
            }else if (num == 1300) {
                setEvasiveDeviceLevelUI();
            }else if (num == 1400) {
                setFindCarUI();
            }
            super.handleMessage(msg);
        }
    };

    private void setLockOrUnLockUI(int arg1) {
        if (EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
            lock_trigger_way.setVisibility(View.GONE);
            unlock_trigger_way.setVisibility(View.GONE);
            item_evasive_control_way.setVisibility(View.GONE);
            if (arg1 == 0) {
                item_evasive_control_way.setVisibility(View.VISIBLE);
                int isEvasiveControlWay = BlueInstructionCollection.getInstance().getEvasiveControlWay();
                setEvasiveControlWayUI(isEvasiveControlWay);
            } else {
                lock_trigger_way.setVisibility(View.VISIBLE);
                unlock_trigger_way.setVisibility(View.VISIBLE);
                if (arg1 == 1) {
                    int isLockTrigerWay = BlueInstructionCollection.getInstance().getLockTrigerWay();
                    setLockTrigerWayUI(isLockTrigerWay);
                } else if (arg1 == 2) {
                    int isUnLockTrigerWay = BlueInstructionCollection.getInstance().getUnLockTrigerWay();
                    setUnLockTrigerWayUI(isUnLockTrigerWay);
                }
            }
        }
    }
    private void setLockOrUnLockLevelUI() {
        if (EquipmentManager.isMinJiaQiang()) {
            int lockUnlockLevel= BlueInstructionCollection.getInstance().getLockUnlockSettingLevel();
            if(lockUnlockLevel==0){
                lock_unlock_settings.txt_bottom.setText("启动前不开锁");
            }else  if(lockUnlockLevel==1){
                lock_unlock_settings.txt_bottom.setText("启动前开关锁");
            }else  if(lockUnlockLevel==2){
                lock_unlock_settings.txt_bottom.setText("启动前开锁");
            }
        }
    }

    private void setEvasiveDeviceLevelUI() {
        if (EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
            int evasiveDeviceMiniLevel= BlueInstructionCollection.getInstance().getEvasiveDeviceMiniLevel();
            cacheProsetData.avoidanceDeviceOperation=evasiveDeviceMiniLevel;
            if(evasiveDeviceMiniLevel==0){
                mini_evasive_device.txt_bottom.setText("开锁通");
            }else  if(evasiveDeviceMiniLevel==1){
                mini_evasive_device.txt_bottom.setText("踩脚刹通");
            }
            pushToServer();
        }
    }
    private void setFindCarUI() {
        Log.e("尋車","設置");
        if (EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
                Log.e("尋車","顯示");
                findcar_way.setVisibility(View.VISIBLE);
                findcar_jiange_time.setVisibility(View.VISIBLE);
        }
    }


    int messageNum = 0;

    @Override
    public void initViews() {
        handleChangeData();
    }

    private void sendQueryData() {
        if(timer==null){
            timer = new Timer();
        }
        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    messageNum++;
                    Message message = new Message();
                    message.what = messageNum;
                    if(handler!=null){
                        handler.sendMessage(message);
                    }
                }
            };
        }
        timer.schedule(timerTask, 50, 200);//延时1s，每隔500毫秒执行一次run方法
    }

    @Override
    public void initEvents() {
        // back
        title.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                messageNum = 0;
                handler=null;
                if(EquipmentManager.isMini()){
                    if(firstProsetData!=null&&!firstProsetData.equals(cacheProsetData)){
                        Log.e("是否改变", "不一样");
                        ProSetDataAndTerminum proSetDataAndTerminum= GetCarInfoUtils.createDeviceSet(cacheProsetData);
                        if(!TextUtils.isEmpty(proSetDataAndTerminum.terminalNum)){
                            OCtrlCar.getInstance().ccmd6001_pushDeviceSet(ProSetDataAndTerminum.toJsonObject(proSetDataAndTerminum));
                        }
                    }else{
                        Log.e("是否改变", "一样");
                    }
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, 0);
            }
        });

        recover_factory.setOnClickListener(v -> new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                .withTitle("提示")
                .withInfo("将要恢复出厂设置，确定吗?")
                .withClick(isClickConfirm -> {
                    if (isClickConfirm){
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.recoverFactorySet()), false);
                    }
                }).show());
        lock_up_window.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if(EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()){
                    int isOpenLockWindowUp = BlueInstructionCollection.getInstance().getIsOpenLockWindowUp();
                    if (isOpenLockWindowUp == 0) {
                        lock_up_window.img_right.setImageResource(R.drawable.car_set_on);
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.openLockWindowUp()), false);
                    } else {
                        lock_up_window.img_right.setImageResource(R.drawable.car_set_off);
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.closeLockWindowUp()), false);
                    }
                } else {
                    if (car.isActive == 1) {
                        if (car.lockCloseWin == 0) {
                            lock_up_window.img_right.setImageResource(R.drawable.car_set_on);
                        } else {
                            lock_up_window.img_right.setImageResource(R.drawable.car_set_off);
                        }
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
            }
        });
        lock_down.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {

                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car != null && car.isActive == 1) {
                    if (car.isQuitLock == 0) {
                        lock_down.img_right.setImageResource(R.drawable.car_set_on);
                    } else {
                        lock_down.img_right.setImageResource(R.drawable.car_set_off);
                    }
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
        open_trunk_way.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"长按9秒","长按7秒","长按5秒","长按3秒", "连按3次", "连按2次"}, "open_trunk_way", ViewProSet.this, "尾箱开启方式");
                //                int isTruckOpenMode = BlueInstructionCollection.getInstance().getTruckOpenMode();
//                if (isTruckOpenMode == 0) {
//                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.longClickOpenTrunk()),false);
//                } else {
//                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.doubleClickOpenTrunk()),false);
//                }
            }
        });

        trumpet_switch_level.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"最高", "高", "中", "低"}, "labayinliang", ViewProSet.this, "喇叭音量");
//                int isTruckLevel = BlueInstructionCollection.getInstance().getTruckLevel();
//                if (isTruckLevel == 0) {
//                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrumpetsTime(3)),false);
//                } else {
//                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrumpetsTime(0)),false);
//                }
            }
        });
        item_upwindow_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"15s", "10s", "5s"}, "item_up_window", ViewProSet.this, "升窗时间");
            }
        });
        item_evasive_pass_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"1500ms","1300ms","1000ms", "900ms", "800ms", "700ms", "600ms", "500ms", "400ms", "300ms"}, "item_evasive_pass", ViewProSet.this, "提前通电时间");
            }
        });
        item_evasive_close_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"1000ms", "900ms", "800ms", "700ms", "600ms", "500ms", "400ms", "300ms"}, "item_evasive_close", ViewProSet.this, "延时断电时间");
            }
        });
        item_evasive_control_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"1000ms", "900ms", "800ms", "700ms", "600ms", "500ms", "400ms", "300ms"}, "item_evasive_control", ViewProSet.this, "按键短按时间");
            }
        });
        item_evasive_control_way.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"连续2次", "1次控制"}, "item_evasive_control_way", ViewProSet.this, "回避器控制方式");
            }
        });

        lock_trigger_way.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"连续2次", "1次控制"}, "lock_triger_way", ViewProSet.this, "关锁触发方式");
            }
        });

        unlock_trigger_way.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"连续2次", "1次控制"}, "unlock_triger_way", ViewProSet.this, "开锁触发方式");
            }
        });

        up_window_delay_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"1000ms", "900ms", "800ms", "700ms", "600ms", "500ms","0ms"}, "up_window_interver_time", ViewProSet.this, "升窗间隔时间");
            }
        });
        lock_unlock_settings.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"启动前开锁", "启动前开关锁", "启动前不开锁"}, "lock_unlock_level_set", ViewProSet.this, "远程启动开锁设置");
            }
        });
        mini_evasive_device.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"踩脚刹通", "开锁通"}, "evasive_device_mini", ViewProSet.this, "回避器控制");
            }
        });
        findcar_way.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"长按7秒","长按5秒","长按3秒","连按3次", "连按2次", "短按1次", "喇叭控制"}, "find_car_wayy", ViewProSet.this, "寻车方式");
            }
        });
        findcar_jiange_time.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButtonBlackStyle.getInstance().show(title, new String[]{"1000ms", "900ms", "800ms", "700ms", "600ms", "500ms"}, "find_car_jiangee_time", ViewProSet.this, "寻车间隔时间");
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_SET_CONTROL_RESULT)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.CAR_LIST_CHANGE)) {
            handleChangeData();
        } else if (eventName.equals(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK)) {
            if(paramObj!=null){
                String obj=(String)paramObj;
                if(obj.equals("1")){
                    int isTruckOpenMode = BlueInstructionCollection.getInstance().getTruckOpenMode();
                    cacheProsetData.trunkOpenWith=isTruckOpenMode;
                    isFirstIn++;
                    if(isFirstIn==1){
                        Log.e("是否改变", "第一次");
                        firstProsetData=new ProSetData();
                        setCacheData(firstProsetData,cacheProsetData);
                        ProSetDataAndTerminum proSetDataAndTerminum= GetCarInfoUtils.createDeviceSet(cacheProsetData);
                        if(EquipmentManager.isMini()){
                            if(!TextUtils.isEmpty(proSetDataAndTerminum.terminalNum)){
                                OCtrlCar.getInstance().ccmd6001_pushDeviceSet(ProSetDataAndTerminum.toJsonObject(proSetDataAndTerminum));
                            }
                        }
                    }
                }
            }
            handleChangeData();
        } else if (eventName.equals(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK)) {
            int isLockOrUnLockBack = (Integer) paramObj;
            handlesetLockOrUnLockWayUI(isLockOrUnLockBack);
        }else if (eventName.equals(OEventName.MINI_SET_FACTORY_RESULT)) {
            boolean isSuccess=(Boolean)paramObj;
            if(isSuccess){
                new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("恢复出厂设置成功").quicklyShow();
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                messageNum = 0;
                sendQueryData();
            }else{
                new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("恢复出厂设置失败").quicklyShow();
            }
        }else if (eventName.equals(OEventName.LOCK_UNLOCK_LEVEL_RESULT_BACK)) {
            handlesetLockOrUnLockLevel();
        }else if (eventName.equals(OEventName.EVASIVE_DEVICE_RESULT_BACK)) {
            handlesetEvasiveDeviceLevel();
        }else if (eventName.equals(OEventName.SOCKET_PRO_SET)) {
            if(paramObj!=null){
                JsonObject data=(JsonObject)paramObj;
                String     terminalNum          = OJsonGet.getString(data, "terminalNum");//消息说明
                String     name          = OJsonGet.getString(data, "name");//消息
                String     instruct           = OJsonGet.getString(data, "instruct");//消息
                sendBySocket(terminalNum,name,instruct);
            }
        }else if (eventName.equals(OEventName.MINI_CONTROL_FINDCAR_RESULT_BACK)) {
            handlesetFindCarUI();
        }
    }
    private void sendBySocket(String terminalNum,String name,String instruct){
        String currentTermiNum=ManagerCarList.getInstance().getCurrentCar().terminalNum;
        if(!TextUtils.isEmpty(terminalNum)&&!TextUtils.isEmpty(currentTermiNum)&&currentTermiNum.equals(terminalNum)){
            if(TextUtils.isEmpty(name))return;
            if(TextUtils.isEmpty(instruct))return;
            isRecivePush=true;
            switch (name){
                case "windowRiseInterval":
                    int upWindowInterverTimeLevel = getLockUpwindowInterverTimeLevelBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setLockUpWindowIntervalTime(upWindowInterverTimeLevel)), false);
                    break;
                case "windowRiseTime" :
                    int upWindowLevel = getUpWindowLevelBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setUpWindowTime(upWindowLevel)), false);
                    break;
                case "hornVolume" :
                    int level = -1;
                    switch (instruct) {
                        case "0":
                            level = 0;
                            break;
                        case "1":
                            level = 1;
                            break;
                        case "2":
                            level = 2;
                            break;
                        case "3":
                            level = 3;
                            break;
                    }
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrumpetsTime(level)), false);
                    break;
                case "trunkOpenWith" :
                    if (instruct.equals("0")) {
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.doubleClickOpenTrunk()), false);
                    } else if (instruct.equals("1")) {
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.longClickOpenTrunk()), false);
                    }else if (instruct.equals("2")) {
                        sendTruckOpen(5);
                    }else if (instruct.equals("3")) {
                        sendTruckOpen(7);
                    }else if (instruct.equals("4")) {
                        sendTruckOpen(9);
                    }
                    break;
                case "electrifyBeforehandTime" :
                    int passLevel = getEvasiveBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasivePassTime(passLevel)), false);
                    break;
                case "switchesOffDelayTime" :
                    int closeLevel = getEvasiveBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveCloseTime(closeLevel)), false);
                    break;
                case "pressKayTime" :
                    int controlLevel = getEvasiveBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveControlTime(controlLevel)), false);
                    break;
                case "avoidanceDeviceTechnique" :
                    int controlWayLevel = getEvasiveControlWayBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveControlWayNotConstor(controlWayLevel)), false);
                    break;
                case "avoidanceDeviceOperation" :
                    int  evasiveDeviceMini= getEvasiveDeviceMiniBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveDeviceMini(evasiveDeviceMini)), false);
                    break;
                case "unlockWay" :
                    int unLocktrigerWayLevel = getLockOrUnlockTrigerWayBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setUnLockTriggerWay(unLocktrigerWayLevel)), false);
                    break;
                case "lockWay" :
                    int lockTrigerwayLevel = getLockOrUnlockTrigerWayBySocket(instruct);
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setLockTriggerWay(lockTrigerwayLevel)), false);
                    break;
                case "carLockWindowRise" :
                    if(instruct.equals("0")){
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.closeLockWindowUp()), false);
                    }else{
                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.openLockWindowUp()), false);
                    }
                    break;

            }
        }
    }

    private void setCacheData(ProSetData data1, ProSetData data2){
        data1.windowRiseInterval=data2.windowRiseInterval;
        data1.windowRiseTime=data2.windowRiseTime;
        data1.hornVolume=data2.hornVolume;
        data1.trunkOpenWith=data2.trunkOpenWith;
        data1.electrifyBeforehandTime=data2.electrifyBeforehandTime;
        data1.switchesOffDelayTime=data2.switchesOffDelayTime;
        data1.pressKayTime=data2.pressKayTime;
        data1.avoidanceDeviceTechnique=data2.avoidanceDeviceTechnique;
        data1.avoidanceDeviceOperation=data2.avoidanceDeviceOperation;
        data1.unlockWay=data2.unlockWay;
        data1.lockWay=data2.lockWay;
        data1.carLockWindowRise=data2.carLockWindowRise;
    }

    private void handlesetLockOrUnLockWayUI(int arg) {
        Message message = Message.obtain();
        message.what = 1100;
        message.arg1 = arg;
        if(handler!=null){
            handler.sendMessage(message);
        }
    }
    private void handlesetLockOrUnLockLevel() {
        Message message = Message.obtain();
        message.what = 1200;
        if(handler!=null){
            handler.sendMessage(message);
        }
    }
    private void handlesetEvasiveDeviceLevel() {
        Message message = Message.obtain();
        message.what = 1300;
        if(handler!=null){
            handler.sendMessage(message);
        }
    }
    private void handlesetFindCarUI() {
        Message message = Message.obtain();
        message.what = 1400;
        if(handler!=null){
            handler.sendMessage(message);
        }
    }

    private void pushToServer(){
        if(isRecivePush){
            if(EquipmentManager.isMini()){
                ProSetDataAndTerminum proSetDataAndTerminum=GetCarInfoUtils.createDeviceSet(cacheProsetData);
                if(!TextUtils.isEmpty(proSetDataAndTerminum.terminalNum)){
                    OCtrlCar.getInstance().ccmd6001_pushDeviceSet(ProSetDataAndTerminum.toJsonObject(proSetDataAndTerminum));
                }
                isRecivePush=false;
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("open_trunk_way")) {
            String o = (String) value;
            if (o.equals("连按2次")) {
                sendTruckOpenLianAn(2);
//                sendTruckOpen(2);
//                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.doubleClickOpenTrunk()), false);
            } else if (o.equals("长按3秒")) {
                sendTruckOpen(3);
//                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.longClickOpenTrunk()), false);
            }else if (o.equals("长按5秒")) {
                sendTruckOpen(5);
            }else if (o.equals("长按7秒")) {
                sendTruckOpen(7);
            }else if (o.equals("长按9秒")) {
                sendTruckOpen(9);
            }else if (o.equals("连按3次")) {
                sendTruckOpenLianAn(20);
            }
        } else if (key.equals("labayinliang")) {
            String oo = (String) value;
            int level = -1;
            switch (oo) {
                case "低":
                    level = 0;
                    break;
                case "中":
                    level = 1;
                    break;
                case "高":
                    level = 2;
                    break;
                case "最高":
                    level = 3;
                    break;
            }
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrumpetsTime(level)), false);
        } else if (key.equals("item_up_window")) {
            String oo = (String) value;
            int upWindowLevel = getUpWindowLevel(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setUpWindowTime(upWindowLevel)), false);
        } else if (key.equals("item_evasive_pass")) {
            String oo = (String) value;
            int passLevel = getEvasive(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasivePassTime(passLevel)), false);
        } else if (key.equals("item_evasive_close")) {
            String oo = (String) value;
            int closeLevel = getEvasive(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveCloseTime(closeLevel)), false);
        } else if (key.equals("item_evasive_control")) {
            String oo = (String) value;
            int controlLevel = getEvasive(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveControlTime(controlLevel)), false);
        } else if (key.equals("item_evasive_control_way")) {
            String oo = (String) value;
            int controlWayLevel = getEvasiveControlWay(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveControlWayNotConstor(controlWayLevel)), false);
        } else if (key.equals("lock_triger_way")) {
            String oo = (String) value;
            int lockTrigerwayLevel = getLockOrUnlockTrigerWay(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setLockTriggerWay(lockTrigerwayLevel)), false);
        } else if (key.equals("unlock_triger_way")) {
            String oo = (String) value;
            int unLocktrigerWayLevel = getLockOrUnlockTrigerWay(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setUnLockTriggerWay(unLocktrigerWayLevel)), false);
        } else if (key.equals("up_window_interver_time")) {
            String oo = (String) value;
            int upWindowInterverTimeLevel = getLockUpwindowInterverTimeLevel(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setLockUpWindowIntervalTime(upWindowInterverTimeLevel)), false);
        } else if (key.equals("lock_unlock_level_set")) {
            String oo = (String) value;
            int lockUnlockLevel = getLockUnlockLevel(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setLockUnLocklll(lockUnlockLevel)), false);
        }else if (key.equals("evasive_device_mini")) {
            String oo = (String) value;
            int  evasiveDeviceMini= getEvasiveDeviceMini(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setEvasiveDeviceMini(evasiveDeviceMini)), false);
        } else if (key.equals("find_car_wayy")) {
            String oo = (String) value;
            int  findcarwayCount= getFindCarWayCount(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setFindCarWayCoamd(findcarwayCount)), false);
        }else if (key.equals("find_car_jiangee_time")) {
            String oo = (String) value;
            int  finacarTimeCount= getFindCarTimeCount(oo);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setFindCarTimeCoamd(finacarTimeCount)), false);
        }
        super.callback(key, value);
    }

    private void sendTruckOpen(int time){
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.longClickOpenTrunk()), false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrunkOpenTime(time)), false);
                }
            },200);
    }
    private void sendTruckOpenLianAn(int time){
        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.doubleClickOpenTrunk()), false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTrunkOpenTime(time)), false);
            }
        },200);
    }

    private int getUpWindowLevel(String value) {
        if (value.equals("5s")) {
            return 0;
        } else if (value.equals("10s")) {
            return 1;
        } else if (value.equals("15s")) {
            return 2;
        }
        return 0;
    }
    private int getUpWindowLevelBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        } else if (value.equals("2")) {
            return 2;
        }
        return 0;
    }

    private int getEvasive(String value) {
        if (value.equals("300ms")) {
            return 0;
        } else if (value.equals("400ms")) {
            return 1;
        } else if (value.equals("500ms")) {
            return 2;
        } else if (value.equals("600ms")) {
            return 3;
        } else if (value.equals("700ms")) {
            return 4;
        } else if (value.equals("800ms")) {
            return 5;
        } else if (value.equals("900ms")) {
            return 6;
        } else if (value.equals("1000ms")) {
            return 7;
        }else if (value.equals("1300ms")) {
            return 8;
        }else if (value.equals("1500ms")) {
            return 9;
        }
        return 0;
    }
    private int getEvasiveBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        } else if (value.equals("2")) {
            return 2;
        } else if (value.equals("3")) {
            return 3;
        } else if (value.equals("4")) {
            return 4;
        } else if (value.equals("5")) {
            return 5;
        } else if (value.equals("6")) {
            return 6;
        } else if (value.equals("7")) {
            return 7;
        }
        return 0;
    }
    private int getLockUnlockLevel(String value) {
        if (value.equals("启动前不开锁")) {
            return 0;
        } else if (value.equals("启动前开关锁")) {
            return 1;
        } else if (value.equals("启动前开锁")) {
            return 2;
        }
        return 0;
    }

    private int getFindCarWayCount(String value) {
        if (value.equals("喇叭控制方式")) {
            return 1;
        } else if (value.equals("短按1次")) {
            return 2;
        }else if (value.equals("连按2次")) {
            return 3;
        }else if (value.equals("连按3次")) {
            return 4;
        }else if (value.equals("长按3秒")) {
            return 5;
        }else if (value.equals("长按5秒")) {
            return 6;
        }else if (value.equals("长按7秒")) {
            return 7;
        }
        return 0;
    }
    private int getFindCarTimeCount(String value) {
        if (value.equals("500ms")) {
            return 1;
        } else if (value.equals("600ms")) {
            return 2;
        }else if (value.equals("700ms")) {
            return 3;
        }else if (value.equals("800ms")) {
            return 4;
        }else if (value.equals("900ms")) {
            return 5;
        }else if (value.equals("1000ms")) {
            return 6;
        }
        return 0;
    }


    private int getEvasiveDeviceMini(String value) {
        if (value.equals("开锁通")) {
            return 0;
        } else if (value.equals("踩脚刹通")) {
            return 1;
        }
        return 0;
    }

    private int getEvasiveDeviceMiniBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        }
        return 0;
    }

    private int getLockUpwindowInterverTimeLevel(String value) {
        if (value.equals("500ms")) {
            return 0;
        } else if (value.equals("600ms")) {
            return 1;
        } else if (value.equals("700ms")) {
            return 2;
        } else if (value.equals("800ms")) {
            return 3;
        } else if (value.equals("900ms")) {
            return 4;
        } else if (value.equals("1000ms")) {
            return 5;
        } else if (value.equals("0ms")) {
            return 6;
        }
        return 0;
    }
    private int getLockUpwindowInterverTimeLevelBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        } else if (value.equals("2")) {
            return 2;
        } else if (value.equals("3")) {
            return 3;
        } else if (value.equals("4")) {
            return 4;
        } else if (value.equals("5")) {
            return 5;
        }
        return 0;
    }


    private int getEvasiveControlWay(String value) {
        if (value.equals("1次控制")) {
            return 0;
        } else if (value.equals("连续2次")) {
            return 1;
        }
        return 0;
    }
    private int getEvasiveControlWayBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        }
        return 0;
    }

    private int getLockOrUnlockTrigerWay(String value) {
        if (value.equals("1次控制")) {
            return 0;
        } else if (value.equals("连续2次")) {
            return 1;
        }
        return 0;
    }
    private int getLockOrUnlockTrigerWayBySocket(String value) {
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("1")) {
            return 1;
        }
        return 0;
    }


    @Override
    public void invalidateUI() {
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        JsonObject obj = DataCarInfo.toJsonObject(car);
        if (BuildConfig.DEBUG) Log.e("车辆禁声", "car " + obj);
        if (car != null) {
            //MINI版本
            if (EquipmentManager.isMini()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    lock_down.setVisibility(View.GONE);
                }else{
                    lock_down.setVisibility(View.VISIBLE);
                    if (car.isQuitLock == 0) {
                        lock_down.img_right.setImageResource(R.drawable.car_set_off);
                    } else {
                        lock_down.img_right.setImageResource(R.drawable.car_set_on);
                    }
                }
                trumpet_switch_level.setVisibility(View.VISIBLE);
                open_trunk_way.setVisibility(View.VISIBLE);
                item_upwindow_time.setVisibility(View.VISIBLE);
                item_evasive_pass_time.setVisibility(View.VISIBLE);
                item_evasive_close_time.setVisibility(View.VISIBLE);
                item_evasive_control_time.setVisibility(View.VISIBLE);
                up_window_delay_time.setVisibility(View.VISIBLE);
                if(EquipmentManager.isMinJiaQiang()){
                    recover_factory.setVisibility(View.GONE);
                }else{
                    recover_factory.setVisibility(View.VISIBLE);
                }
                if(EquipmentManager.isMinJiaQiang()){
                   lock_unlock_settings.setVisibility(View.VISIBLE);
                }else{
                    lock_unlock_settings.setVisibility(View.GONE);
                }
                mini_evasive_device.setVisibility(View.VISIBLE);
//                item_evasive_control_way.setVisibility(View.VISIBLE);
                int isTruckOpenMode = BlueInstructionCollection.getInstance().getTruckOpenMode();
                cacheProsetData.trunkOpenWith=isTruckOpenMode;
                if (isTruckOpenMode == 0) {
                    open_trunk_way.txt_bottom.setText("连按2次");
                } else  if (isTruckOpenMode == 1){
                    open_trunk_way.txt_bottom.setText("长按3秒");
                }else   if (isTruckOpenMode == 2) {
                    open_trunk_way.txt_bottom.setText("长按5秒");
                }else   if (isTruckOpenMode == 3) {
                    open_trunk_way.txt_bottom.setText("长按7秒");
                }else   if (isTruckOpenMode == 4) {
                    open_trunk_way.txt_bottom.setText("长按9秒");
                }else   if (isTruckOpenMode == 32) {
                    open_trunk_way.txt_bottom.setText("连按3次");
                }

                int istruckLevel = BlueInstructionCollection.getInstance().getTruckLevel();
                cacheProsetData.hornVolume=istruckLevel;
                if (istruckLevel == 0) {
                    trumpet_switch_level.txt_bottom.setText("低");
                } else if (istruckLevel == 1) {
                    trumpet_switch_level.txt_bottom.setText("中");
                } else if (istruckLevel == 2) {
                    trumpet_switch_level.txt_bottom.setText("高");
                } else if (istruckLevel == 3) {
                    trumpet_switch_level.txt_bottom.setText("最高");
                }

                int isOpenLockUpWindow = BlueInstructionCollection.getInstance().getIsOpenLockWindowUp();
                cacheProsetData.carLockWindowRise=isOpenLockUpWindow;
                if (isOpenLockUpWindow == 0) {
                    lock_up_window.img_right.setImageResource(R.drawable.car_set_off);
                } else {
                    lock_up_window.img_right.setImageResource(R.drawable.car_set_on);
                }

                int isUpWindowLevel = BlueInstructionCollection.getInstance().getUpWindowLevel();
                setUpwindowUI(isUpWindowLevel);
                int isEvasivePassLevel = BlueInstructionCollection.getInstance().getEvasivePassLevel();
                setEvasiveUI(isEvasivePassLevel, 1);
                int isEvasiveCloseLevel = BlueInstructionCollection.getInstance().getEvasiveCloseLevel();
                setEvasiveUI(isEvasiveCloseLevel, 2);
                int isEvasiveControlLevel = BlueInstructionCollection.getInstance().getEvasiveControlLevel();
                setEvasiveUI(isEvasiveControlLevel, 3);
                int isLockUpWindowIntervalTimeLevel = BlueInstructionCollection.getInstance().getLockUpWindowIntervalTime();
                setUpWindowDelayTimeUI(isLockUpWindowIntervalTimeLevel);
                int findCarway = BlueInstructionCollection.getInstance().getFindCarWay();
                setFindCarWayUI(findCarway);
                int jiangeTime = BlueInstructionCollection.getInstance().getFindCarJianGeTime();
                setFindCarJiangeTimeUI(jiangeTime);
                pushToServer();
                if(isFirstIn>1){
                    if(isTruckOpenMode==2||isTruckOpenMode==3||isTruckOpenMode==4){
                        isRecivePush=true;
                        pushToServer();
                    }
                }
            } else {
                lock_down.setVisibility(View.VISIBLE);
                trumpet_switch_level.setVisibility(View.GONE);
                open_trunk_way.setVisibility(View.GONE);
                if (car.isQuitLock == 0) {
                    lock_down.img_right.setImageResource(R.drawable.car_set_off);
                } else {
                    lock_down.img_right.setImageResource(R.drawable.car_set_on);
                }
                if (car.lockCloseWin == 0) {
                    lock_up_window.img_right.setImageResource(R.drawable.car_set_off);
                } else {
                    lock_up_window.img_right.setImageResource(R.drawable.car_set_on);
                }
            }
        }
    }

    private void setUpwindowUI(int level) {
        cacheProsetData.windowRiseTime=level;
        String txt = "";
        switch (level) {
            case 0:
                txt = "5s";
                break;
            case 1:
                txt = "10s";
                break;
            case 2:
                txt = "15s";
                break;
        }
        item_upwindow_time.txt_bottom.setText(txt);
    }

    private void setEvasiveUI(int level, int type) {
        String txt = level * 100 + "ms";
        if (level == 0) {
            txt = "";
        }
        if (type == 1) {
            cacheProsetData.electrifyBeforehandTime=level-3;
            item_evasive_pass_time.txt_bottom.setText(txt);
        } else if (type == 2) {
            cacheProsetData.switchesOffDelayTime=level-3;
            item_evasive_close_time.txt_bottom.setText(txt);
        } else if (type == 3) {
            cacheProsetData.pressKayTime=level-3;
            item_evasive_control_time.txt_bottom.setText(txt);
        }
    }

    private void setEvasiveControlWayUI(int level) {
        cacheProsetData.avoidanceDeviceTechnique=level;
        pushToServer();
        if (level == 0) {
            item_evasive_control_way.txt_bottom.setText("1次控制");
        } else if (level == 1) {
            item_evasive_control_way.txt_bottom.setText("连续2次");
        }
    }

    private void setLockTrigerWayUI(int level) {
        cacheProsetData.lockWay=level;
        if (level == 0) {
            lock_trigger_way.txt_bottom.setText("1次控制");
        } else if (level == 1) {
            lock_trigger_way.txt_bottom.setText("连续2次");
        }
        pushToServer();
    }

    private void setUnLockTrigerWayUI(int level) {
        cacheProsetData.unlockWay=level;
        if (level == 0) {
            unlock_trigger_way.txt_bottom.setText("1次控制");
        } else if (level == 1) {
            unlock_trigger_way.txt_bottom.setText("连续2次");
        }
        pushToServer();
    }

    private void setUpWindowDelayTimeUI(int level) {
        if(level>0){
            cacheProsetData.windowRiseInterval=level-5;
        }
        String txt = level * 100 + "ms";
        up_window_delay_time.txt_bottom.setText(txt);
    }
    private void setFindCarWayUI(int level) {
        String txt = "";
        switch (level){
            case 0x00:
                txt="喇叭控制";
                break;
            case 0x10:
                txt="短按1次";
                break;
            case 0x20:
                txt="连按2次";
                break;
            case 0x30:
                txt="连按3次";
                break;
            case 0x13:
                txt="长按3秒";
                break;
            case 0x15:
                txt="长按5秒";
                break;
            case 0x17:
                txt="长按7秒";
                break;
        }
        findcar_way.txt_bottom.setText(txt);
    }
    private void setFindCarJiangeTimeUI(int level) {
        String txt = level * 100 + "ms";
        findcar_jiange_time.txt_bottom.setText(txt);
    }
}
