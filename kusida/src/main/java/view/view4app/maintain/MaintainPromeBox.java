package view.view4app.maintain;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import common.GlobalContext;
import ctrl.OCtrlCar;
import model.maintain.DataMaintain;

/**
 * Created by qq522414074 on 2016/11/6.
 */

public class MaintainPromeBox {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private Context        context;
    private RelativeLayout thisView;

    private TextView txt_title, txt_text, btn_cancel, btn_confirm;
    private ImageView img_splitline, view_background;
    private MaintainPromeBoxData data;
    private boolean isShowing = false;

    private        MaintainPromeBox.MyHandler handler;
    private        DataMaintain               maintain;
    // ========================out======================
    private static MaintainPromeBox           _instance;

    public static MaintainPromeBox getInstance() {
        if (_instance == null)
            _instance = new MaintainPromeBox();
        return _instance;
    }

    //===================================================
    public void show(View parentView, MaintainPromeBoxData data) {
        this.data = data;
//        maintain = ManagerMaintainList.getInstance().maintain;
        maintain = ViewMaintain.maintain;
        if (isShowing) return;
        if (data == null) return;
        if (data.title == null) return;
        if (data.info == null) return;
        isShowing = true;
        if (handler == null) handler = new MaintainPromeBox.MyHandler();
        MaintainPromeBox.this.parentView = parentView;
        context = GlobalContext.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_confirm, null);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);
        txt_text = (TextView) thisView.findViewById(R.id.txt_text);
        btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) thisView.findViewById(R.id.btn_confirm);
        img_splitline = (ImageView) thisView.findViewById(R.id.img_splitline);
        view_background = (ImageView) thisView.findViewById(R.id.view_background);

        txt_title.setText(data.title);
        txt_text.setText(data.info);
        switch (data.mark) {
            case 1:
                btn_cancel.setText(context.getResources().getString(R.string.i_know));
                btn_confirm.setText(context.getResources().getString(R.string.check));
                break;
            case 2:
                btn_cancel.setText(context.getResources().getString(R.string.remind_me_later));
                btn_confirm.setText(context.getResources().getString(R.string.i_know));
                break;
            case 3:
                btn_cancel.setText(context.getResources().getString(R.string.remind_me_later));
                btn_confirm.setText(context.getResources().getString(R.string.have_to_maintain));
                break;
            case 4:
                btn_cancel.setText(context.getResources().getString(R.string.cancle));
                btn_confirm.setText(context.getResources().getString(R.string.delete));
                break;
            case 5:
                btn_cancel.setText(context.getResources().getString(R.string.cancle));
                btn_confirm.setText(context.getResources().getString(R.string.has_been_completed));
                break;
        }
        initViews();
        initEvents();
    }

    private void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //禁用点击穿透,开启就是false,false,true
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    private void initEvents() {

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maintain == null || data == null) {
                    exit();
                    return;
                }
                switch (data.mark) {
                    case 1:
                        OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 1);
                        break;
                    case 2:
                        OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 2);
                        break;
                    case 3:
//                        ODispatcher.dispatchEvent(OEventName.WAKE_UP_ME_LATE);
                        OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 2);
                        break;
                }
                exit();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maintain == null || data == null) {
                    exit();
                    return;
                }
                switch (data.mark) {
                    case 1:
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_maintance_remind_me);
                        break;
                    case 2:
                        OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 3);
                        break;
                    case 3:
                        OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 3);
                        break;
                    case 4:
                        ODispatcher.dispatchEvent(OEventName.SHANCHU_BAOYANG);
                        break;
                    case 5:
                        ODispatcher.dispatchEvent(OEventName.BAOYANG_WANCHNEG);
                        break;
                }
                exit();
            }
        });
    }

    private void exit() {
        isShowing = false;
        if (popContain == null) return;
        popContain.dismiss();
        data = null;
        parentView = null;
        thisView = null;
        context = null;
    }

class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 16596:
                break;
        }
    }
}
}
