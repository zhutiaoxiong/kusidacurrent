package com.kulala.staticsview.line;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipLineBtnTxt extends RelativeLayout {
    public SmartImageView img_left;
    public ImageView img_right, img_splitline, img_red_point,img_cover_gray;
    private TextView txt_info;
    private MyHandler handler = new MyHandler();

    public ClipLineBtnTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_line_btntxt, this, true);
        img_left = (SmartImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        img_splitline = (ImageView) findViewById(R.id.img_splitline);
        img_red_point = (ImageView) findViewById(R.id.img_red_point);
        img_cover_gray = (ImageView) findViewById(R.id.img_cover_gray);
        txt_info = (TextView) findViewById(R.id.txt_info);

        TypedArray ta             = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     name           = ta.getString(R.styleable.androidMe_text);
        int        leftId         = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int        rightId        = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        boolean    splitLine      = ta.getBoolean(R.styleable.androidMe_splitLine, true);
        boolean    hideRightImage = ta.getBoolean(R.styleable.androidMe_hideRightImage, false);

        if (name != null && !name.equals("")) {
            txt_info.setText(name);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
            img_left.setVisibility(View.VISIBLE);
        } else {
            img_left.setVisibility(View.GONE);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(View.VISIBLE);
        }
        if (hideRightImage) {
            img_right.setVisibility(View.GONE);
        }
        if (splitLine) {
            img_splitline.setVisibility(View.VISIBLE);
        } else {
            img_splitline.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }

    public void setRedPointVisible(boolean defaultFalse) {
        if (defaultFalse) {
            img_red_point.setVisibility(VISIBLE);
        } else {
            img_red_point.setVisibility(INVISIBLE);
        }
    }

    public void setImgLeftUrl(String url) {
        if (url != null && url.length()>0) {
            img_left.setImageUrl(url);
            img_left.setVisibility(View.VISIBLE);
        }
    }
    public void setRightImgVisible(boolean visible) {
        if (visible) {
            handleShowRightImg(VISIBLE);
        } else {
            handleShowRightImg(GONE);
        }
    }
    /**default 44*44*/
    public void setRightImgSize(int dpW) {
        ViewGroup.LayoutParams params = img_right.getLayoutParams();
        params.width = ODipToPx.dipToPx(getContext(),dpW);
        img_right.setLayoutParams(params);
        img_right.setPadding(0,0,10,0);
    }
    public void setRightImg(Drawable drawable) {
        img_right.setImageDrawable(drawable);
    }
    public void setCoverGrayVisible(int visible){
        img_cover_gray.setVisibility(visible);
    }

    public void handleShowRightImg(int visibleValue) {
        Message message = new Message();
        message.what = 911;
        message.arg1 = visibleValue;
        handler.sendMessage(message);
    }
    public void setText(String text) {
        txt_info.setText(text);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 911:
                    int visibleValue = msg.arg1;
                    img_right.setVisibility(visibleValue);
                    break;
            }
        }
    }
}
