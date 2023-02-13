package common.blue;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.ByteHelper;

import java.util.Arrays;

import model.CarStatusChangeUtil;
import model.ManagerCarList;
import model.carlist.DataCarStatus;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ManagerCarStatus {

    private static byte[] pre21;//电量不要刷新
    public static void setData0x21(byte[] data,long carId){
        if(data.length!=8)return;
        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
        if (BuildConfig.DEBUG) Log.e("比较车辆状态", "status.isLock=: "+status.isLock);
        DataCarStatus newStatus =saveCarStaus( data);
        if(newStatus!=null){
            if(CarStatusChangeUtil.matchDataChangeNovoltage(newStatus,status)){
                //車輛狀態改變發送到液晶鑰匙
                newStatus.voltage=status.voltage;
                newStatus.miles=status.miles;
                newStatus.remainOil=status.remainOil;
                newStatus.lastMiles=status.lastMiles;
//                LcdManagerCarStatus.sendCarStatus(newStatus);
                 if (BuildConfig.DEBUG) Log.e("藍牙囘包", "setData0x21: " );
//                BlueLinkReceiver.getInstance().sendMessagelcd(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(newStatus)),true);
            }
        }
        byte[] states = ByteHelper.getBitArray(data[0]);//门窗状态
        status.leftFrontOpen = states[7];
        status.rightFrontOpen = states[6];
        status.leftBehindOpen = states[5];
        status.rightBehindOpen = states[4];
//        carInfo.footbrake = states[3];//脚啥
        status.isStart = states[2];
        byte[] infos = ByteHelper.getBitArray(data[1]);//车辆基本信息
        status.lightOpen = infos[7];
        status.isLock = infos[6];//门锁状态                0b: 开锁 1b: 上锁
        if (BuildConfig.DEBUG) Log.e("比较车辆状态", "lanya.isLock=: "+status.isLock);

        DataCarStatus status1 = ManagerCarList.getInstance().getCurrentCarStatus();
        if (BuildConfig.DEBUG) Log.e("比较车辆状态", "status1.isLock=: "+status1.isLock);
//        if(BuildConfig.DEBUG) if (BuildConfig.DEBUG) Log.e("锁状态",(infos[6] == 0 ? "开锁" : "上锁"));
        status.afterBehindOpen = infos[5];
//        carInfo.powercover = infos[4];
//        carInfo.handbrake = infos[3];
        status.isTheft = infos[2]*2+infos[1];//防盗
        status.isON = infos[0];//ON开关状态            0b: OFF 1b: ON



        //
//        carInfo.oil_spend = noSign(data[2])/10;//平均油耗 = （平均油耗值）/ 10
//        carInfo.speed = noSign(fdata[3]);
//        carInfo.speed_round = noSign(data[5])*256+noSign(data[4]);
        //
        byte[] normals = ByteHelper.getBitArray(data[6]);//常规数据显示
//        carInfo.unclosedoor_warning = normals[0];
//        carInfo.shakewarning = normals[1];
//        carInfo.startunpre = normals[3];
        status.leftFrontWindowOpen = normals[4];
        status.rightFrontWindowOpen = normals[5];
        status.leftBehindWindowOpen = normals[6];
        status.rightBehindWindowOpen = normals[7];

        byte[] skys = ByteHelper.getBitArray(data[7]);//天窗
        status.skyWindowOpen = skys[6];
        ODispatcher.dispatchEvent(OEventName.SEND_CAR_STATUS_BLUETOOTH,DataCarStatus.toJsonObject(status).toString());
        if(pre21!=null && !Arrays.equals(pre21,data)){
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
//             if (BuildConfig.DEBUG) Log.e("SECOND_CHANGE","CAR_STATUS_SECOND_CHANGE"+"infos[6]:"+infos[6]);
        }
        pre21 = data;
    }
    private  static DataCarStatus saveCarStaus(byte[] data){
        if(data.length!=8)return null;
        DataCarStatus status=new DataCarStatus();
        byte[] states = ByteHelper.getBitArray(data[0]);//门窗状态
        status.leftFrontOpen = states[7];
        status.rightFrontOpen = states[6];
        status.leftBehindOpen = states[5];
        status.rightBehindOpen = states[4];
//        carInfo.footbrake = states[3];//脚啥
        status.isStart = states[2];
        byte[] infos = ByteHelper.getBitArray(data[1]);//车辆基本信息
        status.lightOpen = infos[7];
        status.isLock = infos[6];//门锁状态                0b: 开锁 1b: 上锁
//        if(BuildConfig.DEBUG) if (BuildConfig.DEBUG) Log.e("锁状态",(infos[6] == 0 ? "开锁" : "上锁"));
        status.afterBehindOpen = infos[5];
//        carInfo.powercover = infos[4];
//        carInfo.handbrake = infos[3];
        status.isTheft = infos[2]*2+infos[1];//防盗
        status.isON = infos[0];//ON开关状态            0b: OFF 1b: ON
        //
//        carInfo.oil_spend = noSign(data[2])/10;//平均油耗 = （平均油耗值）/ 10
//        carInfo.speed = noSign(data[3]);
//        carInfo.speed_round = noSign(data[5])*256+noSign(data[4]);
        //
        byte[] normals = ByteHelper.getBitArray(data[6]);//常规数据显示
//        carInfo.unclosedoor_warning = normals[0];
//        carInfo.shakewarning = normals[1];
//        carInfo.startunpre = normals[3];
        status.leftFrontWindowOpen = normals[4];
        status.rightFrontWindowOpen = normals[5];
        status.leftBehindWindowOpen = normals[6];
        status.rightBehindWindowOpen = normals[7];

        byte[] skys = ByteHelper.getBitArray(data[7]);//天窗
        status.skyWindowOpen = skys[6];
        return status;
    }
    //fin
    public static void setData0x22(byte[] data,long carId){
        if(data.length!=8)return;
        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
//        carInfo.sentence = noSign(data[2])*65536+noSign(data[1])*256+noSign(data[0]);//&0xff转为无符号
        double newvoltage=noSign(data[3])/10;
        int mile1=noSignInt(data[0]);
        int mile2=noSignInt(data[1]);
        int mile3=noSignInt(data[2]);


        //電壓改變發模組
//        if(CarStatusChangeUtil.matchDataChangenVoltage(newvoltage,status)){
//            status.voltage=newvoltage;
//            LcdManagerCarStatus.sendCarStatus(status);
//        }
        //里程数
        status.miles=mile3*65536+mile2*256+mile1;
        byte lastmile1=data[4];
        byte lastmile2=data[5];
        //续航里程
        status.lastMiles=lastmile2*256+lastmile1;
        int remainOiloo=noSignInt(data[6]);
        int remainOi2=noSignInt(data[7]);
        status.remainOil=(float) (remainOi2*256+remainOiloo)/10;
//        status.remainOil = (noSignFloat(remainOiloo)*256+noSignFloat(remainOiloo))/10;
        status.voltage = noSign(data[3])/10;
        ODispatcher.dispatchEvent(OEventName.SEND_CAR_STATUS_BLUETOOTH,DataCarStatus.toJsonObject(status).toString());
//        BlueLinkReceiver.getInstance().sendMessagelcd(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(status)),true);
//        BlueLinkReceiver.getInstance().sendMessagelcd(ByteHelper.bytesToHexString(LcdManagerCarStatus.ObjectToByteBlue(status)),true);
//        LcdManagerCarStatus.sendCarStatus(status);
//         if (BuildConfig.DEBUG) Log.e(">>blue>>","voltage电量>>>"+thisCar.status.voltage);
//        carInfo.oil = (noSign(data[7])*256+noSign(data[6]))/10;
    }
    public static double noSign(byte a){
        return a&0xff;
    }

    public static int noSignInt(byte a){
        return a&0xff;
    }
    public static int noSignFloat(byte a){
        return a&0xff;
    }
}
