package com.kulala.staticsview.toast;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.R;

/**
 * 转圈
 */

public class ToastLoading extends RelativeLayout {
    private ImageView img_loading;
    private Animation anmiRotate;
    private ViewGroup decorView;//activity的根View

    private MyHandler handler = new MyHandler();

    // ===================================================
    public ToastLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        LayoutInflater.from(context).inflate(R.layout.toast_loading, this, true);
        img_loading = (ImageView) findViewById(R.id.img_loading);

        anmiRotate = AnimationUtils.loadAnimation(context, R.anim.animation_loading);
        LinearInterpolator lir = new LinearInterpolator();
        anmiRotate.setInterpolator(lir);//必设不然无法均速
    }
    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
    // ===================================================
    //主动开启
    public void handleStartLoading() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16597;
        handler.sendMessage(message);
    }
    //主动关闭
    public void handleStopLoading() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 11003;
        handler.sendMessage(message);
    }
    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16597:
                    if (anmiRotate == null || decorView == null) return;
                    img_loading.startAnimation(anmiRotate);
                    decorView.removeView(ToastLoading.this);
                    decorView.addView(ToastLoading.this);
                    Message message = new Message();
                    message.what = 11004;//04
                    if (handler == null) return;
                    handler.sendMessageDelayed(message, 8000);//6秒后就要停了
                    break;
                case 11003:
                    if (canCancelAnimation()) {
                        img_loading.clearAnimation();
                        img_loading.animate().cancel();
                    }
                    decorView.removeView(ToastLoading.this);
                    break;
                case 11004:
                    if (canCancelAnimation()) {
                        img_loading.clearAnimation();
                        img_loading.animate().cancel();
                    }
                    decorView.removeView(ToastLoading.this);
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_OUTTIME);
                    break;
            }
        }
    }
    // ===================================================
}

