package view.view4app.maintain;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.maintain.ManagerMaintainList;
import view.view4info.card.ClipPopCardGive;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/31.
 */
public class ViewAddMaintain extends LinearLayoutBase {
    private TextView txt_right, maintain_mileage_select, maintain_time_select, maintain_car_select;
    private LinearLayout layout_1, layout_2, layout_3;
    private String time = "", mileage = "";
    private static String thehahTime, themime;
    private ClipTitleMeSet titleHead;

    public ViewAddMaintain(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_add_maintain, this, true);
        txt_right = (TextView) findViewById(R.id.txt_right);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        titleHead = (ClipTitleMeSet) findViewById(R.id.title_head);
        maintain_mileage_select = (TextView) findViewById(R.id.maintain_mileage_select);
        maintain_time_select = (TextView) findViewById(R.id.maintain_time_select);
        maintain_car_select = (TextView) findViewById(R.id.maintain_car_select);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MODIFICATION_MAINTAINLIST_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR, this);
        ODispatcher.addEventListener(OEventName.SELECT_CAR_BACKPPPPP, this);
    }

    @Override
    protected void initViews() {
            maintain_car_select.setText(ManagerCarList.getInstance().getCurrentCar().num);
        maintain_mileage_select.setText(getResources().getString(R.string.maintain_mileage_select));
        maintain_time_select.setText(getResources().getString(R.string.maintain_time_select));
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_maintance_remind_me);
            }
        });
        layout_1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OToastButton.getInstance().show(titleHead, new String[]{"5000km", "8000km", "10000km"}, "selectMaintainmillage", ViewAddMaintain.this);
            }
        });
        layout_2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OToastButton.getInstance().show(titleHead, new String[]{getResources().getString(R.string.three_months),getResources().getString(R.string.six_months),getResources().getString(R.string.twelve_months)}, "selectMaintainTime", ViewAddMaintain.this);
            }
        });
        layout_3.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ClipMaintainChooseCar.getInstance().show(titleHead, new String[]{""}, getResources().getString(R.string.the_vehicle_list), "selectcar", ViewAddMaintain.this);
            }
        });
        txt_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (ManagerCarList.getInstance().getCurrentCar().isActive == 0) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.your_car_is_not_active_please_activate_the_vehicle));
                } else {
                    if (mileage.equals("") || time.equals("")) return;
                    int thetime = 0, themileage = 0;
                    String subMile = "", subTime = "";
                    subMile = mileage.substring(0, mileage.length() - 2);
                    themileage = Integer.parseInt(subMile);
                    subTime = time.substring(0, time.length() - 2);
                    thetime = Integer.parseInt(subTime);
                    OCtrlCar.getInstance().ccmd1228_CarMaintainAddModification(ManagerCarList.getInstance().getCurrentCar().ide, themileage, thetime, 0);
                }
            }
        });
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.HTTP_CONN_ERROR)) {
            int result = (Integer) o;
            if (result == 1408 || result == 1409 || result == 1228) {
                handleModicationMantainResult(false);
            }
        } else if (s.equals(OEventName.MODIFICATION_MAINTAINLIST_RESULT_BACK)) {
            ManagerMaintainList.getInstance().savehasMaintance(1);
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_maintance_remind_me);
            handleModicationMantainResult(true);
        } else if (s.equals(OEventName.SELECT_CAR_BACKPPPPP)) {
            mileage = ManagerMaintainList.getInstance().mileage;
            time = ManagerMaintainList.getInstance().mainTaintime;
            handleSetMileAndTime();
        }
    }

    @Override
    public void callback(String s, Object o) {
        if (s.equals("selectMaintainmillage")) {
            String millage = (String) o;
            handleSelectMileage(millage);
        } else if (s.equals("selectMaintainTime")) {
            String time = (String) o;
            handleSelectTime(time);
        } else if (s.equals("selectcar")) {
            handleSelectCar();
        }
        super.callback(s, o);
    }


    @Override
    protected void invalidateUI() {

    }

    private void setTxtRightShow() {
        if (!mileage.equals("") && !time.equals("")) {
            txt_right.setEnabled(true);
            txt_right.setAlpha(1);
        } else {
            txt_right.setEnabled(false);
            txt_right.setAlpha(0.5f);
        }
    }

    private void handleSelectCar() {
        Message message = Message.obtain();
        message.what = 114;
        message.obj = mileage;
        handler.sendMessage(message);
    }

    private void handleSelectMileage(String mileage) {
        Message message = Message.obtain();
        message.what = 110;
        message.obj = mileage;
        handler.sendMessage(message);
    }

    private void handleSelectTime(String time) {
        Message message = Message.obtain();
        message.what = 111;
        message.obj = time;
        handler.sendMessage(message);
    }

    private void handleModicationMantainResult(boolean isSuccess) {
        Message message = Message.obtain();
        message.what = 112;
        message.obj = isSuccess;
        handler.sendMessage(message);
    }

    private void handleSetMileAndTime() {
        Message message = Message.obtain();
        message.what = 113;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
                mileage = (String) msg.obj;
                maintain_mileage_select.setText(mileage);
                setTxtRightShow();
            } else if (msg.what == 111) {
                time = (String) msg.obj;
                maintain_time_select.setText(time);
                setTxtRightShow();
            } else if (msg.what == 112) {
                boolean isSuccess = (Boolean) msg.obj;
                if (isSuccess) {
                    ClipPopCardGive.getInstance().show(titleHead, R.drawable.givesuccess, getResources().getString(R.string.set_success));
                } else {
                    ClipPopCardGive.getInstance().show(titleHead, R.drawable.netexception, getResources().getString(R.string.network_bad));
                }
            } else if (msg.what == 113) {
                maintain_mileage_select.setText(ManagerMaintainList.getInstance().mileage);
                maintain_time_select.setText(ManagerMaintainList.getInstance().mainTaintime);
                setTxtRightShow();
            } else if (msg.what == 114) {
                maintain_car_select.setText(ManagerCarList.getInstance().getCurrentCar().num);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MODIFICATION_MAINTAINLIST_RESULT_BACK, this);
        ODispatcher.removeEventListener(OEventName.HTTP_CONN_ERROR, this);
        ODispatcher.removeEventListener(OEventName.SELECT_CAR_BACKPPPPP, this);
        super.onDetachedFromWindow();
    }
}
