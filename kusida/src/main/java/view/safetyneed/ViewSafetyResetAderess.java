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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.safety.DataSafeTy;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/28.
 */
public class ViewSafetyResetAderess extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private EditText newPasswod1;
    private Button btn_confirm;
    public static String verifyStr;
    public ViewSafetyResetAderess(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_safety_reset_aderess, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        newPasswod1 = (EditText) findViewById(R.id.txt_new_password_one);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHANGE_MAIL_RESULTBACK, this);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {

        title_head.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(DataSafeTy.from==7){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
                }else{
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, DataSafeTy.back);
                }
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {

            @Override
            public void onClickNoFast(View view) {
                String aderess = newPasswod1.getText().toString();
                if (!isEmail(aderess)) {
                    newPasswod1.setText("");
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.email_fill_in_error_please_input_the_correct_email));
                    return;
                }
                OCtrlRegLogin.getInstance().ccmd1112_changeMail(aderess, ManagerAnswer.verifyStr);
            }
        });
    }

    @Override
    protected void invalidateUI() {
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CHANGE_MAIL_RESULTBACK)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.email_reset_successfully));

            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
        } else {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.validation_fails));
        }
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CHANGE_MAIL_RESULTBACK, this);
        super.onDetachedFromWindow();
    }
}
