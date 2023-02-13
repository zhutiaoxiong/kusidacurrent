package view.clip;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.titlehead.ClipTitleHead;

import adapter.AdapterPickAuthor;
import model.common.DataAuthoredUser;

public class
ClipPopAuthorDetail{
    private       PopupWindow      popContain;//弹出管理
    private       View             parentView;//本对象显示
    private       LinearLayout     thisView;
    private       Context          context;
    private       String           mark;//选择标记
    private       OCallBack        callback;
    public static DataAuthoredUser selectedUser;

    private ClipTitleHead   title_head;
    private ClipLineBtnInptxt txt_nickname, txt_phone, txt_carname;
    private        TextView            txt_time;
    private        ListView            list_authors;
    private        AdapterPickAuthor   authorAdapter;
    // ========================out======================
    private static ClipPopAuthorDetail _instance;

    public static ClipPopAuthorDetail getInstance() {
        if (_instance == null)
            _instance = new ClipPopAuthorDetail();
        return _instance;
    }

    //===================================================
    public void show(View parentView, String mark, OCallBack callback) {
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (LinearLayout) layoutInflater.inflate(R.layout.clip_pop_author_detail, null);
        title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
        txt_nickname = (ClipLineBtnInptxt) thisView.findViewById(R.id.txt_nickname);
        txt_phone = (ClipLineBtnInptxt) thisView.findViewById(R.id.txt_phone);
        txt_carname = (ClipLineBtnInptxt) thisView.findViewById(R.id.txt_carname);
        txt_time = (TextView) thisView.findViewById(R.id.txt_time);
        list_authors = (ListView) thisView.findViewById(R.id.list_authors);
        initView();
        initViews();
        initEvents();
    }

    private void initView() {
        if (selectedUser == null) return;
        txt_nickname.setText(selectedUser.userinfo.name);
        txt_phone.setText(selectedUser.userinfo.phoneNum);
        //carinfo是null，需要parent给初值
        txt_carname.setText(selectedUser.carinfo.num);
        String time = context.getResources().getString(R.string.from)+ ODateTime.time2StringWithHH(selectedUser.startTime) + context.getResources().getString(R.string.to) + ODateTime.time2StringWithHH(selectedUser.endTime);
        txt_time.setText(time);
        authorAdapter = new AdapterPickAuthor(context, selectedUser.authors, R.layout.list_item_name_check_pair);
        list_authors.setAdapter(authorAdapter);
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
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//                selectedUser = null;
//                authorAdapter = null;
//                parentView = null;
//                thisView = null;
                context = null;
                callback = null;
//                popContain = null;
                popContain.dismiss();
            }
        });
    }
}

