package view.view4app.codriver;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastButton;


import java.util.List;

import adapter.AdapterUserListCodriver;
import common.GlobalContext;
import ctrl.OCtrlAuthorization;
import model.ManagerAuthorization;
import model.common.DataAuthoredUser;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by Administrator on 2016/10/15.
 * 已授权副车主
 */
public class ViewCodriverList extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private ListView                list_users;
    private AdapterUserListCodriver adapter;
    private DataAuthoredUser        selectedUser;
    public ViewCodriverList(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_codriverlist, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        list_users = (ListView) findViewById(R.id.list_users);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED, this);
    }

    public void initViews() {
        OCtrlAuthorization.getInstance().ccmd1208_getAuthorUserlist();
    }


    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_app_codriver);
                adapter = null;
            }
        });
        list_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                selectedUser = adapter.getItem(position);
                adapter.setCurrentItem(position);
                OToastButton.getInstance().show(title_head, new String[]{getResources().getString(R.string.stop_the_authorization),getResources().getString(R.string.see_details)}, "operate", ViewCodriverList.this);
            }
        });
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK)) {
            handleChangeData();
        }else if (eventName.equals(OEventName.AUTHORIZATION_USER_STOPED)) {
            OCtrlAuthorization.getInstance().ccmd1208_getAuthorUserlist();
        }
    }
    @Override
    public void callback(String key, Object value) {
        if(key.equals("operate")){
            //"停止授权","查看详情"
            String opp = (String)value;
            if(opp.equals(getResources().getString(R.string.stop_the_authorization))){
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("取消副车主授权？")
                        .withInfo("你确定要取消"+selectedUser.carinfo.num+" "+selectedUser.userinfo.name+" "+selectedUser.userinfo.phoneNum+"的授权吗?")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm){
                                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.have_to_cancel_the_authorization));
                                   long carId= selectedUser.carinfo.ide;
                                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(selectedUser.authorityId, selectedUser.userinfo.userId,carId);

                                }
                            }
                        })
                        .show();
            }else if(opp.equals(getResources().getString(R.string.see_details))){
//                ClipPopAuthorDetail.selectedUser =selectedUser;
//                ClipPopAuthorDetail.getInstance().show(title_head,"",this);
                ActivityAuthorDetails.selectedUser=selectedUser;
                Intent intent=new Intent();
                intent.setClass(getContext(),ActivityAuthorDetails.class);
                getContext().startActivity(intent);
            }
        }
    }

    public void invalidateUI(){
        List<DataAuthoredUser> list  = ManagerAuthorization.getInstance().userListAuthored;
        if (list == null){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.you_have_not_set_up_deputy_owner));
            return;
        }
        adapter = new AdapterUserListCodriver(getContext(), list, R.layout.list_item_head_2string);
        list_users.setAdapter(adapter);
    }
}