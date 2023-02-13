package com.kulala.staticsview.toast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;
import com.kulala.staticsview.static_interface.OCallBack;


public class MyOToastButton {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private RelativeLayout thisView;
    private Context        context;
    private TextView       txt_title,txt_tips;
    private TextView       txt1, txt2, txt3, txt4,txt5,txt_cancel;
    private        View         touch_exit;
    private        String       mark;//选择标记
    private OCallBack callback;
    private        MyHandler    handler;
    // ========================out======================
    private static MyOToastButton _instance;

    public static MyOToastButton getInstance() {
        if (_instance == null)
            _instance = new MyOToastButton();
        return _instance;
    }

    //===================================================
    public void show(View parentView, String[] operate4mx, String mark, OCallBack callback) {
        show(parentView, operate4mx, mark, callback,"","");
    }
    public void show(View parentView, String[] operate4mx, String mark, OCallBack callback,String title,String tips) {
        if (handler == null) handler = new MyHandler();
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.my_toast_popbutton, null);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);
        txt_tips = (TextView) thisView.findViewById(R.id.txt_tips);
        txt1 = (TextView) thisView.findViewById(R.id.txt1);
        txt2 = (TextView) thisView.findViewById(R.id.txt2);
        txt3 = (TextView) thisView.findViewById(R.id.txt3);
        txt4 = (TextView) thisView.findViewById(R.id.txt4);
        txt5 = (TextView) thisView.findViewById(R.id.txt5);
        txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancel);
        touch_exit = (View) thisView.findViewById(R.id.touch_exit);

        if(title == null || title.length() == 0){
            txt_title.setVisibility(View.GONE);
        }else{
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        if(tips == null || tips.length() == 0){
            txt_tips.setVisibility(View.GONE);
        }else{
            txt_tips.setVisibility(View.VISIBLE);
            txt_tips.setText(tips);
        }

        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        txt5.setVisibility(View.GONE);
        if (operate4mx.length >= 1) {
            txt1.setText(operate4mx[0]);
        }
        if (operate4mx.length >= 2) {
            txt2.setText(operate4mx[1]);
            txt2.setVisibility(View.VISIBLE);
        }
        if (operate4mx.length >= 3) {
            txt3.setText(operate4mx[2]);
            txt3.setVisibility(View.VISIBLE);
        }
        if (operate4mx.length >= 4) {
            txt4.setText(operate4mx[3]);
            txt4.setVisibility(View.VISIBLE);
        }
        if (operate4mx.length >= 5) {
            txt5.setText(operate4mx[4]);
            txt5.setVisibility(View.VISIBLE);
        }
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        txt1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, txt1.getText().toString());
            }
        });
        txt2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, txt2.getText().toString());
            }
        });
        txt3.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, txt3.getText().toString());
            }
        });
        txt4.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, txt4.getText().toString());
            }
        });
        txt5.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(callback!=null)callback.callback(mark, txt5.getText().toString());
            }
        });
        txt_cancel.setOnClickListener(new OnClickListenerMy() {
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
    public void setCancleBtnTxtColorForIK() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16598;
        handler.sendMessage(message);
    }
    public void setContentBtnColorForIK() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16599;
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
                    txt_cancel.setTextColor(context.getResources().getColor(R.color.black));
                    break;
                case 16598:
                    txt_cancel.setTextColor(context.getResources().getColor(R.color.black));
                    txt_cancel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    break;
                case 16599:
                    txt1.setTextColor(Color.parseColor("#f18c3b"));
                    txt2.setTextColor(Color.parseColor("#f18c3b"));
                    txt3.setTextColor(Color.parseColor("#f18c3b"));
                    txt4.setTextColor(Color.parseColor("#f18c3b"));
                    txt5.setTextColor(Color.parseColor("#f18c3b"));
                    txt_tips.setTextColor(Color.parseColor("#666666"));
                    txt_title.setTextColor(Color.parseColor("#666666"));
                    break;
            }
        }
    }
}

