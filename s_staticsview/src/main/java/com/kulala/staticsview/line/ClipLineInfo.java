package com.kulala.staticsview.line;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;

/**
 * 输入文字，有标题,后有小图标
 */
public class ClipLineInfo extends LinearLayout {
    private TextView txt_left, txt_right;

    public ClipLineInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_line_info, this, true);
        txt_left = (TextView) findViewById(R.id.txt_left);
        txt_right = (TextView) findViewById(R.id.txt_right);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        //title
        String textLeft  = ta.getString(R.styleable.androidMe_lefttxt);
        String textright = ta.getString(R.styleable.androidMe_righttxt);
        if (textLeft != null && !textLeft.equals("")) {
            txt_left.setText(textLeft);
            txt_left.setVisibility(VISIBLE);
        } else {
            txt_left.setVisibility(GONE);
        }
        if (textright != null && !textright.equals("")) {
            txt_right.setText(textright);
        } else {
            txt_right.setText("");
        }
        ta.recycle();
    }

    public void setText(String txt) {
        txt_right.setText(txt);
    }

    public String getText() {
        return txt_right.getText().toString();
    }

}
