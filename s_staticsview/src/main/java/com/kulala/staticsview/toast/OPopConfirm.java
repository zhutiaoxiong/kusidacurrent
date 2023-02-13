package com.kulala.staticsview.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;
import com.kulala.staticsview.static_interface.OCallBack;

public class OPopConfirm {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private RelativeLayout thisView;
    private Context        context;
    private TextView txt_title, txt_text, btn_cancel, btn_confirm;
    private ImageView img_splitline, view_background;
    private        ImageView         touch_exit;
    private        String       mark;//选择标记
    private        OCallBack    callback;
    private        MyHandler    handler;
    // ========================out======================
    private static OPopConfirm _instance;

    public static OPopConfirm getInstance() {
        if (_instance == null)
            _instance = new OPopConfirm();
        return _instance;
    }

    //===================================================
    public void show(View parentView, String mark, OCallBack callback) {
        show(parentView, mark, callback,"","");
    }
    public void show(View parentView, String mark, OCallBack callback,String title,String tips) {
        if (handler == null) handler = new MyHandler();
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_confirm, null);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);
        txt_text = (TextView) thisView.findViewById(R.id.txt_text);
        btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView)thisView. findViewById(R.id.btn_confirm);
        touch_exit = (ImageView) thisView.findViewById(R.id.view_background);

        if(title == null || title.length() == 0){
            txt_title.setVisibility(View.GONE);
        }else{
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        if(tips == null || tips.length() == 0){
            txt_text.setVisibility(View.GONE);
        }else{
            txt_text.setVisibility(View.VISIBLE);
            txt_text.setText(tips);
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
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, "ImSureNow");
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
                    btn_cancel.setTextColor(context.getResources().getColor(R.color.black));
                    break;
            }
        }
    }
}

