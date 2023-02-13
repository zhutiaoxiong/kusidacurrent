package annualreminder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.timepicker.TimePickerView;
import com.client.proj.kusida.R;
import annualreminder.ctrl.OCtrlAnnual;
import annualreminder.model.ManagerAnnual;
import annualreminder.view.clip.PopChooseCarWarp;
import annualreminder.view.adapter.AdapterAnnualAddSelectCar;
import annualreminder.view.style.StyleSingleLineAnnual;
import annualreminder.view.style.StyleTitleHead;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.listview.ListViewWarp;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义提醒
 */
public class ViewAnnual_ReminderAdd_Manual extends LinearLayoutBase {
    private StyleTitleHead title_head;
    private StyleSingleLineAnnual txt_selectcar, txt_time;
    private TextView                             item_car_txt;
    private ImageView                            item_car_img;
    private Button                               btn_auto;
    private ListViewWarp                         list_cars;
    private AdapterAnnualAddSelectCar adpter;
    private PopChooseCarWarp.DataCarChoose currentCar;
    private List<PopChooseCarWarp.DataCarChoose> list;

    private long           chooseDate;//选择的日期
    private TimePickerView pvTime;
    public ViewAnnual_ReminderAdd_Manual(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_annual_reminder_add_manual, this, true);
        title_head = (StyleTitleHead) findViewById(R.id.title_head);
        txt_selectcar = (StyleSingleLineAnnual) findViewById(R.id.txt_selectcar);
        txt_time = (StyleSingleLineAnnual) findViewById(R.id.txt_time);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        item_car_txt = (TextView) findViewById(R.id.item_car).findViewById(R.id.txt_name);
        item_car_img = (ImageView) findViewById(R.id.item_car).findViewById(R.id.img_arrow);
        list_cars = (ListViewWarp) findViewById(R.id.list_cars);

        findViewById(R.id.item_car).setBackgroundResource(R.drawable.annual_bgst_round_black_btn);
        txt_selectcar.setImgRightVisible(INVISIBLE);
        btn_auto.setEnabled(false);
        btn_auto.setAlpha(0.5f);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.ANNUAL_CHANGE_RESULTBACK, this);
    }
    @Override
    protected void initViews() {
        list = ManagerAnnual.getInstance().getCarActiveList();
        if (list != null && list.size() > 0) {
            adpter = new AdapterAnnualAddSelectCar(getContext(), list);
            list_cars.setAdapter(adpter);
            currentCar = list.get(0);
            item_car_txt.setText(list.get(0).carName);
        } else {
            item_car_txt.setVisibility(INVISIBLE);
        }
        list_cars.setVisibility(INVISIBLE);
        //时间选择器
        pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 0, calendar.get(Calendar.YEAR) + 30);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
    }
    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW, R.layout.view_annual_reminder_fir);
            }
        });
        //选择车辆
        item_car_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list_cars.setVisibility(VISIBLE);
            }
        });
        item_car_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list_cars.setVisibility(VISIBLE);
            }
        });
        list_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list == null || list.size() == 0) return;
                currentCar = list.get(position);
                item_car_txt.setText(list.get(position).carName);
                list_cars.setVisibility(INVISIBLE);
            }
        });
        //选择时间
        txt_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
                pvTime.setMark("chooseDate");
            }
        });
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("chooseDate")) {
                    chooseDate = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(chooseDate);
                    txt_time.setRightText(date);
                    if (currentCar != null) {
                        btn_auto.setEnabled(true);
                        btn_auto.setAlpha(1.0f);
                    }
                }
            }
        });
        //手动提醒
        btn_auto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCar == null) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择车辆!");
                    return;
                }
                if (chooseDate <= 0) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择提醒日期!");
                    return;
                }
                //年检提醒添加 （1241）
                OCtrlAnnual.getInstance().ccmd1241_setAnnualInfo(currentCar.carId,0,1,chooseDate);
            }
        });
    }
    @Override
    protected void invalidateUI() {

    }
    //=====================================================

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.ANNUAL_CHANGE_RESULTBACK)) {
            boolean resultOK = (Boolean) paramObj;
            if (resultOK) {//提示在main,成功回首页
                ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW, R.layout.view_annual_reminder_fir);
            } else {
                //否则停留
            }
        }
        super.receiveEvent(eventName, paramObj);
    }
}
