package com.kulala.staticsview;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;

public class OTime60 implements OEventObject {
    private Button button;
    private static int     time60second                  = 0;
    private long preTime;
    private String btnText = "";
    private MyHandler handler = new MyHandler();
    // ===========================================================================
    private static OTime60 _instance;
    private OTime60() {
    }
    public static OTime60 getInstance() {
        if (_instance == null)
            _instance = new OTime60();
        return _instance;
    }
    public void listener(Button button){
        if(button == null)return;
        btnText = button.getText().toString();
        long now = ODateTime.getNow();
        if(now >= preTime+60000){
            time60second = 0;
        }else{
            time60second = (int)(60-(now - preTime)/1000);
        }
//        if(BuildConfig.DEBUG) Log.e("time60","btnText:"+btnText+"time60second"+time60second);
        this.button = button;
        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, this);
    }
    public void clearButton(){
        ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, OTime60.this);
        this.button = null;
    }
    public void startTime(){
        time60second = 60;
        preTime = ODateTime.getNow();
        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, this);
    }
    public void endTime(){
//        if(BuildConfig.DEBUG) Log.e("time60","endTime time60second:"+time60second);
        time60second = 0;
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.TIME_TICK_SECOND)) {
//            if(BuildConfig.DEBUG) Log.e("time60","time60second:"+time60second);
            if (time60second > 0) {
                handleVerificationBtnSetText(String.valueOf(time60second));
                time60second--;
            }else if (time60second <= 0) {
                time60second--;
                handleVerificationBtnSetText(btnText);
            }
        }
    }
    // ===========================================================================

    // =====================================================================
    public void handleVerificationBtnSetText(String str) {
        Message message = new Message();
        message.what = 1;
        message.obj = str;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(time60second < 0) {
                        ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, OTime60.this);
                    }
                    String str = (String) msg.obj;
                    if(button == null)return;
                    button.setText(str);
                    if(btnText.equals(str)){
                        button.setAlpha(1F);
                        button.setEnabled(true);
                    }else{
                        button.setAlpha(0.5F);
                        button.setEnabled(false);
                    }
                    break;
            }
        }
    }
}
