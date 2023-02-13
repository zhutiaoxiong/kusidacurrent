package view.clip.gesture;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastInput;


import common.GlobalContext;
import ctrl.OCtrlRegLogin;
import model.ManagerCommon;
import model.ManagerGesture;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.ActivityKulalaMain;
import view.find.BasicParamCheckPassWord;
import view.view4me.set.ClipTitleMeSet;

import static view.clip.gesture.GestureEditPage.isForResetGesture;

/**
 * 手势绘制/校验界面
 */
public class GestureVerityPage extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private SmartImageView     mImgUserLogo;
    private TextView           mTextPhoneNumber;
    private TextView           mTextTip;
    private FrameLayout        mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView           mTextForget;
    private TextView           mTextOther;
    private static String INPUT_CODE = "";
    public static boolean isNeedNextToEditGesture = false;
    public static String fromPage="";

    public GestureVerityPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_gesture_verify, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        mImgUserLogo = (SmartImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextOther = (TextView) findViewById(R.id.text_other_account);

        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
    }

    protected void initViews() {
        if(isNeedNextToEditGesture){
            mTextOther.setVisibility(INVISIBLE);
        }
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if(user == null)return;
        if(user.avatarUrl == null || user.avatarUrl.equals("") || user.avatarUrl.length() <10){
        }else {
            mImgUserLogo.setImageUrl(user.avatarUrl);
        }
        mTextPhoneNumber.setText(user.phoneNum);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(getContext(), false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        INPUT_CODE = codeCutOne(inputCode);
                        String md5Code = MD5.MD5generator("kulala_sign_"+INPUT_CODE);
                        String localCode = ManagerGesture.getInstance().getSignPasswordGesture();
                        if(md5Code.equals(localCode)){//密码正确
                            mGestureContentView.clearDrawlineState(0L);
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_is_correct));
                            if(isNeedNextToEditGesture){
                                isNeedNextToEditGesture = false;
                                isForResetGesture = true;
                                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.clip_gesture_edit);
                            }else {
                                if(fromPage.equals("viewLogin")){
                                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, 0);
                                }else{
                                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, ActivityKulalaMain.POP_VIEW_NORMAL);
                                }
                            }
                        }else{
                            mGestureContentView.clearDrawlineState(1300L);
                            mTextTip.setVisibility(View.VISIBLE);
                            mTextTip.setText(Html.fromHtml("<font color='#B03125'>"+getResources().getString(R.string.password_mistake)+"</font>"));
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            mTextTip.startAnimation(shakeAnimation);
                        }
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

    protected void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(fromPage.equals("viewLogin")){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_login);
                }else if(fromPage.equals("kulalaMain")){
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("系统提示")
                            .withInfo("确定要退出系统吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) GlobalContext.getCurrentActivity().finish();
                                }
                            }).show();
                }else if(fromPage.equals("viewSafety")){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
                }
            }
        });
        mTextForget.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastInput.getInstance().showInput(title_head,getResources().getString(R.string.forget_gesture_code),getResources().getString(R.string.please_enter_a_password), new String[]{OToastInput.PASS}, "forgetGesturePass", GestureVerityPage.this);
            }
        });
        mTextOther.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(fromPage.equals("viewLogin")){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_login);
                }else{
                   ManagerCommon.getInstance().exitToLogin("");
                }
            }
        });
    }
    @Override
    public void callback(String key, Object value) {
        if (key.equals("forgetGesturePass")) {
            JsonObject obj  = (JsonObject) value;
            String     pass = OJsonGet.getString(obj, OToastInput.PASS);
            BasicParamCheckPassWord.isFindMain=2;
            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
        }
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {//忘记手势密码
            boolean check = (Boolean) paramObj;
            if (check) {
                if(BasicParamCheckPassWord.isFindMain==2){
                    if(isNeedNextToEditGesture){
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.password_is_correct));
                        isNeedNextToEditGesture = false;
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.clip_gesture_edit);
                    }else {
                        if(fromPage.equals("viewLogin")){
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, 0);
                        }else{
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
                        }
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_modify_gesture_password_as_soon_as_possible));
                    }
                }
            }
        }
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }


}
