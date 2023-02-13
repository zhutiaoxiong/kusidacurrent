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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;

/**
 * Created by qq522414074 on 2017/12/15.
 */

public class ClipPopControlAirConditionNew  implements OEventObject {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;
    private RelativeLayout thisView;

    private RelativeLayout lin_base;
    private static ClipPopControlAirConditionNew _instance;
    private SeekBar seek_control_big_or_small;
    private ImageView bg_control_wind,wind_open;
    private Button  btn_confirm;
    private ImageView btn_cancel;
    private DataCarInfo currentCar;
    private RelativeLayout re1, re2;//re1代表风量那一栏，re2代表空调那一栏
    private ImageView  is_show_air_pop;
    private Button id_img_ac_status;
    private SeekBar viewControlHotOrCold;//控制空调冷热
    private Animation operatingAnim ;
    private ViewSeatMaster seat_master;
    private ViewSeatCopilot seat_copilot;
   private LinearInterpolator lin = new LinearInterpolator();
   private ImageView img_bg_control_hot_or_cold;



    private int nowWindLever, nowHotColdLever, nowAcStatus;
    private int defaultWindLever, defaultHotColdLever, defaultAcStatus;
    public static long time=0;
    public  static boolean isTouch=false;

    public boolean isShowing=false;

    public static ClipPopControlAirConditionNew getInstance() {
        if (_instance == null)
            _instance = new ClipPopControlAirConditionNew();
        return _instance;
    }

    public void show(DataCarInfo currentCar, View parentView) {
//        if(isShowing){
//            return;
//        }
        this.parentView = parentView;
        this.currentCar = currentCar;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_control_air_condition_new, null);
        lin_base = (RelativeLayout) thisView.findViewById(R.id.lin_base);
        re1 = (RelativeLayout) thisView.findViewById(R.id.re1);
        re2 = (RelativeLayout) thisView.findViewById(R.id.re2);
        seek_control_big_or_small = (SeekBar) thisView.findViewById(R.id.seek_control_big_or_small);
        bg_control_wind = (ImageView) thisView.findViewById(R.id.bg_control_wind);
        btn_cancel = (ImageView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        id_img_ac_status = (Button) thisView.findViewById(R.id.id_img_ac_status);
        is_show_air_pop = (ImageView) thisView.findViewById(R.id.is_show_air_pop);
        viewControlHotOrCold = (SeekBar) thisView.findViewById(R.id.air_conditon_hot_cold);
        wind_open = (ImageView) thisView.findViewById(R.id.wind_open);
        seat_master = (ViewSeatMaster) thisView.findViewById(R.id.seat_master);
        seat_copilot = (ViewSeatCopilot) thisView.findViewById(R.id.seat_copilot);
        img_bg_control_hot_or_cold= (ImageView) thisView.findViewById(R.id.img_bg_control_hot_or_cold);
        seat_master.setUI(currentCar);
        seat_copilot.setUI(currentCar);
        setAirCondition(currentCar);
        setIs_show_air_pop();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.TOUCH_AIR_CONTROL,this);
    }
    public void hidePopAirCondition(){
        if(!isShowing)return;
        if(popContain==null)return;
        popContain.dismiss();
        parentView = null;
        isShowing=false;
    }

    private void setWindLevel(int level){
        if(level==0){
            wind_open.setImageResource(R.drawable.ic_wind_small);
        }else   if(level==1){
            wind_open.setImageResource(R.drawable.ic_wind_center);
        }else   if(level==2){
            wind_open.setImageResource(R.drawable.ic_wind_big);
        }

    }

    public void setIs_show_air_pop() {
        String isShowAirPop = ODBHelper.getInstance(context).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId, "isShowAirPop");
        if (isShowAirPop.equals("")) {
            is_show_air_pop.setSelected(false);
        } else if (isShowAirPop.equals("yes")) {
            is_show_air_pop.setSelected(true);
        }
    }

    public void setAirCondition(DataCarInfo currentCar) {
        if(currentCar==null)return;
//        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
        DataCarStatus status = ManagerCarList.getInstance().getNetCurrentCarStatus();
        if (status == null) return;
        viewControlHotOrCold.setThumb(GlobalContext.getContext().getResources().getDrawable(R.drawable.control_big_or_small_thumb_new));
        img_bg_control_hot_or_cold.setImageResource(R.drawable.bg_control_wind_hot_cold_new);
        if (status.ACStatus == 0) {
            defaultAcStatus = 0;
            nowAcStatus=0;
            id_img_ac_status.setSelected(false);
        } else if (status.ACStatus == 1) {
            defaultAcStatus = 1;
            nowAcStatus=1;
            id_img_ac_status.setSelected(true);
        } else if (status.ACStatus == -1) {
            defaultAcStatus=-1;
            nowAcStatus = -1;
        }

        if (status.windLevel == 0) {
            defaultWindLever = 0;
            nowWindLever=0;
            seek_control_big_or_small.setProgress(0);
            setWindLevel(0);
        } else if (status.windLevel == 1) {
            defaultWindLever = 1;
            nowWindLever=1;
            seek_control_big_or_small.setProgress(50);
            setWindLevel(1);
        } else if (status.windLevel == 2) {
            defaultWindLever = 2;
            nowWindLever=2;
            seek_control_big_or_small.setProgress(100);
            setWindLevel(2);
        } else if (status.windLevel == -1) {
            defaultWindLever = -1;
            nowWindLever=-1;
        }

        if (status.tempControlStatus == 0) {
            defaultHotColdLever = 1;
            nowHotColdLever=1;
            viewControlHotOrCold.setProgress(16);
        } else if (status.tempControlStatus == 1) {
            defaultHotColdLever = 2;
            nowHotColdLever=2;
            viewControlHotOrCold.setProgress(56);
        } else if (status.tempControlStatus == 2) {
            defaultHotColdLever = 3;
            nowHotColdLever=3;
            viewControlHotOrCold.setProgress(96);
        } else if (status.tempControlStatus == 3) {
            defaultHotColdLever = 4;
            nowHotColdLever=4;
            viewControlHotOrCold.setProgress(135);
        } else if (status.tempControlStatus == 4) {
            defaultHotColdLever = 5;
            nowHotColdLever=5;
            viewControlHotOrCold.setProgress(175);
        } else if (status.tempControlStatus == 5) {
            defaultHotColdLever = 6;
            nowHotColdLever=6;
            viewControlHotOrCold.setProgress(215);
        } else if (status.tempControlStatus == 6) {
            defaultHotColdLever = 7;
            nowHotColdLever=7;
            viewControlHotOrCold.setProgress(254);
        } else if (status.tempControlStatus == -1) {
            defaultHotColdLever = -1;
            nowHotColdLever=-1;
            viewControlHotOrCold.setThumb(GlobalContext.getContext().getResources().getDrawable(R.drawable.control_big_or_small_thumb_new_gray));
            img_bg_control_hot_or_cold.setImageResource(R.drawable.bg_control_wind_hot_cold_new_gray);
        }

    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);//设了这个就不能点外面了
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);//R.color.background_all
//        Drawable dw = GlobalContext.getContext().getResources().getDrawable(R.color.background_all);//no color no initClick
//        popContain.setBackgroundDrawable(dw);
//        popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();

                    isShowing=false;
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        isShowing=true;
        isTouch=false;
        ViewControlPanelControl.isShowAirCondition=false;
    }

    public void initEvents() {
        id_img_ac_status.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                isTouch=true;
                    if (nowAcStatus == 0) {
                        nowAcStatus = 1;
                        id_img_ac_status.setSelected(true);
                    } else if (nowAcStatus == 1) {
                        nowAcStatus = 0;
                        id_img_ac_status.setSelected(false);
                    }else  if (nowAcStatus == -1) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "暂不支持该功能");
                    }
            }
        });
//        viewControlHotOrCold.setOnThumbChangeListner(new ViewControlHotOrCold.OnThumbChangeListner() {
//            @Override
//            public void backCurrentThumb(int level) {
//                nowHotColdLever = level;
//            }
//
//            @Override
//            public void thumbChange() {
//                isTouch=true;
//            }
//        });
        is_show_air_pop.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String isShowAirPop = ODBHelper.getInstance(context).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId, "isShowAirPop");
                if (isShowAirPop.equals("")) {
                    ODBHelper.getInstance(context).changeUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId, "isShowAirPop", "yes");
                    is_show_air_pop.setSelected(true);
                } else if (isShowAirPop.equals("yes")) {
                    ODBHelper.getInstance(context).changeUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId, "isShowAirPop", "");
                    is_show_air_pop.setSelected(false);
                }
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
                parentView = null;
                isShowing=false;
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                time=System.currentTimeMillis();
                DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//                DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                DataCarStatus status = ManagerCarList.getInstance().getNetCurrentCarStatus();
                if (carInfo == null) return;
                if (status == null) return;
                int acStatus = -1;
                int windLever = -1;
                int hotColdLever = -1;
//                if (nowAcStatus == defaultAcStatus) {
//                    acStatus = -1;
//                } else {
                    acStatus = nowAcStatus;
//                }
//                if (nowWindLever == defaultWindLever) {
//                    windLever = -1;
//                } else {
                    windLever = nowWindLever;
//                }
//                if (nowHotColdLever == defaultHotColdLever) {
//                    hotColdLever = -1;
//                } else {
                    hotColdLever = nowHotColdLever;
//                }
                if(hotColdLever==-1){
                    OCtrlCar.getInstance().ccmd1248_Change_Air_Condition(carInfo.ide, acStatus, windLever,  - 1);
                }else{
                    OCtrlCar.getInstance().ccmd1248_Change_Air_Condition(carInfo.ide, acStatus, windLever, hotColdLever - 1);
                }

                DelaySendAirControl();
                 if (BuildConfig.DEBUG) Log.e("watch", "carInfo.ide=" + carInfo.ide + "carInfo.status.ACStatus=" + status.ACStatus + "windLever=" + windLever + "hotColdLever=" + hotColdLever);
                popContain.dismiss();
                isShowing=false;
                parentView = null;
            }
        });
        seek_control_big_or_small.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch=true;
                int progress = seekBar.getProgress();
                if (progress >= 0 && progress < 25) {
                    nowWindLever = 0;
                    seek_control_big_or_small.setProgress(0);
                    setWindLevel(0);
                } else if (progress >= 25 && progress < 75) {
                    nowWindLever = 1;
                    seek_control_big_or_small.setProgress(50);
                    setWindLevel(1);
                } else if (progress >= 75) {
                    nowWindLever = 2;
                    seek_control_big_or_small.setProgress(100);
                    setWindLevel(2);
                }
            }
        });

        viewControlHotOrCold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("温度","onProgressChanged");
                if(nowHotColdLever==-1){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "暂不支持该功能");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("温度","onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("温度","onStopTrackingTouch");
                isTouch=true;
                int progress = seekBar.getProgress();
                setHotCold(progress);
            }
        });
    }

    private void setHotCold(int progress){
        if (progress >= 0 && progress < 22.5) {
            nowHotColdLever=1;
            viewControlHotOrCold.setProgress(16);
        } else if (progress >= 22.5 && progress < 64.5) {
            nowHotColdLever=2;
            viewControlHotOrCold.setProgress(56);
        } else if (progress >=64.5 && progress < 109.5) {
            nowHotColdLever=3;
            viewControlHotOrCold.setProgress(96);
        }else if (progress >= 109.5 && progress < 154.5) {
            nowHotColdLever=4;
            viewControlHotOrCold.setProgress(135);
        } else if (progress >= 154.5 && progress < 199.5) {
            nowHotColdLever=5;
            viewControlHotOrCold.setProgress(175);
        }else if (progress >= 199.5 && progress <244.5) {
            nowHotColdLever=6;
            viewControlHotOrCold.setProgress(215);
        } else if (progress >= 244.5) {
            nowHotColdLever=7;
            viewControlHotOrCold.setProgress(254);
        }
    }
    private int messageNum;
    public void DelaySendAirControl(){
        new CountDownTimerMy(2000L, 400L) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(messageNum == 0){
                    HttpAirControlUtils.sendLeftAir(seat_master.item1.getmStatus(),1);
                }else if(messageNum == 1){
                    HttpAirControlUtils.sendLeftHeat(seat_master.item2.getmStatus(),2);
                }else if(messageNum == 2){
                    HttpAirControlUtils.sendRightAir(seat_copilot.item3.getmStatus(),3);
                }else if(messageNum == 3){
                    HttpAirControlUtils.sendRightHeat(seat_copilot.item4.getmStatus(),4);
                }
                messageNum++;
            }
            @Override
            public void onFinish() {
                messageNum = 0;
            }
        }.start();
    }
    public void handleChangeAirCondition(DataCarInfo carInfo){
        Log.i("abcde", "动态改变弹框");
        Message message=Message.obtain();
        message.what=110;
        message.obj=carInfo;
        handler.sendMessage(message);
    }

    public void handleCloseAirCondition(){
        Log.i("abcde", "关闭弹框");
        Message message=Message.obtain();
        message.what=112;
        handler.sendMessage(message);
    }
    public void handleChangeSeatMaster(DataCarInfo carInfo){
        Message message=Message.obtain();
        message.what=113;
        message.obj=carInfo;
        handler.sendMessage(message);
    }
    public void handleChangeSeatPolit(DataCarInfo carInfo){
        Message message=Message.obtain();
        message.what=114;
        message.obj=carInfo;
        handler.sendMessage(message);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg!=null){
                if(msg.what==110){
                    DataCarInfo carInfo= (DataCarInfo) msg.obj;
                    if(carInfo!=null){
                        setAirCondition(carInfo);
                    }
                }else if(msg.what==112){
                  hidePopAirCondition();
                }else if(msg.what==113){
                    DataCarInfo carInfo= (DataCarInfo) msg.obj;
                    if(carInfo!=null){
                        seat_master.setUI(carInfo);
                    }
                }else if(msg.what==114){
                    DataCarInfo carInfo= (DataCarInfo) msg.obj;
                    if(carInfo!=null){
                        seat_copilot.setUI(carInfo);
                    }
                }
            }
        }
    };

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.TOUCH_AIR_CONTROL)){
            isTouch=true;
        }
    }
}
