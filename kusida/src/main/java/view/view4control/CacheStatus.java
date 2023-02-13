package view.view4control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.carlist.DataCarStatus;

public class CacheStatus {
    public long	carId;//汽车id
    public int isLock = 1;            // 是否锁定 0：未锁定，1：锁定中
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
    public  int  isTheft;
    public static JsonObject toJsonObject(CacheStatus info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
