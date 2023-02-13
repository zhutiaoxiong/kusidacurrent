package com.kulala.staticsview.toast;

import android.app.Activity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.R;

/**
 * 确认框,可以非主线程
 */
public class ToastTxt extends RelativeLayout {
    private TextView  txt_info;
    private ImageView view_background;
    private Activity  useActivity;//activity的根View
    private ViewGroup decorViewGroup;//activity的根View

    public static ToastTxt ToastTxtThis;//外部用来判断是否弹出了窗
    // ===================================================
    public ToastTxt(Activity contextActivity, AttributeSet attrs) {
        super(contextActivity, attrs);
        if (contextActivity == null) return;
        this.useActivity = contextActivity;
        LayoutInflater.from(contextActivity).inflate(R.layout.toast_text, this, true);
        txt_info = (TextView) findViewById(R.id.txt_info);
        view_background = (ImageView) findViewById(R.id.view_background);
    }
    public ToastTxt(Activity contextActivity, AttributeSet attrs,boolean isNeedBg) {
        super(contextActivity, attrs);
        if (contextActivity == null) return;
        this.useActivity = contextActivity;
        LayoutInflater.from(contextActivity).inflate(R.layout.toast_text, this, true);
        txt_info = (TextView) findViewById(R.id.txt_info);
        view_background = (ImageView) findViewById(R.id.view_background);
        if(!isNeedBg){
            view_background.setVisibility(View.INVISIBLE);
        }
    }
    private boolean exit() {
        if (decorViewGroup != null) {
            decorViewGroup.removeView(ToastTxt.this);
            decorViewGroup = null;
            useActivity = null;
            ToastTxtThis = null;
            return true;
        } else {
            return false;
        }
    }
    // ===================================================
    /**
     * infoTxtColor :默认 0
     */
    public ToastTxt withInfo(CharSequence info) {
        if (info != null && info.length() > 0) {
            txt_info.setText(info);
        }
        return this;
    }
    public ToastTxt withInfo(CharSequence info,int size) {
        if (info != null && info.length() > 0) {
            txt_info.setText(info);
            txt_info.setTextSize(ODipToPx.dipToPx(getContext(),size));
        }
        return this;
    }
    private long preTime = 0;
    public void show() {
        long now = System.currentTimeMillis();
        if (now - preTime < 1000) return;
        preTime = now;
        useActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (decorViewGroup == null && useActivity != null) {
                    if(BuildConfig.DEBUG) Log.e("TOASTTXT", "show");
                    View decorView = useActivity.getWindow().getDecorView();
                    if (decorView == null) return;
                    decorViewGroup = (ViewGroup) decorView.findViewById(android.R.id.content);
                    decorViewGroup.addView(ToastTxt.this);
                    ToastTxtThis = ToastTxt.this;
                    new CountDownTimerMy(1500, 1500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            exit();
                        }
                    }.start();
                }else{
                    if(BuildConfig.DEBUG) Log.e("TOASTTXT", "unshow");
                }
            }
        });
    }
    public void quicklyShow() {
        long now = System.currentTimeMillis();
        if (now - preTime < 50) return;
        preTime = now;
        useActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (decorViewGroup == null && useActivity != null) {
                    if(BuildConfig.DEBUG) Log.e("TOASTTXT", "show");
                    View decorView = useActivity.getWindow().getDecorView();
                    if (decorView == null) return;
                    decorViewGroup = (ViewGroup) decorView.findViewById(android.R.id.content);
                    decorViewGroup.addView(ToastTxt.this);
                    ToastTxtThis = ToastTxt.this;
                    new CountDownTimerMy(1500, 1500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            exit();
                        }
                    }.start();
                }else{
                    if(BuildConfig.DEBUG) Log.e("TOASTTXT", "unshow");
                }
            }
        });
    }
}
