package view.safetyneed;

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

import common.GlobalContext;
import ctrl.OCtrlAnswer;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.ManagerLoginReg;
import model.answer.DataFindway;
import model.safety.DataSafeTy;
import view.view4me.set.ClipTitleMeSet;


/**
 * Created by qq522414074 on 2016/10/27.
 */
public class ViewSafetyResetItem extends LinearLayoutBase{
    private ClipTitleMeSet title_head;
    private ListView listView;
    private AdapterChooseWay adapter;
    private List<DataFindway> list;
    private TextView choose_way;
    public ViewSafetyResetItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_safety_resetitem, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        listView = (ListView) findViewById(R.id.listview);
        choose_way = (TextView) findViewById(R.id.choose_way);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
        OCtrlAnswer.getInstance().ccmd1119_answer(ManagerLoginReg.getInstance().getCurrentUser().phoneNum, 1);
    }

    @Override
    protected void initViews() {
        String txt = "";
        String txt1 = "";
        switch (DataSafeTy.from) {
            //2：修改手机号，3：重设密码，4：修改邮箱，5：修改安全问题
            case 2:
                txt = getContext().getResources().getString(R.string.please_choose_the_following_method_to_reset_your_phone_number);
                txt1 = getContext().getResources().getString(R.string.reset_password);
                break;
            case 3:
                txt = getContext().getResources().getString(R.string.please_choose_the_following_method_to_reset_your_password);
                txt1 = getContext().getResources().getString(R.string.reset_password);;
                break;
            case 4:

                txt = getContext().getResources().getString(R.string.please_choose_the_following_method_to_reset_your_aderess);
                txt1 = getContext().getResources().getString(R.string.reset_password);
                break;
            case 5:
                txt = getContext().getResources().getString(R.string.please_choose_the_following_method_to_reset_your_security_issues);;
                txt1 = getContext().getResources().getString(R.string.reset_password);
                break;
        }
        choose_way.setText(txt);
        title_head.setTitle(txt1);
        handleChangeData();
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position).ide == 1) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_reset_byphone);
                } else if (adapter.getItem(position).ide == 2) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("提示")
                            .withInfo("将发送修改密码的链接到您绑定邮箱，确定吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) {
                                        OCtrlRegLogin.getInstance().ccmd1114_findpassFromMail(ManagerLoginReg.getInstance().getCurrentUser().phoneNum, DataSafeTy.from);
                                    }
                                }
                            })
                            .show();
                } else if (adapter.getItem(position).ide == 3) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_reset_check_reply);
                }
            }

        });
    }

    @Override
    public void callback(String s, Object o) {}

    @Override
    protected void invalidateUI() {
        adapter = new AdapterChooseWay(list, this.getContext());
        listView.setAdapter(adapter);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.SECRETINFOS_RESULTBACK)) {
            list = ManagerAnswer.getInstance().secretTypeslist;
            handleChangeData();

        } else if (s.equals(OEventName.FINDPASS_MAIL_RESULTBACK)) {
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
        ODispatcher.removeEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.FINDPASS_MAIL_RESULTBACK, this);
        super.onDetachedFromWindow();
    }
}
