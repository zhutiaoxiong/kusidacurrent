package model;

import model.carlist.DataCarStatus;

public class CarStatusChangeUtil {
    /**比对车辆状态改變工具*/
    public static boolean matchDataChange(DataCarStatus getStatus, DataCarStatus currentCarStatus){
        if( matchInt(getStatus.leftFrontOpen,currentCarStatus.leftFrontOpen)){
            return true;
        }
        if( matchInt(getStatus.rightFrontOpen,currentCarStatus.rightFrontOpen)){
            return true;
        }
        if( matchInt(getStatus.leftBehindOpen,currentCarStatus.leftBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.rightBehindOpen,currentCarStatus.rightBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.afterBehindOpen,currentCarStatus.afterBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.leftFrontWindowOpen,currentCarStatus.leftFrontWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.rightFrontWindowOpen,currentCarStatus.rightFrontWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.leftBehindWindowOpen,currentCarStatus.leftBehindWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.rightBehindWindowOpen,currentCarStatus.rightBehindWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.isON,currentCarStatus.isON)){
            return true;
        }
        if( matchInt(getStatus.isStart,currentCarStatus.isStart)){
            return true;
        }
        if( matchInt(getStatus.isLock,currentCarStatus.isLock)){
            return true;
        }
        if( matchInt(getStatus.lightOpen,currentCarStatus.lightOpen)){
            return true;
        }
        if( matchInt(getStatus.skyWindowOpen,currentCarStatus.skyWindowOpen)){
            return true;
        }
        if( matchDouble(getStatus.voltage,currentCarStatus.voltage)){
            return true;
        }
        if( matchInt(getStatus.miles,currentCarStatus.miles)){
            return true;
        }
        if( matchInt(getStatus.lastMiles,currentCarStatus.lastMiles)){
            return true;
        }
//        if( matchOil(getStatus.oil,currentCarStatus.oil)){
//            return true;
//        }
        return  false;
    }
    private static boolean matchOil(float oil,float oils){
        if((Math.abs(((oil-oils)*10)))>5){
            return true;
        }
        return false;
    }
    /**比对车辆状态改變工具*/
    public static boolean matchDataChangeNovoltage(DataCarStatus getStatus, DataCarStatus currentCarStatus){
        if( matchInt(getStatus.leftFrontOpen,currentCarStatus.leftFrontOpen)){
            return true;
        }
        if( matchInt(getStatus.rightFrontOpen,currentCarStatus.rightFrontOpen)){
            return true;
        }
        if( matchInt(getStatus.leftBehindOpen,currentCarStatus.leftBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.rightBehindOpen,currentCarStatus.rightBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.afterBehindOpen,currentCarStatus.afterBehindOpen)){
            return true;
        }
        if( matchInt(getStatus.leftFrontWindowOpen,currentCarStatus.leftFrontWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.rightFrontWindowOpen,currentCarStatus.rightFrontWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.leftBehindWindowOpen,currentCarStatus.leftBehindWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.rightBehindWindowOpen,currentCarStatus.rightBehindWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.isON,currentCarStatus.isON)){
            return true;
        }
        if( matchInt(getStatus.isStart,currentCarStatus.isStart)){
            return true;
        }
        if( matchInt(getStatus.isLock,currentCarStatus.isLock)){
            return true;
        }
        if( matchInt(getStatus.lightOpen,currentCarStatus.lightOpen)){
            return true;
        }
        if( matchInt(getStatus.skyWindowOpen,currentCarStatus.skyWindowOpen)){
            return true;
        }
        if( matchInt(getStatus.isTheft,currentCarStatus.isTheft)){
            return true;
        }
        return  false;
    }

    /**比对车辆状态改變工具*/
    public static boolean matchDataChangenVoltage(double voltage, DataCarStatus currentCarStatus){
        if( matchDouble(voltage,currentCarStatus.voltage)){
            return true;
        }
        return  false;
    }

    /**比对车辆状态改變工具*/
    public static boolean machLockChange(DataCarStatus getStatus, DataCarStatus currentCarStatus){
        if( matchInt(getStatus.isLock,currentCarStatus.isLock)){
            return true;
        }
        return  false;
    }

    private static boolean matchInt(int a,int b ){
        if(a!=b)return true;
        return false;
    }
    private static boolean matchDouble(double a,double b ){
        if(a!=b)return true;
        return false;
    }
}
