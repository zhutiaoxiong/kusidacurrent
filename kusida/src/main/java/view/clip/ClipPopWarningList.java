package view.clip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.timepicker.TimePickerView;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.titlehead.ClipTitleHead;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.AdapterWarnings;
import common.GlobalContext;
import model.ManagerCarList;
import model.ManagerWarnings;
import model.carcontrol.DataWarnings;
import model.carlist.DataCarInfo;

/**
 * 弹出列表Warinings
 */
@Deprecated
public class ClipPopWarningList implements OCallBack, OEventObject {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private RelativeLayout thisView;
    private Context        context;

    private ClipTitleHead title_head;
    private ListView        list_states;
    private TextView        txt_timefrom, txt_timeto;
    private Button btn_confirm;
    private Button btn_control, btn_warning, btn_safety;

    private long timestart, timeend;
    private AdapterWarnings adapter;
    private int  selectPos   = 0;                        // 0,1,2
    private long selectCarId = 0;
    private List<DataWarnings> listUsed;
    private MyHandlerlerler handler = new MyHandlerlerler();
    private        TimePickerView     pvTime;
    // ========================out======================
    private static ClipPopWarningList _instance;

    public static ClipPopWarningList getInstance() {
        if (_instance == null)
            _instance = new ClipPopWarningList();
        return _instance;
    }

    //===================================================
    public void show(View parentView) {
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.view_app_warnings, null);
        title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
        list_states = (ListView) thisView.findViewById(R.id.list_states);
        txt_timefrom = (TextView) thisView.findViewById(R.id.txt_timefrom);
        txt_timeto = (TextView) thisView.findViewById(R.id.txt_timeto);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        btn_control = (Button) thisView.findViewById(R.id.btn_control);
        btn_warning = (Button) thisView.findViewById(R.id.btn_warning);
        btn_safety = (Button) thisView.findViewById(R.id.btn_safety);
        selectPos = 0;
        preselectPos = 0;
        //时间选择器,put there because initView returned not init
        pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 3);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        initView();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);
    }

    private void initView() {
        // 时间下划线
        txt_timefrom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_timeto.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if (car == null) {
            handleChangeData();
            return;
        }
        selectCarId = car.ide;
        listUsed = ManagerWarnings.getInstance().getListWarningByPos(1, selectCarId);
        handleChangeData();
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
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("timefrom")) {
                    timestart = datee.getTime();
                    String date = ODateTime.time2StringWithHH(timestart);
                    txt_timefrom.setText(date);
                } else if (mark.equals("timeto")) {
                    timeend = datee.getTime();
                    if (timestart == 0) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.please_select_start_time));
                    } else if (timeend < timestart) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.time_to_end_after_the_start_time));
                    } else {
                        String date1 = ODateTime.time2StringWithHH(timeend);
                        txt_timeto.setText(date1);
                    }
                }
            }
        });
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                exitthis();
            }
        });
        // pick time
        txt_timefrom.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("timefrom");
            }
        });
        txt_timeto.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("timeto");
            }
        });
        btn_control.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clearTime();
                selectPos = 0;
                listUsed = ManagerWarnings.getInstance().getListWarningByPos(selectPos + 1, selectCarId);
                handleChangeData();
            }
        });
        btn_warning.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clearTime();
                selectPos = 1;
                listUsed = ManagerWarnings.getInstance().getListWarningByPos(selectPos + 1, selectCarId);
                handleChangeData();
            }
        });
        btn_safety.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clearTime();
                selectPos = 2;
                listUsed = ManagerWarnings.getInstance().getListWarningByPos(selectPos + 1, selectCarId);
                handleChangeData();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if (car == null || timestart == 0 || timeend == 0) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, context.getResources().getString(R.string.vehicle_no_data_or_complete_loss_of_time));
                } else {
                    long endTime = timeend == 0 ? ODateTime.getNow() : timeend;
                    listUsed = ManagerWarnings.getInstance().getListWarningByPosTime(selectPos + 1, car.ide, timestart, endTime);
                    handleChangeData();
                }
            }
        });
    }

    private void clearTime() {
        txt_timefrom.setText(context.getResources().getString(R.string.start_time));
        txt_timeto.setText(context.getResources().getString(R.string.end_time));
        timestart = 0;
        timeend = 0;
    }

    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.MAIN_CLICK_BACK)) {
            if (pvTime != null) pvTime.handleHide();
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    private void exitthis() {
        ODispatcher.removeEventListener(OEventName.MAIN_CLICK_BACK, this);
        if (adapter != null) {
            for (DataWarnings war : adapter.getDataList()) {
                war.isNew = false;
            }
        }
        list_states.setAdapter(null);
        adapter = null;
        popContain.dismiss();
    }

    // 换页或退出清掉 新消息数
    private void clearNew() {
        if (preselectPos != selectPos && adapter != null) {
            for (DataWarnings war : adapter.getDataList()) {
                war.isNew = false;
            }
        }
    }

    private long preselectPos = 0;

    public void invalidateUI() {
        if (preselectPos != selectPos) {
            clearNew();
            preselectPos = selectPos;
        }
        btn_control.setBackgroundResource(R.color.background_all);
        btn_warning.setBackgroundResource(R.color.background_all);
        btn_safety.setBackgroundResource(R.color.background_all);
        if (selectPos == 0) {
            btn_control.setBackgroundResource(R.color.background_top);
        } else if (selectPos == 1) {
            btn_warning.setBackgroundResource(R.color.background_top);
        } else if (selectPos == 2) {
            btn_safety.setBackgroundResource(R.color.background_top);
        }
        if (listUsed != null) {
            adapter = new AdapterWarnings(context, listUsed, R.layout.list_item_warnings);
            list_states.setAdapter(adapter);
        }
    }

    public void handleChangeData() {
        Message message = new Message();
        message.what = 123456;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandlerlerler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123456:
                    invalidateUI();
                    break;
            }
        }
    }
}

