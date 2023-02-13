package view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlCar;
import model.demomode.DemoMode;

//import com.kulala.staticsview.LinearLayoutBase;

/**
 * Created by qq522414074 on 2016/10/10.
 */
public class ViewPromeBox extends LinearLayoutBase {

    private Button activate,demo,buy;
    private View view;
    private TextView tip,please_choose;

    public ViewPromeBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.prompt_box,this,true);
        activate=(Button)findViewById(R.id.activate);
        demo=(Button)findViewById(R.id.demo);
        buy=(Button)findViewById(R.id.buy);
        view=(View)findViewById(R.id.touch_exit);
        tip=(TextView)findViewById(R.id.tip);
        please_choose=(TextView)findViewById(R.id.please_choose);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR,this);
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE,this);
    }

    @Override
    public void receiveEvent(String s, Object o) {
         if (s.equals(OEventName.HTTP_CONN_ERROR)) {
            int result = (Integer) o;
            if (result == 1408 || result == 1409 || result == 1228||result==1226) {
                if(DemoMode.isBeginOrFinish==1){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.network_anomaly_temporarily_unable_to_enter_the_demo_mode));
                }else if(DemoMode.isBeginOrFinish==2){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.network_anomaly_temporarily_unable_to_exit_the_demo_mode));
                }
            }
        }else if(s.equals(OEventName.LANGUAGE_CHANGE)){
             handleChangeData();
         }
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {
        activate.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_UP_BOX);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_main);
            }
        });
        demo.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_UP_BOX);
                if(DemoMode.jumpToWhere.equals(getResources().getString(R.string.carmanager))){
                    ODispatcher.dispatchEvent(OEventName.ADDCAR_WINDOW_TOAST);
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_main);
                }else if(DemoMode.jumpToWhere.equals(getResources().getString(R.string.direct_demonstration))){
                    DemoMode.isBeginOrFinish=1;
                    OCtrlCar.getInstance().ccmd1226_DemoMode(DemoMode.isBeginOrFinish);
//                    DemoMode.isDemoMode="演示开始";
//                 ODispatcher.dispatchEvent(OEventName.DEMO_MODE_START);
                }

            }

        });
        buy.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_UP_BOX);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.buy_car);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "购买流程");
                String address = "http://api.91kulala.com/kulala/user/index.html";
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }

        });
        view.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_UP_BOX);
            }
        });
    }
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return true;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void invalidateUI() {
        activate.setText(getResources().getString(R.string.active_vehicles));
        demo.setText(getResources().getString(R.string.simulation_demonstration));
        buy.setText(getResources().getString(R.string.buy_equipment));
        tip.setText(getResources().getString(R.string.the_tip));
        please_choose.setText(getResources().getString(R.string.no_activation_device_please_choose));
    }


    @Override 
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.HTTP_CONN_ERROR,this);
        ODispatcher.removeEventListener(OEventName.LANGUAGE_CHANGE,this);
        super.onDetachedFromWindow();
    }
}
