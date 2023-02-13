package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlCar;
import model.demomode.DemoMode;

/**
 * Created by qq522414074 on 2016/10/12.
 */
public class ViewPromeBoxExit extends LinearLayoutBase {
    private Button cancle,btn_confirm;
    private TextView txt_title,txt_text;
    public ViewPromeBoxExit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_prombox_exit,this,true);
        cancle=(Button)findViewById(R.id.btn_cancel);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);
        txt_title=(TextView)findViewById(R.id.txt_title);
        txt_text=(TextView)findViewById(R.id.txt_text);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR,this);
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE,this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        cancle.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
//                DemoMode.isDemoMode="演示结束";
//             ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS);
                DemoMode.isBeginOrFinish=2;
                OCtrlCar.getInstance().ccmd1226_DemoMode(DemoMode.isBeginOrFinish);
                ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS);
            }
        });
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
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.HTTP_CONN_ERROR,this);
        ODispatcher.removeEventListener(OEventName.LANGUAGE_CHANGE,this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void invalidateUI() {
        txt_title.setText(getResources().getString(R.string.the_tip));
        txt_text.setText(getResources().getString(R.string.whether_out_of_simulation_demonstration));
        cancle.setText(getResources().getString(R.string.cancle));
        btn_confirm .setText(getResources().getString(R.string.confirm));
    }
}
