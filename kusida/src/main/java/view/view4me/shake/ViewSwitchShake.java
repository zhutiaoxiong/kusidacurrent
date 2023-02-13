package view.view4me.shake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCommon;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.status.DataSwitch;
import view.clip.ClipLineBtnInptxt;
import view.find.OToastButtonBlackStyle;
import view.find.ProItem;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;

import static android.content.Context.SENSOR_SERVICE;

public class ViewSwitchShake extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    //    private ClipLineBtnTxt txt_selectcar;
    private LinearLayout  lin_blueswitch, lin_blue_shake_tips;
    private TextView txt_info, txt_quite, txt_sure;
    private ImageView img_blueswitch, img_blue_vibration;
//    private ImageView img_blue_vibration_cover;
    private List<DataCarInfo> cacheBlueList;
    private DataCarInfo       cacheShowingCar;
    //    private RelativeLayout re_shake;
    private RelativeLayout    re_windows, lin_blue_vibration,re2_a;
    private Button            btn_know, btn_cancle;
    private ClipLineBtnInptxt txt_shake_product, txt_shake_bind;
    private ClipSetItem txt_shake_set,txt_shake_tips,txt_shake_phone;
    private MyHandler handler = new MyHandler();
    public static ViewSwitchShake viewSwitchShakeThis;
    private Context mContext;
    private ProItem shake_level_set;
    private TextView tv_content;
    private SpannableString msp = null;

    public ViewSwitchShake(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_shake, this, true);
        title_head =  findViewById(R.id.title_head);
//        txt_selectcar = (ClipLineBtnTxt) findViewById(R.id.txt_selectcar);
        lin_blueswitch = (LinearLayout) findViewById(R.id.lin_blueswitch);
        lin_blue_vibration = (RelativeLayout) findViewById(R.id.lin_blue_vibration);//震动开关
//        re_shake = (RelativeLayout) findViewById(R.id.re_shake);
        txt_shake_set =  findViewById(R.id.txt_shake_set);
        txt_shake_tips = findViewById(R.id.txt_shake_tips);
        txt_shake_phone =findViewById(R.id.txt_shake_phone);
        txt_shake_product = (ClipLineBtnInptxt) findViewById(R.id.txt_shake_product);
        txt_shake_bind = (ClipLineBtnInptxt) findViewById(R.id.txt_shake_bind);
        img_blueswitch = (ImageView) findViewById(R.id.img_blueswitch);
        img_blue_vibration = (ImageView) findViewById(R.id.img_blue_vibration);//震动开关
//        img_blue_vibration_cover = (ImageView) findViewById(R.id.img_blue_vibration_cover);
        re_windows = (RelativeLayout) findViewById(R.id.re_windows);
        btn_know = (Button) findViewById(R.id.btn_know);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        shake_level_set= findViewById(R.id.shake_level_set);
        re2_a= findViewById(R.id.re2_a);
        tv_content= findViewById(R.id.tv_content);
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE,this);
        initViews();
        initEvents();
    }
    @Override
    protected void onAttachedToWindow() {
        viewSwitchShakeThis = this;
        super.onAttachedToWindow();
    }
    @Override
    protected void onDetachedFromWindow() {
        viewSwitchShakeThis = null;
        ODispatcher.removeEventListener(OEventName.CAR_LIST_CHANGE,this);
        super.onDetachedFromWindow();
    }

    @Override
    public void initViews() {
        msp=new SpannableString("1.\t手机蓝牙保持常开;\n\n2.\t设置保活:手机设置中找【应用启动管理】项目并设置\n\n\t\t允许本APP后台活动，以便手机在灭屏时自动连接蓝牙\n\n\t\t及识别摇一摇手势动作;");
        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#e93232")), 0, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
        tv_content.setText(msp);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//        tv_content.setText();
        String shakeLevel=BlueLinkNetSwitch.getShakeLevel();
        if(TextUtils.isEmpty(shakeLevel)){
            shake_level_set.txt_bottom.setText("中");
        }else{
            shake_level_set.txt_bottom.setText(shakeLevel);
        }
        handlerChangeSwitch();
    }

    @Override
    public void initEvents() {
        re2_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        re_windows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                re_windows.setVisibility(View.INVISIBLE);
            }
        });
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);

            }
        });
        shake_level_set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OToastButtonBlackStyle.getInstance().show(title_head, new String[]{"最重","重", "中", "轻"}, "item_shake_level", ViewSwitchShake.this, "摇一摇力度设置");
            }
        });
//        txt_selectcar.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewSwitchShakeSelectCar.getInstance().show(title_head, cacheShowingCar, cacheBlueList, new ViewSwitchShakeSelectCar.OnSelectCarListener() {
//                    @Override
//                    public void onSelectCar(DataCarInfo carInfo) {
//                        cacheShowingCar = carInfo;
//                        showSelectCar();
//                    }
//                });
//            }
//        });
        img_blue_vibration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BlueLinkNetSwitch.getIsShakeOpenVibrate()){
                    handler.obtainMessage(HANDLER_VIBRAT_OFF).sendToTarget();
                }else{
                    handler.obtainMessage(HANDLER_VIBRAT_ON).sendToTarget();
                }
            }
        });
        //要的开关
        img_blueswitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorManager sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
                List<Sensor>  listSensor    = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                boolean isHaveShakeSensor = false;
                if (listSensor != null && listSensor.size() > 0) isHaveShakeSensor = true;
                if (!isHaveShakeSensor) {
                    Toast.makeText(getContext(), "您的手机没有震动传感器，不能使用摇一摇功能!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
                //未选中车，就设为选中
                if (shakeinfo.isOpen == 1) {//无或选中
                    shake_level_set.setVisibility(View.GONE);
                    OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(),false);
//                    handler.obtainMessage(HANDLER_car_set_off).sendToTarget();
                } else {//
                    re_windows.setVisibility(View.VISIBLE);
//                    new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo("请设置允许自启权限").show();
//                    img_blueswitch.setImageResource(R.drawable.car_set_on);
//                    new TimeDelayTask().runTask(1000L, new TimeDelayTask.OnTimeEndListener() {
//                        @Override
//                        public void onTimeEnd() {
//                            SelfAutoRunPermission.gotoPermissionAutoStart(GlobalContext.getCurrentActivity());
//                            OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(true);
//                        }
//                    });
                }
            }
        });
        btn_cancle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(),false);
//                handler.obtainMessage(HANDLER_car_set_off).sendToTarget();
            }
        });
        btn_know.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                re_windows.setVisibility(View.INVISIBLE);
                if(BluePermission.checkPermission(GlobalContext.getCurrentActivity())!=1){
                    BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
                }else {
                    shake_level_set.setVisibility(View.VISIBLE);
                    OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(), true);
                }

            }
        });

//        re_shake.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View view) {
//                DataShakeTips.saveInsideShake(getContext(), true);
//                re_shake.setVisibility(View.INVISIBLE);
//            }
//        });
        txt_shake_set.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_set);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_set_txt);
            }
        });
        txt_shake_tips.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
               if(mContext!=null){
                   Intent intent = new Intent();
                   intent.setClass(mContext, JiecaoVideoPlayerShakeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   mContext.startActivity(intent);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_tips);
               }

            }
        });
        txt_shake_phone.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_phone);
            }
        });
        txt_shake_product.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_product);
            }
        });
        txt_shake_bind.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_bind);
            }
        });
    }

    // =====================================================
    @Override
    public void callback(String key, Object value) {
        if (key.equals("item_shake_level")) {
            String o = (String) value;
            shake_level_set.txt_bottom.setText(o);
            BlueLinkNetSwitch.setShakeLevel(o);//默认不开震动
        }
        super.callback(key, value);
    }

    @Override
    public void receiveEvent(String key, Object value) {
        super.receiveEvent(key, value);
        if (key.equals(OEventName.CAR_LIST_CHANGE)) {
            handlerChangeSwitch();
        }
    }

    @Override
    public void invalidateUI() {
    }
    // ===================================================
    //只能外部
    public void handlerChangeSwitch(){
        handler.obtainMessage(HANDLER_SWITCH_CHANGE).sendToTarget();
    }

    private static final int HANDLER_SWITCH_CHANGE             = 10011;
    private static final int HANDLER_VIBRAT_ON             = 10021;
    private static final int HANDLER_VIBRAT_OFF            = 10020;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SWITCH_CHANGE://切换状态
                    DataSwitch shakeinfo = BlueLinkNetSwitch.getSwitchShakeInfo();
                    DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                    if (shakeinfo.isOpen == 1) {
                        img_blueswitch.setImageResource(R.drawable.car_set_on);
                        if(BlueLinkNetSwitch.getIsShakeOpenVibrate()){
                            img_blue_vibration.setImageResource(R.drawable.car_set_on);
                        }else{
                            img_blue_vibration.setImageResource(R.drawable.car_set_off);
                        }
                        img_blue_vibration.setClickable(true);//是否可开震动
//                        img_blue_vibration_cover.setVisibility(INVISIBLE);
                        shake_level_set.setVisibility(View.VISIBLE);
                        String shakeLevel=BlueLinkNetSwitch.getShakeLevel();
                        if(TextUtils.isEmpty(shakeLevel)){
                            shake_level_set.txt_bottom.setText("中");
                        }else{
                            shake_level_set.txt_bottom.setText(shakeLevel);
                        }
//                        BlueLinkReceiver.needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
                        BlueLinkReceiver.needChangeCarData(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
                    } else {
                        re_windows.setVisibility(View.INVISIBLE);
                        img_blueswitch.setImageResource(R.drawable.car_set_off);
                        img_blue_vibration.setClickable(false);//是否可开震动
                        img_blue_vibration.setImageResource(R.drawable.car_set_off);
//                        img_blue_vibration_cover.setVisibility(VISIBLE);
                        BlueLinkNetSwitch.setIsShakeOpenVibrate(false);//默认不开震动
//                        BlueLinkNetSwitch.setSwitchNetAll();
//                        BlueLinkReceiver.needChangeCar(0,"","",carInfo.isBindBluetooth,carInfo.carsig,0);
//                        BlueLinkReceiver.needChangeCarData(0,"","",carInfo.isBindBluetooth,carInfo.carsig,0);
                        BlueLinkReceiver.needChangeCarData(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
                    }
                    break;
                case HANDLER_VIBRAT_ON:
                    img_blue_vibration.setImageResource(R.drawable.car_set_on);
                    BlueLinkNetSwitch.setIsShakeOpenVibrate(true);//默认不开震动
                    break;
                case HANDLER_VIBRAT_OFF:
                    img_blue_vibration.setImageResource(R.drawable.car_set_off);
                    BlueLinkNetSwitch.setIsShakeOpenVibrate(false);//默认不开震动
                    break;
            }
        }
    }
}
