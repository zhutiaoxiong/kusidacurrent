package view.loginreg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.OJsonGet;

import ctrl.OCtrlRegLogin;
import model.loginreg.DataUser;
import view.clip.ClipLineBtnInptxt;
import view.clip.ClipPopChooseHead;
import view.view4me.set.ClipTitleMeSet;

public class ViewFinshInfo extends LinearLayoutBase{
    private ClipTitleMeSet title_head;
    private ClipLineBtnInptxt txt_head, txt_nickname, txt_mail;
    private Button btn_confirm;
    private DataUser cacheUser;

    public ViewFinshInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_finshinfo, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_head = (ClipLineBtnInptxt) findViewById(R.id.txt_head);
        txt_nickname = (ClipLineBtnInptxt) findViewById(R.id.txt_nickname);
        txt_mail = (ClipLineBtnInptxt) findViewById(R.id.txt_mail);
//        txt_idcard = (ClipLineBtnInptxt) findViewById(R.id.txt_idcard);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
    }

    @Override
    public void initViews() {
        cacheUser = new DataUser();
        handleChangeData();
    }

    @Override
    public void initEvents() {
        txt_head.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ClipPopChooseHead.getInstance().show(title_head, ViewFinshInfo.this);
            }
        });
        txt_nickname.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OToastInput.getInstance().showInput(title_head, getResources().getString(R.string.enter_the_nickname), getResources().getString(R.string.please_enter_the_nickname), new String[]{OToastInput.OTHER_TEXT},
                        "nickname", ViewFinshInfo.this);
            }
        });
        txt_mail.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OToastInput.getInstance().showInput(title_head, getResources().getString(R.string.enter_the_email), "", new String[]{
                        OToastInput.NEW_EMAIL, OToastInput.REPT_EMAIL}, "mail", ViewFinshInfo.this);
            }
        });
//        txt_idcard.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View view) {
//                OToastOMG.getInstance().showInput(getContext(), "输入身份证号", "", new String[]{OToastOMG
//                        .IDCARD, OToastOMG.REPT_IDCARD}, "idcard", ViewFinshInfo.this);
//            }
//        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (cacheUser.avatarUrl.equals("0")) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_select_a_picture));
                } else if (cacheUser.name.equals("")) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_enter_a_nickname_simbol));
                } else if (cacheUser.email.equals("")) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_input_binding_mailbox));
                }else {
                    OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(cacheUser.toJsonObject());
                }
//                else if (cacheUser.email.equals("") && cacheUser.idCard.equals("")) {
//                    OToastOMG.getInstance().showText("身份证号或邮箱至少填写一项!");
//                }
            }
        });
    }

    @Override
    public void callback(String key, Object obj) {
        if (key.equals("chooseHead")) {
            int respos = (Integer) obj;
            cacheUser.avatarUrl = String.valueOf(respos);
        } else if (key.equals("nickname")) {
            JsonObject ooo = (JsonObject) obj;
            String nick = OJsonGet.getString(ooo, OToastInput.OTHER_TEXT);
            cacheUser.name = nick;
//            OCtrlRegLogin.getInstance().ccmd1110_changeUserNickHead(nick, user.avatarUrl);
        } else if (key.equals("mail")) {
            JsonObject ooo = (JsonObject) obj;
            String mail = OJsonGet.getString(ooo, OToastInput.NEW_EMAIL);
            String mailrep = OJsonGet.getString(ooo, OToastInput.REPT_EMAIL);
            if (!mail.equals(mailrep)) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.two_information_input_please_input_again));
            } else {
                cacheUser.email = mail;
            }
        } else if (key.equals("idcard")) {
            JsonObject ooo = (JsonObject) obj;
            String idcard = OJsonGet.getString(ooo, OToastInput.IDCARD);
            String idcardrep = OJsonGet.getString(ooo, OToastInput.REPT_IDCARD);
            if (!idcard.equals(idcardrep)) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.two_information_input_please_input_again));
            } else {
                cacheUser.idCard = idcard;
            }
        }
        handleChangeData();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CHANGE_USER_INFO_OK)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.modify_the_success));
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,0);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        if(!cacheUser.avatarUrl.equals("0")){
            txt_head.setText("");
            txt_head.setRightImage(DataUser.getUserHeadResId(cacheUser.avatarUrl));
        }
        if (!cacheUser.name.equals("")) txt_nickname.setText(cacheUser.name);
        if (!cacheUser.email.equals("")) txt_mail.setText(cacheUser.email);
//        if (!cacheUser.idCard.equals("")) txt_idcard.setText(cacheUser.idCard);
    }
    // =====================================================
}
