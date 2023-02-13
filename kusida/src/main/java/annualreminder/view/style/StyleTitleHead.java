package annualreminder.view.style;

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

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

public class StyleTitleHead extends RelativeLayout implements OEventObject {
    public ImageView img_left, img_right;
    private TextView txt_title_show;

    //	private LinearLayout linq;
    private MyHandler handler = new MyHandler();
    public StyleTitleHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.style_titlehead_annual, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        txt_title_show = (TextView) findViewById(R.id.txt_title_show);
//		linq = (LinearLayout) findViewById(R.id.linq);

        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     name    = ta.getString(R.styleable.androidMe_text);
        int        leftId  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int        rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
//        int background = ta.getColor(R.styleable.androidMe_bkcolor,0);
        if (name != null && !name.equals("")) {
            txt_title_show.setText(name);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
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
