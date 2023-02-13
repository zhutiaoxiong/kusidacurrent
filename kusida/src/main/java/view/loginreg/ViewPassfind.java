package view.loginreg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.OnClickListenerMy;


import common.GlobalContext;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import view.view4me.set.ClipTitleMeSet;

public class ViewPassfind extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private Button        btn_find_phone, btn_find_mail;
    private RelativeLayout lin_input_mail, lin_input_pass, lin_input_phone;

    private EditText txt_phone, txt_input_verificationcode;
    private Button   btn_getverificationcode;
    private EditText txt_new_pass, txt_repeat_pass;
    private EditText txt_mail;
    private Button   btn_confirm, btn_confirm_mail;

    private int    currentPageNum;
    private String cachePhoneNum, cacheVerifycode;

    public ViewPassfind(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_passfind, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        btn_find_phone = (Button) findViewById(R.id.btn_find_phone);
        btn_find_mail = (Button) findViewById(R.id.btn_find_mail);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm_mail = (Button) findViewById(R.id.btn_confirm_mail);
        btn_getverificationcode = (Button) findViewById(R.id.btn_getverificationcode);

        lin_input_mail = (RelativeLayout) findViewById(R.id.lin_input_mail);
        lin_input_pass = (RelativeLayout) findViewById(R.id.lin_input_pass);
        lin_input_phone = (RelativeLayout) findViewById(R.id.lin_input_phone);

        txt_phone = (EditText) findViewById(R.id.txt_phone);
        txt_input_verificationcode = (EditText) findViewById(R.id.txt_input_verificationcode);
        txt_new_pass = (EditText) findViewById(R.id.txt_new_pass);
        txt_repeat_pass = (EditText) findViewById(R.id.txt_repeat_pass);
        txt_mail = (EditText) findViewById(R.id.txt_mail);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.FINDPASS_IDCARD_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS, this);
    }

    @Override
    public void initViews() {
        OTime60.getInstance().listener(btn_getverificationcode);
        changePage(0);
    }

    /**
     * 0 输手机号 1输邮箱 2输确认密码
     *
     * @param pageNum
     */
    private void changePage(int pageNum) {
        currentPageNum = pageNum;
        if (pageNum == 0) {
            btn_find_phone.setBackgroundResource(R.color.white);
            btn_find_mail.setBackgroundResource(R.color.blue_light);
            btn_find_phone.setTextColor(getResources().getColor(R.color.black));
            btn_find_mail.setTextColor(getResources().getColor(R.color.black));
            lin_input_mail.setVisibility(INVISIBLE);
            lin_input_pass.setVisibility(INVISIBLE);
            lin_input_phone.setVisibility(VISIBLE);
            btn_confirm.setVisibility(VISIBLE);
        } else if (pageNum == 1) {
            btn_find_phone.setBackgroundResource(R.color.blue_light);
            btn_find_mail.setBackgroundResource(R.color.white);
            btn_find_phone.setTextColor(getResources().getColor(R.color.black));
            btn_find_mail.setTextColor(getResources().getColor(R.color.black));
            lin_input_mail.setVisibility(VISIBLE);
            lin_input_pass.setVisibility(INVISIBLE);
            lin_input_phone.setVisibility(INVISIBLE);
            btn_confirm.setVisibility(INVISIBLE);
        } else if (pageNum == 2) {
            btn_find_phone.setBackgroundResource(R.color.white);
            btn_find_mail.setBackgroundResource(R.color.blue_light);
            btn_find_phone.setTextColor(getResources().getColor(R.color.black));
            btn_find_mail.setTextColor(getResources().getColor(R.color.black));
            lin_input_mail.setVisibility(INVISIBLE);
            lin_input_pass.setVisibility(VISIBLE);
            lin_input_phone.setVisibility(INVISIBLE);
            btn_confirm.setVisibility(VISIBLE);
        }

    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,R.layout.view_loginreg_login);
            }
        });
        // get
        btn_getverificationcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OTimeSchedule.getInstance().init();
                OTime60.getInstance().startTime();
                String phoneNum = txt_phone.getText().toString();
                OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(phoneNum, 3,1);
            }
        });
        btn_find_phone.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                changePage(0);
            }
        });
        btn_find_mail.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                changePage(1);
            }
        });
        // confirm
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (currentPageNum == 0) {//0 输手机号 1输邮箱 2输确认密码
                    cachePhoneNum = txt_phone.getText().toString();
                    cacheVerifycode = txt_input_verificationcode.getText().toString();
                    boolean isCheckForPhone = OInputValidation.chkInputPhoneNum(cachePhoneNum);
                    if (!isCheckForPhone) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_enter_a_correct_phone_number));
                        return;
                    } else {
                        OCtrlRegLogin.getInstance().ccmd1116_checkVerifycode(cachePhoneNum, cacheVerifycode, 3);
                    }
                } else if (currentPageNum == 2) {//0 输手机号 1输邮箱 2输确认密码
                    String password = txt_new_pass.getText().toString();
                    String passwordRep = txt_repeat_pass.getText().toString();
                    if (!OInputValidation.chkInputPassword(password)) {
                        txt_new_pass.setText("");
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_is_wrong_please_input_english_numbers_underscore_combination));
                        return;
                    }
                    if (!OInputValidation.chkRepWords(password, passwordRep)) {
                        txt_repeat_pass.setText("");
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.repeated_password_is_wrong));
                        return;
                    }
                    OCtrlRegLogin.getInstance().ccmd1117_resetPassword_from_phoneNum(cachePhoneNum, cacheVerifycode, password);
                }
//                OToastOMG.getInstance().showConfirm(getContext(), "邮箱密码找回", "我们将发送一封邮件到您注册邮箱，确定吗?", "mail", ViewPassfind.this);
//                OToastOMG.getInstance().showInput(getContext(), "身份认证密码找回", "", new String[]{OToastOMG.IDCARD, OToastOMG.NEW_PASS, OToastOMG.REPT_PASS},
//                        "idcard", ViewPassfind.this);
            }
        });
        btn_confirm_mail.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                boolean isCheckForPhone = OInputValidation.chkInputPhoneNum(txt_mail.getText().toString());
                if (!isCheckForPhone) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_enter_a_correct_phone_number));
                }else{
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("提示")
                            .withInfo("将发送修改密码的链接到您绑定邮箱，确定吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm){
                                        String phoneNum = txt_mail.getText().toString();
                                        OCtrlRegLogin.getInstance().ccmd1114_findpassFromMail(phoneNum,3);
                                    }
                                }
                            }).show();
                }
            }
        });
//        TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String phoneNum = editable.toString();
//                if (OInputValidation.chkInputPhoneNum(phoneNum)) {
//                    phoneNumCheckOK = true;
//                }else{
//                    phoneNumCheckOK = false;
//                }
//                checkVerificationEnable();
//            }
//        };
//        txt_phone.addTextChangedListener(watcher);
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.FINDPASS_MAIL_RESULTBACK)) {
            String mess = (String) paramObj;
            if (mess == null || mess.equals("")) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.we_have_sent_an_email_to_your_mailbox_please_pay_attention_to_check));
            } else {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("提示")
                        .withInfo(mess)
                        .withButton("","确定")
                        .show();
            }
        } else if (eventName.equals(OEventName.FINDPASS_IDCARD_RESULTBACK)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.the_new_password_is_enabled_please_login));
        } else if (eventName.equals(OEventName.CHECK_VERIFYCODE_SUCCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.authentication_is_successful_aa));
            handleChangeData();//changePage(1);
        } else if (eventName.equals(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_reset_successfully));
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,R.layout.view_loginreg_login);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        OTime60.getInstance().clearButton();
        ODispatcher.removeEventListener(OEventName.FINDPASS_IDCARD_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS, this);
        ODispatcher.removeEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        changePage(2);
    }

}
