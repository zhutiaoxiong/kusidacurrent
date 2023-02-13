package model.carlist;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import common.global.NAVI;

public class DataCarStatus    {
    public long	carId;//汽车id
    public int isStart;            // 是否启动 0：熄火中，1：已启动
    public int isON;            // 0：关闭，1：开启，用这个字段来控制启动按钮是否开启，如果isON为1，isStart为0，则点击时弹出“当前车辆正在耗电，请先关闭”
    public int isTheft;            // 0：解防 白色 1：设防 红色 2:报警 3:二次防盗 ,撤防0 执行设防，isTheft 1,2,3其它的都算设防
    public int isLock = 1;            // 是否锁定 0：未锁定，1：锁定中
    public int isOnline;            // 是否在线 0：不在线，1：在线中
    public int gpsOpen;            // gps是否在线 0：不在线，1：在线中
    public int lightOpen;            // 大灯是否开启 0：未启动，1：已启动
    public int skyWindowOpen;            // 天窗状态	0：关闭，1：开启


    public int leftFrontOpen;        // 左前门是否开启 0：未开启，1：已开启
    public int rightFrontOpen;    // 右前门是否开启 0：未开启，1：已开启
    public int leftBehindOpen;    // 左后门是否开启 0：未开启，1：已开启
    public int rightBehindOpen;    // 右后门是否开启 0：未开启，1：已开启
    public int afterBehindOpen;    // 后尾箱是否开启 0：未开启，1：已开启

    public  int    leftFrontWindowOpen;//左前窗是否开启	0：未开启，1：已开启
    public  int    rightFrontWindowOpen;//右前窗是否开启	0：未开启，1：已开启
    public  int    leftBehindWindowOpen;//左后窗是否开启	0：未开启，1：已开启
    public  int    rightBehindWindowOpen;//右后窗是否开启	0：未开启，1：已开启
    public  double  voltage;            // 电压
    private String gps;                // pos
    public  int    direction;//汽车方向	 0～359，正北为0，顺时针
    public  int    voltageStatus;        // 1：正常，2：电压低
    public double temp;//温度 如果为-999的话则表示没值，不显示
    public  int    ACStatus = -1;        // 0：关闭，1：开启，-1：未设置值
    public  int    windLevel = -1;        // 0：小，1：中，2：大，-1：未设置值
    public  int    tempControlStatus =-1;        // 0：强（冷），1：中（冷），2：弱（冷），3：中间档，4：低（热），5：中（热），6：高（热），-1：未设置值
    public long startTime;//汽车启动/熄火时间
    public  int airConditionOpen;//0关闭，1开启
    public  int miles;//里程
    public  int lastMiles;//续航里程
    public  float remainOil;//油量
    public  int isWarn;//二次防盗
    public  String chairRightHeat;//右边座椅加热 0无 1有
    public  String chairLeftHeat;//左边座椅加热 0无 1有
    public  String chairRightAir;//右边座椅通风 0关1开
    public  String chairLeftAir;//左边座椅通风 0关1开
    public  String chairRightHeatStatus;//右边加热状态 0关1开
    public  String chairRightAirStatus;//右边通风状态 0关1开
    public  String chairLeftHeatStatus;//左边加热状态 0关1开
    public  String chairLeftAirStatus;//左边通风状态  0关1开


    @Override
    public DataCarStatus clone() throws CloneNotSupportedException {
        return (DataCarStatus)super.clone();
    }
    //取车门对应状态的字符
    public static String getOpenCloseChar(int open) {
        if (open == 1) return "open";
        else return "close";
    }
    public static String getOpenCloseWinChar(int open) {
        if (open == 1) return "_win";
        else return "";
    }
    public void setGps(String gps) {
        this.gps = gps;
    }

    public LatLng getGps() {
        if (gps != null && !gps.equals("")) {
            LatLng toBaiduarea = NAVI.gps2baidu(NAVI.Str2Latlng(gps));
            return toBaiduarea;
        }
        return null;
    }

    public static DataCarStatus fromJsonObject(JsonObject obj) {
        Gson          gson    = new Gson();
        DataCarStatus thisobj;
        try {
            thisobj = gson.fromJson(obj, DataCarStatus.class);
            return thisobj;
        }catch (NumberFormatException e){
             if (BuildConfig.DEBUG) Log.e("DataCarStatus",e.toString()+"\nNumberFormatException:"+obj.toString());
            return null;
        }
    }

    public static JsonObject toJsonObject(DataCarStatus info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
