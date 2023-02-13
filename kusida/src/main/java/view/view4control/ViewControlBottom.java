package view.view4control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

import java.text.DecimalFormat;

import common.GlobalContext;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import view.basicview.CheckForgroundUtils;


/**
 * 车身
 */
public class ViewControlBottom extends LinearLayout implements OEventObject {
//    private RelativeLayout tab_1;
    private RelativeLayout tab_2;
    private RelativeLayout tab_0;
    private TextView t1;
    private TextView t2;
//    private TextView t3;
    private TextView t5;
//    private TextView t4;
    private TextView t6;
    private ImageView car_fan;
    private Animation anmiRotate;

    public ViewControlBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_control_bottom, this, true);
        tab_0 = (RelativeLayout) findViewById(R.id.tab_0);
//        tab_1 = (RelativeLayout) findViewById(R.id.tab_1);
        tab_2 = (RelativeLayout) findViewById(R.id.tab_2);
        t1 = tab_0.findViewById(R.id.txt_top);
        t2 = tab_0.findViewById(R.id.txt_bottom);
//        t3 = tab_1.findViewById(R.id.txt_top);
//        t4 = tab_1.findViewById(R.id.txt_bottom);
        t5 = tab_2.findViewById(R.id.txt_top);
        t6 = tab_2.findViewById(R.id.txt_bottom);
        car_fan = (ImageView) findViewById(R.id.car_fan);
        anmiRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
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
        t2.setText("电瓶电压");
//        t4.setText("车牌号码");
        t6.setText("车牌号码");
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(!TextUtils.isEmpty(car.num)) {
            t5.setText(car.num);
        }
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        ODispatcher.addEventListener(OEventName.STOP_ANIM_ROTATE, this);
    }


    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        ODispatcher.removeEventListener(OEventName.STOP_ANIM_ROTATE, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void receiveEvent(String eventName, Object o) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            if (CheckForgroundUtils.isAppForeground()) {
                handler.sendEmptyMessage(1);
            }
        }
//        else if(OEventName.STOP_ANIM_ROTATE.equals(eventName)){
//            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(car_fan!=null){
//                        Log.e("风扇","不转2");
//                        car_fan.clearAnimation();
//                        if (BuildConfig.DEBUG) Log.e("Fan","STOP Rotate");
//                    }
//                }
//            });
//        }
    }
    public static int startAnimationCount;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                if (car == null ) {//car null
                    t1.setText("--");
//                    t3.setText("--");
                    t5.setText("--");
                }else{
                   if(!TextUtils.isEmpty(car.num)) {
                       t5.setText(car.num);
                   }
                }
                if (status != null) {
                    if (status.voltage == 0) {
                        t1.setText("--");
                    } else {
                        //                        DecimalFormat df = new DecimalFormat("##.0");
                        t1.setText(status.voltage + "V");
                    }
//                    if (status.miles == 0) {
//                        t5.setText("--");
//                    } else {
//                        t5.setText(String.valueOf(status.miles));
//                    }
                }
                if(car==null||car.isActive==0&&status!=null){
                    status.isStart=0;
                }
                if(status!=null&&status.isStart == 1){
                    if(startAnimationCount==0){
                        Log.e("风扇","转1");
                        car_fan.startAnimation(anmiRotate);
                        startAnimationCount++;
                    }
                }else{
                    if(startAnimationCount==1){
                        Log.e("风扇","不转1");
                        car_fan.clearAnimation();
                        startAnimationCount=0;
                    }
                }
            }
        }
    };
}
