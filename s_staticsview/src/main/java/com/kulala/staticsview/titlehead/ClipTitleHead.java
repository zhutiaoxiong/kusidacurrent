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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.R;

public class ClipTitleHead extends RelativeLayout implements OEventObject {
    public ImageView img_left, img_right;
    private TextView txt_title_show;
    public TextView txt_left,txt_right;

    //	private LinearLayout linq;
    private MyHandler handler = new MyHandler();
    public ClipTitleHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.titlehead, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        txt_title_show = (TextView) findViewById(R.id.txt_title_show);
        txt_left = (TextView) findViewById(R.id.txt_leftn);
        txt_right = (TextView) findViewById(R.id.txt_rightn);
//		linq = (LinearLayout) findViewById(R.id.linq);

        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     name    = ta.getString(R.styleable.androidMe_text);
        int        leftId  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int        rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        String     lefttxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     righttxt = ta.getString(R.styleable.androidMe_righttxt);
        if (name != null && !name.equals("")) {
            txt_title_show.setText(name);
        }
        if (lefttxt != null && !lefttxt.equals("")) {
            txt_left.setText(lefttxt);
        }else{
            txt_left.setText("");
        }
        if (righttxt != null && !righttxt.equals("")) {
            txt_right.setText(righttxt);
        }else{
            txt_right.setText("");
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(VISIBLE);
        }else{
            img_right.setVisibility(INVISIBLE);
        }
//        if(background!=0){
//        	linq.setBackgroundColor(background);
//        }
        ta.recycle();
        if (!isInEditMode()) ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MAIN_CLICK_BACK, this);
        super.onDetachedFromWindow();
    }

    public void setTitle(String title) {
        txt_title_show.setText(title);
    }

    public void setRightRes(int resId) {
        if (resId == 0) {
            img_right.setImageDrawable(null);
        } else {
            img_right.setImageResource(resId);
        }
    }
    /**比如主页面是不要左箭头的*/
    public void hideLeftImg(){
        setLeftRes(0);
    }
    public void setLeftRes(int resId) {
        if (resId == 0) {
            img_left.setImageDrawable(null);
        } else {
            img_left.setImageResource(resId);
        }
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
}
