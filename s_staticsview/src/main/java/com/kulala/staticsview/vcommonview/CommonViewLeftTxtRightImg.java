package com.kulala.staticsview.vcommonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.R;

/**
 * 用于我的页面左边文字右边图片
 */

public class CommonViewLeftTxtRightImg extends RelativeLayout {
    public TextView txt_left;
    public  ImageView img_splitline;
    public SmartImageView img_right;
    public CommonViewLeftTxtRightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_view_lefttxt_right_img, this, true);
        img_right= (SmartImageView) findViewById(R.id.img_right);
        txt_left= (TextView) findViewById(R.id.txt_left);
        img_splitline= (ImageView) findViewById(R.id.img_splitline);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        boolean     spitLine    = ta.getBoolean(R.styleable.androidMe_splitLine,false);
        int        rightId  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
        }
        if(spitLine){
            img_splitline.setVisibility(View.VISIBLE);
        }else{
            img_splitline.setVisibility(View.GONE);
        }
        ta.recycle();
    }
 }
