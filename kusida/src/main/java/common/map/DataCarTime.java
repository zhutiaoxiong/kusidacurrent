package common.map;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
/**
 * Created by Administrator on 2017/3/16.
 */


public class DataCarTime {
    public long   carID;
    public LatLng carPos;
    public String carName;
    public String logo;
    public int    isStart;//0 close 1 open
    public long   time;//启动或熄火成功的时间

    public static boolean isSameData(DataCarTime data1,DataCarTime data2){
        if(data1 == null || data2 == null)return false;
        if(data1.carID != data2.carID)return false;
        if(data1.isStart != data2.isStart)return false;
        if(data1.time != data2.time)return false;
        if(TextUtils.isEmpty(data1.logo) || TextUtils.isEmpty(data2.logo) || !data1.logo.equals(data2.logo))return false;
        if(TextUtils.isEmpty(data1.carName) || TextUtils.isEmpty(data2.carName) || !data1.carName.equals(data2.carName))return false;
        if(data1.carPos == null || data2.carPos == null || data1.carPos.latitude!=data2.carPos.latitude || data1.carPos.longitude!=data2.carPos.longitude)return false;
        return true;
    }
}