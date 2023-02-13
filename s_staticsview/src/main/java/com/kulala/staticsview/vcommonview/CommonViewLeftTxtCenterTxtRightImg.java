package com.kulala.staticsview.vcommonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


/**
 * 用于我的页面左边文字中间文字右边图片
 */

public class CommonViewLeftTxtCenterTxtRightImg extends RelativeLayout {
    public TextView txt_left,txt_center;
    public  ImageView img_right,img_splitline;
    public CommonViewLeftTxtCenterTxtRightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_view_lefttxt_centertxt_rightimg, this, true);
        img_right= (ImageView) findViewById(R.id.img_right);
        txt_left= (TextView) findViewById(R.id.txt_left);
        txt_center= (TextView) findViewById(R.id.txt_center);
        img_splitline= (ImageView) findViewById(R.id.img_splitline);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     centerTet    = ta.getString(R.styleable.androidMe_text);
        boolean     spitLine    = ta.getBoolean(R.styleable.androidMe_splitLine,true);
        boolean     hideRightImg    = ta.getBoolean(R.styleable.androidMe_hideRightImage,false);
        boolean     hideCenterTxt    = ta.getBoolean(R.styleable.androidMe_hideCenterTxt,false);
        int        rightId  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
        }
        if (centerTet != null && !centerTet.equals("")) {
            txt_center.setText(centerTet);
        }
        if(spitLine){
            img_splitline.setVisibility(View.VISIBLE);
        }else{
            img_splitline.setVisibility(View.GONE);
        }
        if(hideRightImg){
            img_right.setVisibility(View.INVISIBLE);
        }else{
            img_right.setVisibility(View.VISIBLE);
        }
        if(hideCenterTxt){
            txt_center.setVisibility(View.INVISIBLE);
        }else{
            txt_center.setVisibility(View.VISIBLE);
        }
        ta.recycle();
    }
}
