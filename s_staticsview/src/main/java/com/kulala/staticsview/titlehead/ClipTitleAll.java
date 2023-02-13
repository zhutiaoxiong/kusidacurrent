package com.kulala.staticsview.titlehead;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.dispatcher.param.OEventSingleTopObject;
import com.kulala.staticsview.R;

import static com.kulala.dispatcher.IKStaticValue.MOTION_EVENT_UP;

/**
 * Created by qq522414074 on 2017/5/4.
 */

public class ClipTitleAll extends RelativeLayout implements OEventObject,OEventSingleTopObject {
    public ImageView img_left,img_right;
    public TextView edit,txt_right;

    public ClipTitleAll(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_title_all, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        edit = (TextView) findViewById(R.id.edit);
        img_right = (ImageView) findViewById(R.id.img_right);
        txt_right = (TextView) findViewById(R.id.txt_right);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     name    = ta.getString(R.styleable.androidMe_text);
        String     rightTxt    = ta.getString(R.styleable.androidMe_righttxt);
        int        leftId  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int        rightId  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        boolean        hideRightRes  = ta.getBoolean(R.styleable.androidMe_hideRightImage, true);
        boolean        hideRightTxt  = ta.getBoolean(R.styleable.androidMe_hideRightTxt, true);

        if (name != null && !name.equals("")) {
            edit.setText(name);
        }
        if (rightTxt != null && !rightTxt.equals("")) {
            txt_right.setText(rightTxt);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
        }
        if (hideRightRes) {
            img_right.setVisibility(View.INVISIBLE);
        }else{
            img_right.setVisibility(View.VISIBLE);
        }
        if (hideRightTxt) {
            txt_right.setVisibility(View.INVISIBLE);
        }else{
            txt_right.setVisibility(View.VISIBLE);
        }
        ta.recycle();
        ODispatcher.addSingleTopListener(OEventName.BLUE_RECEIVE_MSG, this);//蓝牙手势
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
         ODispatcher.removeSingleTopListener(OEventName.BLUE_RECEIVE_MSG, this);//蓝牙手势
    }
    public void setText(String txt){
        edit.setText(txt);
    }
    public void setpic(int pic){
        if(pic==0){
            img_left.setImageResource(R.drawable.img_back);
        }else if(pic==1){
            img_left.setImageResource(R.drawable.titlehead_back);
        }
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {

    }

    @Override
    public boolean receiveReturnIsSingleTopNeed(String s, Object o) {
        if(s.equals(OEventName.BLUE_RECEIVE_MSG)){
            if(this.getVisibility() == INVISIBLE || !this.isShown())return false;
            String cmd = (String)o;
            if(cmd != null && cmd.equals(MOTION_EVENT_UP)){
                img_left.callOnClick();
            }
            return true;
        }
        return false;
    }
}
