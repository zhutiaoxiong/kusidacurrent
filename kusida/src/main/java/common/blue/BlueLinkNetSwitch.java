package common.blue;

import android.text.TextUtils;
import android.util.Log;


import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.ArrayList;
import java.util.List;

import model.ManagerCarList;
import model.carlist.CarBlueNet;
import model.carlist.DataCarInfo;
import model.status.DataSwitch;

import static common.GlobalContext.getContext;

/**
 * Created by Administrator on 2017/11/15.
 */

public class BlueLinkNetSwitch {
    private static List<CarBlueNet> carNetModelList;//是否为蓝牙模式，只暂存,切换一定要先断再连，不然连不了另一个
    //============================switch=========================
    public static DataSwitch getSwitchShakeInfo() {
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        DataSwitch switchShakeInfo = DataSwitch.fromJsonObject(carInfo.shakeInfo);
        if (switchShakeInfo != null) {
            switchShakeInfo.isShow = 1;
        } else {
            switchShakeInfo = new DataSwitch();
            switchShakeInfo.isOpen = 0;
            switchShakeInfo.isShow = 1;
        }
        return switchShakeInfo;
    }
    //============================switch=========================
    public static boolean getIsShakeOpenVibrate(){
        String result = ODBHelper.getInstance(getContext()).queryCommonInfo("isShakeOpenVibrate");
        int isShakeOpenVibrate = ODBHelper.queryResult2Integer(result,-1);
        return isShakeOpenVibrate == 1 ? true : false;
    }
    public static void setIsShakeOpenVibrate(boolean openVibrate){
        int isShakeOpenVibrate = openVibrate ? 1 : 0;
        ODBHelper.getInstance(getContext()).changeCommonInfo("isShakeOpenVibrate", String.valueOf(isShakeOpenVibrate));
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        DataSwitch shake = DataSwitch.fromJsonObject(carInfo.shakeInfo);
        if(shake!=null && shake.isOpen == 1){
            BlueLinkReceiver.needChangeCarData(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
//            BlueLinkReceiver.needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
        }
    }

    public static String getShakeLevel(){
        String shakeLevel = ODBHelper.getInstance(getContext()).queryCommonInfo("shakeLevel");
        return shakeLevel;
    }
    public static void setShakeLevel(String level){
        ODBHelper.getInstance(getContext()).changeCommonInfo("shakeLevel",level);
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        DataSwitch shake = DataSwitch.fromJsonObject(carInfo.shakeInfo);
        if(shake!=null && shake.isOpen == 1){
            BlueLinkReceiver.needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig,carInfo.isBindBluetooth,carInfo.carsig,carInfo.isMyCar);
        }
    }
    //============================switch=========================
    public static boolean getIsNetModel(long carId) {
        if (carNetModelList == null) carNetModelList = new ArrayList<CarBlueNet>();
        for (CarBlueNet data : carNetModelList) {
            if (data.carId == carId) return data.isUseNet;
        }
        return true;
    }
    public static void setSwitchNetAll() {
        if (carNetModelList == null) carNetModelList = new ArrayList<CarBlueNet>();
        for (CarBlueNet data : carNetModelList) {
                data.isUseNet = true;
        }
    }
    public static void setSwitchBlueModel(boolean useBlueModel, long carId,String from) {
        if (carNetModelList == null) carNetModelList = new ArrayList<CarBlueNet>();
        boolean haveData = false;
        for (CarBlueNet data : carNetModelList) {
            if (data.carId == carId) {
                data.isUseNet = !useBlueModel;
                haveData = true;
                break;
            }
        }
        if(!haveData) {
            CarBlueNet model = new CarBlueNet();
            model.isUseNet = !useBlueModel;
            model.carId = carId;
            carNetModelList.add(model);
        }
        DataSwitch shakeinfo = getSwitchShakeInfo();//摇一摇开关
        if(!useBlueModel && shakeinfo.isOpen == 0){//未开摇一摇
            BlueLinkReceiver.getInstance().closeBlueConnClearName("切换为网络模式");
//            BlueLinkReceiver.getInstance().disconnectAll("切换为网络模式");
        }
    }
}
