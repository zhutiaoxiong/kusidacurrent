package view;

import android.text.TextUtils;

import model.ManagerCarList;
import model.carlist.DataCarInfo;

public class EquipmentManager {
    public static boolean isMini(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return !TextUtils.isEmpty(car.bluetoothName) && car.bluetoothName.startsWith("NFC")&&(!(!TextUtils.isEmpty(car.terminalNum) && car.terminalNum.startsWith("6")));
//            return true;
        }
        return false;
    }
    public static boolean isShouweiSix(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return !TextUtils.isEmpty(car.terminalNum) && car.terminalNum.startsWith("6");
//            return true;
        }
        return false;
    }
    public static boolean isStartWithAkl(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return !TextUtils.isEmpty(car.bluetoothName) && car.bluetoothName.startsWith("AKL");
//            return true;
        }
        return false;
    }
    public static boolean isMinJiaQiang(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return !TextUtils.isEmpty(car.bluetoothName) && car.bluetoothName.startsWith("MIN");
//            return true;
        }
        return false;
    }
    public static boolean isMinNoMozu(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return !TextUtils.isEmpty(car.terminalNum) &&car.terminalNum.startsWith("MIN");
//            return true;
        }
        return false;
    }
    public static boolean isShouweiSixSelectCar(DataCarInfo selectCar){
        if(selectCar!=null){
            return !TextUtils.isEmpty(selectCar.terminalNum) && selectCar.terminalNum.startsWith("6");
//            return true;
        }
        return false;
    }
    public static boolean isActive(){
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            return car.isActive==1;
//            return true;
        }
        return false;
    }
}
