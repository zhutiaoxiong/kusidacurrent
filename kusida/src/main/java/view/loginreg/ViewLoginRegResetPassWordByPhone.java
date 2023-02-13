package view.loginreg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.OnClickListenerMy;

import common.pinyinzhuanhuan.KeyBoard;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import view.clip.ClipPopSpeechReceive;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/8/15.
 */
public class ViewLoginRegResetPassWordByPhone extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView txt_phone;
    private Button btn_confirm;
    private String cachePhoneNum;
    private EditText txt_input_verificationcode;
    private Button  btn_getverificationcode;
    private TextView can_notget_verficode;

    public ViewLoginRegResetPassWordByPhone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_phone_resetpassword, this, true);
        title_head=(ClipTitleMeSet) findViewById(R.id.title_head);
        txt_phone=(TextView) findViewById(R.id.txt_phone);
        txt_input_verificationcode=(EditText)findViewById(R.id.txt_input_verificationcode) ;
        btn_getverificationcode=(Button)findViewById(R.id.btn_getverificationcode);
        btn_confirm=(Button) findViewById(R.id.btn_confirm);
        can_notget_verficode=(TextView)findViewById(R.id.txt_cannotget_verficode) ;
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK,this);
    }

    @Override
    protected void initViews() {
        title_head.setTitle(getResources().getString(R.string.mobile_phone_number_to_reset_the_password));
        txt_phone.setText(ManagerAnswer.phoneNum);
        OTime60.getInstance().listener(btn_getverificationcode);
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_reset_password_logreg);
            }
        });
        btn_getverificationcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OTimeSchedule.getInstance().init();
                OTime60.getInstance().startTime();
                OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(ManagerAnswer.phoneNum, 3,1);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                String cacheVerifycode = txt_input_verificationcode.getText().toString();
                OCtrlRegLogin.getInstance().ccmd1116_checkVerifycode(ManagerAnswer.phoneNum, cacheVerifycode, 3);

            }
        });
        can_notget_verficode.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                KeyBoard.hintKb();
                ClipPopSpeechReceive.phoneNum= ManagerAnswer.phoneNum;
                ClipPopSpeechReceive.getInstance().show(can_notget_verficode,7);

            }
        });

    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CHECK_VERIFYCODE_SUCCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.authentication_is_successful_aa));

            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,R.layout.view_loginreg_input_two_password);
        }else if(s.equals(OEventName.VERIFICATION_CODE_BACKOK)){
            OTime60.getInstance().endTime();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS,this);
        ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_BACKOK,this);
        super.onDetachedFromWindow();
    }
}
