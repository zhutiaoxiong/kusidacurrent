package view.view4me.set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipTitleMeSet extends RelativeLayout implements OEventObject {
    public ImageView img_left;
    public TextView txt_title_show;
    public ImageView img_right;
    public TextView txt_right;
    private MyHandler handler = new MyHandler();
    public ClipTitleMeSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.me_title_my, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        txt_title_show = (TextView) findViewById(R.id.txt_title_show);
        img_right = (ImageView) findViewById(R.id.img_right);
        txt_right= (TextView) findViewById(R.id.txt_right);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        int leftId = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        String rightTxt=ta.getString(R.styleable.androidMe_righttxt);
        if (name != null && !name.equals("")) {
            txt_title_show.setText(name);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
        }
        if (rightTxt != null && !rightTxt.equals("")) {
            txt_right.setText(rightTxt);
        }
        ta.recycle();
    }
    public void setTitle(String name){
        if (name != null && !name.equals("")) {
            txt_title_show.setText(name);
        }
    }
    public void setRightRes(int rightId){
        if (rightId != 0) {
            img_right.setImageResource(rightId);
        }
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.MAIN_CLICK_BACK)) {
            handleClickLeft();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);;
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MAIN_CLICK_BACK, this);
        super.onDetachedFromWindow();
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
