package annualreminder.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import annualreminder.model.ManagerAnnual;
import annualreminder.view.clip.ClipAnnualReminder_NoRecode;
import annualreminder.view.clip.ClipAnnualReminder_Recode;
import annualreminder.view.clip.ClipAnnualReminder_select_car;
import annualreminder.view.style.StyleTitleHead;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
/**
 * 年检提醒首页
 */

/**
 * Annual页面显示，需要先设managerlist
 */
public class ViewAnnualReminder_Fir extends LinearLayoutBase {
    private StyleTitleHead title_head;
    private ClipAnnualReminder_select_car view_select_car;
    private RelativeLayout                lin_bottom;
    private ImageView                     img_add_reminder,img_add,img_no_recode;
    private TextView  txt_annual_reminder,txt_no_recode;
    public ViewAnnualReminder_Fir(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_annual_reminder_fir, this, true);
        title_head = (StyleTitleHead) findViewById(R.id.title_head);
        view_select_car = (ClipAnnualReminder_select_car) findViewById(R.id.view_select_car);
        lin_bottom = (RelativeLayout) findViewById(R.id.lin_bottom);
        img_add = (ImageView) findViewById(R.id.img_add);
//        txt_annual_reminder = (TextView) findViewById(R.id.txt_annual_reminder);
//        txt_no_recode = (TextView) findViewById(R.id.txt_no_recode);
//        title_head.setTitle("年检提醒");
//        title_head.setLeftRes(R.drawable.titlehead_back);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.ANNUAL_FROM_CAR_RESULTBACK,this);//首页刷新
    }
    @Override
    protected void initViews() {
        invalidateUI();
    }
    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,0);
                ManagerAnnual.getInstance().exit();//清空数据
            }
        });
        img_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ManagerAnnual.currentCarId<=0)return;
//                ViewAnnualRecode_Add.repairRecode = null;
//                ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW,R.layout.view_annual_recode_add);
                ActivityAnnual_RecodeAdd.repairRecode = null;
                Intent intent = new Intent();
                intent.setClass(getContext(), ActivityAnnual_RecodeAdd.class);
                getContext().startActivity(intent);
            }
        });
    }
    @Override
    protected void invalidateUI() {
        lin_bottom.removeAllViews();
        ManagerAnnual.RecodeCar recode= ManagerAnnual.getInstance().getRecodeByCarId(ManagerAnnual.currentCarId);
        if(recode == null || recode.recodeList == null || recode.recodeList.size() == 0){//显示无激活车辆
            lin_bottom.addView(new ClipAnnualReminder_NoRecode(getContext(),null));
        }else{
            lin_bottom.addView(new ClipAnnualReminder_Recode(getContext(),null));
        }
        view_select_car.handleChangeData();
    }
    //=====================================================

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.ANNUAL_FROM_CAR_RESULTBACK)){
            handleChangeData();
        }
        super.receiveEvent(eventName, paramObj);
    }
}
