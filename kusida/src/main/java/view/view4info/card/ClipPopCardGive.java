package view.view4info.card;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;

/**
 * Created by qq522414074 on 2016/10/20.
 */
public class ClipPopCardGive implements OEventObject{
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private ImageView pic;
    private TextView text;
    private        MyHandler     handler;
    // ========================out======================
    private static ClipPopCardGive _instance;
    public static ClipPopCardGive getInstance() {
        if (_instance == null)
            _instance = new ClipPopCardGive();
        return _instance;
    }
    //===================================================
    public void show(View parentView, int resid,String mark) {
        if (handler == null) handler = new MyHandler();
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_card_give, null);
        pic = (ImageView) thisView.findViewById(R.id.picture);
        text=(TextView) thisView.findViewById(R.id.text_jjj);
        pic.setImageResource(resid);
        text.setText(mark);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    handlehide();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND,this);
    }


    public void initEvents() {
        thisView.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                handlehide();
            }
        });
    }

    private int count;
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.TIME_TICK_SECOND)){
            count++;
            if(count>=2){
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, this);
                handlehide();
            }
        }
    }
    private void handlehide() {
        if (handler == null){
            return;
        }
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if(popContain == null)return;
                    count = 0;
                    popContain.dismiss();
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
            }
        }
    }
}
