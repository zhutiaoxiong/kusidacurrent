package view.view4control;

import android.content.Context;
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

import common.blue.BlueLinkNetSwitch;
import model.ManagerCarList;
import model.carlist.DataCarInfo;


public class OToastSelectBlueOrNet {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private RelativeLayout thisView;
    private Context        context;

    private TextView txt_wifi, txt_bluetooth, txt_cancel;
    private ImageView img_wifi, img_bluetooth;
    private View touch_exit;

    private        OnChooseListener      callback;
    // ========================out======================
    private static OToastSelectBlueOrNet _instance;

    public static OToastSelectBlueOrNet getInstance() {
        if (_instance == null)
            _instance = new OToastSelectBlueOrNet();
        return _instance;
    }

    //===================================================
    public void show(View parentView) {
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_blue_net_select, null);

        txt_wifi = (TextView) thisView.findViewById(R.id.txt_wifi);
        txt_bluetooth = (TextView) thisView.findViewById(R.id.txt_bluetooth);
        txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancel);
        touch_exit = (View) thisView.findViewById(R.id.touch_exit);
        img_wifi = (ImageView) thisView.findViewById(R.id.img_wifi);
        img_bluetooth = (ImageView) thisView.findViewById(R.id.img_bluetooth);

        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        if(carInfo == null){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,context.getResources().getString(R.string.no_car));
            popContain.dismiss();
            return;
        }
        initView(carInfo);
        initViews();
        initEvents();
    }

    public void initView(DataCarInfo carInfo) {
        if (!BlueLinkNetSwitch.getIsNetModel(carInfo.ide)) {
            img_bluetooth.setVisibility(View.VISIBLE);
            img_wifi.setVisibility(View.INVISIBLE);
        } else {
            img_bluetooth.setVisibility(View.INVISIBLE);
            img_wifi.setVisibility(View.VISIBLE);
        }
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }


    public void initEvents() {
        touch_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popContain.dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popContain.dismiss();
            }
        });
        txt_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosedExit(false);
            }
        });
        img_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosedExit(false);
            }
        });
        txt_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosedExit(true);
            }
        });
        img_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosedExit(true);
            }
        });
    }

    private void choosedExit(boolean selectBlue) {
        if (callback != null) callback.OnChoose(selectBlue);
        popContain.dismiss();
        callback = null;
        parentView = null;
        thisView = null;
        context = null;
    }

    public interface OnChooseListener {
        void OnChoose(boolean isBluetooth);
    }

    public void setonChooseListener(OnChooseListener listener) {
        this.callback = listener;
    }
}

