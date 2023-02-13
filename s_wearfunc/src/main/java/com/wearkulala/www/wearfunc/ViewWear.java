package com.wearkulala.www.wearfunc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.line.ClipLineBtnTxt;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import com.kulala.staticsview.toast.ToastTxt;

/**
 * 1.必须连上手机才能开关，否这不能操作，灰色
 * 2.手表退出，手机也关
 */
public class ViewWear extends LinearLayoutBase {
    private static final int SWITCH_ON = 1;
    private static final int SWITCH_OFF = 0;

    private ClipTitleHead  title_head;
    private ClipLineBtnTxt txt_wear_switch;
    private MyHandler handler = new MyHandler();
    public static ViewWear viewWearThis;
    public ViewWear(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(com.wearkulala.www.wearfunc.R.layout.view_me_wear, this, true);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        txt_wear_switch = (ClipLineBtnTxt) findViewById(com.wearkulala.www.wearfunc.R.id.txt_wear_switch);
        txt_wear_switch.setRightImgSize(50);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.WEAR_LINK_STATE_CHANGE,this);
        ODispatcher.addEventListener(OEventName.SWITCH_WEARS_RESULTBACK,this);
    }
    @Override
    protected void initViews() {
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewWearThis = this;
        WearReg.getInstance().ccmd1305_getSwitchInfo();//取开关列表
        handler.obtainMessage(HANDLER_CHANGE_SWITCH).sendToTarget();//连接是否成功
        handler.obtainMessage(HANDLER_CHECK_WEAR_ENABLE,true).sendToTarget();//连接是否成功
    }
    @Override
    protected void onDetachedFromWindow() {
        viewWearThis = null;
        super.onDetachedFromWindow();
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
//        if (eventName.equals(OEventName.WEAR_LINK_STATE_CHANGE)) {//开关列表
//            Log.e("YXH", "WEAR_LINK_STATE_CHANGE");
//            handler.obtainMessage(HANDLER_CHECK_WEAR_ENABLE,paramObj).sendToTarget();//连接是否成功
//        }else
            if (eventName.equals(OEventName.SWITCH_WEARS_RESULTBACK)) {
            Log.e("YXH", "SWITCH_WEARS_RESULTBACK");
            handler.obtainMessage(HANDLER_CHANGE_SWITCH).sendToTarget();
        }
        super.receiveEvent(eventName, paramObj);
    }
    @Override
    protected void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, 0);
            }
        });
        //open switch
        txt_wear_switch.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(WearLinkServicePhone.getInstance() == null)return;
                if(!WearLinkServicePhone.openBlueIfNeed())return;
                //点击切换登录退出
                int switchOpen = WearReg.getInstance().getSwitchWearsOpen();
                if(switchOpen==-1){
                    new ToastTxt(WearReg.getInstance().getCurrentActivity(),null).withInfo("服务端无数据").show();
                    return;
                }
                if (switchOpen == 1) {//之前是打开的,发消息去关
                    WearLinkServicePhone.changeUser(0,"");
                    WearReg.getInstance().ccmd1306_changeSwitch(WearReg.getInstance().getSwitchWearsIde(), false);
                } else {//之前是关闭的,发消息去开
                    long uid = WearReg.getInstance().getUserID();
                    WearLinkServicePhone.changeUser(uid,WearReg.getInstance().getWatchToken());
                    WearReg.getInstance().ccmd1306_changeSwitch(WearReg.getInstance().getSwitchWearsIde(), true);
                }
            }
        });
    }
    @Override
    protected void invalidateUI() {

    }
    public void handlerChangeSwitch(){
        handler.obtainMessage(HANDLER_CHANGE_SWITCH).sendToTarget();
    }
    private static final int HANDLER_CHANGE_SWITCH         = 10014;
    private static final int HANDLER_CHECK_WEAR_ENABLE     = 10018;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_SWITCH:
                    int switchOpen = WearReg.getInstance().getSwitchWearsOpen();
                    if(switchOpen==-1){
                        txt_wear_switch.setRightImg(getResources().getDrawable(R.drawable.switch_off_wear));
//                        new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo("服务端无手表数据").show();
                        return;
                    }else if(switchOpen == 1){
                        txt_wear_switch.setRightImg(getResources().getDrawable(R.drawable.switch_on_wear));
                    }else{
                        txt_wear_switch.setRightImg(getResources().getDrawable(R.drawable.switch_off_wear));
                    }
                    break;
                case HANDLER_CHECK_WEAR_ENABLE:
                    boolean isWearConned = (Boolean) msg.obj;//手表是否连上
                    if(isWearConned){
                        txt_wear_switch.setCoverGrayVisible(View.INVISIBLE);
                        txt_wear_switch.img_right.setClickable(true);
                    }else{
                        txt_wear_switch.setCoverGrayVisible(View.VISIBLE);
                        txt_wear_switch.img_right.setClickable(false);
                    }
                    break;
            }
        }
    }
}
