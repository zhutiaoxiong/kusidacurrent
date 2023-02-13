package view.view4app;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.timepicker.TimePickerView;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.AdapterWarnings;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.ManagerWarnings;
import model.carcontrol.DataWarnings;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

import com.kulala.staticsview.listview.ListViewPushRefresh;

/**
 * 警告列表
 * 1.初始化更新消息0-19
 * 2.下移上移刷新消息0-20,并清除旧数据
 * 3.如果选中了时间，就显示时间泛围内的
 * 4.初进先删除所有数据，其它全保存
 */

public class ViewWarning extends RelativeLayoutBase{
    private ClipTitleMeSet title_head;
    private ListViewPushRefresh list_states;
    private TextView           txt_timefrom, txt_timeto;
    private Button btn_confirm;
    private Button btn_control, btn_warning, btn_safety;

    private long timestart, timeend;
    private boolean isCheckingForTime = false;//是否使用指定时间来查徇
    private AdapterWarnings adapter;
    private int  selectPos   = 0;                        // 0,1,2
    private long preselectPos = 0;
    private long selectCarId = 0;
    private TimePickerView     pvTime;

    public ViewWarning(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_warnings, this, true);
        title_head = findViewById(R.id.title_head);
        list_states = (ListViewPushRefresh) findViewById(R.id.list_states);
        txt_timefrom = (TextView) findViewById(R.id.txt_timefrom);
        txt_timeto = (TextView) findViewById(R.id.txt_timeto);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_control = (Button) findViewById(R.id.btn_control);
        btn_warning = (Button) findViewById(R.id.btn_warning);
        btn_safety = (Button) findViewById(R.id.btn_safety);
        //时间选择器,put there because initView returned not init
        pvTime = new TimePickerView(getContext(), TimePickerView.Type.ALL);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        initViews();
        initEvents();
        /**初始化就要先查下最近消息*/
        OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,1,0,20,ManagerCarList.getInstance().getCurrentCar().ide);

        ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);
        ODispatcher.addEventListener(OEventName.CAR_SEARCHWARNING_LISTBACK, this);//滑动loading结束
    }
    public void initViews() {
        clearTime();
        // 时间下划线
        txt_timefrom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_timeto.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //初始显示
        selectPos = 1;
        preselectPos = 0;
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if (car != null)selectCarId = car.ide;
        invalidateUI();
    }


    public void initEvents() {
        //底部加载 end-> +20
        list_states.setOnLoadBottomListener(new ListViewPushRefresh.OnLoadBottomListener() {
            @Override
            public void onLoad() {
                /**底部加载，要减去header&footer*/
                int startpos = list_states.getCount()-2;
        Log.i("setOnLoadBottomListener","list_states.getCount():"+list_states.getCount());
                if(isCheckingForTime){
                    OCtrlCar.getInstance().ccmd1221_getWarninglist(timestart,timeend,selectPos,startpos,20,selectCarId);
                }else{
                    OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,selectPos,startpos,20,selectCarId);
                }
                // 为了显示效果，采用延迟加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_states.loadComplete();
                    }
                }, 2000);
            }
        });
        //头部加载0-19
        list_states.setonLoadHeaderListener(new ListViewPushRefresh.OnLoadHeaderListener() {
            @Override
            public void onLoad() {
                /**头部加载，要清掉所有旧数据*/
                ManagerWarnings.getInstance().DBClearDataAll();
                if(isCheckingForTime){
                    OCtrlCar.getInstance().ccmd1221_getWarninglist(timestart,timeend,selectPos,0,20,selectCarId);
                }else{
                    OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,selectPos,0,20,selectCarId);
                }
                // 为了显示效果，采用延迟加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_states.loadComplete();
                    }
                }, 2000);
            }
        });
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
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_select_start_time));
                    } else if (timeend < timestart) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.time_to_end_after_the_start_time));
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
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, 0);
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
                //变动查下最近消息
                ManagerWarnings.getInstance().DBClearDataAll();
                list_states.setSelection(0);
                list_states.loadStart();
                OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,1,0,20,selectCarId);
                clearTime();
                selectPos = 1;
                // 为了显示效果，采用延迟加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_states.loadComplete();
                    }
                }, 2000);
            }
        });
        btn_warning.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                //变动查下最近消息
                ManagerWarnings.getInstance().DBClearDataAll();
                list_states.setSelection(0);
                list_states.loadStart();
                OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,2,0,20,selectCarId);
                clearTime();
                selectPos = 2;
                // 为了显示效果，采用延迟加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_states.loadComplete();
                    }
                }, 2000);
            }
        });
        btn_safety.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                //变动查下最近消息
                ManagerWarnings.getInstance().DBClearDataAll();
                list_states.setSelection(0);
                list_states.loadStart();
                OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,3,0,20,selectCarId);
                clearTime();
                selectPos = 3;
                // 为了显示效果，采用延迟加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_states.loadComplete();
                    }
                }, 2000);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (timestart != 0) {
                    timeend = timeend == 0 ? ODateTime.getNow() : timeend;
                    isCheckingForTime = true;
                    //变动查下最近消息
                    ManagerWarnings.getInstance().DBClearDataAll();
                    list_states.setSelection(0);
                    list_states.loadStart();
                    OCtrlCar.getInstance().ccmd1221_getWarninglist(timestart,timeend,selectPos,0,20,selectCarId);
                    // 为了显示效果，采用延迟加载
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list_states.loadComplete();
                        }
                    }, 2000);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.vehicle_no_data_or_complete_loss_of_time));
                }
            }
        });
    }

    private void clearTime() {
        txt_timefrom.setText(getResources().getString(R.string.start_time));
        txt_timeto.setText(getResources().getString(R.string.end_time));
        timestart = 0;
        timeend = 0;
        isCheckingForTime = false;
    }

    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.MAIN_CLICK_BACK)) {
            if (pvTime != null) pvTime.handleHide();
        }else if (key.equals(OEventName.CAR_SEARCHWARNING_LISTBACK)) {//滑动loading结束
            handleChangeData();
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MAIN_CLICK_BACK, this);
        if (adapter != null) {
            for (DataWarnings war : adapter.getDataList()) {
                war.isNew = false;
            }
        }
        list_states.setAdapter(null);
        adapter = null;
        super.onDetachedFromWindow();
    }

    // 换页或退出清掉 新消息数
    private void clearNew() {
        if (preselectPos != selectPos && adapter != null) {
            for (DataWarnings war : adapter.getDataList()) {
                war.isNew = false;
            }
        }
    }

    public void invalidateUI() {
        if (preselectPos != selectPos) {
            clearNew();
            preselectPos = selectPos;
        }
        btn_control.setBackgroundResource(R.color.white);
        btn_warning.setBackgroundResource(R.color.white);
        btn_safety.setBackgroundResource(R.color.white);
        if (selectPos == 1) {
            btn_control.setBackgroundResource(R.color.all_background_color);
        } else if (selectPos == 2) {
            btn_warning.setBackgroundResource(R.color.all_background_color);
        } else if (selectPos == 3) {
            btn_safety.setBackgroundResource(R.color.all_background_color);
        }
        List<DataWarnings> listUsed;
        if(isCheckingForTime){
            listUsed = ManagerWarnings.getInstance().getListWarningByPosTime(selectPos, selectCarId,timestart,timeend);
        }else{
            listUsed = ManagerWarnings.getInstance().getListWarningByPos(selectPos, selectCarId);
        }
        if (listUsed != null) {
            int prePos = list_states.getFirstVisibleItem();
            adapter = new AdapterWarnings(getContext(), listUsed, R.layout.list_item_warnings);
            list_states.setAdapter(adapter);
            list_states.setSelection(prePos);
        }
    }

}

