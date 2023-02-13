package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.OInputValidation;

import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.ManagerLoginReg;
import model.safety.DataSafeTy;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/28.
 */
public class ViewSafetyResetPassword extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private EditText newPasswod1, newPasswod2;
    private Button btn_confirm;

    public ViewSafetyResetPassword(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_safety_reset_password, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        newPasswod1 = (EditText) findViewById(R.id.txt_new_password_one);
        newPasswod2 = (EditText) findViewById(R.id.txt_new_password_second);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS, this);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {

        title_head.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, DataSafeTy.back);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {

            @Override
            public void onClickNoFast(View view) {
                String password1 = newPasswod1.getText().toString();
                String password2 = newPasswod2.getText().toString();
                if (!OInputValidation.chkInputPassword(password1)) {
                    newPasswod1.setText("");
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_is_wrong_please_input_english_numbers_underscore_combination));
                    return;
                }
                if (!OInputValidation.chkRepWords(password1, password2)) {
                    newPasswod2.setText("");
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.repeated_password_is_wrong));
                    return;
                }
                OCtrlRegLogin.getInstance().ccmd1117_resetPassword_from_phoneNum(ManagerLoginReg.getInstance().getCurrentUser().phoneNum, ManagerAnswer.verifyStr, password1);
            }
        });
    }

    @Override
    protected void invalidateUI() {
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_reset_successfully));
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
        }
    }
}
