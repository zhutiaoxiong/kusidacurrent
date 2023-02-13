package view.loginreg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.List;

import adapter.AdapterForFindPassWord;
import common.GlobalContext;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.answer.DataFindway;
import view.view4me.set.ClipTitleMeSet;


/**
 * Created by qq522414074 on 2016/8/8.
 */
public class ViewResetPassWord extends LinearLayoutBase {
    private       ClipTitleMeSet          title_head;
    public static String                 cachepheNum;
    private       ListView               resetpassword_listview;
    private       AdapterForFindPassWord adapter;
    private       List<DataFindway>      list;
    private       TextView               xiaotishi, selectfindway;


    public ViewResetPassWord(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_reset_password_logreg, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.view_reset_password_titlehead);
        resetpassword_listview = (ListView) findViewById(R.id.view_loginreg_resetpassword_listview);
        xiaotishi = (TextView) findViewById(R.id.small_tishi);
        selectfindway = (TextView) findViewById(R.id.police_select_way);
        initViews();
        initEvents();
//        ODispatcher.addEventListener(OEventName.SECRETINFOS_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
//        OCtrlAnswer.getInstance().ccmd1119_answer(ViewResetPassWord.cachepheNum,1);
    }

    @Override
    protected void initViews() {
        title_head.setTitle(getResources().getString(R.string.reset_password));
        selectfindway.setText(getResources().getString(R.string.please_choose_the_following_method_to_reset_your_password));
        list = ManagerAnswer.getInstance().secretTypeslist;
        handleChangeData();
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_username);
            }
        });

        resetpassword_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position).ide == 1) {
                    ViewLoginRegSubmmitPassWord.isPhoneComeHere = R.layout.view_loginreg_phone_resetpassword;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_phone_resetpassword);
                } else if (adapter.getItem(position).ide == 2) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("提示")
                            .withInfo("将发送修改密码的链接到您绑定邮箱，确定吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) {
                                        OCtrlRegLogin.getInstance().ccmd1114_findpassFromMail(ManagerAnswer.phoneNum, 3);
                                    }
                                }
                            })
                            .show();
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW,R.layout.view_loginreg_phone_resetpassword);
                } else if (adapter.getItem(position).ide == 3) {
                    ViewLoginRegSubmmitPassWord.isPhoneComeHere = R.layout.view_answer_get_for_login;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_answer_get_for_login);
                }
            }

        });
    }

    @Override
    public void callback(String s, Object o) {
    }

    @Override
    protected void invalidateUI() {
        adapter = new AdapterForFindPassWord(list, this.getContext());
        resetpassword_listview.setAdapter(adapter);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.FINDPASS_MAIL_RESULTBACK)) {
            String mess = (String) o;
            if (mess == null || mess.equals("")) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.we_have_sent_an_email_to_your_mailbox_please_pay_attention_to_check));
            } else {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("提示")
                        .withInfo(mess)
                        .withButton("","确定")
                        .show();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
//        ODispatcher.removeEventListener(OEventName.SECRETINFOS_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
        super.onDetachedFromWindow();
    }
}
