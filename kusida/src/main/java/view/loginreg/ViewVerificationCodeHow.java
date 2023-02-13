package view.loginreg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import model.safety.DataSafeTy;
import view.view4me.set.ClipTitleMeSet;

public class ViewVerificationCodeHow extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    public ViewVerificationCodeHow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_verificationcode_how, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

                if (DataSafeTy.entrance == 1) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,R.layout.view_loginreg_reg);
                } else if (DataSafeTy.entrance== 6) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_reset_phone);
                } else if (DataSafeTy.entrance== 7) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_phone_resetpassword);
                }else{
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,  R.layout.view_safety_reset_byphone);
                }
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
