package annualreminder.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import annualreminder.view.style.StyleResult;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

/**
 * 年检提醒主页面
 *
 1.跳转页面前:
   ManagerAnnual.getInstance().saveCarActiveList(ManagerCarList.getInstance().getCarAnnualList());
   ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_annual_reminder_main);
 2.每项目需要加入
   add CtrlAnnual to CtrlBaseHttp
   backdata_1223_getLostNotifation 需要设置提醒消息
 */

public class ViewAnnualReminderMain extends RelativeLayoutBase {
    private RelativeLayout lin_detail,lin_toast,lin_tips;
    private int lastViewResId;
    private MyHandler handler = new MyHandler();
    public ViewAnnualReminderMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_annual_reminder_main, this, true);
        lin_detail = (RelativeLayout) findViewById(R.id.lin_detail);
        lin_toast = (RelativeLayout) findViewById(R.id.lin_toast);
        lin_tips = (RelativeLayout) findViewById(R.id.lin_tips);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW, this);
        ODispatcher.addEventListener(OEventName.ANNUAL_CHANGE_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.ANNUAL_RECODE_RESULTBACK,this);
    }
    @Override
    protected void initViews() {
        handlePopView(R.layout.view_annual_reminder_fir);
    }
    @Override
    protected void initEvents() {

    }
    @Override
    protected void invalidateUI() {

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW)) {
            int resId = (Integer) paramObj;
            handlePopView(resId);
        }else if(eventName.equals(OEventName.ANNUAL_CHANGE_RESULTBACK)){
            handleShowResult(paramObj);
        }else if(eventName.equals(OEventName.ANNUAL_RECODE_RESULTBACK)){
            boolean resultOK = (Boolean) paramObj;
            if(resultOK) {
                handleShowResult(paramObj);
            }
        }
    }
    private void popView(int resId) {
        lastViewResId = resId;
        lin_detail.removeAllViews();
        if(resId == R.layout.view_annual_reminder_fir) {//library not support switch
            lastViewResId = R.layout.view_annual_reminder_fir;
            lin_detail.addView(new ViewAnnualReminder_Fir(getContext(), null));
        }else if(resId == R.layout.view_annual_reminder_add) {//library not support switch
            lastViewResId = R.layout.view_annual_reminder_add;
            lin_detail.addView(new ViewAnnual_ReminderAdd(getContext(), null));
        }else if(resId == R.layout.view_annual_reminder_add_manual) {//library not support switch
            lastViewResId = R.layout.view_annual_reminder_add_manual;
            lin_detail.addView(new ViewAnnual_ReminderAdd_Manual(getContext(), null));
        }else if(resId == R.layout.view_annual_auto_intro) {//library not support switch
            lastViewResId = R.layout.view_annual_auto_intro;
            lin_detail.addView(new ViewAnnualAutoIntro(getContext(), null));
        }
//        else if(resId == R.layout.view_annual_recode_add) {//library not support switch
//            lastViewResId = R.layout.view_annual_recode_add;
//            lin_detail.addView(new ViewAnnualRecode_Add(getContext(), null));
//        }
    }

    public void handlePopView(int resId) {
        Message message = new Message();
        message.what = 312;
        message.arg1 = resId;
        handler.sendMessage(message);
    }
    public void handleShowResult(Object resultOK) {
        Message message = new Message();
        message.what = 313;
        message.obj = resultOK;
        handler.sendMessage(message);
    }
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 312:
                    popView(msg.arg1);
                    break;
                case 313:
                    boolean resultOK = (Boolean)msg.obj;
                    if(resultOK){
                        StyleResult.getInstance().show(lin_toast,true,"提交成功!");
                    }else{
                        StyleResult.getInstance().show(lin_toast,false,"提交失败!");
                    }
                    break;
            }
        }
    }
}
