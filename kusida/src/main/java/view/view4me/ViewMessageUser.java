package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;

import model.ManagerCommon;
import model.messageuser.DataMessageUser;
import view.view4me.set.ClipTitleMeSet;

public class ViewMessageUser extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView      txt_nomessage,txt_message,txt_time;
    private LinearLayout lin_havemessage;
    public ViewMessageUser(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_message_user, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_nomessage = (TextView) findViewById(R.id.txt_nomessage);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_time = (TextView) findViewById(R.id.txt_time);
        lin_havemessage = (LinearLayout) findViewById(R.id.lin_havemessage);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MESSAGE_USER_NEW_BACK, this);
    }

    @Override
    public void initViews() {
        handleChangeData();
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.MESSAGE_USER_NEW_BACK)) {// 删除后刷新列表
            handleChangeData();
        }
    }

    @Override
    public void callback(String key, Object value) {
//        if (key.equals("newcar")) {
//            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_newcar);
//        }
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
//        list_cars.setAdapter(null);
//        adapter = null;
        ODispatcher.removeEventListener(OEventName.MESSAGE_USER_NEW_BACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        DataMessageUser messageUserList = ManagerCommon.getInstance().messageUserList;
        if (messageUserList == null || messageUserList.content.equals("")) {
            txt_nomessage.setVisibility(VISIBLE);
            lin_havemessage.setVisibility(INVISIBLE);
        } else {
            txt_nomessage.setVisibility(INVISIBLE);
            lin_havemessage.setVisibility(VISIBLE);
            txt_message.setText(messageUserList.content);
            txt_time.setText(ODateTime.time2StringWithHH(messageUserList.createTime));
        }
    }
    // =====================================================
}
