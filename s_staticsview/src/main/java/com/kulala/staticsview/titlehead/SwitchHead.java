package com.kulala.staticsview.titlehead;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;

public class SwitchHead extends RelativeLayout implements OEventObject {
    public static int select_type,SELECT_LEFT = 1,SELECT_RIGHT = 2;
    public  ImageView img_left;
    private Button    btn_left,btn_right;

    private OnSelectedListener onSelectedListener;
    private MyHandler handler = new MyHandler();
    public SwitchHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_switchhead, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);

        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     lefttxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     righttxt    = ta.getString(R.styleable.androidMe_righttxt);

        if (lefttxt != null && lefttxt.length()>0)btn_left.setText(lefttxt);
        if (righttxt != null && righttxt.length()>0)btn_right.setText(righttxt);
        ta.recycle();
        if (!isInEditMode()) ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);

        setSelected(SELECT_LEFT);
        initEvent();
    }
    private void initEvent(){
        btn_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setSelected(SELECT_LEFT);
                if(onSelectedListener!=null)onSelectedListener.onSelect(SELECT_LEFT);
            }
        });
        btn_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setSelected(SELECT_RIGHT);
                if(onSelectedListener!=null)onSelectedListener.onSelect(SELECT_RIGHT);
            }
        });
    }
    public void setSelected(int selectWay){
        select_type = selectWay;
        if(selectWay == SELECT_LEFT){
            btn_left.setSelected(true);
            btn_right.setSelected(false);
            btn_left.setTextColor(getResources().getColor(R.color.black));
            btn_right.setTextColor(getResources().getColor(R.color.white));
        }else if(selectWay == SELECT_RIGHT){
            btn_left.setSelected(false);
            btn_right.setSelected(true);
            btn_left.setTextColor(getResources().getColor(R.color.white));
            btn_right.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MAIN_CLICK_BACK, this);
        super.onDetachedFromWindow();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.MAIN_CLICK_BACK)) {
            handleClickLeft();
        }
    }
    public void handleClickLeft() {
        Message message = new Message();
        message.what = 314;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 314:
                    img_left.callOnClick();
                    break;
            }
        }
    }
    // ===================================================
    /**selectWay: SELECT_LEFT,SELECT_RIGHT*/
    public interface OnSelectedListener{
        void onSelect(int selectWay);
    }
    public void SetOnSelectedListener(OnSelectedListener listener){
        this.onSelectedListener = listener;
    }
}
