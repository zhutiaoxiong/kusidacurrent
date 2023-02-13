package com.kulala.staticsview.line;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;

/**
 * 按扭左右图标，中间文字
 */
public class ClipLineTxtButton extends LinearLayout {
    private TextView  txt_title;
    public  ImageView img_left,img_right, img_splitline;

    public ClipLineTxtButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_line_txtbutton, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        img_splitline = (ImageView) findViewById(R.id.img_splitline);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        //title
        String textLeft = ta.getString(R.styleable.androidMe_text);
        if (textLeft != null && !textLeft.equals("")) {
            txt_title.setText(textLeft);
        } else {
            txt_title.setText("");
        }
        int textColor = ta.getColor(R.styleable.androidMe_textcolor, 0);
        if (textColor != 0) {
            txt_title.setTextColor(textColor);
        }
        String gravity = ta.getString(R.styleable.androidMe_gravityTxt);
        if(gravity!=null) {
            if (gravity.equals("left")) {
                txt_title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else if (gravity.equals("center")) {
                txt_title.setGravity(Gravity.CENTER);
            }
        }
        //left img
        int leftId = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        if (leftId != 0) {
            img_left.setImageResource(leftId);
            img_left.setVisibility(View.VISIBLE);
        } else {
            img_left.setVisibility(View.INVISIBLE);
        }
        //right img
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        if (rightId != 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(View.VISIBLE);
        } else {
            img_right.setVisibility(View.INVISIBLE);
        }
        //splitLine
        boolean splitLine = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (splitLine) {
            img_splitline.setVisibility(View.VISIBLE);
        } else {
            img_splitline.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }


    public void setText(String txt) {
        txt_title.setText(txt);
    }

    public String getText() {
        return txt_title.getText().toString();
    }
    //=================================================
    public class AllCapTransformationMethod extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }
    }
}
