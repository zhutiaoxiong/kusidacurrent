package view.loginreg;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.OInputValidation;

import ctrl.OCtrlAnswer;
import model.ManagerAnswer;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/8/15.
 */
public class ViewLoginRegUserName extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private EditText txt_phone;
    private Button btn_confirm;
    private RelativeLayout find_bysafetyproblem;
    private String cachePhoneNum;
    private ImageView img_gabage;

    public ViewLoginRegUserName(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_username, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_phone = (EditText) findViewById(R.id.txt_phone);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        img_gabage = (ImageView) findViewById(R.id.img_gabage);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
    }

    @Override
    protected void initViews() {
        img_gabage.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_login);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                cachePhoneNum = txt_phone.getText().toString();
                boolean isCheckForPhone = OInputValidation.chkInputPhoneNum(cachePhoneNum);
                if (!isCheckForPhone) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_a_correct_phone_number));
                    return;
                }
                ManagerAnswer.phoneNum = cachePhoneNum;
                OCtrlAnswer.getInstance().ccmd1119_answer(cachePhoneNum, 1);

            }
        });
        img_gabage.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                txt_phone.setText("");
            }
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txt_phone.getText().toString().length() >= 1) {
                    img_gabage.setVisibility(View.VISIBLE);
                } else {
                    img_gabage.setVisibility(View.INVISIBLE);
                }
            }
        };
        txt_phone.addTextChangedListener(watcher);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.SECRETINFOS_RESULTBACK)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_reset_password_logreg);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void invalidateUI() {

    }
}
