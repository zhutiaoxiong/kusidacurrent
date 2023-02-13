package com.kulala.staticsview.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;
import com.kulala.staticsview.static_interface.OCallBack;

public class OToastConformInput {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private RelativeLayout thisView;
    private Context        context;
    private TextView txt_title, txt_text, btn_cancel, btn_confirm;
    private ImageView img_splitline, view_background;
    private EditText txt_other_text;
    private        View         touch_exit;
    private        String       mark;//选择标记
    private        OCallBack    callback;
    private        MyHandler    handler;
    // ========================out======================
    private static OToastConformInput _instance;

    public static OToastConformInput getInstance() {
        if (_instance == null)
            _instance = new OToastConformInput();
        return _instance;
    }

    //===================================================
//    public void show(View parentView, String[] operate4mx, String mark, OCallBack callback) {
//        show(parentView, operate4mx, mark, callback,"","");
//    }
    public void show(View parentView, String[] operate4mx, OCallBack callback) {
        if (handler == null) handler = new MyHandler();
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_confirm_input, null);
        txt_title = (TextView)thisView. findViewById(R.id.txt_title);
        txt_text = (TextView) thisView.findViewById(R.id.txt_text);
        btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) thisView.findViewById(R.id.btn_confirm);
        img_splitline = (ImageView) thisView.findViewById(R.id.img_splitline);
        txt_other_text=(EditText) thisView.findViewById(R.id.txt_other_text);

        if(operate4mx[0] == null || operate4mx[0].length() == 0){
            txt_title.setVisibility(View.GONE);
        }else{
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(operate4mx[0]);
        }
        if(operate4mx[1] == null || operate4mx[1].length() == 0){
            txt_text.setVisibility(View.GONE);
        }else{
            txt_text.setVisibility(View.VISIBLE);
            txt_text.setText(operate4mx[1]);
        }
        if(operate4mx[2] == null || operate4mx[2].length() == 0){
            txt_other_text.setVisibility(View.GONE);
        }else{
            txt_other_text.setVisibility(View.VISIBLE);
            txt_other_text.setText(operate4mx[2]);
            txt_other_text.setSelection(operate4mx[2].length());
        }
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }


    public void initEvents() {
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback("changeGroupName",txt_other_text.getText().toString());
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
            }
        });
    }
    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }
    public void setCancleBtnTxtColor() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16597;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if(popContain == null)return;
                    popContain.dismiss();
                    callback = null;
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
                case 16597:
//                    txt_cancel.setTextColor(context.getResources().getColor(R.color.black));
                    break;
            }
        }
    }
}

