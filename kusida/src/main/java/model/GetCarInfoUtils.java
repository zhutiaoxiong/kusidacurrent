package model;

import model.carlist.DataCarInfo;
import view.find.ProSetData;
import view.find.ProSetDataAndTerminum;
import view.view4me.shake.BlueNotouchInData;
import view.view4me.shake.BlueNotouchInDataAndTerminum;

public class GetCarInfoUtils {
    public static long getCurrentCarId(){
        DataCarInfo info=ManagerCarList.getInstance().getCurrentCar();
        if(info!=null){
            return info.ide;
        }else{
            return 0;
        }
    }
    public static String getCurrentCarTerminalNum(){
        DataCarInfo info=ManagerCarList.getInstance().getCurrentCar();
        if(info!=null){
            return info.terminalNum;
        }else{
            return "";
        }
    }

    public static BlueNotouchInDataAndTerminum createDeviceSet(BlueNotouchInData blueNotouchInData){
        BlueNotouchInDataAndTerminum blueNotouchIndataAndter=new BlueNotouchInDataAndTerminum();
        blueNotouchIndataAndter.terminalNum= getCurrentCarTerminalNum();
        blueNotouchIndataAndter.openDistance= blueNotouchInData.openDistance;
        blueNotouchIndataAndter.lockAgile= blueNotouchInData.lockAgile;
        blueNotouchIndataAndter.openQty= blueNotouchInData.openQty;
        blueNotouchIndataAndter.lockQty= blueNotouchInData.lockQty;
        blueNotouchIndataAndter.openNear= blueNotouchInData.openNear;
        blueNotouchIndataAndter.lockNear= blueNotouchInData.lockNear;
        return blueNotouchIndataAndter;
    }

    public static ProSetDataAndTerminum createDeviceSet(ProSetData proSetData){
        ProSetDataAndTerminum proSetDataAndTerminum=new ProSetDataAndTerminum();
        proSetDataAndTerminum.terminalNum= getCurrentCarTerminalNum();
        proSetDataAndTerminum.windowRiseInterval= proSetData.windowRiseInterval;
        proSetDataAndTerminum.windowRiseTime= proSetData.windowRiseTime;
        proSetDataAndTerminum.hornVolume= proSetData.hornVolume;
        proSetDataAndTerminum.trunkOpenWith= proSetData.trunkOpenWith;
        proSetDataAndTerminum.electrifyBeforehandTime= proSetData.electrifyBeforehandTime;
        proSetDataAndTerminum.switchesOffDelayTime= proSetData.switchesOffDelayTime;
        proSetDataAndTerminum.pressKayTime= proSetData.pressKayTime;
        proSetDataAndTerminum.avoidanceDeviceTechnique= proSetData.avoidanceDeviceTechnique;
        proSetDataAndTerminum.avoidanceDeviceOperation= proSetData.avoidanceDeviceOperation;
        proSetDataAndTerminum.unlockWay= proSetData.unlockWay;
        proSetDataAndTerminum.lockWay= proSetData.lockWay;
        proSetDataAndTerminum.carLockWindowRise= proSetData.carLockWindowRise;
        return proSetDataAndTerminum;
    }

}
