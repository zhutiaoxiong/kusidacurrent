package com.kulala.staticsview.line;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;

/**
 * 输入文字，有标题,后有小图标
 */
public class ClipLineTxtInput extends LinearLayout {
    private TextView  txt_title;
    public  EditText  txt_input;
    public  ImageView img_right, img_splitline;

    public ClipLineTxtInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_line_txtinput, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_input = (EditText) findViewById(R.id.txt_input);
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
        //input
        String righthint = ta.getString(R.styleable.androidMe_hint);
        if (righthint != null && !righthint.equals("")) {
            txt_input.setHint(righthint);
        } else {
            txt_input.setHint("");
        }

        String inputType = ta.getString(R.styleable.androidMe_inputType);
        int    maxLength = ta.getInteger(R.styleable.androidMe_maxLength, 0);
        openInput(inputTypeConvert(inputType), maxLength);

        String digits = ta.getString(R.styleable.androidMe_digits);
        if (digits != null && !digits.equals("")) {
            txt_input.setKeyListener(DigitsKeyListener.getInstance(digits));
        }

        int textRightcolor = ta.getColor(R.styleable.androidMe_righttxtcolor, 0);
        if (textRightcolor != 0) {
            txt_input.setTextColor(textRightcolor);
        }
        String gravity = ta.getString(R.styleable.androidMe_gravityTxt);
        if(gravity!=null) {
            if (gravity.equals("left")) {
                txt_input.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else if (gravity.equals("center")) {
                txt_input.setGravity(Gravity.CENTER);
            }
        }
        //right img
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        setRightImage(rightId);
        //splitLine
        boolean splitLine = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (splitLine) {
            img_splitline.setVisibility(View.VISIBLE);
        } else {
            img_splitline.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }

    private int inputTypeConvert(String inputType) {
        if (inputType.equals("phone")) return InputType.TYPE_CLASS_PHONE;
        if (inputType.equals("number")) return InputType.TYPE_CLASS_NUMBER;
        if (inputType.equals("password")) return InputType.TYPE_TEXT_VARIATION_PASSWORD;
        return 0;
    }

    public void openInput(int inputType, int maxnum) {
        txt_input.setVisibility(VISIBLE);
        txt_input.setEnabled(true);
        txt_input.setInputType(inputType);
        if (inputType == InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS) {
            txt_input.setTransformationMethod(new AllCapTransformationMethod());
        }
        if (maxnum > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxnum)};
            txt_input.setFilters(filters);
        }
    }

    public void setText(String txt) {
        txt_input.setText(txt);
    }

    public String getText() {
        return txt_input.getText().toString();
    }

    public void setRightImage(int resId) {
        if (resId != 0) {
            img_right.setImageResource(resId);
            img_right.setVisibility(View.VISIBLE);
        } else {
            img_right.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightTextColorResid(int resid) {
        txt_input.setTextColor(getResources().getColor(resid));
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
