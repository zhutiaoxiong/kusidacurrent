package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import common.pinyinzhuanhuan.KeyBoard;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.safety.DataSafeTy;
import view.clip.ClipPopSpeechReceive;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/28.
 */
public class ViewSafetyResetPhone extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private EditText txt_phone;
    private Button btn_confirm;
    private String cachePhoneNum;
    private EditText txt_input_verificationcode;
    private Button btn_getverificationcode;
    private TextView can_notget_verficode;
    private String verficode;

    public ViewSafetyResetPhone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_safety_reset_phone, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_phone = (EditText) findViewById(R.id.txt_phone);
        txt_input_verificationcode = (EditText) findViewById(R.id.txt_input_verificationcode);
        btn_getverificationcode = (Button) findViewById(R.id.btn_getverificationcode);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        can_notget_verficode = (TextView) findViewById(R.id.txt_cannotget_verficode);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK, this);
    }
    @Override
    protected void initViews() {
        OTime60.getInstance().listener(btn_getverificationcode);
    }

    @Override
    protected void initEvents() {
        title_head.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, DataSafeTy.back);
            }
        });
        btn_getverificationcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                cachePhoneNum = txt_phone.getText().toString();
                if (cachePhoneNum.length() == 11) {
                    OTimeSchedule.getInstance().init();
                    OTime60.getInstance().startTime();
                    OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(cachePhoneNum, 6, 1);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_the_complete_phone_number));
                }
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (checkConfirmResult().equals("")) {
                    cachePhoneNum = txt_phone.getText().toString();
                    if (cachePhoneNum.length() == 11) {
                        verficode = txt_input_verificationcode.getText().toString();
                        OCtrlRegLogin.getInstance().ccmd1113_changePhoneNum(cachePhoneNum, verficode, ManagerAnswer.verifyStr);
                    } else {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_the_complete_phone_number));
                    }
                }
            }
        });

        can_notget_verficode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                KeyBoard.hintKb();
                if (checkConfirmResult().equals("") && cachePhoneNum.length() == 11) {
//                    ManagerAnswer.gowhere=R.layout.view_loginreg_input_two_password_txt_phone;
//                    ManagerAnswer.whocome=2;
                    ClipPopSpeechReceive.phoneNum = cachePhoneNum;
                    ClipPopSpeechReceive.getInstance().show(can_notget_verficode, 6);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_the_complete_phone_number));
                }
            }
        });
    }

    private String checkConfirmResult() {
        cachePhoneNum = txt_phone.getText().toString();
        if (!OInputValidation.chkInputPhoneNum(cachePhoneNum)) {
            txt_phone.setText("");
            return getResources().getString(R.string.the_phone_number_wrong);
        }
        return "";
    }

    @Override
    protected void invalidateUI() {
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.VERIFICATION_CODE_BACKOK)) {
            OTime60.getInstance().endTime();
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.verification_code_for_failure_please_try_again));
        } else if (s.equals(OEventName.CHANGE_PHONENUM_RESULTBACK)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.modify_the_success));
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
        ODispatcher.removeEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK, this);
        GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onDetachedFromWindow();
    }
    @Override
    protected void onAttachedToWindow() {
        GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onAttachedToWindow();
    }
}
