package annualreminder.view.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import annualreminder.ctrl.OCtrlAnnual;
import annualreminder.model.ManagerAnnual;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.List;
/**
 * Created by Administrator on 2017/2/23.
 * 主页选车，上半部
 */

public class ClipAnnualReminder_select_car extends LinearLayoutBase {
    private ClipAnnualReminder_select_car_item item_selectcar;
    private RelativeLayout lin_time_left;
    private ImageView img_add_reminder;
    private TextView  txt_left_day,txt_reset;
    public ClipAnnualReminder_select_car(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_annual_reminder_select_car, this, true);
        item_selectcar = (ClipAnnualReminder_select_car_item) findViewById(R.id.item_selectcar);
        img_add_reminder = (ImageView) findViewById(R.id.img_add_reminder);
        lin_time_left = (RelativeLayout) findViewById(R.id.lin_time_left);
        txt_left_day = (TextView) findViewById(R.id.txt_left_day);
        txt_reset = (TextView) findViewById(R.id.txt_reset);
        initViews();
        initEvents();
    }
    @Override
    protected void initViews() {
        //初始显示无天数
        lin_time_left.setVisibility(INVISIBLE);
        img_add_reminder.setVisibility(VISIBLE);
        List<PopChooseCarWarp.DataCarChoose> carActiveList = ManagerAnnual.getInstance().getCarActiveList();
        if(carActiveList==null || carActiveList.size() == 0){
            item_selectcar.setVisibility(INVISIBLE);
        }else{//多个车
            if(ManagerAnnual.currentCarId>0){//有之前选中的车
                item_selectcar.setData(ManagerAnnual.getInstance().getCurrentCarChoose());
                //确定了车，就要去获取车辆记录,接收为parent.child.handlerChangeData();
                OCtrlAnnual.getInstance().ccmd1239_getAnnualInfoByCar(ManagerAnnual.currentCarId);
            }else{
                item_selectcar.setData(carActiveList.get(0));
                //确定了车，就要去获取车辆记录,接收为parent.child.handlerChangeData();
                OCtrlAnnual.getInstance().ccmd1239_getAnnualInfoByCar(carActiveList.get(0).carId);
            }
        }


    }
    @Override
    protected void initEvents() {
        item_selectcar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PopChooseCarWarp.DataCarChoose> carActiveList = ManagerAnnual.getInstance().getCarActiveList();
                if(carActiveList!=null){
                    PopChooseCarWarp.getInstance().setOnCarChooseListener(new PopChooseCarWarp.OnCarChooseListener() {
                        @Override
                        public void onChooseCar(PopChooseCarWarp.DataCarChoose car) {
                            item_selectcar.setData(car);
                            ManagerAnnual.currentCarId = car.carId;
                            //确定了车，就要去获取车辆记录,接收为parent.child.handlerChangeData();
                            OCtrlAnnual.getInstance().ccmd1239_getAnnualInfoByCar(car.carId);
                        }
                    });
                    PopChooseCarWarp.getInstance().show(item_selectcar,carActiveList);
                }
            }
        });
        img_add_reminder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ManagerAnnual.currentCarId <=0){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无可用车辆!");
                }else{
                    ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW,R.layout.view_annual_reminder_add);
                }
            }
        });
        //逻辑同上
        txt_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ManagerAnnual.currentCarId <=0){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无可用车辆!");
                }else{
                    ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW,R.layout.view_annual_reminder_add);
                }
            }
        });

    }
    @Override
    protected void invalidateUI() {
        if(ManagerAnnual.currentCarId == -1)return;
        ManagerAnnual.RecodeCar recodeCar = ManagerAnnual.getInstance().getRecodeByCarId(ManagerAnnual.currentCarId);
        if(recodeCar == null || recodeCar.remainingDay<=0 || recodeCar.isAlert == 0){
            //显示无天数
            lin_time_left.setVisibility(INVISIBLE);
            img_add_reminder.setVisibility(VISIBLE);
        }else{
            //显示有天数
            lin_time_left.setVisibility(VISIBLE);
            img_add_reminder.setVisibility(INVISIBLE);
            txt_left_day.setText(""+recodeCar.remainingDay);
        }

    }
}
