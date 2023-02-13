package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;

import com.kulala.linkscarpods.blue.BlueAdapter;
import com.kulala.staticsfunc.time.CountDownTimerMy;

import java.util.ArrayList;
import java.util.List;

import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;

public class ViewTestBlueRsi extends LinearLayout {
    private static final String LOCK_TXT = "设防信号强度:";
    private static final String UNLOCK_TXT = "撤防信号强度:";
    private static final String AUTO_TXT = "自动设防撤防:";
    private static final String TIME_TXT = "信号间隔(ms):";
    private static final String MEGE_TXT = "设撤防判定数:";
    private static final String AVERAGE_TXT = "信号平均次数:";
    private static final String TYPE_TXT = "信号取值:";
    public static int VALUE_LOCK = 95;
    public static int VALUE_UNLOCK = 65;
    public static int VALUE_TIME = 300;
    public static int VALUE_AVERAGE = 3;
    public static int VALUE_MEGE = 2;
    public static String VALUE_TYPE = "最强";
    private TextView  txt_state,txt_state_motion;//提示:未连接
    private TextView  txt_rsi,txt_used_rsi;//信号强度:85
    private Button btn_type;//自动设防撤防:开
    private Button btn_auto;//自动设防撤防:开
    private Button    btn_lock,btn_lock_add,btn_lock_cut;
    private Button    btn_unlock,btn_unlock_add,btn_unlock_cut;
    private Button    btn_time,btn_time_add,btn_time_cut;
    private Button    btn_mege,btn_mege_add,btn_mege_cut;
    private Button    btn_average,btn_average_add,btn_average_cut;
    //=========================
    public static ViewTestBlueRsi viewTestBlueRsiThis;
    private MyHandler handler = new MyHandler();
    public ViewTestBlueRsi(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_control_panel_testbluersi, this, true);
        txt_state = (TextView) findViewById(R.id.txt_state);
        txt_state_motion = (TextView) findViewById(R.id.txt_state_motion);
        txt_rsi = (TextView) findViewById(R.id.txt_rsi);
        txt_used_rsi = (TextView) findViewById(R.id.txt_used_rsi);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_lock = (Button) findViewById(R.id.btn_lock);
        btn_lock_add = (Button) findViewById(R.id.btn_lock_add);
        btn_lock_cut = (Button) findViewById(R.id.btn_lock_cut);
        btn_unlock = (Button) findViewById(R.id.btn_unlock);
        btn_unlock_add = (Button) findViewById(R.id.btn_unlock_add);
        btn_unlock_cut = (Button) findViewById(R.id.btn_unlock_cut);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_time_add = (Button) findViewById(R.id.btn_time_add);
        btn_time_cut = (Button) findViewById(R.id.btn_time_cut);
        btn_mege = (Button) findViewById(R.id.btn_mege);
        btn_mege_add = (Button) findViewById(R.id.btn_mege_add);
        btn_mege_cut = (Button) findViewById(R.id.btn_mege_cut);
        btn_average = (Button) findViewById(R.id.btn_average);
        btn_average_add = (Button) findViewById(R.id.btn_average_add);
        btn_average_cut = (Button) findViewById(R.id.btn_average_cut);
        btn_type = (Button) findViewById(R.id.btn_type);
        initEvents();
        btn_unlock.setText(UNLOCK_TXT+VALUE_UNLOCK);
        btn_lock.setText(LOCK_TXT+VALUE_LOCK);
        btn_time.setText(TIME_TXT+VALUE_TIME);
        btn_mege.setText(MEGE_TXT+VALUE_MEGE);
        btn_average.setText(AVERAGE_TXT+VALUE_AVERAGE);
        btn_type.setText(TYPE_TXT+VALUE_TYPE);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewTestBlueRsiThis = this;
        changeRsiTime();
    }
    private void changeRsiTime(){
        if(countDownTimerMy!=null)countDownTimerMy.cancel();
        int ms = getBtnValueRsi(btn_time);
        if(ms<100 || ms>900)return;
        countDownTimerMy = new CountDownTimerMy(Integer.MAX_VALUE,ms) {
            @Override
            public void onTick(long millisUntilFinished) {
                handler.obtainMessage(HANDLER_SHOW_STATE).sendToTarget();
                if(BlueLinkReceiver.getInstance().getIsBlueConnOK()){
                    BlueAdapter.getInstance().readRssiRemote();
                }
            }
            @Override
            public void onFinish() {

            }
        };
        countDownTimerMy.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        viewTestBlueRsiThis = null;
        if(countDownTimerMy!=null)countDownTimerMy.cancel();
        super.onDetachedFromWindow();

    }
    int count = 0;//1000/300
    List<Integer>  rssiCache;
    public void onReadRemoteRssi(int rssi, int status) {
        handler.obtainMessage(HANDLER_READ_RSSI,Math.abs(rssi),status).sendToTarget();
        int maxcount = getBtnValueRsi(btn_average);
        count ++;
        if(count == 1){
            rssiCache = new ArrayList<Integer>();
        }
        if(count>maxcount){
            String preCount = getBtnValueTxt(btn_type);
            Integer minValue = 1000,maxValue = 0;
            if(maxcount>=3){
                for(int value : rssiCache){
                    if(value<minValue)minValue = value;
                    if(value>maxValue)maxValue = value;
                }
                rssiCache.remove(minValue);
                rssiCache.remove(maxValue);
            }
            int result = 0;
            if(preCount.equals("最强")){
                for(int value : rssiCache){
                    if(result == 0){
                        result = value;
                    }else if(value<result){
                        result = value;
                    }
                }
            }else if(preCount.equals("平均")){
                int mix=0,addNum=0;
                for(int value : rssiCache){
                    if(value!=0){
                        mix += value;
                        addNum++;
                    }
                }
                result = mix/addNum;
            }else{
                for(int value : rssiCache){
                    if(value!=0 && value>result)result = value;
                }
            }
             if (BuildConfig.DEBUG) Log.e("rsi","count:"+count+"  result:"+result);
            handler.obtainMessage(HANDLER_DO_CMD,result,0).sendToTarget();
            handler.obtainMessage(HANDLER_COUNT_RSSI,result,0).sendToTarget();
            count = 0;
        }else {
            rssiCache.add(Math.abs(rssi));
        }
    }

    private CountDownTimerMy countDownTimerMy;

    public void initEvents() {
        btn_time_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preTime = getBtnValueRsi(btn_time);
                if(preTime<=800){
                    btn_time.setText(TIME_TXT+(preTime+100));
                    VALUE_TIME = preTime+100;
                    changeRsiTime();
                }
            }
        });
        btn_time_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preTime = getBtnValueRsi(btn_time);
                if(preTime>=200){
                    btn_time.setText(TIME_TXT+(preTime-100));
                    VALUE_TIME = preTime-100;
                    changeRsiTime();
                }
            }
        });
        btn_average_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preCount = getBtnValueRsi(btn_average);
                if(preCount<=9){
                    btn_average.setText(AVERAGE_TXT+(preCount+1));
                    VALUE_AVERAGE = preCount+1;
                }
            }
        });
        btn_average_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preCount = getBtnValueRsi(btn_average);
                if(preCount>=2){
                    btn_average.setText(AVERAGE_TXT+(preCount-1));
                    VALUE_AVERAGE = preCount-1;
                }
            }
        });
        btn_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String preCount = getBtnValueTxt(btn_type);
                if(preCount.equals("最强")){
                    btn_type.setText(TYPE_TXT+"平均");
                    VALUE_TYPE = "平均";
                }else if(preCount.equals("平均")){
                    btn_type.setText(TYPE_TXT+"最弱");
                    VALUE_TYPE = "最弱";
                }else{
                    btn_type.setText(TYPE_TXT+"最强");
                    VALUE_TYPE = "最强";
                }
            }
        });

        btn_mege_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preCount = getBtnValueRsi(btn_mege);
                if(preCount<=4){
                    btn_mege.setText(MEGE_TXT+(preCount+1));
                    VALUE_MEGE = preCount+1;
                    matchLockCount = preCount+1;
                    matchUnLockCount = preCount+1;
                }
            }
        });
        btn_mege_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int preCount = getBtnValueRsi(btn_mege);
                if(preCount>=2){
                    btn_mege.setText(MEGE_TXT+(preCount-1));
                    VALUE_MEGE = preCount-1;
                    matchLockCount = preCount-1;
                    matchUnLockCount = preCount-1;
                }
            }
        });
        btn_lock_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevalue = getBtnValueRsi(btn_lock);
                btn_lock.setText(LOCK_TXT+(prevalue+1));
                VALUE_LOCK = prevalue+1;
            }
        });
        btn_lock_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int setValue = getBtnValueRsi(btn_lock)-1;
                int unValue = getBtnValueRsi(btn_unlock);
                if(setValue<=0)setValue = 0;
                if(setValue <=unValue)setValue = unValue +1;
                btn_lock.setText(LOCK_TXT+setValue);
                VALUE_LOCK = setValue;
            }
        });
        btn_unlock_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int setValue = getBtnValueRsi(btn_unlock)+1;
                int loValue = getBtnValueRsi(btn_lock);
                if(setValue >=loValue)setValue = loValue -1;
                btn_unlock.setText(UNLOCK_TXT+setValue);
                VALUE_UNLOCK = setValue;
            }
        });
        btn_unlock_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int setValue = getBtnValueRsi(btn_unlock)-1;
                if(setValue<=0)setValue = 0;
                btn_unlock.setText(UNLOCK_TXT+setValue);
                VALUE_UNLOCK = setValue;
            }
        });
        btn_auto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_auto.setText(AUTO_TXT+(getBtnValueTxt(btn_auto).equals("开") ? "关" : "开"));
            }
        });
    }
    private int getBtnValueRsi(Button btn){
        String txt = btn.getText().toString().trim();
        String[] arr = txt.split(":");
        if(arr.length == 2){
            return  Integer.valueOf(arr[1]);
        }
        return 0;
    }
    private String getBtnValueTxt(Button btn){
        String txt = btn.getText().toString().trim();
        String[] arr = txt.split(":");
        if(arr.length == 2){
            return  arr[1];
        }
        return "";
    }


    //======================================
    private static final int HANDLER_SHOW_STATE     = 101;
    private static final int HANDLER_READ_RSSI      = 102;
    private static final int HANDLER_DO_CMD         = 103;
    private static final int HANDLER_COUNT_RSSI      = 104;
    private int matchLockCount=2,matchUnLockCount=2,lockCount=0,unLockCount = 0;
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_STATE:
                    if(BlueLinkReceiver.getInstance().getIsBlueConnOK()){
                        txt_state.setText("提示:已连接");
                    }else{
                        txt_state.setText("提示:未连接");
                    }
                    break;
                case HANDLER_READ_RSSI:
                    int rssi = msg.arg1;
                    int states = msg.arg2;
                    txt_rsi.setText("信号强度:"+rssi+" 状态值:"+states);
                    break;
                case HANDLER_COUNT_RSSI:
                    int rssiu = msg.arg1;
                    txt_used_rsi.setText(""+rssiu);
                    break;
                case HANDLER_DO_CMD:
                    int rssiMin = msg.arg1;
                    if(getBtnValueTxt(btn_auto).equals("开") && BlueLinkReceiver.getInstance().getIsBlueConnOK()){
                        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                        if (carInfo == null || carInfo.isBindBluetooth == 0) return;
                        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                        if (carStatus.isON == 1 && carStatus.isTheft == 0) return;//启动非防盗不起作用
                        if(carStatus.isTheft == 0){//需要设防
                            if(rssiMin>getBtnValueRsi(btn_lock)){
                                lockCount++;
                                if(lockCount>=matchLockCount){
                                    OCtrlCar.getInstance().ccmd1233_controlCar(carInfo, 3, 0);
                                    ViewControlPanelControl.preControlCmd = 3;
                                    txt_state_motion.setText("执行设防");
                                    lockCount = 0;
                                }
                            }else{
                                 if (BuildConfig.DEBUG) Log.e("rsi","取消设防计数");
                                lockCount = 0;
                            }
                            txt_state_motion.setText("设防计数:"+lockCount);
                            unLockCount = 0;
                        }else if(carStatus.isTheft == 1){//近车要撤防
                            if(rssiMin<getBtnValueRsi(btn_unlock)){
                                unLockCount++;
                                if(unLockCount>=matchLockCount){
                                    OCtrlCar.getInstance().ccmd1233_controlCar(carInfo, 4, 0);
                                    ViewControlPanelControl.preControlCmd = 4;
                                    txt_state_motion.setText("执行撤防");
                                    unLockCount = 0;
                                }
                            }else{
                                 if (BuildConfig.DEBUG) Log.e("rsi","取消撤防计数");
                                unLockCount = 0;
                            }
                            txt_state_motion.setText("撤防计数:"+unLockCount);
                            lockCount = 0;
                        }
                    }
                    break;
            }
        }

    }
    //======================================
}
