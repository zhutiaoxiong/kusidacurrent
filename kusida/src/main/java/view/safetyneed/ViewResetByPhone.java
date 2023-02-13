package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import common.pinyinzhuanhuan.KeyBoard;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.safety.DataSafeTy;
import view.clip.ClipPopSpeechReceive;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/27.
 */
public class ViewResetByPhone extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private EditText txt_phone;
    private Button btn_confirm;
    private EditText txt_input_verificationcode;
    private Button btn_getverificationcode;
    private TextView can_notget_verficode;

    public ViewResetByPhone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_safety_reset_byphone, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_phone = (EditText) findViewById(R.id.txt_phone);
        txt_input_verificationcode = (EditText) findViewById(R.id.txt_input_verificationcode);
        btn_getverificationcode = (Button) findViewById(R.id.btn_getverificationcode);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        can_notget_verficode = (TextView) findViewById(R.id.txt_cannotget_verficode);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
    }


    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_resetitem);
            }
        });
        btn_getverificationcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OTimeSchedule.getInstance().init();
                OTime60.getInstance().startTime();
                OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(ManagerLoginReg.getInstance().getCurrentUser().phoneNum, DataSafeTy.from, 1);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                String cacheVerifycode = txt_input_verificationcode.getText().toString();
                OCtrlRegLogin.getInstance().ccmd1116_checkVerifycode(ManagerLoginReg.getInstance().getCurrentUser().phoneNum, cacheVerifycode, DataSafeTy.from);
            }
        });
        can_notget_verficode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                KeyBoard.hintKb();
                ClipPopSpeechReceive.phoneNum = ManagerLoginReg.getInstance().getCurrentUser().phoneNum;
                ClipPopSpeechReceive.getInstance().show(can_notget_verficode, DataSafeTy.from);
            }
        });
    }

    private void hintKbOne() {
       InputMethodManager imm = (InputMethodManager) GlobalContext.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
       if (imm.isActive()) {
             // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                   InputMethodManager.HIDE_NOT_ALWAYS);
          }
      }



    @Override
    protected void initViews() {
        String txt = "";
        switch (DataSafeTy.from) {
            //2：修改手机号，3：重设密码，4：修改邮箱，5：修改安全问题
            case 2:
                txt = getResources().getString(R.string.mobile_phone_number_to_reset_the_phone_number);
                break;
            case 3:
                txt = getResources().getString(R.string.mobile_phone_number_to_reset_the_password);
                break;
            case 4:
                txt = getResources().getString(R.string.reset_email_phone_number);
                break;
            case 5:
                txt = getResources().getString(R.string.mobile_phone_number_to_reset_the_security_issues);
                break;
        }
        title_head.setTitle(txt);
        txt_phone.setText(ManagerLoginReg.getInstance().getCurrentUser().phoneNum);
        OTime60.getInstance().listener(btn_getverificationcode);
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CHECK_VERIFYCODE_SUCCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.authentication_is_successful_aa));
            //2：修改手机号，3：重设密码，4：修改邮箱，5：修改安全问题
            int i = 0;
            switch (DataSafeTy.from) {
                case 2:
                    i = R.layout.view_safety_reset_phone;
                    break;
                case 3:
                    i = R.layout.view_safety_reset_password;
                    break;
                case 4:
                    i = R.layout.view_safety_reset_aderess;
                    break;
                case 5:
                    i = R.layout.view_safety_reset_qustion;
                    break;
            }
            DataSafeTy.back = R.layout.view_safety_reset_byphone;
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, i);
        } else if (s.equals(OEventName.VERIFICATION_CODE_BACKOK)) {
            OTime60.getInstance().endTime();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
       GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onDetachedFromWindow();
    }
    @Override
    protected void onAttachedToWindow() {
      GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onAttachedToWindow();
    }
}
