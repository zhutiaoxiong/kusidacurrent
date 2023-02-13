package view.clip.gesture;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.MD5;

import ctrl.OCtrlGesture;
import model.ManagerGesture;
import view.clip.gesture.GestureDrawline.GestureCallBack;
import view.view4me.set.ClipTitleMeSet;

/**
 * 手势密码设置界面
 */
public class GestureEditPage extends RelativeLayoutBase{
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER    = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE     = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private ClipTitleMeSet title_head;
    private LockIndicator      mLockIndicator;
    private TextView           mTextTip;
    private FrameLayout        mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView           mTextReset;
    private Button             btn_cancel;
    private boolean mIsFirstInput    = true;
    private String  mFirstPassword   = null;

    private static String INPUT_CODE = "";
    public static boolean isForResetGesture = false;

    public GestureEditPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_gesture_edit, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        mTextReset = (TextView) findViewById(R.id.text_reset);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        if(isForResetGesture){
            btn_cancel.setVisibility(VISIBLE);
        }else{
            btn_cancel.setVisibility(INVISIBLE);
        }
        isForResetGesture = false;
        mTextReset.setClickable(false);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SETUP_GESTURE_RESULTBACK,this);
    }
    protected void initViews(){
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(getContext(), false, "", new GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#B03125'>"+getResources().getString(R.string.link_at_least_four_points_please_enter_again)+"</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextReset.setClickable(true);
                    mTextReset.setText("再输一次手势密码");
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.set_up_the_success));
                        mGestureContentView.clearDrawlineState(0L);
                        OCtrlGesture.getInstance().ccmd1311_setupGesture(1,INPUT_CODE);
                        ManagerGesture.getInstance().saveGesture(1, MD5.MD5generator("kulala_sign_"+INPUT_CODE));
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);
                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>"+getResources().getString(R.string.do_not_agree_with_the_last_drawing_please_draw_again)+"</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }
    protected void initEvents(){
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.gestures_password_has_been_canceled));
                ManagerGesture.getInstance().saveGesture(0, "");
                OCtrlGesture.getInstance().ccmd1311_setupGesture(0,"");
                new TimeDelayTask().runTask(1500, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);
                    }
                });
            }
        });
        mTextReset.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getContext().getString(R.string.set_gesture_pattern));
            }
        });
    }
    @Override
    public void receiveEvent(String key, Object value) {
        if(key.equals(OEventName.SETUP_GESTURE_RESULTBACK)){
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);
        }
    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
        INPUT_CODE = codeCutOne(inputCode);
    }
    private String codeCutOne(String inputCode){
        if(inputCode == null || inputCode.equals(""))return"";
        String result = "";
        for(int i=0;i<inputCode.length();i++){
            String cut = inputCode.substring(i,i+1);
            int value = Integer.parseInt(cut);
            result += String.valueOf(value-1);
        }
        return result;
    }

    @Override
    protected void invalidateUI() {

    }


    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

}
