package view.view4app;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.tools.ActivityUtils;

import common.GlobalContext;
import common.http.HttpConn;
import ctrl.OCtrlCommon;
import model.AppList.AppListData;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.basicview.CheckForgroundUtils;
import view.view4app.lendcartemporary.ActivityLendCarTemporary;

/**
 * Created by qq522414074 on 2016/12/14.
 */

public class ViewAppListItem extends LinearLayoutBase {
    private TextView  tv;
    private ImageView iv;
    private   int       location;//这是第几个
    private String[] names;//自己使用的数据
    private int[] images;//自己使用的数据
    private int preIsMyCar = -1;//副车主判断
    public ViewAppListItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //        if(SystemMe.isPad(context)){
//            LayoutInflater.from(context).inflate(R.layout.grid_item_app_pad, this, true);
//        }else{
        LayoutInflater.from(context).inflate(R.layout.grid_item_app, this, true);
//        }
        tv = (TextView) findViewById(R.id.textView);
        iv = (ImageView) findViewById(R.id.imageView);
        initViews();
        initEvents();
    }
    public void setData(int position) {
        this.location = position;
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
    }
    private long reFreshTime;
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            long currentTime=System.currentTimeMillis();
            if((currentTime-reFreshTime)>1*1000){
                reFreshTime=currentTime;
                if(CheckForgroundUtils.isAppForeground()){
                    GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                            if(carInfo.isMyCar == 0){//副车主用的
                                names = AppListData.names_codriver;
                                images = AppListData.images_codriver;
                            }else{
                                names = AppListData.names;
                                images = AppListData.images;
                            }
                            if(preIsMyCar!=carInfo.isMyCar) {
                                if(location<names.length) {
                                    tv.setText(names[location]);
                                    iv.setImageResource(images[location]);
                                }else{
                                    tv.setText("");
                                    iv.setImageDrawable(null);
                                }
                            }
                            preIsMyCar = carInfo.isMyCar;
                            if(preIsMyCar == 1 && location == 1){
                                if (carInfo.authorityEndTime1 > System.currentTimeMillis()) {
                                    tv.setText("取消借车");
                                }else{
                                    tv.setText("临时借车");
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {
        iv.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

                //先判断是否有网
                if (!HttpConn.isHttpConn) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.bad_condition_of_the_network));
                    OCtrlCommon.getInstance().ccmd1115_getLoginState();
                    return;
                }
                int loc = location;
                if(preIsMyCar == 0){
                    loc = location+2;
                }
                switch (loc) {
                    case 0://信任借车
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
                        break;
                    case 1://临时借车
                        ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLendCarTemporary.class);
                        break;
                    case 2://状态记录
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_warnings);
                        break;
                    case 3://围栏
                        Intent intent = new Intent();
                        intent.setClass(getContext(), ViewGpsCarArea.class);//围栏
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_car_area);
                        break;
                    case 4://轨迹
//                        if (HttpConn.isHttpConn) {
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
//							OToastOMG.getInstance().showInput(getContext(), "密码验证", "请输入密码", new String[]{OToastOMG.PASS}, "password", ViewAppList.this);
//                        } else {
//                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.bad_condition_of_the_network));
//                            OCtrlCommon.getInstance().ccmd1115_getLoginState();
//                        }
                        break;
                    case 5://保养提醒
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_maintance_remind_me);
                        break;
                    case 6://年检提醒
//                        ManagerAnnual.getInstance().saveCarActiveList(ManagerCarList.getInstance().getCarAnnualList());
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_annual_reminder_main);
                        break;
//                    case 7://违章
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_carlist);
//                        break;
//                    case 8://违章
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_carlist);
//                        break;

//                    case 7://导航
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_navi_main);
//                        break;
//                    case 8://违章
//                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_carlist);
//                        break;
                }
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
