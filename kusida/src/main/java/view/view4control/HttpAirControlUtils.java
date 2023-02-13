package view.view4control;

import android.text.TextUtils;

import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarStatus;

public  class HttpAirControlUtils {

    public static void sendLeftAir(int currentLeftAirStatus,int where){
        DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
        String leftAirStatus=String.valueOf(currentLeftAirStatus);
        if(currentStatus!=null&&!TextUtils.isEmpty(currentStatus.chairLeftAir)&&currentStatus.chairLeftAir.equals("1")){
            if(!TextUtils.isEmpty(currentStatus.chairLeftAirStatus)&&!leftAirStatus.equals(currentStatus.chairLeftAirStatus)){
                OCtrlCar.getInstance().ccmd1253_seat_airheat_control(ManagerCarList.getInstance().getCurrentCarID(),12,getCoamad(currentLeftAirStatus,where));
            }
        }
    }
    private static int getCoamad(int coamand,int where){
        switch (where){
            case 1:
            case 2:
                if(coamand==0){
                    return 0;
                }else{
                    return 1;
                }
            case 3:
            case 4:
                if(coamand==0){
                    return 2;
                }else{
                    return 3;
                }
        }
        return 0;
    }

    public static void sendLeftHeat(int currentLeftHeatStatus,int where){
        DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
        String leftHeat=String.valueOf(currentLeftHeatStatus);
        if(currentStatus!=null&&!TextUtils.isEmpty(currentStatus.chairLeftHeat)&&currentStatus.chairLeftHeat.equals("1")){
            if(!TextUtils.isEmpty(currentStatus.chairLeftHeatStatus)&&!leftHeat.equals(currentStatus.chairLeftHeatStatus)){
                OCtrlCar.getInstance().ccmd1253_seat_airheat_control(ManagerCarList.getInstance().getCurrentCarID(),11,getCoamad(currentLeftHeatStatus,where));
            }
        }

    }
    public static void sendRightAir(int currentRightAirStatus,int where){
        DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
        String rightAirStatus=String.valueOf(currentRightAirStatus);
        if(currentStatus!=null&&!TextUtils.isEmpty(currentStatus.chairRightAir)&&currentStatus.chairRightAir.equals("1")){
            if(!TextUtils.isEmpty(currentStatus.chairRightAirStatus)&&!rightAirStatus.equals(currentStatus.chairRightAirStatus)){
                OCtrlCar.getInstance().ccmd1253_seat_airheat_control(ManagerCarList.getInstance().getCurrentCarID(),12,getCoamad(currentRightAirStatus,where));
            }
        }

    }
    public static void sendRightHeat(int currentRightHeatStatus,int where){
        DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
        String rightHeatStatus=String.valueOf(currentRightHeatStatus);
        if(currentStatus!=null&&!TextUtils.isEmpty(currentStatus.chairRightHeat)&&currentStatus.chairRightHeat.equals("1")){
            if(!TextUtils.isEmpty(currentStatus.chairRightHeatStatus)&&!rightHeatStatus.equals(currentStatus.chairRightHeatStatus)){
                OCtrlCar.getInstance().ccmd1253_seat_airheat_control(ManagerCarList.getInstance().getCurrentCarID(),11,getCoamad(currentRightHeatStatus,where));
            }
        }

    }
}
