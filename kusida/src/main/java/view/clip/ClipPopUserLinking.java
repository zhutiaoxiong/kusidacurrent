package view.clip;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;
import com.kulala.staticsview.toast.OPopConfirm;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.staticsview.toast.OToastInput;
import com.tencent.tauth.TencentCommon;

import java.util.List;

import adapter.AdapterUserList;
import common.GlobalContext;
import common.global.OWXShare;
import ctrl.OCtrlAuthorization;
import model.ManagerAuthorization;
import model.ManagerCommon;
import model.common.DataShare;
import model.loginreg.DataUser;
import view.view4app.ViewCodriverAuthorization;
import view.view4me.set.ClipTitleMeSet;

public class ClipPopUserLinking  implements OEventObject,OCallBack {
    private PopupWindow     popContain;//弹出管理
    private View            parentView;//本对象显示
    private Context         context;

    private RelativeLayout thisView;
    private ClipTitleMeSet title_head;
    private ListView        list_users;
    private AdapterUserList adapter;
    private int             selectedPos;
    private DataUser        selectedUser;
    public static boolean isForSelect = false;
    private LinearLayout lin_shows;
    private TextView btn_cancel;
    private TextView btn_sure;
    private RelativeLayout shareLayout;
    private LinearLayout li1;
    private LinearLayout li2;
    private LinearLayout li3;
    private LinearLayout li4;
    private TextView txt_cancel;


    private        String             mark;//选择标记
    private        OCallBack          callback;
    private MyHandler handler = new MyHandler();
    // ========================out======================
    private static ClipPopUserLinking _instance;
    public static ClipPopUserLinking getInstance() {
        if (_instance == null)
            _instance = new ClipPopUserLinking();
        return _instance;
    }
    //===================================================
    public void show(boolean isForSelect,View parentView,String mark,OCallBack callback) {
        this.isForSelect = isForSelect;
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout)layoutInflater.inflate(R.layout.view_app_userlist, null);
        title_head = (ClipTitleMeSet) thisView.findViewById(R.id.title_head);
        list_users = (ListView) thisView.findViewById(R.id.list_users);
        btn_sure=  thisView.findViewById(R.id.btn_sure);
        btn_cancel=  thisView.findViewById(R.id.btn_cancel);
        lin_shows=  thisView.findViewById(R.id.lin_shows);
        shareLayout=  thisView.findViewById(R.id.sharelayout);
        li1=  thisView.findViewById(R.id.li1);
        li2=  thisView.findViewById(R.id.li2);
        li3=  thisView.findViewById(R.id.li3);
        li4=  thisView.findViewById(R.id.li4);
        txt_cancel=  thisView.findViewById(R.id.txt_cancel);
        if(isForSelect){
            title_head.setTitle("选择或添加被授权好友");
        }else{
            title_head.setTitle("好友列表");
        }
        initView();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USERLIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE, this);
    }
    public void initView(){
        OCtrlAuthorization.getInstance().ccmd1210_getuserlist();
    }
    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
    }
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
            }
        });
        // new
        title_head.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                OToastOMG.getInstance().showInput(context, "新增联系人", "请输入联系人手机号", new String[]{OToastOMG.PHONE}, "phoneNum", ClipPopUserLinking.this);
                OToastButton.getInstance().show(parentView,new String[]{GlobalContext.getContext().getResources().getString(R.string.choose_from_a_local_contact),GlobalContext.getContext().getResources().getString(R.string.manually_enter_contacts)},"addContact",ClipPopUserLinking.this);
            }
        });
        list_users.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
                selectedPos = position;
                selectedUser = adapter.getItem(position);
                adapter.setCurrentItem(position);
                if(isForSelect){
                    //                    callback.callback(mark,selectedUser);
                    ViewCodriverAuthorization.selectUsaer=selectedUser;
                    popContain.dismiss();
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver_authorization);
                }else{
                    OToastButton.getInstance().show(parentView, new String[]{GlobalContext.getContext().getResources().getString(R.string.delete_the_contact),GlobalContext.getContext().getResources().getString(R.string.make_a_phone_call),GlobalContext.getContext().getResources().getString(R.string.change_nickname)}, "operate", ClipPopUserLinking.this);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_shows.setVisibility(View.GONE);
                shareLayout.setVisibility(View.VISIBLE);
            }
        });
        li1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
                if(shareInfo!=null){
                    OWXShare.ShareFriendURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
                    shareLayout.setVisibility(View.GONE);
                }
            }
        });
        li2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
                if(shareInfo!=null){
                    OWXShare.ShareTimeLineURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
                    shareLayout.setVisibility(View.GONE);
                }
            }
        });
        li3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
                if(shareInfo!=null){
                    TencentCommon.toTencent(GlobalContext.getCurrentActivity(),shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl,0,shareInfo.sharePic);
                    shareLayout.setVisibility(View.GONE);
                }
            }
        });
        li4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
                if(shareInfo!=null){
                    TencentCommon.toTencent(GlobalContext.getCurrentActivity(),shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl,1,shareInfo.sharePic);
                    shareLayout.setVisibility(View.GONE);
                }
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLayout.setVisibility(View.GONE);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_shows.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.AUTHORIZATION_USERLIST_RESULTBACK)) {
            String error = (String) paramObj;
            if (error.equals(""))
                handleChangeData();
        }else  if (eventName.equals(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE)) {
            boolean haserror = (Boolean) paramObj;
            if(haserror){
                handleShowNoYonghu();
            }else{
                handleChangeData();
            }
        }
    }
    @Override
    public void callback(String key, Object value) {
        if (key.equals("addContact")) {
            String addOpp=(String)value;
            if(addOpp.equals(GlobalContext.getContext().getResources().getString(R.string.manually_enter_contacts))){
//                OToastInput.getInstance().showInput(parentView, context.getResources().getString(R.string.add_contact), context.getResources().getString(R.string.please_enter_the_contact_number_of_hands), new String[]{OToastInput.PHONE}, "phoneNum", ClipPopUserLinking.this);
                OToastInput.getInstance().showInput(parentView, context.getResources().getString(R.string.add_contact), "两个或者以上输入框", new String[]{context.getResources().getString(R.string.please_enter_the_contact_number_of_hands), "请输入备注(非必填)"}, new String[]{OToastInput.PHONE,OToastInput.OTHER_TEXT}, "phoneNum", ClipPopUserLinking.this);
            }else if(addOpp.equals(GlobalContext.getContext().getResources().getString(R.string.choose_from_a_local_contact))){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionReadContacts =GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    //拔打电话权限||
                    if (permissionReadContacts != PackageManager.PERMISSION_GRANTED) {
                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                    }else {
                        ClipPopUserLocal.getInstance().show(false,parentView,"",ClipPopUserLinking.this);
                    }
                }else{
                    ClipPopUserLocal.getInstance().show(false,parentView,"",ClipPopUserLinking.this);
                }
            }
//            switch(addOpp) {
//                case "手动输入联系人":
//                    OToastInput.getInstance().showInput(parentView, context.getResources().getString(R.string.add_contact), context.getResources().getString(R.string.please_enter_the_contact_number_of_hands), new String[]{OToastInput.PHONE}, "phoneNum", ClipPopUserLinking.this);
//                    break;
//                case "从本地联系人中选择":
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        int permissionReadContacts =GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
//                        //拔打电话权限||
//                        if (permissionReadContacts != PackageManager.PERMISSION_GRANTED) {
//                            GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
//                        }else {
//                            ClipPopUserLocal.getInstance().show(false,parentView,"",ClipPopUserLinking.this);
//                        }
//                    }else{
//                        ClipPopUserLocal.getInstance().show(false,parentView,"",ClipPopUserLinking.this);
//                    }
//                    break;
//            }
        } else if (key.equals("operate")) {
            // "删除联系人","拔打电话"
            String opp = (String) value;
            if (opp.equals(context.getResources().getString(R.string.delete_the_contact))) {
                OPopConfirm.getInstance().show(parentView,"deletePeopleOrCancle",ClipPopUserLinking.this,"删除联系人","你将要删除："+ selectedUser.name + ":" + selectedUser.phoneNum + "确定吗?");
//                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//                        .withInfo("删除联系人", "你将要删除："+ selectedUser.name + ":" + selectedUser.phoneNum + "确定吗?", 0)
//                        .withButton("取消", "开启")
//                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                            @Override
//                            public void onClickConfirm(boolean isClickConfirm) {
//                                if (isClickConfirm) {
//                                    OCtrlAuthorization.getInstance().ccmd1212_deleteuser(selectedUser.userId);
//                                }
//                            }
//                        }).show();
            }else if(opp.equals(context.getResources().getString(R.string.make_a_phone_call))){
                ODispatcher.dispatchEvent(OEventName.CALL_MY_PHONE,selectedUser.phoneNum);
            }else if(opp.equals(context.getResources().getString(R.string.change_nickname))){
                OToastInput.getInstance().showInput(parentView,context.getResources().getString(R.string.change_nickname), context.getResources().getString(R.string.please_enter_the_nickname), new String[]{OToastInput.OTHER_TEXT}, "modificationNickName", ClipPopUserLinking.this);
            }
        }else if(key.equals("phoneNum")){
            JsonObject ooo = (JsonObject) value;
            String phone = OJsonGet.getString(ooo, OToastInput.PHONE);
            String nickName = OJsonGet.getString(ooo, OToastInput.OTHER_TEXT);
            if (!phone.equals("")) {
//                if (OInputValidation.chkInputPhoneNum(phone)) {
                if(TextUtils.isEmpty(nickName)){
                    OCtrlAuthorization.getInstance().ccmd1211_findnewuser(phone,"");
                }else{
                    OCtrlAuthorization.getInstance().ccmd1211_findnewuser(phone,nickName);
                }
//                } else {
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,context.getResources().getString(R.string.you_enter_the_phone_number_format_error));
//                }
            }

        }else if(key.equals("modificationNickName")){
            JsonObject jjj = (JsonObject) value;
            String newnickName = OJsonGet.getString(jjj, OToastInput.OTHER_TEXT);
            if(newnickName == null || newnickName.equals("")){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,context.getResources().getString(R.string.nickname_cannot_be_empty));
            }else {
                OCtrlAuthorization.getInstance().ccmd1211_findnewuser(selectedUser.phoneNum, newnickName);
            }
        }else if(key.equals("deletePeopleOrCancle")){
            String opp = (String) value;
            if(opp.equals("ImSureNow")){
                OCtrlAuthorization.getInstance().ccmd1212_deleteuser(selectedUser.userId);
            }
        }
    }

    public void invalidateUI(){
        List<DataUser> list = ManagerAuthorization.getInstance().userList;
        if (list == null)
            return;
        adapter = new AdapterUserList(context, list, R.layout.list_item_head_2string);
        list_users.setAdapter(adapter);
    }
    public void handleChangeData() {
        Message message = new Message();
        message.what = 311;
        handler.sendMessage(message);
    }
    public void handleShowNoYonghu() {
        Message message = new Message();
        message.what = 313;
        handler.sendMessage(message);
    }
    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 311 :
                    invalidateUI();
                    break;
                case 313 :
                    lin_shows.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

}

