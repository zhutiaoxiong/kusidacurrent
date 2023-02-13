package view.view4control;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;

import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;

/**
 * Created by qq522414074 on 2016/9/18.
 */
public class ViewSeatMaster extends LinearLayoutBase {
    public ViewSeatItem item1,item2;

    public ViewSeatMaster(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_seat_master, this, true);
        item1 =findViewById(R.id.item1);
        item2=findViewById(R.id.item2) ;
        initViews();
        initEvents();
    }

    public void setUI(DataCarInfo carInfo){
        if(carInfo==null)return;
//        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
        DataCarStatus status = ManagerCarList.getInstance().getNetCurrentCarStatus();
        if (status == null) return;

        int enable;
        if(TextUtils.isEmpty(status.chairLeftAir)||status.chairLeftAir.equals("0")){
            enable=2;
        }else{
            enable=1;
        }
        int mStatus;
        if(!TextUtils.isEmpty(status.chairLeftAirStatus)){
            mStatus=Integer.parseInt(status.chairLeftAirStatus);
        }else{
            mStatus=0;
        }
        item1.setUIEnable(enable,mStatus,carInfo.ide);

        int enable2;
        if(TextUtils.isEmpty(status.chairLeftHeat)||status.chairLeftHeat.equals("0")){
            enable2=2;
        }else{
            enable2=1;
        }
        int mStatus2;
        if(!TextUtils.isEmpty(status.chairLeftHeatStatus)){
            mStatus2=Integer.parseInt(status.chairLeftHeatStatus);
        }else{
            mStatus2=0;
        }
        item2.setUIEnable(enable2,mStatus2,carInfo.ide);
    }

//    public void setUI(DataCarInfo carInfo,View parentView){
//        if(carInfo==null)return;
//        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
//        if (status == null) return;
//
//        int enable;
//        if(TextUtils.isEmpty(status.chairLeftAir)||status.chairLeftAir.equals("0")){
//            enable=2;
//        }else{
//            enable=1;
//        }
//        int mStatus;
//        if(!TextUtils.isEmpty(status.chairLeftAirStatus)){
//            mStatus=Integer.parseInt(status.chairLeftAirStatus);
//        }else{
//            mStatus=0;
//        }
//        item1.setUIEnable(enable,mStatus,carInfo.ide,parentView);
//
//        int enable2;
//        if(TextUtils.isEmpty(status.chairLeftHeat)||status.chairLeftHeat.equals("0")){
//            enable2=2;
//        }else{
//            enable2=1;
//        }
//        int mStatus2;
//        if(!TextUtils.isEmpty(status.chairLeftHeatStatus)){
//            mStatus2=Integer.parseInt(status.chairLeftHeatStatus);
//        }else{
//            mStatus2=0;
//        }
//        item2.setUIEnable(enable2,mStatus2,carInfo.ide,parentView);
//    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void receiveEvent(String s, Object o) {

    }

    @Override
    protected void invalidateUI() {

    }
}



