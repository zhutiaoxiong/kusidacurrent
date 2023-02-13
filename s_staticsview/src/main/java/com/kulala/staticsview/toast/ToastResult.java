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

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.R;

/**
 * 提交成功，提交失败
 */

public class ToastResult implements OEventObject {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    private        RelativeLayout thisView;
    private        ImageView      img_result;
    private        TextView       txt_info;
    private static int            timeNum;

    private        MyHandler   handler;
    // ========================out======================
    private static ToastResult _instance;

    public static ToastResult getInstance() {
        if (_instance == null)
            _instance = new ToastResult();
        return _instance;
    }

    //===================================================
    public void show(View parentView, boolean resultOk, String infoTxt) {
        if (handler == null) handler = new MyHandler();
        timeNum = 0;
        ToastResult.this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_result, null);
        img_result = (ImageView) thisView.findViewById(R.id.img_result);
        txt_info = (TextView) thisView.findViewById(R.id.txt_info);
        if (resultOk) {
            img_result.setImageResource(R.drawable.result_ok);
            if (infoTxt == null || infoTxt.length() == 0) {
                txt_info.setText("提交成功");
            } else {
                txt_info.setText(infoTxt);
            }
        } else {
            img_result.setImageResource(R.drawable.result_fail);
            if (infoTxt == null || infoTxt.length() == 0) {
                txt_info.setText("提交失败");
            } else {
                txt_info.setText(infoTxt);
            }
        }
        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, ToastResult.this);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //禁用点击穿透,开启就是false,false,true
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void initEvents() {
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.TIME_TICK_SECOND)) {
            timeNum++;
            if (timeNum >= 3) {
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, this);
                handlehide();
            }
        }
    }

    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if (popContain == null) return;
                    popContain.dismiss();
                    timeNum = 0;
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
            }
        }
    }
}

