package com.kulala.staticsview.toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;
import com.kulala.staticsview.static_interface.OCallBack;
//import com.kulala.staticsview.static_interface.OCallBack;

public class OToastInput {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    public static String OTHER_TEXT   = "OTHER_TEXT";
    public static String OTHER_NUMBER = "OTHER_NUMBER";
    public static String PASS         = "PASS";
    public static String IDCARD       = "IDCARD";
    public static String PHONE        = "PHONE";
    public static String NEW_PHONE    = "NEW_PHONE";
    public static String REPT_PHONE   = "REPT_PHONE";
    public static String NEW_EMAIL    = "NEW_EMAIL";
    public static String REPT_EMAIL   = "REPT_EMAIL";
    public static String NEW_IDCARD   = "NEW_IDCARD";
    public static String REPT_IDCARD  = "REPT_IDCARD";
    public static String NEW_PASS     = "NEW_PASS";
    public static String REPT_PASS    = "REPT_PASS";

    private RelativeLayout thisView;
    private TextView       txt_title;
    private TextView       btn_cancel, btn_confirm;
    private EditText txt_other_text, txt_other_number, txt_pass, txt_idcard, txt_phone, txt_newphone, txt_reptphone, txt_newemail, txt_reptemail,
            txt_newidcard, txt_reptidcard, txt_newpass, txt_reptpass,txt_my_num;
    private String    mark;//选择标记
    private OCallBack callback;
    private MyHandler handler;
    private String[]    hintTexts;

    private        String[]    needValue;
    private        String      otherTxt;
    // ========================out======================
    private static OToastInput _instance;

    public static OToastInput getInstance() {
        if (_instance == null)
            _instance = new OToastInput();
        return _instance;
    }

    //===================================================

    /**
     * @param parentView
     * @param title      标题
     * @param otherTxt   普通文本的提示
     * @param needValue  特定的输入
     * @param markk      callback标志
     * @param callbackk
     */
    public void showInput(View parentView, String title, String otherTxt, String[] needValue, String markk, OCallBack callbackk) {
        if (handler == null) handler = new MyHandler();
        this.mark = markk;
        this.callback = callbackk;
        this.parentView = parentView;
        this.otherTxt = otherTxt;
        this.needValue = needValue;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_input, null);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);

        txt_other_text = (EditText) thisView.findViewById(R.id.txt_other_text);
        txt_other_number = (EditText) thisView.findViewById(R.id.txt_other_number);
        txt_pass = (EditText) thisView.findViewById(R.id.txt_pass);
        txt_idcard = (EditText) thisView.findViewById(R.id.txt_idcard);
        txt_phone = (EditText) thisView.findViewById(R.id.txt_phone);
        txt_newphone = (EditText) thisView.findViewById(R.id.txt_newphone);
        txt_reptphone = (EditText) thisView.findViewById(R.id.txt_reptphone);
        txt_newemail = (EditText) thisView.findViewById(R.id.txt_newemail);
        txt_reptemail = (EditText) thisView.findViewById(R.id.txt_reptemail);
        txt_newidcard = (EditText) thisView.findViewById(R.id.txt_newidcard);
        txt_reptidcard = (EditText) thisView.findViewById(R.id.txt_reptidcard);
        txt_newpass = (EditText) thisView.findViewById(R.id.txt_newpass);
        txt_reptpass = (EditText) thisView.findViewById(R.id.txt_reptpass);
        txt_my_num= (EditText) thisView.findViewById(R.id.txt_my_num);

        btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) thisView.findViewById(R.id.btn_confirm);

        initViews(title);
        initEvents();
    }
    /**
     * @param parentView
     * @param title      标题
     * @param otherTxt   普通文本的提示
     * @param needValue  特定的输入
     * @param markk      callback标志
     * @param callbackk
     */
    public void showInput(View parentView, String title, String otherTxt, String[] hintTexts, String[] needValue, String markk, OCallBack callbackk) {
        if (handler == null) handler = new MyHandler();
        this.mark = markk;
        this.callback = callbackk;
        this.parentView = parentView;
        this.hintTexts = hintTexts;
        this.otherTxt = otherTxt;
        this.needValue = needValue;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_input, null);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);

        txt_other_text = (EditText) thisView.findViewById(R.id.txt_other_text);
        txt_other_number = (EditText) thisView.findViewById(R.id.txt_other_number);
        txt_pass = (EditText) thisView.findViewById(R.id.txt_pass);
        txt_idcard = (EditText) thisView.findViewById(R.id.txt_idcard);
        txt_phone = (EditText) thisView.findViewById(R.id.txt_phone);
        txt_newphone = (EditText) thisView.findViewById(R.id.txt_newphone);
        txt_reptphone = (EditText) thisView.findViewById(R.id.txt_reptphone);
        txt_newemail = (EditText) thisView.findViewById(R.id.txt_newemail);
        txt_reptemail = (EditText) thisView.findViewById(R.id.txt_reptemail);
        txt_newidcard = (EditText) thisView.findViewById(R.id.txt_newidcard);
        txt_reptidcard = (EditText) thisView.findViewById(R.id.txt_reptidcard);
        txt_newpass = (EditText) thisView.findViewById(R.id.txt_newpass);
        txt_reptpass = (EditText) thisView.findViewById(R.id.txt_reptpass);

        btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) thisView.findViewById(R.id.btn_confirm);
        txt_my_num= (EditText) thisView.findViewById(R.id.txt_my_num);

        initViews(title);
        initEvents();
    }

    public void initViews(String title) {
        //set UI
        handleChangeViewDefault();
        handleSetTitle(title);
        handleAddInput();
        //set UI
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void initEvents() {
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                callback.callback("10086", "取消");
                handlehide();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                callback.callback(mark, getJsonObjectValue());
                handlehide();
            }
        });
    }

    private JsonObject getJsonObjectValue() {
        JsonObject obj = new JsonObject();
        if(otherTxt.equals("两个或者以上输入框")){
            obj.addProperty(OTHER_TEXT, txt_other_text.getText().toString());
            obj.addProperty(OTHER_NUMBER, txt_other_number.getText().toString());
            obj.addProperty(PASS, txt_pass.getText().toString());
            obj.addProperty(IDCARD, txt_idcard.getText().toString());
            obj.addProperty(PHONE, txt_my_num.getText().toString());
            obj.addProperty(NEW_PHONE, txt_newphone.getText().toString());
            obj.addProperty(REPT_PHONE, txt_reptphone.getText().toString());
            obj.addProperty(NEW_EMAIL, txt_newemail.getText().toString());
            obj.addProperty(REPT_EMAIL, txt_reptemail.getText().toString());
            obj.addProperty(NEW_IDCARD, txt_newidcard.getText().toString());
            obj.addProperty(REPT_IDCARD, txt_reptidcard.getText().toString());
            obj.addProperty(NEW_PASS, txt_newpass.getText().toString());
            obj.addProperty(REPT_PASS, txt_reptpass.getText().toString());
        }else{
            obj.addProperty(OTHER_TEXT, txt_other_text.getText().toString());
            obj.addProperty(OTHER_NUMBER, txt_other_number.getText().toString());
            obj.addProperty(PASS, txt_pass.getText().toString());
            obj.addProperty(IDCARD, txt_idcard.getText().toString());
            obj.addProperty(PHONE, txt_phone.getText().toString());
            obj.addProperty(NEW_PHONE, txt_newphone.getText().toString());
            obj.addProperty(REPT_PHONE, txt_reptphone.getText().toString());
            obj.addProperty(NEW_EMAIL, txt_newemail.getText().toString());
            obj.addProperty(REPT_EMAIL, txt_reptemail.getText().toString());
            obj.addProperty(NEW_IDCARD, txt_newidcard.getText().toString());
            obj.addProperty(REPT_IDCARD, txt_reptidcard.getText().toString());
            obj.addProperty(NEW_PASS, txt_newpass.getText().toString());
            obj.addProperty(REPT_PASS, txt_reptpass.getText().toString());
        }
        return obj;
    }

    private void addInput(String[] needValue, String otherHint) {
        if (needValue == null)
            return;
        if(otherTxt.equals("两个或者以上输入框")){
            if(hintTexts==null||hintTexts.length==0)return;
            for (int i = 0; i < needValue.length; i++) {
                String value = needValue[i];
                if (value.equals(OTHER_TEXT)) {
                    txt_other_text.setVisibility(View.VISIBLE);
                    txt_other_text.setHint(hintTexts[i]);
                    txt_other_text.setText("");
                } else if (value.equals(OTHER_NUMBER)) {
                    txt_other_number.setVisibility(View.VISIBLE);
                    txt_other_number.setHint(hintTexts[i]);
                    txt_other_number.setText("");
                } else if (value.equals(PASS)) {
                    txt_pass.setVisibility(View.VISIBLE);
                    txt_pass.setText("");
                } else if (value.equals(IDCARD)) {
                    txt_idcard.setVisibility(View.VISIBLE);
                    txt_idcard.setText("");
                } else if (value.equals(PHONE)) {
//                    txt_phone.setVisibility(View.VISIBLE);
//                    txt_phone.setText("");
                    txt_my_num.setVisibility(View.VISIBLE);
                    txt_my_num.setHint(hintTexts[i]);
                    txt_my_num.setText("");
                } else if (value.equals(NEW_PHONE)) {
                    txt_newphone.setVisibility(View.VISIBLE);
                    txt_newphone.setText("");
                } else if (value.equals(REPT_PHONE)) {
                    txt_reptphone.setVisibility(View.VISIBLE);
                    txt_reptphone.setText("");
                } else if (value.equals(NEW_EMAIL)) {
                    txt_newemail.setVisibility(View.VISIBLE);
                    txt_newemail.setText("");
                } else if (value.equals(REPT_EMAIL)) {
                    txt_reptemail.setVisibility(View.VISIBLE);
                    txt_reptemail.setText("");
                } else if (value.equals(NEW_IDCARD)) {
                    txt_newidcard.setVisibility(View.VISIBLE);
                    txt_newidcard.setText("");
                } else if (value.equals(REPT_IDCARD)) {
                    txt_reptidcard.setVisibility(View.VISIBLE);
                    txt_reptidcard.setText("");
                } else if (value.equals(NEW_PASS)) {
                    txt_newpass.setVisibility(View.VISIBLE);
                    txt_newpass.setText("");
                } else if (value.equals(REPT_PASS)) {
                    txt_reptpass.setVisibility(View.VISIBLE);
                    txt_reptpass.setText("");
                }
            }
        }else{
            for (int i = 0; i < needValue.length; i++) {
                String value = needValue[i];
                if (value.equals(OTHER_TEXT)) {
                    txt_other_text.setVisibility(View.VISIBLE);
                    txt_other_text.setHint(otherHint);
                    txt_other_text.setText("");
                } else if (value.equals(OTHER_NUMBER)) {
                    txt_other_number.setVisibility(View.VISIBLE);
                    txt_other_number.setHint(otherHint);
                    txt_other_number.setText("");
                } else if (value.equals(PASS)) {
                    txt_pass.setVisibility(View.VISIBLE);
                    txt_pass.setText("");
                } else if (value.equals(IDCARD)) {
                    txt_idcard.setVisibility(View.VISIBLE);
                    txt_idcard.setText("");
                } else if (value.equals(PHONE)) {
                    txt_phone.setVisibility(View.VISIBLE);
                    txt_phone.setText("");
                } else if (value.equals(NEW_PHONE)) {
                    txt_newphone.setVisibility(View.VISIBLE);
                    txt_newphone.setText("");
                } else if (value.equals(REPT_PHONE)) {
                    txt_reptphone.setVisibility(View.VISIBLE);
                    txt_reptphone.setText("");
                } else if (value.equals(NEW_EMAIL)) {
                    txt_newemail.setVisibility(View.VISIBLE);
                    txt_newemail.setText("");
                } else if (value.equals(REPT_EMAIL)) {
                    txt_reptemail.setVisibility(View.VISIBLE);
                    txt_reptemail.setText("");
                } else if (value.equals(NEW_IDCARD)) {
                    txt_newidcard.setVisibility(View.VISIBLE);
                    txt_newidcard.setText("");
                } else if (value.equals(REPT_IDCARD)) {
                    txt_reptidcard.setVisibility(View.VISIBLE);
                    txt_reptidcard.setText("");
                } else if (value.equals(NEW_PASS)) {
                    txt_newpass.setVisibility(View.VISIBLE);
                    txt_newpass.setText("");
                } else if (value.equals(REPT_PASS)) {
                    txt_reptpass.setVisibility(View.VISIBLE);
                    txt_reptpass.setText("");
                }
            }
        }
    }

    private void changeViewDefault() {
        txt_other_text.setVisibility(View.GONE);
        txt_other_number.setVisibility(View.GONE);
        txt_pass.setVisibility(View.GONE);
        txt_idcard.setVisibility(View.GONE);
        txt_phone.setVisibility(View.GONE);
        txt_newphone.setVisibility(View.GONE);
        txt_reptphone.setVisibility(View.GONE);
        txt_newemail.setVisibility(View.GONE);
        txt_reptemail.setVisibility(View.GONE);
        txt_newidcard.setVisibility(View.GONE);
        txt_reptidcard.setVisibility(View.GONE);
        txt_newpass.setVisibility(View.GONE);
        txt_reptpass.setVisibility(View.GONE);
        txt_my_num.setVisibility(View.GONE);
    }

    private void handleChangeViewDefault() {
        if (handler == null) handler = new MyHandler();
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    public void handleSetTitle(String title) {
        if (handler == null) handler = new MyHandler();
        Message message = new Message();
        message.what = 2;
        message.obj = title;
        handler.sendMessage(message);
    }

    public void handleAddInput() {
        if (handler == null) handler = new MyHandler();
        Message message = new Message();
        message.what = 3;
        handler.sendMessage(message);
    }

    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    changeViewDefault();
                    break;
                case 2:
                    String title = (String) msg.obj;
                    txt_title.setText(title);
                    break;
                case 3:
                    addInput(needValue, otherTxt);
                    break;
                case 16596:
                    if(popContain == null)return;
                    popContain.setFocusable(false);
                    popContain.dismiss();
                    callback = null;
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
            }
        }
    }
}

