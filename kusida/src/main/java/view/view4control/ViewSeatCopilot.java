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
public class ViewSeatCopilot extends LinearLayoutBase {
    public ViewSeatItem item3,item4;

    public ViewSeatCopilot(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_seat_copilot, this, true);
        item3 =findViewById(R.id.item3);
        item4=findViewById(R.id.item4) ;
        initViews();
        initEvents();
    }

    public void setUI(DataCarInfo carInfo){
        if(carInfo==null)return;
//        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
        DataCarStatus status = ManagerCarList.getInstance().getNetCurrentCarStatus();
        if (status == null) return;

        int enable;
        if(TextUtils.isEmpty(status.chairRightAir)||status.chairRightAir.equals("0")){
            enable=2;
        }else{
            enable=1;
        }
        int mStatus;
        if(!TextUtils.isEmpty(status.chairRightAirStatus)){
            mStatus=Integer.parseInt(status.chairRightAirStatus);
        }else{
            mStatus=0;
        }
        item3.setUIEnable(enable,mStatus,carInfo.ide);

        int enable2;
        if(TextUtils.isEmpty(status.chairRightHeat)||status.chairRightHeat.equals("0")){
            enable2=2;
        }else{
            enable2=1;
        }
        int mStatus2;
        if(!TextUtils.isEmpty(status.chairRightHeatStatus)){
            mStatus2=Integer.parseInt(status.chairRightHeatStatus);
        }else{
            mStatus2=0;
        }
        item4.setUIEnable(enable2,mStatus2,carInfo.ide);
    }

//    public void setUI(DataCarInfo carInfo,View parentView){
//        if(carInfo==null)return;
//        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
//        if (status == null) return;
//
//        int enable;
//        if(TextUtils.isEmpty(status.chairRightAir)||status.chairRightAir.equals("0")){
//            enable=2;
//        }else{
//            enable=1;
//        }
//        int mStatus;
//        if(!TextUtils.isEmpty(status.chairRightAirStatus)){
//            mStatus=Integer.parseInt(status.chairRightAirStatus);
//        }else{
//            mStatus=0;
//        }
//        item3.setUIEnable(enable,mStatus,carInfo.ide,parentView);
//
//        int enable2;
//        if(TextUtils.isEmpty(status.chairRightHeat)||status.chairRightHeat.equals("0")){
//            enable2=2;
//        }else{
//            enable2=1;
//        }
//        int mStatus2;
//        if(!TextUtils.isEmpty(status.chairRightHeatStatus)){
//            mStatus2=Integer.parseInt(status.chairRightHeatStatus);
//        }else{
//            mStatus2=0;
//        }
//        item4.setUIEnable(enable2,mStatus2,carInfo.ide,parentView);
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



