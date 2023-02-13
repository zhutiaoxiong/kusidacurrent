package com.kulala.linkscarpods.blue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2016/9/22.
 */
public class LcdManagerCarStatus {
    public static byte[] ObjectToByte(Object obj) {
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


//    /**发送汽车状态对象*/
//    public static void sendCarStatus(DataCarStatus status){
//        if(status!=null){
//             if (BuildConfig.DEBUG) Log.e("比较车辆状态", "藍牙状态不一样已发送");
//            MyLcdBlueAdapterBack.getInstance().sendMessage(LcdManagerCarStatus.ObjectToByteBlue(status));
//        }
//    }
}
