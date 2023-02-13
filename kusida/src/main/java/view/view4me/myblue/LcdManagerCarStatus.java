package view.view4me.myblue;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.MyLcdBlueAdapterBack;
import com.kulala.staticsfunc.static_assistant.ByteHelper;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;

/**
 * Created by Administrator on 2016/9/22.
 */
public class LcdManagerCarStatus {
    private static byte[] pre21;//电量不要刷新

    public static void setData0x21(byte[] data, long carId) {
        if (data.length != 8) return;
        DataCarInfo thisCar = ManagerCarList.getInstance().getCarByID(carId);
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if (thisCar == null) return;
        byte[] states = ByteHelper.getBitArray(data[0]);//门窗状态
        carStatus.leftFrontOpen = states[7];
        carStatus.rightFrontOpen = states[6];
        carStatus.leftBehindOpen = states[5];
        carStatus.rightBehindOpen = states[4];
//        carInfo.footbrake = states[3];//脚啥
        carStatus.isStart = states[2];
        byte[] infos = ByteHelper.getBitArray(data[1]);//车辆基本信息
        carStatus.lightOpen = infos[7];
        carStatus.isLock = infos[6];//门锁状态                0b: 开锁 1b: 上锁
         if (BuildConfig.DEBUG) Log.e("锁状态", (infos[6] == 0 ? "开锁" : "上锁"));
        carStatus.afterBehindOpen = infos[5];
//        carInfo.powercover = infos[4];
//        carInfo.handbrake = infos[3];
        carStatus.isTheft = infos[2] * 2 + infos[1];//防盗
        carStatus.isON = infos[0];//ON开关状态            0b: OFF 1b: ON
        //
//        carInfo.oil_spend = noSign(data[2])/10;//平均油耗 = （平均油耗值）/ 10
//        carInfo.speed = noSign(data[3]);
//        carInfo.speed_round = noSign(data[5])*256+noSign(data[4]);
        //
        byte[] normals = ByteHelper.getBitArray(data[6]);//常规数据显示
//        carInfo.unclosedoor_warning = normals[0];
//        carInfo.shakewarning = normals[1];
//        carInfo.startunpre = normals[3];
        carStatus.leftFrontWindowOpen = normals[4];
        carStatus.rightFrontWindowOpen = normals[5];
        carStatus.leftBehindWindowOpen = normals[6];
        carStatus.rightBehindWindowOpen = normals[7];

        byte[] skys = ByteHelper.getBitArray(data[7]);//天窗
        carStatus.skyWindowOpen = skys[6];
        if (pre21 != null && !Arrays.equals(pre21, data)) {
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
             if (BuildConfig.DEBUG) Log.e("SECOND_CHANGE", "CAR_STATUS_SECOND_CHANGE");
        }
        pre21 = data;
    }

    public static byte[] ObjectToByte(java.lang.Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;

    }

    public static byte[] ObjectToByteBlue(DataCarStatus status) {
        byte[] data = new byte[17];
        data[0] = 0x02;
        data[1] = 14;
        if (1 == status.leftFrontOpen) {
            data[2] |= (0x01 << 0);
        } else {
            data[2] &= ~(0x01 << 0);
        }
        if (1 == status.rightFrontOpen) {
            data[2] |= (0x01 << 1);
        } else {
            data[2] &= ~(0x01 << 1);
        }
        if (1 == status.leftBehindOpen) {
            data[2] |= (0x01 << 2);
        } else {
            data[2] &= ~(0x01 << 2);
        }
        if (1 == status.rightBehindOpen) {
            data[2] |= (0x01 << 3);
        } else {
            data[2] &= ~(0x01 << 3);
        }
        if (1 == status.afterBehindOpen) {
            data[2] |= (0x01 << 4);
        } else {
            data[2] &= ~(0x01 << 4);
        }
        data[2] &= ~(0x01 << 5);
        if (1 == status.isLock) {
            data[2] |= (0x01 << 6);
        } else {
            data[2] &= ~(0x01 << 6);
        }

        if (1 == status.isStart) {
            data[2] |= (0x01 << 7);
        } else {
            data[2] &= ~(0x01 << 7);
        }

        if (1 == status.skyWindowOpen) {
            data[3] |= (0x01 << 0);
        } else {
            data[3] &= ~(0x01 << 0);
        }

        if (1 == status.leftFrontWindowOpen) {
            data[3] |= (0x01 << 1);
        } else {
            data[3] &= ~(0x01 << 1);
        }

        if (1 == status.rightFrontWindowOpen) {
            data[3] |= (0x01 << 2);
        } else {
            data[3] &= ~(0x01 << 2);
        }

        if (1 == status.leftBehindWindowOpen) {
            data[3] |= (0x01 << 3);
        } else {
            data[3] &= ~(0x01 << 3);
        }
        if (1 == status.rightBehindWindowOpen) {
            data[3] |= (0x01 << 4);
        } else {
            data[3] &= ~(0x01 << 4);
        }

        if (1 == status.lightOpen) {
            data[3] |= (0x01 << 5);
        } else {
            data[3] &= ~(0x01 << 5);
        }

        if (1 == status.isON) {
            data[3] |= (0x01 << 6);
        } else {
            data[3] &= ~(0x01 << 6);
        }

        if (1 == status.isTheft) {
            data[4] &= 0xc0;
            data[4] |= (0x01 << 6);
        } else if (0 == status.isTheft) {
            data[4] &= 0xc0;
            data[4] |= (0x00 << 6);
        }else if (2 == status.isTheft) {
            data[4] &= 0xc0;
            data[4] |= (0x02 << 6);
        }else if (3 == status.isTheft) {
            data[4] &= 0xc0;
            data[4] |= (0x03 << 6);
        }
        data[8]=(byte)status.miles;//miles[2];
        data[9]=(byte)(status.miles >> 8);//miles[1];
        data[10]=(byte)(status.miles >> 16);//miles[0];

        data[11]=(byte) (status.remainOil*10);
        data[12]=(byte)((int)(status.remainOil*10)>>8);

        data[13]=(byte) (status.lastMiles);
        data[14]=(byte) (status.lastMiles>>8);
        data[15] |= (int)(status.voltage*10);
        //data[2] |= 0x80;
        data[16] = 0;
        for (int i = 0; i < 16; i++) {
            data[16] += data[i];
        }
        data[16] ^= 0xff;
        return data;
    }


    //fin
    public static void setData0x22(byte[] data, long carId) {
        if (data.length != 8) return;
        DataCarInfo thisCar = ManagerCarList.getInstance().getCarByID(carId);
        if (thisCar == null) return;
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//        carInfo.sentence = noSign(data[2])*65536+noSign(data[1])*256+noSign(data[0]);//&0xff转为无符号
        carStatus.voltage = noSign(data[3]) / 10;
//         if (BuildConfig.DEBUG) Log.e(">>blue>>","voltage电量>>>"+thisCar.status.voltage);
//        carInfo.oil = (noSign(data[7])*256+noSign(data[6]))/10;
    }

    public static double noSign(byte a) {
        return a & 0xff;
    }

    /**发送汽车状态对象*/
    public static void sendCarStatus(DataCarStatus status){
        if(status!=null){
             if (BuildConfig.DEBUG) Log.e("比较车辆状态", "藍牙状态不一样已发送");
            MyLcdBlueAdapterBack.getInstance().sendMessage(LcdManagerCarStatus.ObjectToByteBlue(status));
        }
    }
}
